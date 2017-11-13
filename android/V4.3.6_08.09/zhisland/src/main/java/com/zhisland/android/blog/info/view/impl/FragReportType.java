package com.zhisland.android.blog.info.view.impl;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.ReportTypeModel;
import com.zhisland.android.blog.info.presenter.ReportTypePresenter;
import com.zhisland.android.blog.info.view.IReportType;
import com.zhisland.android.blog.info.view.impl.adapter.ReportTypeAdapter;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;

import java.util.ArrayList;

/**
 * 举报原因选择页面
 */
public class FragReportType extends FragBaseMvp<ReportTypePresenter> implements IReportType, ReportTypeAdapter.CallBack {

    public static final String INTENT_KEY_REPORT_INFO = "intent_key_report_info";

    @Override
    protected String getPageName() {
        return "FragReportType";
    }

    ReportTypeAdapter adapter;
    ListView listView;

    @Override
    public void onAttach(Activity activity) {
        //如果没有传举报资讯，则关闭自己。
        Object obj = getActivity().getIntent().getSerializableExtra(INTENT_KEY_REPORT_INFO);
        if (obj == null) {
            getActivity().finish();
        }
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        listView = new ListView(getActivity());
        listView.setFastScrollEnabled(false);
        listView.setDividerHeight(0);
        listView.setBackgroundResource(R.color.color_bg2);
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(lp);
        adapter = new ReportTypeAdapter(this);
        listView.setAdapter(adapter);
        listView.addHeaderView(headerView());
        return listView;
    }

    private View headerView() {
        TextView textView = new TextView(getActivity());
        textView.setPadding(DensityUtil.dip2px(10), DensityUtil.dip2px(20), DensityUtil.dip2px(10), DensityUtil.dip2px(18));
        textView.setText("选择举报原因");
        textView.setTextColor(getResources().getColor(R.color.color_f2));
        DensityUtil.setTextSize(textView, R.dimen.txt_12);
        return textView;
    }

    @Override
    protected ReportTypePresenter createPresenter() {
        ReportTypePresenter presenter = new ReportTypePresenter();
        ReportTypeModel model = new ReportTypeModel();
        presenter.setModel(model);

        ZHInfo info = (ZHInfo) getActivity().getIntent().getSerializableExtra(INTENT_KEY_REPORT_INFO);
        presenter.setInfo(info);
        return presenter;
    }

    @Override
    public void refreshData(ArrayList<ReportType> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void gotoReportReason(ZHInfo info, ReportType reportType) {
        FragReportReason.invoke(getActivity(), info, reportType);
    }

    @Override
    public void onItemClick(ReportType reportType) {
        presenter().onReportTypeClick(reportType);
    }
}
