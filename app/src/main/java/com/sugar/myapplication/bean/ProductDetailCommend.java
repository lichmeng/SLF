package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

import java.util.List;

/**
 * Created by wjx on 2016/11/25.
 */

public class ProductDetailCommend implements IResponse {

    /**
     * comment : [{"content":"裙子不错裙子不错裙子不错裙子不错裙子不错裙子不错裙子","time":2014,"title":"裙子","username":"赵刚"}]
     * response : productComment
     */

    public String response;
    /**
     * content : 裙子不错裙子不错裙子不错裙子不错裙子不错裙子不错裙子
     * time : 2014
     * title : 裙子
     * username : 赵刚
     */

    public List<CommentBean> comment;


    public static class CommentBean {
        public String content;
        public int time;
        public String title;
        public String username;

    }
}
