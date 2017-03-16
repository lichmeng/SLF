package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

import java.util.List;

/**
 * Created by wangyu on 2016/11/23.
 */

public class TopicPListResponse implements IResponse {

    /**
     * productList : [{"id":15,"marketPrice":300,"name":"韩版粉嫩外套","pic":"/images/product/detail/q13.jpg","price":160},{"id":16,"marketPrice":400,"name":"韩版秋装","pic":"/images/product/detail/q14.jpg","price":300},{"id":22,"marketPrice":200,"name":"新款毛衣","pic":"/images/product/detail/q20.jpg","price":160},{"id":23,"marketPrice":150,"name":"新款打底上衣","pic":"/images/product/detail/q21.jpg","price":160},{"id":37,"marketPrice":200,"name":"智力圆环","pic":"/images/product/detail/q26.jpg","price":200},{"id":38,"marketPrice":180,"name":"音乐小天才","pic":"/images/product/detail/q26.jpg","price":160},{"id":39,"marketPrice":180,"name":"小叮当","pic":"/images/product/detail/q26.jpg","price":160},{"id":40,"marketPrice":180,"name":"智力图形记忆起","pic":"/images/product/detail/q26.jpg","price":160}]
     * response : topicProductList
     */

    private String response;
    private List<ProductListInfo> productList;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<ProductListInfo> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListInfo> productList) {
        this.productList = productList;
    }

    /*public static class ProductListBean {
        *//**
         * id : 15
         * marketPrice : 300
         * name : 韩版粉嫩外套
         * pic : /images/product/detail/q13.jpg
         * price : 160
         *//*

        private int id;
        private int marketPrice;
        private String name;
        private String pic;
        private int price;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(int marketPrice) {
            this.marketPrice = marketPrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }*/
}
