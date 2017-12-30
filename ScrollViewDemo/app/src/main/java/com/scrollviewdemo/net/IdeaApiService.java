package com.scrollviewdemo.net;


import com.scrollviewdemo.model.GankGay;
import com.scrollviewdemo.model.MyIncomeBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dell on 2017/4/1.
 */
public interface IdeaApiService {
    /**
     * 网络请求超时时间毫秒
     */
    int DEFAULT_TIMEOUT = 20000;
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("data/{category}/{pagesize}/{pagenum}")
    Observable<GankGay> getContent(@Path("category") String category, @Path("pagesize") String pagesize, @Path("pagenum") int pagenum);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @GET("mine/rebate_history")
    Observable<MyIncomeBean> getMyincome(@Query("pageNow") int pageNow);

}
