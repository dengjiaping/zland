package com.zhisland.android.blog.info.view.impl;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.android.blog.info.model.impl.RecommendCommitModel;
import com.zhisland.android.blog.info.presenter.RecommendCommitPresenter;
import com.zhisland.android.blog.info.view.IRecommendCommit;
import com.zhisland.lib.mvp.view.FragBaseMvp;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 资讯推荐原因输入页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragRecommendReason extends FragBaseMvp<RecommendCommitPresenter> implements IRecommendCommit {

    private static final String INTENT_KEY_RECOMMEND_INFO = "intent_key_recommenf_info";

    private static final int TAG_ID_RIGHT = 100;

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    private static final String PAGE_NAME = "InformationAddLinkReason";

    @InjectView(R.id.tvTitle)
    TextView tvTitle;

    @InjectView(R.id.etReason)
    EditText etReason;

    public static void invoke(Context context, RecommendInfo recommendInfo) {
        if (recommendInfo == null) {
            return;
        }
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragRecommendReason.class;
        param.title = "发布";
        param.enableBack = true;
        param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
        param.runnable = titleRunnable;
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = "如何推荐？";
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc);
        param.titleBtns.add(tb);
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_RECOMMEND_INFO, recommendInfo);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            FragRecommendGuide.invoke(fragment.getActivity());
        }
    };

    @Override
    protected RecommendCommitPresenter createPresenter() {
        RecommendCommitPresenter presenter = new RecommendCommitPresenter();
        RecommendCommitModel model = new RecommendCommitModel();
        presenter.setModel(model);

        RecommendInfo recommendInfo = (RecommendInfo) getActivity().getIntent().getSerializableExtra(INTENT_KEY_RECOMMEND_INFO);
        presenter.setRecommendInfo(recommendInfo);
        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_recommend_reason, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        EditTextUtil.limitEditTextLengthChinese(etReason, 150, null);
        return root;
    }

    @OnClick(R.id.tvCommit)
    void commitClick() {
        presenter().onCommitClick();
    }

    @Override
    public void setInfoTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public String getReason() {
        return etReason.getText().toString();
    }

    @Override
    public void gotoInfoPreview(String url) {
        FragPreview.invoke(getActivity(), url);
    }
}
