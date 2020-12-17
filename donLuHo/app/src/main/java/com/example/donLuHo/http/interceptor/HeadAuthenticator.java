package com.example.donLuHo.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * desc   : 请求头拦截
 */
public class HeadAuthenticator implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

//        builder
//                .addHeader("Platform", "0")
//                .addHeader("VersionName", BuildConfig.VERSION_NAME)
//                .addHeader("Distribution",BuildConfig.CHANNEL_ID);
//                .addHeader(HttpKeyConstant.TERMINALHEADER, HttpKeyConstant.TERMINALHEADER_VALUE);
////                .addHeader("Connection", "keep-alive")
////                .addHeader("Accept", "*/*")
////                .addHeader(HttpKeyConstant.VERSION, AppInfoManager.versionName);
//
//        //如果登录了有token，就插入token
//        if (UserInfoManager.getInstance().isLogin() && TextUtils.isEmpty(authorizationNo)) {
//            builder.addHeader(HttpKeyConstant.AUTHORIZATION, UserInfoManager.getInstance().getAccessToken());
//        }
//        if(!TextUtils.isEmpty(BuildConfig.CHANNEL_ID)){
//            builder.addHeader(HttpKeyConstant.CHANNELID, BuildConfig.CHANNEL_ID);
//        }
//        if(!TextUtils.isEmpty(BuildConfig.PLATFORMCODE)){
//            builder.addHeader(HttpKeyConstant.PLATFORMCODE, BuildConfig.PLATFORMCODE);
//        }


            Request request = builder.build();
        return chain.proceed(request);


    }
}
