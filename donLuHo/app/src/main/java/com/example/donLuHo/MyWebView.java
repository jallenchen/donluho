package com.example.donLuHo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.donLuHo.http.JSInterface;
import com.example.donLuHo.util.L;
import com.google.gson.Gson;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

public class MyWebView {
    private WebView webView;
    private FrameLayout videoView;
    private WebSettings webSettings;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private Uri cameraUri;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CHOOSE = 2;
    private String h5Url;
    private MainActivity mContext;

    public MyWebView(MainActivity context,String url){
        mContext = context;
        webView = context.findViewById(R.id.webView);
        videoView = context.findViewById(R.id.video);
        h5Url = url;
        setCookie(h5Url);
        initWebView();
    }

    public void setDisplay(){
        webView.setVisibility(View.VISIBLE);
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
                    mContext.loadFinish = true;
                }
                L.e("progress:"+progress);
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


    private void selectImage() {
        String[] selectPicTypeStr = mContext.getResources().getStringArray(R.array.picks);
        new AlertDialog.Builder(mContext)
                .setOnCancelListener(new ReOnCancelListener())
                .setItems(selectPicTypeStr,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        openImageCamera();
                                        break;
                                    case 1:
                                        openVideoCamera();
                                        break;
                                    case 2:
                                        chosePicture();
                                        break;
                                    case 3:
                                        cancelPick();
                                        break;
                                }
                            }
                        }).show();

    }


    private class ReOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            cancelPick();
        }
    }

    private void cancelPick(){
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
        }

        if (mUploadCallbackAboveL != null) {
            mUploadCallbackAboveL.onReceiveValue(null);
            mUploadCallbackAboveL = null;
        }
    }

    private void openVideoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        String videoPaths = Const.ROOT_PATH + (System.currentTimeMillis() + ".mp4");
        // 必须确保文件夹路径存在，否则拍照后无法完成回调
        File vFile = new File(videoPaths);
        if (!vFile.exists()) {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        } else {
            if (vFile.exists()) {
                vFile.delete();
            }
        }
        if (Build.VERSION.SDK_INT >= 24) {
            cameraUri = FileProvider.getUriForFile(mContext, "com.example.donLuHo.fileprovider", vFile);
        } else {
            cameraUri = Uri.fromFile(vFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        mContext.startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void openImageCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imagePaths = Const.ROOT_PATH + (System.currentTimeMillis() + ".jpg");
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
            cameraUri = FileProvider.getUriForFile(mContext, "com.example.donLuHo.fileprovider", vFile);
        } else {
            cameraUri = Uri.fromFile(vFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        mContext.startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void chosePicture() {
        Intent mediaChooser = new Intent(Intent.ACTION_PICK);
        //comma-separated MIME types
        mediaChooser.setType("video/*, image/*");
        mContext.startActivityForResult(mediaChooser, REQUEST_CHOOSE);
    }

    private Uri afterChosePic(Intent data) {
        if (data != null) {
            final String path = data.getData().getPath();
            if (path != null && (path.endsWith(".png") || path.endsWith(".PNG") || path.endsWith(".jpg") || path.endsWith(".JPG"))) {
                return data.getData();
            } else if (path != null && (path.endsWith(".mp4") || path.endsWith(".MP4"))) {
                return data.getData();
            }else {
                Toast.makeText(mContext, "上传的图片或视频", Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }

    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        Uri[] results = null;
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            results = new Uri[]{cameraUri};
        }
        if (requestCode == REQUEST_CHOOSE && resultCode == Activity.RESULT_OK) {
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

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mUploadCallbackAboveL != null) {
            onActivityResultAboveL(requestCode, resultCode, data);
        }
        if (mUploadMessage == null) return;
        Uri uri = null;

        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            uri = cameraUri;
            Log.e("onActivityResult: ", uri.toString());
        }

        if (requestCode == REQUEST_CHOOSE && resultCode == Activity.RESULT_OK) {
            uri = afterChosePic(data);
        }

        mUploadMessage.onReceiveValue(uri);
        mUploadMessage = null;
    }

    //退出时的时间
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if(webView.canGoBack()){
                webView.goBack();
            }else if(event.getRepeatCount() == 0) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    String txt =  mContext.getString(R.string.alert_exit_app, mContext.getResources().getString(R.string.app_name));
                    Toast.makeText(mContext, txt, Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {
                    mContext.finish();
                }
            }
            return true;
        }
        return false;
    }

}
