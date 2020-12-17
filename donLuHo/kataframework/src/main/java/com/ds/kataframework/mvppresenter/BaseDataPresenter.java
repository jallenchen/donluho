package com.ds.kataframework.mvppresenter;

import com.ds.kataframework.bean.HttpResult;
import com.ds.kataframework.http.observer.HttpResultObserver;
import com.ds.kataframework.mvpviewable.BaseDataViewable;
import com.ds.kataframework.rx.MainThread;
import com.ds.kataframework.rx.RxBus;
import com.trello.rxlifecycle3.RxLifecycle;
import com.trello.rxlifecycle3.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public abstract class BaseDataPresenter<V extends BaseDataViewable> extends BasePresenter<V>{
    private final BehaviorSubject lifecycleSubject = BehaviorSubject.create();

    //刷新状态
    private boolean refreshing = false;
    //空数据状态
    private boolean statusEmpty = false;
    //加载中状态
    private boolean statusLoading = false;
    //错误状态
    private boolean statusError = false;
    //网络异常状态
    private boolean statusNetworkError = false;
    //控制loading状态只有一次,对于列表的loading概念，就是首次加载数据，其余加载是刷新
    private boolean once = false;

    public boolean isOnce() {
        return once;
    }

    public void setOnce(boolean once) {
        this.once = once;
    }

    public boolean isRefreshing() {
        return refreshing;
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
        view.onRefreshing(refreshing);
    }

    public void setRefreshingWithOutViewRefresh(boolean refreshing) {
        this.refreshing = refreshing;
    }

    public boolean isStatusEmpty() {
        return statusEmpty;
    }

    public void setStatusEmpty(String msg) {
        this.statusEmpty = true;
        view.onStatusEmpty(msg);
    }

    public boolean isStatusLoading() {
        return statusLoading;
    }

    public void setStatusLoading() {
        this.statusLoading = true;
        view.onStatusLoading();
    }

    public boolean isStatusError() {
        return statusError;
    }

    public void setStatusError(int code, String msg, Object data) {
        this.statusError = true;
        view.onStatusError(code,msg, data);
    }

    public boolean isStatusNetworkError() {
        return statusNetworkError;
    }

    public void setStatusNetworkError(String msg) {
        this.statusNetworkError = true;
        view.onStatusNetworkError(msg);
    }

    public void onLoadComplete(){
        view.onLoadComplete();
    }

    @Override
    public void onDestroy() {
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        super.onDestroy();
    }

    public HttpResultObserver callBack;

    /**
     * 网络请求
     * @return
     */
    public abstract<Data> Observable<HttpResult<Data>> onLoadDataHttpRequest();
    public abstract void onLoadData();

    public void onCallHttpRequest(Observable observable,Observer callBack){
        observable.compose(MainThread.io()).compose(RxLifecycle.bindUntilEvent(this.lifecycleSubject, ActivityEvent.DESTROY)).subscribe(callBack);
    }

    public <T> void toObservable(final Class<T> eventType,Consumer<? super T> onNext) {
        RxBus.getInstance().toObservable(eventType).compose(MainThread.io()).compose(RxLifecycle.bindUntilEvent(this.lifecycleSubject,ActivityEvent.DESTROY)).subscribe(onNext);
    }

    public void post(Object event){
        RxBus.getInstance().post(event);
    }
}
