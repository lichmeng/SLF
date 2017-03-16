package com.sugar.myapplication.global;

import com.sugar.myapplication.App;

/**
 * Created by sugar on 2016/11/23.
 */

public class Constants {
    public static final String SDCARD_DIR = App.mContext.getExternalFilesDir(null).getAbsolutePath();
    public static final String FRAGMENT_CART = "cart";
    public static final String URL_DISCOVERY = "http://h5.m.jd.com" ;
    public static String[] categories = new String[]{
            "推荐分类","孕婴专区","潮流女装","品牌男装","各户化妆","家用电器","电脑办公","手机数码",
            "图书音像","家居家纺","居家生活","家具建材","食品生鲜","酒水饮料","运动户外","鞋靴箱包",
            "奢品礼品","钟表珠宝","玩具乐器","内衣配饰","汽车用品","医药保健","计生情趣","京东金融",
            "生活旅行","全球购","宠物农资","宝宝用品","玩具童车","个人护理"
    };

    public static String testJson = "{\n" +
            "\n" +
            "    \"productList\": [\n" +
            "        {\n" +
            "            \"id\": 9,\n" +
            "            \"marketPrice\": 300,\n" +
            "            \"name\": \"女鞋\",\n" +
            "            \"pic\": \"/images/product/detail/q7.jpg\",\n" +
            "            \"price\": 200\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 10,\n" +
            "            \"marketPrice\": 180,\n" +
            "            \"name\": \"韩版棉袄\",\n" +
            "            \"pic\": \"/images/product/detail/q8.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 11,\n" +
            "            \"marketPrice\": 180,\n" +
            "            \"name\": \"韩版秋装\",\n" +
            "            \"pic\": \"/images/product/detail/q9.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 12,\n" +
            "            \"marketPrice\": 180,\n" +
            "            \"name\": \"女士外套\",\n" +
            "            \"pic\": \"/images/product/detail/q10.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 13,\n" +
            "            \"marketPrice\": 300,\n" +
            "            \"name\": \"时尚女装\",\n" +
            "            \"pic\": \"/images/product/detail/q11.jpg\",\n" +
            "            \"price\": 200\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 14,\n" +
            "            \"marketPrice\": 300,\n" +
            "            \"name\": \"萌妹外套\",\n" +
            "            \"pic\": \"/images/product/detail/q12.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 15,\n" +
            "            \"marketPrice\": 300,\n" +
            "            \"name\": \"韩版粉嫩外套\",\n" +
            "            \"pic\": \"/images/product/detail/q13.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 16,\n" +
            "            \"marketPrice\": 400,\n" +
            "            \"name\": \"韩版秋装\",\n" +
            "            \"pic\": \"/images/product/detail/q14.jpg\",\n" +
            "            \"price\": 300\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 17,\n" +
            "            \"marketPrice\": 300,\n" +
            "            \"name\": \"春装新款\",\n" +
            "            \"pic\": \"/images/product/detail/q15.jpg\",\n" +
            "            \"price\": 200\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 18,\n" +
            "            \"marketPrice\": 200,\n" +
            "            \"name\": \"短裙\",\n" +
            "            \"pic\": \"/images/product/detail/q16.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 19,\n" +
            "            \"marketPrice\": 200,\n" +
            "            \"name\": \"新款秋装\",\n" +
            "            \"pic\": \"/images/product/detail/q17.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 20,\n" +
            "            \"marketPrice\": 200,\n" +
            "            \"name\": \"妈妈新款\",\n" +
            "            \"pic\": \"/images/product/detail/q18.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 21,\n" +
            "            \"marketPrice\": 300,\n" +
            "            \"name\": \"春秋新款外套\",\n" +
            "            \"pic\": \"/images/product/detail/q19.jpg\",\n" +
            "            \"price\": 200\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 22,\n" +
            "            \"marketPrice\": 200,\n" +
            "            \"name\": \"新款毛衣\",\n" +
            "            \"pic\": \"/images/product/detail/q20.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 23,\n" +
            "            \"marketPrice\": 150,\n" +
            "            \"name\": \"新款打底上衣\",\n" +
            "            \"pic\": \"/images/product/detail/q21.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 24,\n" +
            "            \"marketPrice\": 200,\n" +
            "            \"name\": \"春秋新款外套\",\n" +
            "            \"pic\": \"/images/product/detail/q22.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 25,\n" +
            "            \"marketPrice\": 150,\n" +
            "            \"name\": \"新款秋装\",\n" +
            "            \"pic\": \"/images/product/detail/q23.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 26,\n" +
            "            \"marketPrice\": 200,\n" +
            "            \"name\": \"粉色系暖心套装\",\n" +
            "            \"pic\": \"/images/product/detail/q24.jpg\",\n" +
            "            \"price\": 200\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 27,\n" +
            "            \"marketPrice\": 150,\n" +
            "            \"name\": \"韩版粉嫩外套\",\n" +
            "            \"pic\": \"/images/product/detail/q25.jpg\",\n" +
            "            \"price\": 160\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 28,\n" +
            "            \"marketPrice\": 300,\n" +
            "            \"name\": \"春装新款\",\n" +
            "            \"pic\": \"/images/product/detail/q26.jpg\",\n" +
            "            \"price\": 200\n" +
            "        }\n" +
            "    ],\n" +
            "    \"response\": \"search\"\n" +
            "\n" +
            "}";
}
