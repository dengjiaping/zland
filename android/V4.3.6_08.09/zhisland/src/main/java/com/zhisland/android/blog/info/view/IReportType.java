package com.zhisland.android.blog.info.view;

import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public interface IReportType extends IMvpView {

    void refreshData(ArrayList<ReportType> data);

    void gotoReportReason(ZHInfo info,ReportType reportType);

}
