package com.example.donLuHo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ds.imageloader.ImageLoader;
import com.ds.katafoundation.activity.BaseDetailActivity;
import com.example.donLuHo.bean.BaseH5Bean;
import com.example.donLuHo.presenter.SplashPresenter;
import com.example.donLuHo.util.SharePreferenceUtil;
import com.example.donLuHo.viewale.SplashViewable;
import com.ruffian.library.RTextView;

import java.lang.ref.WeakReference;

public class SplashView{
    private int DELAY = 5000;
    private FinishHandler handler;
    private RTextView tvCounter;
    private ImageView ivBg;
    private MainActivity activity;
    private View view;

    public SplashView(MainActivity activity){
        this.activity = activity;
        initView(activity);
        initData();
    }

    protected void initData() {
        handler = new FinishHandler(this);
        countDown();
    }

    protected void initView(MainActivity activity) {
        view = activity.findViewById(R.id.splash);
        tvCounter = view.findViewById(R.id.tvCounter);
        ivBg = view.findViewById(R.id.ivBg);
        String url = (String) SharePreferenceUtil.get(MainApp.getInstance(),"loadStartupsImage", "");
        int seconds = (int) SharePreferenceUtil.get(MainApp.getInstance(),"loadStartupsSeconds", 5);
        DELAY = seconds * 1000;
        String firstImage = (String) SharePreferenceUtil.get(MainApp.getInstance(),"firstImage", "");
        if(!TextUtils.isEmpty(url) && !TextUtils.isEmpty(firstImage)){
            ImageLoader.displayImageNoPlaceHolder(activity, url, ivBg);
        }
        SharePreferenceUtil.put(MainApp.getInstance(),"firstImage", "firstImage");

    }


    static class FinishHandler extends Handler {
        WeakReference<SplashView> mWeakReference;

        public FinishHandler(SplashView activity) {
            mWeakReference = new WeakReference<SplashView>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SplashView activity = mWeakReference.get();
            if (activity != null) {
                activity.countDown();
            }
        }
    }

    public void countDown(){
        if(DELAY <= 0){
            handler.removeCallbacksAndMessages(null);
            view.setVisibility(View.GONE);
            this.activity.splashFinish();
        } else{
            DELAY = DELAY - 1000;
            tvCounter.setText(MainApp.getInstance().getString(R.string.countdown_s, DELAY/1000));
            handler.sendMessageDelayed(handler.obtainMessage(), 1000);
        }

    }
}
