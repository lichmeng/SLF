package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

import java.util.List;

/**
 * Created by sugar on 2016/11/24.
 */

public class ProductResponse implements IResponse{

    /**
     * available : true
     * http://192.168.12.39:8080/RedBabyServer/images/product/detail/b2.jpg
     * bigPic : ["/imgaes/product/detail/b1.jpg","/images/product/detail/b2.jpg","/imgaes/product/detail/b3.jpg","/imgaes/product/detail/c1.jpg"]
     * buyLimit : 10
     * commentCount : 10
     * id : 5
     * inventoryArea : 仅限北京
     * leftTime : 14000
     * limitPrice : 68
     * marketPrice : 98
     * name : 时尚女裙
     * pics : ["/images/product/detail/a1.jpg","/images/product/detail/a2.jpg","/images/product/detail/a4.jpg"]
     * price : 108
     * productProperty : [{"id":1,"k":"颜色","v":"红色"},{"id":2,"k":"颜色","v":"绿色"},{"id":3,"k":"尺码","v":"M"},{"id":4,"k":"尺码","v":"XXL"},{"id":5,"k":"尺码","v":"XXXL"}]
     * score : 5
     */

    public ProductBean product;
    /**
     * product : {"available":true,"bigPic":["/imgaes/product/detail/b1.jpg","/imgaes/product/detail/b2.jpg","/imgaes/product/detail/b3.jpg","/imgaes/product/detail/c1.jpg"],"buyLimit":10,"commentCount":10,"id":5,"inventoryArea":"仅限北京","leftTime":14000,"limitPrice":68,"marketPrice":98,"name":"时尚女裙","pics":["/images/product/detail/a1.jpg","/images/product/detail/a2.jpg","/images/product/detail/a4.jpg"],"price":108,"productProperty":[{"id":1,"k":"颜色","v":"红色"},{"id":2,"k":"颜色","v":"绿色"},{"id":3,"k":"尺码","v":"M"},{"id":4,"k":"尺码","v":"XXL"},{"id":5,"k":"尺码","v":"XXXL"}],"score":5}
     * response : product
     */

    public String response;

    public static class ProductBean {
        public boolean available;
        public int buyLimit;
        public int commentCount;
        public int id;
        public String inventoryArea;
        public int leftTime;
        public int limitPrice;
        public int marketPrice;
        public String name;
        public int price;
        public int score;
        public List<String> bigPic;
        public List<String> pics;
        /**
         * id : 1
         * k : 颜色
         * v : 红色
         */

        public List<ProductPropertyBean> productProperty;

        public static class ProductPropertyBean {
            public int id;
            public String k;
            public String v;


        }
    }
}
