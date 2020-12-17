package com.example.donLuHo.http;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JSInterface {
    @JavascriptInterface
    public void acceptUrl(String imgUrl) {//此方法是将android端获取的url返给js

    }
    @JavascriptInterface
    public void fnUrl(String s) {
        //js可以调用此方法 将s值传给android端，然后android端进行相应的操作，此参数可以是任意类型的
    }
    @JavascriptInterface
    public void  fnId(String received){
        //android 调用js js会回传参数
        Log.e("received--","---"+received);
    }

    @JavascriptInterface
    public void callAndroid(String param ) {
    }

    @JavascriptInterface
    public void getImage() {
        Log.e("TAG", "getImage");
    }
}
