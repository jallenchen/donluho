package com.ds.katafoundation.presenter;


import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ds.katafoundation.mvpviewable.BaseListRAMViewable;
import com.ds.kataframework.mvppresenter.BaseListPresenter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;


/**
 * Created by sai on 2018/3/18.
 * refresh and loadMore 基类Presenter
 */

public abstract class BaseListRAMPresenter<V extends BaseListRAMViewable>  extends BaseListPresenter<V> {
    protected OnLoadMoreListener loadingMoreListener;
    protected OnRefreshListener ptrHandler;
    public OnLoadMoreListener getLoadingMoreListener(){
        if(loadingMoreListener == null) {
            loadingMoreListener = new OnLoadMoreListener() {
                @Override
                public void onLoadMore(RefreshLayout refreshLayout) {
                    if(!isLoadingMore()&&!isRefreshing()) {
                        onListLoadMore();
                    }

                }

            };
        }
        return loadingMoreListener;
    }

    public OnRefreshListener getPtrHandler() {
        if(ptrHandler == null){
            ptrHandler = new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    if(!isLoadingMore()&&!isRefreshing()) {
                        onLoadData();
                    }
                }
            };
        }
        return ptrHandler;
    }

}
