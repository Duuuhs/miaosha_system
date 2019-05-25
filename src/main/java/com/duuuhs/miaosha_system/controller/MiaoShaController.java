package com.duuuhs.miaosha_system.controller;

import com.duuuhs.miaosha_system.access.AccessLimit;
import com.duuuhs.miaosha_system.model.MiaoShaGoods;
import com.duuuhs.miaosha_system.model.MiaoShaOrder;
import com.duuuhs.miaosha_system.model.OrderInfo;
import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.rabbitmq.MQSender;
import com.duuuhs.miaosha_system.rabbitmq.MiaoShaMessage;
import com.duuuhs.miaosha_system.redis.GoodsKey;
import com.duuuhs.miaosha_system.redis.OrderKey;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.redis.UserKey;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.service.*;
import com.duuuhs.miaosha_system.util.MD5Util;
import com.duuuhs.miaosha_system.util.UUIDUtil;
import com.duuuhs.miaosha_system.vo.GoodsVo;
import com.sun.javafx.collections.MappingChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static com.duuuhs.miaosha_system.util.Assert.isNull;
import static com.duuuhs.miaosha_system.util.Assert.notNull;

import com.duuuhs.miaosha_system.result.Reslut;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: DMY
 * @Date: 2019/4/18 23:24
 * @Description:
 */
@Controller
@RequestMapping("miaosha")
public class MiaoShaController implements InitializingBean {

    Logger logger = LoggerFactory.getLogger(MiaoShaController.class);

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MiaoShaService miaoShaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private VerifyCodeService verifyCodeService;

    //内存标记商品是否售完
    Map<Long, Boolean> localOverMap = new HashMap<>();


