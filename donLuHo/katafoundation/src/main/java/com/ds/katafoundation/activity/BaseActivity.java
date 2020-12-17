package com.ds.katafoundation.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ds.katafoundation.R;
import com.ds.katafoundation.manager.AppManager;
import com.ds.utils.StatusBarUtil;


public abstract class BaseActivity extends AppCompatActivity {

    private boolean isAutoHideKeyWord = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x00000000);
        }


        setContentView(getLayoutResID());
        AppManager.getAppManager().addActivity(this);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        AppManager.getAppManager().addActivity(this);
    }

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected abstract void initData();

    protected void initListener() {
    }

    @Override
    public void finish() {
        AppManager.getAppManager().finishActivity(this);
        super.finish();
    }

    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(int msgId) {
        showToast(getApplicationContext().getString(msgId), Toast.LENGTH_SHORT);
    }

    public void showToast(String msg, int duration) {
        TextView view = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.toast, null);
        view.setText(msg);
        Toast toast = new Toast(getApplicationContext());
        toast.setView(view);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();

    }

    public void onTopBarBack(View view) {
        finish();
    }

    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 携带数据的页面跳转
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    /**
     * 开启/关闭键盘呼出时，是否点击输入框外自动收起
     * isOpen=true 打开输入法自动隐藏，false时候关闭输入法自动隐藏
     * 默认关闭
     * @param isOpen
     */
    public void setAutoHideKeyWord(boolean isOpen) {
        this.isAutoHideKeyWord = isOpen;
    }


    /**
     * 处理点击软键盘之外的空白处，隐藏软件盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isAutoHideKeyWord){
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHide(v, ev)) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return super.dispatchTouchEvent(ev);

            }
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHide(View v, MotionEvent event) {
        if (v != null &&
                (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            boolean b = event.getX() > left &&
                    event.getX() < right &&
                    event.getY() > top &&
                    event.getY() < bottom;
            return !b;
        }
        return false;
    }

}
