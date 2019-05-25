package com.duuuhs.miaosha_system.dao;

import com.duuuhs.miaosha_system.model.MiaoShaOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MiaoShaOrderMapper {

    @Select("select * from miaosha_order")
    List<MiaoShaOrder> listALL();

    /*
     * 根据用户id与商品id查询秒杀订单消息
     */
    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    MiaoShaOrder getMiaoShaOrderByUserIdGoodsId(@Param("userId")Long userId,@Param("goodsId")Long goodsId);

    int deleteByPrimaryKey(Long id);

    int insert(MiaoShaOrder record);

    int insertSelective(MiaoShaOrder record);

    MiaoShaOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoShaOrder record);

    int updateByPrimaryKey(MiaoShaOrder record);
}