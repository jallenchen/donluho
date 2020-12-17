package com.ds.katafoundation.fragment;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.ds.katafoundation.R;
import com.ds.kataframework.mvppresenter.BaseDetailPresenter;
import com.ds.kataframework.mvpviewable.BaseDetailViewable;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public abstract class BaseRefreshDetailFragment<P extends BaseDetailPresenter> extends BaseDetailFragment<P> implements BaseDetailViewable {
    protected RefreshLayout refreshLayout;
    protected boolean refreshable = true;

    @Override
    protected void initView() {

        if (refreshable) {

            refreshLayout = findViewById(R.id.refreshLayout);

            MaterialHeader header = new MaterialHeader(getContext());
            header.setColorSchemeColors(new int[]{ContextCompat.getColor(getContext(), R.color.colorRefresh)});
            header.setPadding(0, 50, 0, 50);

            refreshLayout.setRefreshHeader(header);
            OnRefreshListener ptrHandler = new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    if(!getPresenter().isRefreshing()) {
                        getPresenter().onLoadData();
                    }
                }
            };
            if (ptrHandler != null) {
                refreshLayout.setOnRefreshListener(ptrHandler);
            }
        }
    }
    @Override
    public void onRefreshing(boolean refreshing) {
        if (refreshLayout!=null){
            if (refreshing) {
                if (!refreshLayout.isRefreshing())
                    refreshLayout.autoRefresh();
            } else {
                refreshLayout.finishRefresh();
            }
        }

    }

    @Override
    public void onLoadComplete() {
        if (refreshLayout.isLoading())
            refreshLayout.finishLoadMore();
    }

}
