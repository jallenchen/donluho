package com.ds.kataframework.mvppresenter;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ds.kataframework.bean.ResultBean;
import com.ds.kataframework.http.observer.HttpResultObserver;
import com.ds.kataframework.mvpviewable.BaseListViewable;

import java.util.List;

import io.reactivex.Observable;

public abstract class BaseListPresenter<V extends BaseListViewable> extends BaseDataPresenter<V> {
    protected boolean dataEmpty;
    protected final int firstPage = 1;
    //分页页码
    protected int page = firstPage;
    //分页每页Item数量
    public static final int PAGESIZE_DEFULT = 20;
    protected int pageSize = PAGESIZE_DEFULT;
    protected boolean isHasMore = true;
    protected boolean isLoadingMore = false;
    protected BaseQuickAdapter.OnItemClickListener onItemClickListener;
    protected BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener;


    public int getFirstPage() {
        return firstPage;
    }

    public boolean isFirstPage() {
        return firstPage == page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasMore() {
        return isHasMore;
    }

    public void setHasMore(boolean hasMore) {
        isHasMore = hasMore;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        isLoadingMore = loadingMore;
        view.onLoadingMore(loadingMore);
    }

    public void setPageAdd() {
        page++;
    }

    public void addData(Object data, String msg) {
        if ((data == null || ((data instanceof List) && ((List) data).isEmpty()))) {
            if(dataEmpty) {
                view.onStatusEmpty(msg);
            }
            setHasMore(false);
        }
        else  {
            dataEmpty = false;
            if (data != null)
                view.onDataSetChange(data, msg);
        }
    }

    public boolean isDataEmpty() {
        return dataEmpty;
    }

    public void clearDatas() {
        dataEmpty = true;
    }

    /**
     * 这里重写onLoadData，列表不能直接onLoadData，而是刷新
     */
    @Override
    public void onLoadData() {
        onListRefresh();
    }

    /**
     * 加载更多
     */
    public void onLoadMore() {
        onListLoadMore();
    }

    /**
     * 刷新数据
     */
    public void onListRefresh() {
        setRefreshing(true);
        //把分页配置还原成加载第一页状态
        setPage(getFirstPage());
        setHasMore(true);
        setLoadingMore(false);
        if (!isOnce())
            setStatusLoading();
        try {
            onLoadHttpData();
        } catch (NullPointerException e) {
        }
    }

    public void onRefreshWithOutViewRefresh(){
        setRefreshingWithOutViewRefresh(true);
        //把分页配置还原成加载第一页状态
        setPage(getFirstPage());
        setHasMore(true);
        setLoadingMore(false);

        onLoadHttpData();
    }


    /**
     * 加载数据
     */
    public void onListLoadMore() {
        //判断是否已经在进行加载更多 或 没有更多了，是则直接返回等待加载完成。
        if (isLoadingMore() || !isHasMore()) return;
        //刷新中也直接返回不加载更多
        if (isRefreshing()) return;
        setLoadingMore(true);
        //分页增加
        setPageAdd();

        onLoadHttpData();
    }

    /**
     * 刷新/加载数据
     */
    public void onLoadHttpData() {
        Observable observable = onLoadDataHttpRequest();
        if (observable != null) {
            onCallHttpRequest(observable, callBack);
        }
    }


    public HttpResultObserver callBack = new HttpResultObserver<Object>() {
        @Override
        public void onHttpSuccess(Object resultData, String msg) {

            //如果是第一页就清空旧数据再加入新数据
            if (isFirstPage()) {
                clearDatas();
            }
            if (resultData != null) {
                //为了兼容接口不规范的嵌套问题
                if(resultData instanceof ResultBean){
                    ResultBean resultBean = (ResultBean) resultData;
                    addData(resultBean.getResult(),msg);
                }
                else
                    addData(resultData, msg);
            } else {
                setHasMore(false);
                if (isDataEmpty()) {
                    setStatusEmpty(msg);
                }
            }
        }

        @Override
        public void onHttpFail(int code, String msg, Object data) {
            setStatusError(code, msg, data);
        }

        @Override
        public void onNetWorkError(String msg) {
            setStatusNetworkError(msg);
        }

        @Override
        public void onComplete() {
            setOnce(true);

            setRefreshing(false);

            if (isFirstPage())//因为在刷新之前已经把page设为了firstPage，所以可以判断isFirstPage()来判断当前是否刷新
                setRefreshing(false);
            else
                setLoadingMore(false);

            onLoadComplete();
        }
    };

    public BaseQuickAdapter.OnItemClickListener getOnItemClickListener() {
        if (onItemClickListener == null) {
            onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    getView().onItemClick(adapter, view, position);
                }
            };
        }

        return onItemClickListener;
    }

    public BaseQuickAdapter.OnItemChildClickListener getOnItemChildClickListener() {
        if (onItemChildClickListener == null) {
            onItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    getView().onItemChildClick(adapter, view, position);
                }
            };
        }
        return onItemChildClickListener;
    }


    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(context);
    }

}
