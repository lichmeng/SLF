package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

/**
 * Created by wangyu on 2016/11/24.
 */

public class ProductListInfo implements IResponse,Comparable{
    /**
     * id : 12
     * marketPrice : 180
     * name : 女士外套
     * pic : /images/product/detail/q10.jpg
     * price : 160
     */

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

    @Override
    public int compareTo(Object another) {
        ProductListInfo info = (ProductListInfo) another;
        int result = this.price - info.price;
        return result > 0? (this.getId() - info.getId()>0 ? -1:1):result;
    }
}
