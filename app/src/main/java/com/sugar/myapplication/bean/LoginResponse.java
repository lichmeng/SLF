package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

/**
 * Created by wjx on 2016/11/23.
 */

public class LoginResponse implements IResponse {

    /**
     * response : login
     * userInfo : {"userid":"20428"}
     */

    public String response;
    /**
     * userid : 20428
     */

    public Users userInfo;



    public static class Users {
        public String userid;
    }
}
