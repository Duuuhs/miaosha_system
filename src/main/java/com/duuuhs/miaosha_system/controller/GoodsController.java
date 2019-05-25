package com.duuuhs.miaosha_system.controller;

import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.GoodsKey;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.redis.UserKey;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.service.GoodsService;
import com.duuuhs.miaosha_system.service.UserService;
import com.duuuhs.miaosha_system.vo.GoodsDetailVo;
import com.duuuhs.miaosha_system.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static com.duuuhs.miaosha_system.util.Assert.isNull;
import static com.duuuhs.miaosha_system.util.Assert.notNull;
import com.duuuhs.miaosha_system.result.Reslut;

/**
 * @Author: DMY
 * @Date: 2019/4/16 15:58
 * @Description: 商品控制器
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    //用于手动渲染视图
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /*
     * 跳转到商品列表,此页面用到了页面缓存方案
     * @parm: model
     * @parm: response
     * @parm: request
     * @parm: cookieToken
     * @parm: paramToken
     * @return: String
     */
    @RequestMapping(value = "to_goods_list", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toGoodsList(Model model, HttpServletResponse response, HttpServletRequest request,
                              @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
                              @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken){

//        return "goods_list";

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (notNull(html)){
            return html;
        }

        //无缓存
        //根据token获取用户信息
        String token = isNull(paramToken) ? cookieToken : paramToken;
        User user = userService.getByToken(response, token);
        model.addAttribute("user", user);
        //获取商品信息
        List<GoodsVo> goodsVos = goodsService.listGoodVo();
        model.addAttribute("goodsVos", goodsVos);
        //手动渲染
        WebContext ctx = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (notNull(html)){
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }


    /*
     * 跳转到商品详情页面,此方法用到了页面缓存方案
     * @parm: goods_id
     * @Parm: user
     * @Parm: model
     * @return: String
     */
    @RequestMapping(value = "to_detail/{goods_id}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail(HttpServletResponse response, HttpServletRequest request,
                           @PathVariable("goods_id") Long goods_id, User user, Model model){

        //取缓存
        String html = redisService.get(GoodsKey.getDetaiList, ""+goods_id, String.class);
        if (notNull(html)){
            return html;
        }
        //无缓存
        GoodsVo goods = goodsService.getMiaoShaById(goods_id);
        int miaoshaStatus;//秒杀状态
        int remainSeconds;//距离秒杀开始时间
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        if (now < startAt){
            //秒杀未开始,倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)(startAt - now)/1000;
        } else if (now > endAt){
            //秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        } else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("goods", goods);
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
//        return "goods_detail";

        //手动渲染
        WebContext ctx = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (notNull(html)){
            redisService.set(GoodsKey.getDetaiList, ""+goods_id, html);
        }
        return html;
    }



    /*
     * 跳转到商品详情页面,此方法前端用ajax进行数据交互
     * @parm: goods_id
     * @Parm: user
     * @Parm: model
     * @return: String
     */
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Reslut<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user,
                                        @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getMiaoShaById(goodsId);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoodsVo(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Reslut.success(CodeMsg.SUCCESS, vo);
    }


    /*
     * 项目配置了WenConfig,实现了WebMvcConfigurer完成参数校验，可惜一直跑不到配置中去，暂时注释
     */
    @RequestMapping("/to_goods_list1")
    public String toGoods(User user, Model model){
        if (isNull(user.getUserId())){
            return "login";
        }
        model.addAttribute("user", user);
        return "goods_list";
    }

}
