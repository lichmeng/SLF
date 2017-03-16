package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

import java.util.List;

/**
 * Created by sugar on 2016/11/25.
 */

public class SearchResultResponse implements IResponse{

    /**
     * productList : [{"id":9,"marketPrice":300,"name":"女鞋","pic":"/images/product/detail/q7.jpg","price":200},{"id":10,"marketPrice":180,"name":"韩版棉袄","pic":"/images/product/detail/q8.jpg","price":160},{"id":11,"marketPrice":180,"name":"韩版秋装","pic":"/images/product/detail/q9.jpg","price":160},{"id":12,"marketPrice":180,"name":"女士外套","pic":"/images/product/detail/q10.jpg","price":160},{"id":13,"marketPrice":300,"name":"时尚女装","pic":"/images/product/detail/q11.jpg","price":200},{"id":14,"marketPrice":300,"name":"萌妹外套","pic":"/images/product/detail/q12.jpg","price":160},{"id":15,"marketPrice":300,"name":"韩版粉嫩外套","pic":"/images/product/detail/q13.jpg","price":160},{"id":16,"marketPrice":400,"name":"韩版秋装","pic":"/images/product/detail/q14.jpg","price":300},{"id":17,"marketPrice":300,"name":"春装新款","pic":"/images/product/detail/q15.jpg","price":200},{"id":18,"marketPrice":200,"name":"短裙","pic":"/images/product/detail/q16.jpg","price":160}]
     * response : search
     */

    private String response;
    /**
     * id : 9
     * marketPrice : 300
     * name : 女鞋
     * pic : /images/product/detail/q7.jpg
     * price : 200
     */

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
}
