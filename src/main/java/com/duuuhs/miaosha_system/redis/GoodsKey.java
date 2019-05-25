package com.duuuhs.miaosha_system.redis;

/**
 * @Author: DMY
 * @Date: 2019/4/14 22:53
 * @Description:
 */
public class GoodsKey extends KeyPrefixImpl{

    //goods缓存有效期
    public static final int GOODS_EXPIRE = 60;


    private GoodsKey(String prefix) {
        super(prefix);
    }

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /*
     *商品信息列表,保存60s
     */
    public static GoodsKey getGoodsList = new GoodsKey(GOODS_EXPIRE, "goodsList");

    /*
     *某件商品详细信息,保存60s
     */
    public static GoodsKey getDetaiList = new GoodsKey(GOODS_EXPIRE, "goodsDetail");

    /*
     *秒杀商品的数量,永久保存,系统初始化就加载进redis
     */
    public static GoodsKey getMiaoShaStock = new GoodsKey(0, "miaoShaStock");

    /*
     *标记某件秒杀商品是否已售完
     */
    public static GoodsKey isGoodsOver = new GoodsKey(0, "goodsOver");

    /*
     *秒杀地址
     */
    public static GoodsKey getMiaoShaPath = new GoodsKey(60, "miaoShaPath");

    /*
     * 图片验证码
     */
    public static GoodsKey getMiaoshaVerifyCode = new GoodsKey(300, "miaoShaVerifyCode");
}
