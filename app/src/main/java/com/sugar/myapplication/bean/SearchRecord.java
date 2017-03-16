package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

/**
 * Created by wjx on 2016/11/23.
 * 搜索记录的业务bean
 */

public class SearchRecord implements IResponse {
    private String userId;//用户id
    private String searchName;//搜索名称
    private int searchCount;//搜索次数
    private int searchTime;//搜索时间

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public int getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(int searchTime) {
        this.searchTime = searchTime;
    }
}
