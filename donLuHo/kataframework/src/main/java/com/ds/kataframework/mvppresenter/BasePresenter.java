package com.ds.kataframework.mvppresenter;

import android.content.Context;


public abstract class BasePresenter<V> {
    protected Context context;
    protected V view;

    public void setVM(V v,  Context context) {
        this.context = context;
        this.view = v;
    }

    public void onDestroy() {
        context = null;
        view = null;
    }

    public Context getContext() {
        return context;
    }

    public V getView() {
        return view;
    }

}