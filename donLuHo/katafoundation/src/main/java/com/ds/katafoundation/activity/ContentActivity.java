package com.ds.katafoundation.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.ds.katafoundation.R;

/**
 * author : vicki
 * time   : 2019/7/5 12:39
 * desc   : 承载Fragment的Activity
 */
public class ContentActivity extends BaseActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //防止覆盖
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "ContentActivity");
        } else {
            Class<Fragment> fragmentCls = (Class) getIntent().getSerializableExtra("cls");
            try {
                fragment = fragmentCls.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.fra_base_content, fragment).commit();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_content;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment != null) {
            getSupportFragmentManager().putFragment(outState, "ContentActivity", fragment);
        }
    }

}
