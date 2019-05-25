package com.duuuhs.miaosha_system.dao;

import com.duuuhs.miaosha_system.model.MiaoShaGoods;
import com.duuuhs.miaosha_system.vo.TwoTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MiaoShaGoodsMapper {

    @Select("select * from miaosha_goods")
    List<MiaoShaGoods> listAll();

    @Select("SELECT m.*,u.user_id, u.nickname from miaosha_goods m LEFT JOIN user u on u.login_count = m.id")
    List<TwoTable> listTwoTable();

    /*
     * 减少秒杀商品库存
     */
    @Update("update miaosha_goods set stock_count = stock_count-1 where goods_id = #{goodsId} and stock_count > 0")
    int reduceStock(MiaoShaGoods miaoShaGoods);

    int deleteByPrimaryKey(Long id);

    int insert(MiaoShaGoods record);

    int insertSelective(MiaoShaGoods record);

    MiaoShaGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MiaoShaGoods record);

    int updateByPrimaryKey(MiaoShaGoods record);

}