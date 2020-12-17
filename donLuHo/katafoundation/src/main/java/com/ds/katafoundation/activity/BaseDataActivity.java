package com.ds.katafoundation.activity;

import android.os.Bundle;

import com.ds.kataframework.mvppresenter.BaseDataPresenter;
import com.ds.kataframework.utils.TUtil;

public abstract class BaseDataActivity<P extends BaseDataPresenter> extends BaseActivity {
    private P presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    protected void initPresenter() {
        presenter =  TUtil.getT(this, 0);
        presenter.setVM(this,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    public P getPresenter() {
        return presenter;
    }

    public void onRefreshing(boolean refreshing) {

    }

    public void onStatusEmpty(String msg) {

    }

    public void onStatusLoading() {

    }

    public void onStatusError(int code, String msg, Object data) {

    }

    public void onStatusNetworkError(String msg) {

    }

    public void onDataSetChange(Object data, String msg) {

    }

    public void onLoadComplete() {

    }

}
