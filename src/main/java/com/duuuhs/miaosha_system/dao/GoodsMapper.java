package com.duuuhs.miaosha_system.dao;

import com.duuuhs.miaosha_system.model.Goods;
import com.duuuhs.miaosha_system.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper {
    @Select("select * from goods")
    List<Goods> listAll();

    @Select("SELECT g.*,mg.miaosha_price, mg.stock_count, mg.start_date, mg.end_date from miaosha_goods mg LEFT JOIN goods g on g.id = mg.goods_id")
    List<GoodsVo> listAllMiaoSha();

    @Select("SELECT g.*,mg.miaosha_price, mg.stock_count, mg.start_date, mg.end_date from miaosha_goods mg LEFT JOIN goods g on g.id = mg.goods_id where g.id = #{goodsId}")
    GoodsVo getMiaoShaById(@Param("goodsId") Long goodsId);


    int deleteByPrimaryKey(Long id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);
}