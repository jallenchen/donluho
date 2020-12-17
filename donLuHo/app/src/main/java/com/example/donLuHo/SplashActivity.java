package com.example.donLuHo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ds.imageloader.ImageLoader;
import com.ds.katafoundation.activity.BaseActivity;
import com.ds.katafoundation.activity.BaseDetailActivity;
import com.example.donLuHo.bean.BaseH5Bean;
import com.example.donLuHo.presenter.MainPresenter;
import com.example.donLuHo.presenter.SplashPresenter;
import com.example.donLuHo.util.SharePreferenceUtil;
import com.example.donLuHo.viewale.MainViewable;
import com.example.donLuHo.viewale.SplashViewable;
import com.ruffian.library.RTextView;

import java.lang.ref.WeakReference;

public class SplashActivity extends BaseDetailActivity<SplashPresenter> implements SplashViewable {
    private int DELAY = 5000;
    private FinishHandler handler;
    private RTextView tvCounter;
    private ImageView ivBg;
    private String h5Url;

    @Override
    protected void initData() {
        handler = new FinishHandler(this);
        countDown();
        getPresenter().getBaseUrls();
        getPresenter().loadStartupsImage();
    }

    @Override
    protected void initView() {
        tvCounter = findViewById(R.id.tvCounter);
        ivBg = findViewById(R.id.ivBg);
        String url = (String) SharePreferenceUtil.get(MainApp.getInstance(),"loadStartupsImage", "");
        int seconds = (int) SharePreferenceUtil.get(MainApp.getInstance(),"loadStartupsSeconds", 5);
        DELAY = seconds * 1000;
        String firstImage = (String) SharePreferenceUtil.get(MainApp.getInstance(),"firstImage", "");
        if(!TextUtils.isEmpty(url) && !TextUtils.isEmpty(firstImage)){
            ImageLoader.displayImageNoPlaceHolder(this, url, ivBg);
        }
        SharePreferenceUtil.put(MainApp.getInstance(),"firstImage", "firstImage");

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    static class FinishHandler extends Handler {
        WeakReference<SplashActivity> mWeakReference;

        public FinishHandler(SplashActivity activity) {
            mWeakReference = new WeakReference<SplashActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SplashActivity activity = mWeakReference.get();
            if (activity != null) {
                activity.countDown();
            }
        }
    }

    public void countDown(){
        if(DELAY <= 0){
            finish();
            Bundle bundle = new Bundle();
            bundle.putString("h5Url",h5Url);
            startActivity(MainActivity.class,bundle);
        } else{
            DELAY = DELAY - 1000;
            tvCounter.setText(MainApp.getInstance().getString(R.string.countdown_s, DELAY/1000));
            handler.sendMessageDelayed(handler.obtainMessage(), 1000);

        }

    }

    @Override
    public void onRespH5Url(BaseH5Bean result) {
        h5Url = result.getH5();
    }
}
