package com.ds.katafoundation.fragment;

import androidx.core.content.ContextCompat;

import com.ds.katafoundation.R;
import com.ds.katafoundation.presenter.BaseListRAMPresenter;
import com.ds.kataframework.mvpviewable.BaseListViewable;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public abstract class BaseRefreshFragment<P extends BaseListRAMPresenter> extends BaseDataFragment<P> implements BaseListViewable{

    protected RefreshLayout refreshLayout;
    protected boolean refreshable = true;
    protected boolean loadmoreable = true;

    @Override
    protected void initView() {

        if (refreshable) {

            refreshLayout = findViewById(R.id.refreshLayout);

            MaterialHeader header = new MaterialHeader(getContext());
            header.setColorSchemeColors(new int[]{ContextCompat.getColor(getContext(), R.color.colorRefresh)});
            header.setPadding(0, 50, 0, 50);

            refreshLayout.setRefreshHeader(header);
            OnRefreshListener ptrHandler = getPresenter().getPtrHandler();
            if (ptrHandler != null) {
                refreshLayout.setOnRefreshListener(ptrHandler);
            }
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        if(loadmoreable){
            refreshLayout = findViewById(R.id.refreshLayout);
            refreshLayout.setEnableAutoLoadMore(true);
            ClassicsFooter footer = new ClassicsFooter(getContext());
            refreshLayout.setRefreshFooter(footer);
            refreshLayout.setOnLoadMoreListener(getPresenter().getLoadingMoreListener());
            refreshLayout.setFooterHeight(0);
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
        if (!getPresenter().isHasMore()) {
            refreshLayout.finishRefreshWithNoMoreData();
        }
    }

    @Override
    public void onLoadingMore(boolean isLoadingMore) {
        if (!isLoadingMore) {
            if (refreshLayout.isLoading()) {
                refreshLayout.finishLoadMore();
            }
        }
    }


}
