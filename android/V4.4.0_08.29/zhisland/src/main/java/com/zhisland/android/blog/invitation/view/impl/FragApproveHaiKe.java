package com.zhisland.android.blog.invitation.view.impl;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.controller.FragPrivacy;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.model.impl.ApproveHaiKeModel;
import com.zhisland.android.blog.invitation.presenter.ApproveHaiKePresenter;
import com.zhisland.android.blog.invitation.view.IApproveHaiKe;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 确认批准海客申请页面
 * Created by Mr.Tui on 2016/8/10.
 */
public class FragApproveHaiKe extends FragBaseMvp<ApproveHaiKePresenter> implements IApproveHaiKe {

    private static final int TAG_ID_RIGHT = 100;
    private static final String INTENT_KEY_INVITE_USER = "intent_key_invite_user";

    @InjectView(R.id.ivAvatar)
    AvatarView ivAvatar;

    @InjectView(R.id.tvName)
    TextView tvName;

    @InjectView(R.id.tvTag)
    TextView tvTag;

    @InjectView(R.id.tvComAndPos)
    TextView tvComAndPos;

    @InjectView(R.id.tvMessage)
    TextView tvMessage;

    @InjectView(R.id.tvWarn)
    TextView tvWarn;

    @InjectView(R.id.etComment)
    EditText etComment;

    @InjectView(R.id.ivCheckReliable)
    ImageView ivCheckReliable;

    @InjectView(R.id.ivCheckComment)
    ImageView ivCheckComment;

    @InjectView(R.id.tvCommit)
    TextView tvCommit;

    ScrollView root;

    public static void invoke(Context context, InviteUser inviteUser) {
        if (inviteUser == null) {
            return;
        }
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragApproveHaiKe.class;
        param.title = "确认批准";
        param.enableBack = true;
        param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
        param.runnable = titleRunnable;
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = "批准须知";
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc);
        param.titleBtns.add(tb);
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INTENT_KEY_INVITE_USER, inviteUser);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            ((FragApproveHaiKe) fragment).showAllowHaiKeDialog();
        }
    };

    @Override
    protected ApproveHaiKePresenter createPresenter() {
        ApproveHaiKePresenter presenter = new ApproveHaiKePresenter();
        ApproveHaiKeModel model = new ApproveHaiKeModel();
        presenter.setModel(model);
        presenter.setInviteUser((InviteUser) getActivity().getIntent().getSerializableExtra(INTENT_KEY_INVITE_USER));
        return presenter;
    }

    @Override
    public String getPageName() {
        return "ApproveHaiKe";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ScrollView) inflater.inflate(R.layout.frag_approve_haike, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        super.onCreateView(inflater, container, savedInstanceState);
        EditTextUtil.limitEditTextLengthChinese(etComment, 150, null, false);
        root.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom - bottom > DensityUtil.dip2px(150)) {
                    root.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }
        });
        return root;
    }

    @OnClick(R.id.llReliable)
    void onReliableClick() {
        presenter().onReliableClick();
    }

    @OnClick(R.id.llComment)
    void onCommentClick() {
        presenter().onCommentClick();
    }

    @OnClick(R.id.tvCommit)
    void onCommitClick() {
        presenter().onCommitClick();
    }

    @OnTextChanged(R.id.etComment)
    void onCommentChanged() {
        presenter().checkCommitBtn();
    }

    @Override
    public void updateView(InviteUser inviteUser) {
        if (inviteUser != null && inviteUser.user != null) {
            ivAvatar.fill(inviteUser.user, false);
            tvName.setText(inviteUser.user.name == null ? "" : inviteUser.user.name);
            tvComAndPos.setText(inviteUser.user.combineCompanyAndPosition());
        }
//        if (inviteUser.inviteRegister == InviteUser.INVITE_BG_SELF) {
//            tvTag.setVisibility(View.VISIBLE);
//        } else {
//            tvTag.setVisibility(View.GONE);
//        }
        tvTag.setVisibility(View.GONE);
        if (StringUtil.isNullOrEmpty(inviteUser.requestExplanation)) {
            tvMessage.setVisibility((View.GONE));
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(inviteUser.requestExplanation);
        }
    }

    @Override
    public void showWarnView() {
        SpannableString ss = new SpannableString("当前用户可能不符合海客要求，查看批准须知");
        ss.setSpan(spanWarnClick, 14, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvWarn.setMovementMethod(LinkMovementMethod.getInstance());
        tvWarn.setText(ss);
        tvWarn.setVisibility(View.VISIBLE);
    }

    ClickableSpan spanWarnClick = new ClickableSpan() {

        @Override
        public void onClick(View widget) {
            showAllowHaiKeDialog();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(getResources().getColor(R.color.color_dc));
            ds.setUnderlineText(false);
        }

    };

    @Override
    public void hideWarnView() {
        tvWarn.setVisibility(View.GONE);
    }

    @Override
    public void showCommentView() {
        etComment.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideCommentView() {
        etComment.setVisibility(View.GONE);
    }

    @Override
    public void setReliableSelect(boolean select) {
        ivCheckReliable.setImageResource(select ? R.drawable.chk_checked : R.drawable.chk_default);
    }

    @Override
    public void setCommentSelect(boolean select) {
        ivCheckComment.setImageResource(select ? R.drawable.chk_checked : R.drawable.chk_default);
    }

    @Override
    public void setCommitBtnEnabled(boolean enabled) {
        tvCommit.setEnabled(enabled);
    }

    @Override
    public String getCommentStr() {
        return etComment.getText().toString();
    }

    @Override
    public void toast(String str) {
        ToastUtil.showShort(str);
    }

    @Override
    public void closeSelf() {
        getActivity().finish();
    }

    /**
     * 显示批准须知dialog
     */
    @Override
    public void showAllowHaiKeDialog() {
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.view_approve_haike, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.UpdateDialog);
        dialog.setContentView(layout);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER_VERTICAL;
        wmlp.height = DensityUtil.getHeight();
        wmlp.width = DensityUtil.getWidth();// 宽度
        dialog.show();
        dialog.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
