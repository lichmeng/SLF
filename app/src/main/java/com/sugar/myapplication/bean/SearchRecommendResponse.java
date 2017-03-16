package com.sugar.myapplication.bean;

import org.senydevpkg.net.resp.IResponse;

import java.util.List;

/**
 * Created by wjx on 2016/11/24.
 */

public class SearchRecommendResponse implements IResponse {

    /**
     * response : searchrecommend
     * searchKeywords : ["粉色毛衣","女裙","帽子","时尚女裙","时尚秋装","韩版外套","情女装"]
     */

    public String response;
    public List<String> searchKeywords;


}
