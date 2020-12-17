package com.ds.kataframework.mvpviewable;

public interface BaseDataViewable extends IBaseViewable {
    void onRefreshing(boolean refreshing);

    void onStatusEmpty(String msg);

    void onStatusLoading();

    void onStatusError(int code, String msg, Object data);

    void onStatusNetworkError(String msg);

    void onDataSetChange(Object data, String msg);

    void onLoadComplete();

    //###### 19/07/06 vicki添加，便于Presenter使用 start #############
    void showToast(String msg);

    void showToast(int msgId);
    //###### 19/07/06 vicki添加，便于Presenter使用  end #############
}
