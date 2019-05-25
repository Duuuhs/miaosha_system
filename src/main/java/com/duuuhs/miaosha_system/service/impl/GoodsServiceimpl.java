package com.duuuhs.miaosha_system.service.impl;

import com.duuuhs.miaosha_system.dao.GoodsMapper;
import com.duuuhs.miaosha_system.dao.MiaoShaGoodsMapper;
import com.duuuhs.miaosha_system.model.Goods;
import com.duuuhs.miaosha_system.model.MiaoShaGoods;
import com.duuuhs.miaosha_system.service.GoodsService;
import com.duuuhs.miaosha_system.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.duuuhs.miaosha_system.util.Assert.isNull;
/**
 * @Author: DMY
 * @Date: 2019/4/17 17:30
 * @Description: 商品服务实现类
 */
@Service
public class GoodsServiceimpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private MiaoShaGoodsMapper miaoShaGoodsMapper;


    /*
     * 获取所有秒杀商品信息
     * return: List
     */
    @Override
    public List<GoodsVo> listGoodVo() {
        return goodsMapper.listAllMiaoSha();
    }


    /*
     * 获取某件秒杀商品的信息
     * @Parm: goodsId
     * @return: GoodsVo
     */
    @Override
    public GoodsVo getMiaoShaById(Long goodsId){
        if (isNull(goodsId)){
            return null;
        }
        GoodsVo goodsVo = goodsMapper.getMiaoShaById(goodsId);
        return goodsVo;
    }

    /*
     * 减少秒杀商品的库存
     * @parm: goods
     */
    @Override
    public boolean reduceStock(Goods goods){
        MiaoShaGoods miaoShaGoods = new MiaoShaGoods();
        miaoShaGoods.setGoodsId(goods.getId());
        int i = miaoShaGoodsMapper.reduceStock(miaoShaGoods);
        return i>0;
    }
}
