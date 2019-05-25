package com.duuuhs.miaosha_system.service;

import com.duuuhs.miaosha_system.model.Goods;
import com.duuuhs.miaosha_system.vo.GoodsVo;

import java.util.List;

/**
 * @Author: DMY
 * @Date: 2019/4/17 17:30
 * @Description: 商品服务
 */
public interface GoodsService {

    /*
     * 获取所有商品信息和他们的秒杀信息
     * @return: List
     */
    List<GoodsVo> listGoodVo();


    /*
     * 获取某件商品的信息
     * @Parm: goodsId
     * @return: GoodsVo
     */
    GoodsVo getMiaoShaById(Long goodsId);

    /*
     * 减少秒杀善品的库存
     * @parm: goods
     */
    boolean reduceStock(Goods goods);
}
