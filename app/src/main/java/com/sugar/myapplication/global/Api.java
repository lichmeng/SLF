package com.sugar.myapplication.global;

/**
 * Created by xiongmc on 2016/11/21.
 */
public interface Api {
     String URL_SERVER = "http://192.168.12.26/RedBabyServer";
     //String URL_SERVER = "http://92.11.0.39:8080/RedBabyServer";

    //商品分类列表
    String URL_CATEGORY= URL_SERVER + "/category";

    //     int REQUEST_CODE_TOPIC = 100;
    int REQUEST_CODE_CATEGORY = 104;

    // 促销快报
    String URL_TOPIC = URL_SERVER + "/topic";
    String URL_LOGIN = URL_SERVER + "/login";
    //专题商品列表
    String URL_TOPIC_LIST = URL_SERVER + "/topic/plist";
    String URL_ADDRESSLIST = URL_SERVER + "/addresslist";
    //推荐品牌
    String URL_RECOMMENDED_BRAND = URL_SERVER + "/brand";
    //轮转大图，点击进入专题商品列表页
    String URL_HOME = URL_SERVER + "/home";
    //搜索推荐
    String URL_RECOMMEND = URL_SERVER + "/search/recommend";
    ///搜索商品列表
    String URL_SEARCH = URL_SERVER + "/search";
    String URL_SEARCH_TEST= URL_SERVER + "/search?keyword=?page=1?pageNum=20?orderby=saleDown";
    //限时抢购
    String URL_LIMITBUY = URL_SERVER + "/limitbuy";
    //新品上架
    String URL_NEW_PRODUCT = URL_SERVER + "/newproduct";
    //热门单品
    String URL_HOT_PRODUCT = URL_SERVER + "/hotproduct";
    //商品详情
    String URL_PRODUCT_DETAIL = URL_SERVER + "/product";
    //商品评论
    String URL_PRODUCT_COMMENT= URL_SERVER + "/product/comment";
    //商品描述
    String URL_PRODUCT_DES = URL_SERVER + "/product/description";
    //升级url
    String URL_UPDATA = URL_SERVER + "/config.json";
    //品牌商品
    String URL_BRAND_LIST = URL_SERVER + "/brand/plist";
    //wjx
    int REQUEST_CODE_LOGIN = 501;
    int REQUEST_CODE_REGISTER = 502;
    int REQUEST_CODE_PRODUCT_DETAIL = 504;
    int REQUEST_CODE_PRODUCT_DES = 505;
    int REQUEST_CODE_PRODUCT_COMMEND = 506;
    int REQUEST_CODE_SEARCH = 503;
    //搜索推荐关键字
    int REQUEST_CODE_SEARCH_RECOMMEND = 507;
    //内容观察者uri
    String CONTENT_OBSERVER_URI="content://com.sugar.myapplication";

    int REQUEST_CODE_ADDRESSLIST = 102;
    int REQUEST_CODE_SEARCH_LIST = 103;

    /*
         以下为:200-299号段
     */
    //购物车
    String URL_CART = URL_SERVER + "/cart";
    int REQUEST_CODE_CART = 201;

    /*
         以下为:1-99号段
     */
    int REQUEST_CODE_TOPIC = 10;
    int REQUEST_CODE_PROMOTIONS = 11;
    int REQUEST_CODE_PROMOTIONS_PLIST = 12;
    int REQUEST_CODE_LIMITBUY = 13;
    int REQUEST_CODE_RECOMMENDED_BRAND = 14;
    int REQUEST_CODE_NEW_PRODUCT = 15;
    int REQUEST_CODE_HOT_PRODUCT = 16;
    int REQUEST_CODE_BRAND_LIST = 17;

}
