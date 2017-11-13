package com.zhisland.android.blog.info.view.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.info.bean.RecommendInfo;
import com.zhisland.android.blog.info.model.impl.AddLinkModel;
import com.zhisland.android.blog.info.presenter.AddLinkPresenter;
import com.zhisland.android.blog.info.view.IAddLink;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

/**
 * 资讯推荐添加link页面
 * Created by Mr.Tui on 2016/6/29.
 */
public class FragLinkEdit extends FragBaseMvp<AddLinkPresenter> implements IAddLink {

    private static final int TAG_ID_RIGHT = 100;

    public static final String KEY_LAST_CLIP = "last_clip_text";

    @Override
    public String getPageName() {
        return PAGE_NAME;
    }

    private static final String PAGE_NAME = "InformationAddLink";

    @InjectView(R.id.etLink)
    EditText etLink;

    @InjectView(R.id.ivClose)
    ImageView ivClose;

    @InjectView(R.id.tvPrompt)
    TextView tvPrompt;

    @InjectView(R.id.tvError)
    TextView tvError;

    @InjectView(R.id.llProgress)
    LinearLayout llProgress;

    @InjectView(R.id.tvCommit)
    TextView tvCommit;

    /**
     * 会进行权限检查
     *
     * @param context
     */
    public static void TryInvoke(Context context) {
        if (PermissionsMgr.getInstance().canInfoRecommend()) {
            invoke(context);
        } else {
            DialogUtil.showPermissionsDialog(context, "");
        }
    }

    private static void invoke(Context context) {
        if (!PrefUtil.Instance().getInfoGuideShowOver()) {
            PrefUtil.Instance().setInfoGuideShowOver(true);
            FragRecommendGuide.invokeFirst(context);
            return;
        }
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragLinkEdit.class;
        param.title = "发布";
        param.enableBack = true;
        param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
        param.runnable = titleRunnable;
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = "如何推荐？";
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc);
        param.titleBtns.add(tb);
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            FragRecommendGuide.invoke(fragment.getActivity());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_add_link, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        return root;
    }

    @Override
    protected AddLinkPresenter createPresenter() {
        AddLinkPresenter presenter = new AddLinkPresenter();
        AddLinkModel model = new AddLinkModel();
        presenter.setModel(model);
        return presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter().onViewResume();
    }

    @OnClick(R.id.ivClose)
    void clearClick() {
        presenter().onClearClick();
    }

    @OnClick(R.id.tvCommit)
    void commitClick() {
        presenter().onCommitClick();
    }

    @OnClick(R.id.llRoot)
    void rootClick() {
        etLink.clearFocus();
        tvPrompt.setFocusable(true);
        tvPrompt.setFocusableInTouchMode(true);
        tvPrompt.requestFocus();
        tvPrompt.requestFocusFromTouch();
    }

    @OnTextChanged(R.id.etLink)
    void linkTextChanged() {
        checkCloseBtn();
        presenter().checkCommitContent();
    }

    @OnFocusChange(R.id.etLink)
    void linkFocusChange() {
        checkCloseBtn();
        if (etLink.hasFocus()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    etLink.setSelection(etLink.getText().length());
                }
            });
        }
    }

    private void checkCloseBtn() {
        if (etLink.hasFocus() && etLink.getText().length() > 0) {
            ivClose.setVisibility(View.VISIBLE);
        } else {
            ivClose.setVisibility(View.GONE);
        }
    }

    //------------------------- IAddLink start ------------------------

    @Override
    public void setCommitBtnEnabled(boolean enabled) {
        tvCommit.setEnabled(enabled);
    }

    @Override
    public String getClipText() {
        return StringUtil.getClipText(getActivity());
    }

    @Override
    public void setEditLinkText(String str) {
        etLink.setText(str == null ? "" : str);
    }

    @Override
    public String getLinkEditText() {
        return etLink.getText().toString();
    }

    @Override
    public boolean isLoadingShowing() {
        return llProgress.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showLoading() {
        hidePrompt();
        llProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        llProgress.setVisibility(View.GONE);
    }

    @Override
    public void showErrorPrompt(String text) {
        tvError.setText(text);
        hideProgressDlg();
        tvError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidePrompt() {
        tvError.setVisibility(View.INVISIBLE);
    }

    @Override
    public void gotoRecommendReason(RecommendInfo recommendInfo) {
        FragRecommendReason.invoke(getActivity(), recommendInfo);
    }


}
