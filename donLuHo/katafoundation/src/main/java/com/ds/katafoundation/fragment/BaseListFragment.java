package com.ds.katafoundation.fragment;


import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ds.katafoundation.R;
import com.ds.kataframework.mvppresenter.BaseListPresenter;
import com.ds.kataframework.mvpviewable.BaseListViewable;

import java.util.List;

/**
 * Created by sai on 2018/3/18.
 */

public abstract class BaseListFragment<P extends BaseListPresenter> extends BaseDataFragment<P> implements BaseListViewable {
    protected RecyclerView recyclerView;
    protected BaseQuickAdapter adapter;

    @Override
    protected void initView() {

        recyclerView = findViewById(R.id.recyclerView);
        adapter = getAdapter();
        adapter.bindToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(getPresenter().getLayoutManager());
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void initData() {
        getPresenter().onLoadData();
    }

    @Override
    protected void initListener() {
        super.initListener();

        adapter.setOnItemClickListener(getPresenter().getOnItemClickListener());
        adapter.setOnItemChildClickListener(getPresenter().getOnItemChildClickListener());
    }

    protected abstract BaseQuickAdapter getAdapter();


    public void addData(Object data) {
        if (data == null) return;
        adapter.addData(data);
    }

    public void addData(int position, Object data) {
        if (data == null) return;
        adapter.addData(position, data);
        adapter.notifyItemRangeChanged(position + adapter.getHeaderLayoutCount(), adapter.getItemCount() - position);//通知数据与界面重新绑定
    }

    public Object getItem(int position) {
        return adapter.getItem(position);
    }

    @Override
    public void onRefreshing(boolean refreshing) {

    }
    @Override
    public void onLoadingMore(boolean isLoadingMore) {

    }
    public void addDatas(List datas) {
        if (datas == null || datas.isEmpty()) {
            getPresenter().setHasMore(false);
            return;
        }
        if (getPresenter().isFirstPage()) {
            adapter.setNewData(datas);
        } else {
            adapter.addData(datas);
        }
        if (datas.size() < getPresenter().getPageSize()) {
            getPresenter().setHasMore(false);
        }

    }


}
