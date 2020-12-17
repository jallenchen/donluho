package com.ds.kataframework.bean;

import com.google.gson.annotations.SerializedName;

public class ResultBean<T> {
    @SerializedName(value = "result",alternate = {"list","data","history"})
    T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
