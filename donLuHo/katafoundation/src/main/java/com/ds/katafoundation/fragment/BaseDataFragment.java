package com.ds.katafoundation.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ds.kataframework.mvppresenter.BaseDataPresenter;
import com.ds.kataframework.utils.TUtil;


public abstract class BaseDataFragment <P extends BaseDataPresenter> extends BaseLazyFragment {
    private P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    protected void initPresenter() {
        presenter =  TUtil.getT(this, 0);
        presenter.setVM(this,getActivity());
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
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
