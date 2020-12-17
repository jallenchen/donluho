package com.ds.kataframework.mvppresenter;

import com.ds.kataframework.http.observer.HttpResultObserver;
import com.ds.kataframework.mvpviewable.BaseDetailViewable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public abstract class BaseDetailPresenter<V extends BaseDetailViewable> extends BaseDataPresenter<V> {
    Object data;

    public void setData(Object data, String msg) {
        this.data = data;
        view.onDataSetChange(data, msg);
    }

    public Object getData() {
        return data;
    }

    /**
     * 刷新/加载数据
     */
    public void onLoadData() {
        setRefreshing(true);
        //这里考虑到首次加载是 loading，以后加载是refresh 模式
        if (!isOnce())
            setStatusLoading();
        onRefreshWithOutViewRefresh();
    }

    public void onRefreshWithOutViewRefresh(){
        setRefreshingWithOutViewRefresh(true);
        Observable observable = onLoadDataHttpRequest();
        if (observable != null) {
            onCallHttpRequest(observable, callBack);
        }

    }
    public void onRefreshWithOutViewRefreshDelay(){
        setRefreshingWithOutViewRefresh(true);
        Observable observable = onLoadDataHttpRequest().delay(1, TimeUnit.SECONDS);
        if (observable != null) {
            onCallHttpRequest(observable, callBack);
        }

    }


    public HttpResultObserver callBack = new HttpResultObserver<Object>() {
        @Override
        public void onHttpSuccess(Object resultData, String msg) {
            if (resultData != null) {
                setData(resultData, msg);
            } else {
                setStatusEmpty(msg);
            }
        }

        @Override
        public void onHttpFail(int code, String msg, Object data) {
            setStatusError(code, msg, data);
        }

        @Override
        public void onNetWorkError(String msg) {
            setStatusNetworkError(msg);
        }

        @Override
        public void onComplete() {
            setOnce(true);
            setRefreshing(false);
            onLoadComplete();
        }
    };
}
