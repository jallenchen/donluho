package com.example.donLuHo.viewale;

import com.ds.kataframework.mvpviewable.BaseDetailViewable;
import com.example.donLuHo.bean.BaseH5Bean;
import com.example.donLuHo.bean.UpdateBean;

public interface MainViewable extends BaseDetailViewable {
    void onCheckVersionUpdate(UpdateBean result);
    void onRespH5Url(BaseH5Bean result);
}
