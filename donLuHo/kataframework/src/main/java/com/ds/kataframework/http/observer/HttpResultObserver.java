package com.ds.kataframework.http.observer;

import android.accounts.NetworkErrorException;

import com.ds.kataframework.bean.HttpResult;
import com.ds.kataframework.http.constants.HttpStatusConstants;
import com.ds.kataframework.rx.RxBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 网络结果预处理
 */
public class HttpResultObserver<T> implements Observer<HttpResult<T>> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(HttpResult<T> result) {
        if (result == null) {
            //通常是服务器出错返回了非约定格式
            onHttpFail(HttpStatusConstants.CODE_DEFAULT,"网络错误,返回非正常格式，请稍后再试", null);
        } else {
            if (result.getCode() == HttpStatusConstants.CODE_SUCCESS) {
                //正确返回约定的CODE_SUCCESS码
                onHttpSuccess(result.getResult(),result.getMsg());
            }
            else {
                //返回约定的其他类型码，可根据返回码进行相对应的操作
                if(result.getCode() == 401){
                    RxBus.getInstance().post("CODE401");
                }
                onHttpFail(result.getCode(),result.getMsg(),result.getResult());
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof ConnectException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onNetWorkError("网络异常，请稍后再试");
            }
//            else if(e instanceof TimeoutException || e instanceof SocketTimeoutException){
//                RxBus.getInstance().post("TimeoutException");
//            }
            else {
                onNetWorkError(e.getMessage());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            onComplete();
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 正常返回结果
     * @param result 结果
     * @param msg 附带消息
     */
    public void onHttpSuccess(T result,String msg){}

    /**
     * 正常返回但code不是CODE_SUCCESS
     * @param code 约定的错误码
     * @param msg 附带消息
     */
    public void onHttpFail(int code, String msg, T result){}

    /**
     * 非正常返回，通常是网络异常问题
     * @param msg 异常描述
     */
    public void onNetWorkError(String msg){}
//    public abstract void onHttpComplete();


}
