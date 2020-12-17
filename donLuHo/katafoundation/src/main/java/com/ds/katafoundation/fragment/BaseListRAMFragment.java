package com.ds.katafoundation.fragment;

import android.view.View;

import androidx.core.content.ContextCompat;

import com.ds.katafoundation.R;
import com.ds.katafoundation.presenter.BaseListRAMPresenter;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;


/**
 * Created by sai on 2018/3/18.
 */

public abstract class BaseListRAMFragment<P extends BaseListRAMPresenter> extends BaseListFragment<P> {

    protected RefreshLayout refreshLayout;
    protected boolean refreshable = true;
    protected boolean loadmoreable = true;

    @Override
    protected void initView() {
        super.initView();

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
            ClassicsFooter.REFRESH_FOOTER_NOTHING = getString(R.string.srl_footer_nothing);
            ClassicsFooter footer = new ClassicsFooter(getContext());
            refreshLayout.setRefreshFooter(footer);
            refreshLayout.setOnLoadMoreListener(getPresenter().getLoadingMoreListener());
            refreshLayout.setEnableFooterFollowWhenNoMoreData(true);

//            refreshLayout.setFooterHeight(0);
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

    public void addDatas(List datas) {
        if (datas == null || datas.isEmpty()) {
            getPresenter().setHasMore(false);
            refreshLayout.finishLoadMoreWithNoMoreData();
            return;
        }
        if (getPresenter().isFirstPage()) {
            adapter.setNewData(datas);
        } else {
            adapter.addData(datas);
        }
        if (datas.size() < getPresenter().getPageSize()) {
            getPresenter().setHasMore(false);
            refreshLayout.finishLoadMoreWithNoMoreData();
        }

    }
    @Override
    public void onLoadComplete() {
//        if (refreshLayout.isLoading())
            refreshLayout.finishLoadMore();
        if (!getPresenter().isHasMore()) {
            refreshLayout.finishLoadMoreWithNoMoreData();
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
