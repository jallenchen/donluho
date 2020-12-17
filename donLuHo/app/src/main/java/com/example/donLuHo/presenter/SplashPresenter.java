package com.example.donLuHo.presenter;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ds.imageloader.ImageLoader;
import com.ds.kataframework.http.observer.HttpResultObserver;
import com.ds.kataframework.mvppresenter.BaseDetailPresenter;
import com.example.donLuHo.MainApp;
import com.example.donLuHo.bean.BaseH5Bean;
import com.example.donLuHo.bean.StartupsImageBean;
import com.example.donLuHo.bean.UpdateBean;
import com.example.donLuHo.http.HttpServiceGenerator;
import com.example.donLuHo.http.api.API;
import com.example.donLuHo.util.SharePreferenceUtil;
import com.example.donLuHo.viewale.MainViewable;
import com.example.donLuHo.viewale.SplashViewable;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class SplashPresenter extends BaseDetailPresenter<SplashViewable> {
    @Override
    public Observable<Object> onLoadDataHttpRequest() {
        return null;
    }


    public void getBaseUrls() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("handicap_code", 0);
        onCallHttpRequest(HttpServiceGenerator.create(API.class).getBaseUrls(map), new HttpResultObserver<BaseH5Bean>(){
            @Override
            public void onHttpSuccess(BaseH5Bean result, String msg) {
                super.onHttpSuccess(result, msg);
                getView().onRespH5Url(result);
            }
        });

    }

    public void loadStartupsImage() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("handicap_code", 0);
        onCallHttpRequest(HttpServiceGenerator.create(API.class).loadStartupsImage(), new HttpResultObserver<List<StartupsImageBean>>(){
            @Override
            public void onHttpSuccess(final List<StartupsImageBean> result, String msg) {
                super.onHttpSuccess(result, msg);
                if(result!=null && !result.isEmpty()) {
                    ImageLoader.downloadImage(MainApp.getInstance(), result.get(0).getUrl(), new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            SharePreferenceUtil.put(MainApp.getInstance(), "loadStartupsImage", result.get(0).getUrl());
                            SharePreferenceUtil.put(MainApp.getInstance(), "loadStartupsSeconds", result.get(0).getSeconds());
                            return false;
                        }
                    });
                }
            }
        });

    }
}
