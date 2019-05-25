package com.duuuhs.miaosha_system.controller;

import com.duuuhs.miaosha_system.model.OrderInfo;
import com.duuuhs.miaosha_system.model.User;
import com.duuuhs.miaosha_system.redis.RedisService;
import com.duuuhs.miaosha_system.redis.UserKey;
import com.duuuhs.miaosha_system.result.CodeMsg;
import com.duuuhs.miaosha_system.service.GoodsService;
import com.duuuhs.miaosha_system.service.OrderService;
import com.duuuhs.miaosha_system.service.UserService;
import com.duuuhs.miaosha_system.vo.GoodsVo;
import com.duuuhs.miaosha_system.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.duuuhs.miaosha_system.result.Reslut;

import javax.servlet.http.HttpServletResponse;

import static com.duuuhs.miaosha_system.util.Assert.isNull;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;

	/*
	 * 获取订单信息
	 * @parm: response
	 * @parm: cookieToken
	 * @parm: paramToken
	 * @parm: orderId
	 * @return: Reslut<OrderDetailVo>
	 */
    @RequestMapping("/detail")
    @ResponseBody
    public Reslut<OrderDetailVo> info(HttpServletResponse response,
									  @CookieValue(value = UserKey.COOKIE_NAME_TOKEN, required = false) String cookieToken,
									  @RequestParam(value = UserKey.COOKIE_NAME_TOKEN, required = false) String paramToken,
									  @RequestParam("orderId") long orderId) {
		if (isNull(paramToken) && isNull(cookieToken)){
			return Reslut.error(CodeMsg.SESSION_ERROR);
		}
		//根据token获取用户信息
		String token = isNull(paramToken) ? cookieToken : paramToken;
		User user = userService.getByToken(response, token);
    	if(user == null) {
			return Reslut.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return Reslut.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.getMiaoShaById(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Reslut.success(CodeMsg.SUCCESS, vo);
    }
    
}
