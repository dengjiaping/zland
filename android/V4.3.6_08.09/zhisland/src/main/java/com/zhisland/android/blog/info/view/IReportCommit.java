package com.zhisland.android.blog.info.view;

import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.mvp.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by Mr.Tui on 2016/6/29.
 */
public interface IReportCommit extends IMvpView {

    /**
     * 隐藏进度对话框
     * */
    public void hideProgressDlg();

    /**
     * 显示进度对话框
     * */
    public void showProgressDlg();

    /**
     * 跳转到举报成功
     * */
    public void gotoReportOk(ZHInfo info);

    /**
     * 获取view中原因
     * */
    public String getReason();

}
