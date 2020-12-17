package com.ds.kataframework.mvpviewable;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by sai on 2018/3/17.
 */

public interface BaseListViewable extends BaseDataViewable {
    void onLoadingMore(boolean isLoadingMore);
    void onItemClick(BaseQuickAdapter adapter, View view, int position);
    boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position);

}
