package com.example.donLuHo;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.azhon.appupdate.config.UpdateConfiguration;
import com.azhon.appupdate.manager.DownloadManager;
import com.ds.katafoundation.activity.BaseDetailActivity;
import com.example.donLuHo.bean.BaseH5Bean;
import com.example.donLuHo.bean.UpdateBean;
import com.example.donLuHo.http.JSInterface;
import com.example.donLuHo.presenter.MainPresenter;
import com.example.donLuHo.util.ArithUtil;
import com.example.donLuHo.viewale.MainViewable;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

public class MainActivity extends BaseDetailActivity<MainPresenter> implements MainViewable {
    private WebView webView;
    private FrameLayout videoView;
    private WebSettings webSettings;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private Uri cameraUri;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CHOOSE = 2;
    private String h5Url;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        webView = findViewById(R.id.webView);
        videoView = findViewById(R.id.video);

        h5Url = getIntent().getStringExtra("h5Url");
        Log.e("MainActivity","h5Url:"+h5Url);
        setCookie(h5Url);
        initWebView();
        getPermissions();
    }

   private void setCookie(String url) {
        String StringCookie = "app_type=android";
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();

            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, StringCookie);
    }


    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    private void initWebView(){

        webSettings = webView.getSettings();
        webView.addJavascriptInterface(new JSInterface(), "android_app");

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(true);


//
//
//
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setSupportMultipleWindows(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setGeolocationEnabled(true);
//        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
//        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
//        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
////        this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
//        webSettings.setAllowContentAccess(true);
//        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
//        webSettings.setAllowUniversalAccessFromFileURLs(true);
//        webSettings.setBlockNetworkImage(true);
//        webSettings.setDomStorageEnabled(true);


        //设置不用系统浏览器打开,直接显示在当前Webview
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            private View mView;
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
            }

            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                if (mUploadCallbackAboveL != null) {
                    mUploadCallbackAboveL.onReceiveValue(null);
                } else {
                    mUploadCallbackAboveL = filePathCallback;
                    selectImage();
                }
                return true;
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType) {
                if (mUploadMessage != null) return;
                mUploadMessage = uploadMsg;
                selectImage();
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }


            @Override
            public void onProgressChanged(WebView view, int progress) {
                //通过图片的延迟载入，让网页能更快地显示
                if(progress == 100){
                    webSettings.setBlockNetworkImage(false);
                }
            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                 videoView.removeView(mView);
                videoView.setVisibility(View.GONE);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                if (view instanceof FrameLayout){
                    mView = view;
                    videoView.addView(view);
                    videoView.setVisibility(View.VISIBLE);
                }
            }
        });

        webView.loadUrl(h5Url);
    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().checkVersionUpdate();
    }

    @Override
    public void onRespH5Url(BaseH5Bean result) {
        webView.loadUrl(result.getH5());
    }


    //退出时的时间
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showToast(getString(R.string.alert_exit_app, getResources().getString(R.string.app_name)));
                mExitTime = System.currentTimeMillis();
            } else {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        if (mUploadCallbackAboveL != null) {
            onActivityResultAboveL(requestCode, resultCode, data);
        }
        if (mUploadMessage == null) return;
        Uri uri = null;

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            uri = cameraUri;
            Log.e("onActivityResult: ", uri.toString());
        }

        if (requestCode == REQUEST_CHOOSE && resultCode == RESULT_OK) {
            uri = afterChosePic(data);
        }
        mUploadMessage.onReceiveValue(uri);
        mUploadMessage = null;
        super.onActivityResult(requestCode, resultCode, data);
     }

    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        Uri[] results = null;
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            results = new Uri[]{cameraUri};
        }
        if (requestCode == REQUEST_CHOOSE && resultCode == RESULT_OK) {
            if (data != null) {
                String dataString = data.getDataString();
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }



    private void selectImage() {
        String[] selectPicTypeStr = {"拍照", "图库"};
        new AlertDialog.Builder(this)
                .setOnCancelListener(new ReOnCancelListener())
                .setItems(selectPicTypeStr,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        openCamera();
                                        break;
                                    case 1:
                                        chosePicture();
                                        break;
                                }
                            }
                        }).show();

    }

    private class ReOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }

            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(null);
                mUploadCallbackAboveL = null;
            }
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imagePaths = Environment.getExternalStorageDirectory().getPath() + "/donluho/Images/" + (System.currentTimeMillis() + ".jpg");
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        File vFile = new File(imagePaths);
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        if (Build.VERSION.SDK_INT >= 24) {
            cameraUri = FileProvider.getUriForFile(this, "com.example.donLuHo.provider", vFile);
        } else {
            cameraUri = Uri.fromFile(vFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void chosePicture() {
        Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        //comma-separated MIME types
        mediaChooser.setType("video/*, image/*");
        startActivityForResult(mediaChooser, REQUEST_CHOOSE);
    }

    private Uri afterChosePic(Intent data) {
        if (data != null) {
            final String path = data.getData().getPath();
            if (path != null && (path.endsWith(".png") || path.endsWith(".PNG") || path.endsWith(".jpg") || path.endsWith(".JPG"))) {
                return data.getData();
            } else if (path != null && (path.endsWith(".mp4") || path.endsWith(".MP4"))) {
                return data.getData();
            }else {
                Toast.makeText(this, "上传的图片或视频", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
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
