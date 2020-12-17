package com.example.donLuHo.http.api;


import com.ds.kataframework.bean.HttpResult;
import com.ds.kataframework.bean.ResultBean;
import com.example.donLuHo.bean.BaseH5Bean;
import com.example.donLuHo.bean.StartupsImageBean;
import com.example.donLuHo.bean.UpdateBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface API {

    /**
     * 获取H5地址
     *
     * @return
     */
    @GET("SixLotteryNoLogin/getBaseUrls")
    Observable<HttpResult<BaseH5Bean>> getBaseUrls(@QueryMap HashMap<String, Object> map);


    /**
     * 版本升级
     *
     * @return
     */
    @GET("Expert/lastPackageInfo")
    Observable<HttpResult<UpdateBean>> checkVersionUpdate(@QueryMap HashMap<String, Object> map);

    /**
     * 启动图
     *
     * @return
     */
    @GET("Expert/startups")
    Observable<HttpResult<List<StartupsImageBean>>> loadStartupsImage();

}

