package com.zhisland.android.blog.info.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.model.impl.ReportCommitModel;
import com.zhisland.android.blog.info.presenter.ReportCommitPresenter;
import com.zhisland.android.blog.info.view.IReportCommit;
import com.zhisland.lib.mvp.view.FragBaseMvp;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 举报原因页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragReportReason extends FragBaseMvp<ReportCommitPresenter> implements IReportCommit {

    private static final String INTENT_KEY_REPORT_TYPE = "intent_key_report_type";
    private static final String INTENT_KEY_REPORT_INFO = "intent_key_report_info";

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

    private static final String PAGE_NAME = "FragReportReason";

    @InjectView(R.id.etReason)
    EditText etReason;

    AProgressDialog dialog;

    public static void invoke(Context context, ZHInfo info, ReportType reportType) {
        if (reportType == null || info == null) {
            return;
        }
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragReportReason.class;
        param.title = "举报";
        param.enableBack = true;
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_REPORT_TYPE, reportType);
        intent.putExtra(INTENT_KEY_REPORT_INFO, info);
        context.startActivity(intent);
    }

    @Override
    protected ReportCommitPresenter createPresenter() {
        ReportCommitPresenter presenter = new ReportCommitPresenter();
        ReportCommitModel model = new ReportCommitModel();
        presenter.setModel(model);

        ZHInfo info = (ZHInfo) getActivity().getIntent().getSerializableExtra(INTENT_KEY_REPORT_INFO);
        presenter.setInfo(info);
        ReportType reportType = (ReportType) getActivity().getIntent().getSerializableExtra(INTENT_KEY_REPORT_TYPE);
        presenter.setReportType(reportType);
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_report_reason, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        EditTextUtil.limitEditTextLengthChinese(etReason, 50, null);
        return root;
    }

    @OnClick(R.id.tvCommit)
    void commitClick() {
        presenter().onCommitClick();
    }

    @Override
    public void showProgressDlg() {
        if (dialog == null) {
            dialog = new AProgressDialog(getActivity());
            dialog.setText("正在提交。。。");
        }
        dialog.show();
    }

    @Override
    public void hideProgressDlg() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void gotoReportOk(ZHInfo info) {
        FragReportOk.invoke(getActivity(), info);
    }

    @Override
    public String getReason(){
        return etReason.getText().toString();
    }
}
