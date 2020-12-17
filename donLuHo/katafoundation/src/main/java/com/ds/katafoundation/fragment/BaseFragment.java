package com.ds.katafoundation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ds.katafoundation.R;
import com.ds.katafoundation.activity.BaseActivity;
import com.ds.katafoundation.activity.ContentActivity;

/**
 * Created by sai on 2018/3/18.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    private boolean isAutoHideKeyWord = true;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResID(), container, false);

            initView();
            initListener();
            if(getActivity() instanceof BaseActivity){
                BaseActivity act = (BaseActivity) getActivity();
                act.setAutoHideKeyWord(isAutoHideKeyWord);
            }

        }
        return rootView;
    }

    public <T extends View> T findViewById(int id) {
        return rootView.findViewById(id);
    }

    protected abstract void initData();

    protected abstract void initView();

    protected void initListener() {
    }

    protected abstract int getLayoutResID();

    public void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public void showToast(int msgId) {
        showToast(getActivity().getApplicationContext().getString(msgId), Toast.LENGTH_SHORT);
    }

    public void showToast(String msg, int duration) {
        if (TextUtils.isEmpty(msg)) return;
        TextView view = (TextView) LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.toast, null);
        view.setText(msg);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setView(view);
        //设置Toast要显示的位置，水平居中并在底部，X轴偏移0个单位，Y轴偏移70个单位，
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }


    /**
     * 开启activity
     *
     * @param clz
     */
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
        intent.setClass(getContext(), clz);
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
        intent.setClass(getContext(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public Intent getIntent() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return activity.getIntent();
        } else {
            return null;
        }
    }

    /**
     * 开启/关闭键盘呼出时，是否点击输入框外自动收起
     * 默认关闭
     * @param isOpen
     */
    public void setAutoHideKeyWord(boolean isOpen) {
        this.isAutoHideKeyWord = isOpen;
    }

    //#################### create by vicki 19/07/05 end ########################


}