    /*
     *系统初始化的时候实现了InitializingBean接口系统会回调这个方法
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVos = goodsService.listGoodVo();
        if (isNull(goodsVos)){
            return;
        }
        //将秒杀商品的数量加载进redis
        for (GoodsVo g : goodsVos) {
            redisService.set(GoodsKey.getMiaoShaStock, ""+g.getId(), g.getStockCount());
            localOverMap.put(g.getId(), false);
        }
    }



    /*
     * 获取秒杀接口
     */
    @RequestMapping("path")
    @ResponseBody
    @AccessLimit(maxCount = 5, seconds = 5, needLogin = true)
    public Reslut<String> getMiaoshaPath(HttpServletResponse response, @RequestParam("goodsId")Long goodsId,
                                         @RequestParam(value = "inputVerifyCode")int inputVerifyCode,
                                         @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                                         @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken){
        //根据token获取用户信息
        String token = isNull(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        //查询访问次数,做限流防刷限制

        //检查用户输入的验证码
        boolean check = verifyCodeService.checkVerifyCode(user, goodsId, inputVerifyCode);
        if (!check){
            return Reslut.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //创建秒杀地址
        String str = miaoShaService.createPath(user, goodsId);
        return Reslut.success(CodeMsg.SUCCESS, str);
    }



    /*
     * 秒杀第三版本：优化秒杀功能,在第二版本基础上,使用redis预减库存,mq异步下单
     *              1.内存标记商品是否为空
     *              2.redis预减库存,原子递减
     *              3.mq异步下单
     * @parm: response
     * @parm: cookieToken
     * @parm: paramToken
     * @parm: goodsId
     * @return: Reslut<Integer>
     */

    @RequestMapping(value = "{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public  Reslut<Integer> doMiaoSha(HttpServletResponse response, @PathVariable("path")String path,
                            @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                            @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken,
                            @RequestParam("goodsId")Long goodsId){
        if (isNull(paramToken) && isNull(cookieToken)){
            logger.info("请先登录！");
            return Reslut.error(CodeMsg.SESSION_ERROR);
        }
        //根据token获取用户信息
        String token = isNull(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        if (isNull(user)){
            return Reslut.error(CodeMsg.SESSION_ERROR);
        }
        //验证path
        boolean check = miaoShaService.checkPath(user, goodsId, path);
        if (!check){
            return Reslut.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        Boolean over = localOverMap.get(goodsId);
        if (over){
            return Reslut.error(CodeMsg.MIAOSHA_OVER);
        }
        //预减库存,redis原子递减
        Long stock = redisService.decr(GoodsKey.getMiaoShaStock, "" + goodsId);
        if (stock < 0){
            localOverMap.put(goodsId, true);
            return Reslut.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否用户是否秒杀过了,redis中取
//        MiaoShaOrder miaoShaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getUserId(), goodsId);
        OrderInfo getOrderInfoByRedis = redisService.get(OrderKey.getOrderByUserIdGoodsId,
                "_" + user.getUserId() + "_" + goodsId, OrderInfo.class);
        if (notNull(getOrderInfoByRedis)){
            return Reslut.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //mq入队
        MiaoShaMessage mm = new MiaoShaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoShaMessage(mm);
        //排队中
        return Reslut.success(CodeMsg.SUCCESS, null);


    }



    /*
     * 轮询秒杀结果 orderId:秒杀成功;-1:秒杀失败;0:排队中
     */
    @RequestMapping(value = "result", method = RequestMethod.GET)
    @ResponseBody
    public  Reslut<Long> getMiaoshaResult(HttpServletResponse response,
                                             @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                                             @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken,
                                             @RequestParam("goodsId")Long goodsId){
        //根据token获取用户信息
        String token = isNull(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        if (isNull(user)){
            logger.info("登录失效！");
            return Reslut.error(CodeMsg.SESSION_ERROR);
        }
        Long result = orderService.getMiaoShaResult(user.getUserId(), goodsId);
        return Reslut.success(CodeMsg.SUCCESS, result);
    }




    /*
     * 秒杀第二版本：优化秒杀功能,采用ajax交互,实现静态资源304
     * @parm: response
     * @parm: cookieToken
     * @parm: paramToken
     * @parm: goodsId
     * @return: Reslut<OrderInfo>
     */
/*
    @RequestMapping(value = "do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public  Reslut<OrderInfo> doMiaoSha(HttpServletResponse response,
                            @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                            @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken,
                            @RequestParam("goodsId")Long goodsId){
        if (isNull(paramToken) && isNull(cookieToken)){
            logger.info("请先登录！");
            return Reslut.error(CodeMsg.SESSION_ERROR);
        }
        //根据token获取用户信息
        String token = isNull(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        if (isNull(user)){
            return Reslut.error(CodeMsg.SESSION_ERROR);
        }
        //判断秒杀的库存
        GoodsVo goods = goodsService.getMiaoShaById(goodsId);
        int stockCount = goods.getStockCount();
        if (stockCount <= 0){
            return Reslut.error(CodeMsg.MIAOSHA_OVER);
        }
        //判断是否用户是否秒杀过了,redis中取
//        MiaoShaOrder miaoShaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getUserId(), goodsId);
        OrderInfo getOrderInfoByRedis = redisService.get(OrderKey.getOrderByUserIdGoodsId,
                "_" + user.getUserId() + "_" + goodsId, OrderInfo.class);
        if (notNull(getOrderInfoByRedis)){
            return Reslut.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单 (事务原子性)
        OrderInfo orderInfo = miaoShaService.miaosha(user, goods);
        logger.info(user.getUserId() + "用户秒杀到了,订单号为：" + orderInfo.getId());
        return Reslut.success(CodeMsg.SUCCESS, orderInfo);
    }
*/



    /*
     * 秒杀第一版本：未优化秒杀功能,返回页面
     * @parm: user
     * @parm: model
     * @parm: goodsId
     */
/*
    @RequestMapping("do_miaosha")
    public String doMiaoSha2(Model model, HttpServletResponse response,
                            @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                            @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken,
                            @RequestParam("goodsId")Long goodsId){
        if (isNull(paramToken) && isNull(cookieToken)){
            logger.info("请先登录！");
            return "login";
        }
        //根据token获取用户信息
        String token = isNull(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        model.addAttribute("user", user);
//        if (isNull(user)){
//            return "login";
//        }
        //判断秒杀的库存
        GoodsVo goods = goodsService.getMiaoShaById(goodsId);
        int stockCount = goods.getStockCount();
        if (stockCount <= 0){
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否用户是否秒杀过了
        MiaoShaOrder miaoShaOrder = orderService.getMiaoShaOrderByUserIdGoodsId(user.getUserId(), goodsId);
        if (notNull(miaoShaOrder)){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
        }
        //减库存 下订单 写入秒杀订单 (事务原子性)
        OrderInfo orderInfo = miaoShaService.miaosha(user, goods);
        logger.info(user.getUserId() + "用户秒杀到了,订单号为：" + orderInfo.getId());
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return "order_detail";
    }
*/


}
