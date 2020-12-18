package com.example.donLuHo;

import androidx.annotation.Nullable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.ds.katafoundation.activity.BaseDetailActivity;
import com.example.donLuHo.bean.BaseH5Bean;
import com.example.donLuHo.bean.UpdateBean;
import com.example.donLuHo.presenter.MainPresenter;
import com.example.donLuHo.util.ArithUtil;
import com.example.donLuHo.viewale.MainViewable;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class MainActivity extends BaseDetailActivity<MainPresenter> implements MainViewable {
    public MyWebView myWebView;
   private SplashView splashView;
    public boolean loadFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        splashView = new SplashView(this);
        getPermissions();
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().getBaseUrls();
        getPresenter().loadStartupsImage();
    }

    @Override
    public void onRespH5Url(BaseH5Bean result) {
        myWebView = new MyWebView(this,result.getH5());
        myWebView.setDisplay();

        Log.e("onRespH5Url","onRespH5Url");
    }


    public void splashFinish(){
        splashView = null;
        Log.e("onRespH5Url","myWebView"+myWebView);
        findViewById(R.id.webView).setVisibility(View.VISIBLE);
        getPresenter().checkVersionUpdate();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return myWebView.onKeyDown(keyCode,event);
    }


    @Override
    public void onCheckVersionUpdate(UpdateBean result) {
        if(result == null)return;
        UpdateConfiguration configuration = new UpdateConfiguration()
                //输出错误日志
                .setEnableLog(true)
                //设置自定义的下载
                //.setHttpManager()
                //下载完成自动跳动安装页面
                .setJumpInstallPage(true)
                //设置对话框背景图片 (图片规范参照demo中的示例图)
                //.setDialogImage(R.drawable.ic_dialog)
                //设置按钮的颜色
                //.setDialogButtonColor(Color.parseColor("#E743DA"))
                //设置对话框强制更新时进度条和文字的颜色
                //.setDialogProgressBarColor(Color.parseColor("#E743DA"))
                //设置按钮的文字颜色
                .setDialogButtonTextColor(Color.WHITE)
                //设置是否显示通知栏进度
                .setShowNotification(true)
                //设置是否提示后台下载toast
                .setShowBgdToast(false)
                //设置强制更新
                .setForcedUpgrade(result.getForce_update() == 1);
        //设置对话框按钮的点击监听
//                .setButtonClickListener(this)
        //设置下载过程的监听
//                .setOnDownloadListener(this);

        DownloadManager manager = DownloadManager.getInstance(this);

        int compareVersion = ArithUtil.compareVersion(BuildConfig.VERSION_NAME, result.getVersion_name());
        if(compareVersion == 0 || compareVersion == 1) {
            return;
        }
        else
            compareVersion = BuildConfig.VERSION_CODE+1;
        manager.setApkVersionCode(compareVersion);
        manager.setApkName("app.apk")
                .setApkUrl(result.getDownload_url())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowNewerToast(true)
                .setConfiguration(configuration)
//                .setApkVersionCode(2)
                .setApkVersionName(result.getVersion_name())
//                .setApkSize("20.4")
                .setApkDescription(result.getModify_content())
                .setApkMD5(result.getPackage_md5())
                .download();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        myWebView.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
     }

    private void getPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Permission permission) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
