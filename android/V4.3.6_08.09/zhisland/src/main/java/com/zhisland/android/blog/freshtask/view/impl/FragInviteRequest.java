package com.zhisland.android.blog.freshtask.view.impl;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.common.webview.FragWebViewActivity;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.freshtask.model.impl.InviteRequestModel;
import com.zhisland.android.blog.freshtask.presenter.InviteRequestPresenter;
import com.zhisland.android.blog.freshtask.view.IInviteRequestView;
import com.zhisland.android.blog.freshtask.view.impl.holder.InviteRequestHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.InviteRequestItemHolder;
import com.zhisland.android.blog.freshtask.view.impl.holder.InviteRequestItemHolderListener;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.mvp.view.FragPullListMvp;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新手任务 求邀请 升级
 */
public class FragInviteRequest extends FragPullListMvp<InviteUser, InviteRequestPresenter>
        implements IInviteRequestView, InviteRequestItemHolderListener {

    private static final String KEY_SHOW_GUIDE = "key_show_guide1";
    private static final int TAG_ID_RIGHT = 111;
    private static final String KEY_IS_FROM_FRESH_TASK = "key_is_from_fresh_task";

    private InviteRequestHolder viewHolder = new InviteRequestHolder();
    private InviteRequestAdapter adapter;
    private AProgressDialog progressDialog;
    // 留言dilaog
    private Dialog leavingDlg;
    // 是否是新手任务跳转过来的
    private boolean isFromTask;

    /**
     * @param isFromTask 是否是从新手任务跳转过来的
     */
    public static void invoke(Context context, boolean isFromTask) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragInviteRequest.class;
        param.enableBack = true;
        param.swipeBackEnable = false;
        param.title = "升级为海客";
        param.titleColor = R.color.white;
        param.btnBackResID = R.drawable.sel_btn_back_white_two;
        param.titleBackground = R.drawable.task_background_tabbar;
        param.hideBottomLine = true;

        param.runnable = titleRunnable;
        param.titleBtns = new ArrayList<>();
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = context.getString(R.string.str_haike);
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.white);
        param.titleBtns.add(tb);

        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(KEY_IS_FROM_FRESH_TASK, isFromTask);
        context.startActivity(intent);
    }

    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            if (tagId == TAG_ID_RIGHT) {
                FragWebViewActivity.launch(context, Config.getUserPermissionUrl(1), "会员权限");
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getPullProxy().needRefreshOnResume = false;
        adapter = new InviteRequestAdapter();
        getPullProxy().setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.frag_invite_request, container, false);
        ButterKnife.inject(viewHolder, view);
        ButterKnife.inject(this, view);
        View pullView = super.onCreateView(inflater, container, savedInstanceState);
        viewHolder.flContainer.addView(pullView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        initBottomHeight();
        isFromTask = getActivity().getIntent().getBooleanExtra(KEY_IS_FROM_FRESH_TASK, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        internalView.setDividerHeight(0);
        internalView.setFastScrollEnabled(false);

        EmptyView emptyView = new EmptyView(getActivity());
        emptyView.setImgRes(R.drawable.register_img_cannotfind_friend);
        emptyView.setPrompt("非常遗憾，未找到可以帮你升\n级成为海客的好友");
        emptyView.setBtnVisibility(View.VISIBLE);
        emptyView.setBtnText("返回");
        emptyView.setBtnTextBackgroundResource(R.drawable.rect_bwhite_sdiv_clarge);
        emptyView.setBtnTextColor(R.color.color_f1);
        emptyView.setBtnTextWidth(DensityUtil.dip2px(240));
        emptyView.setBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        internalView.setEmptyView(emptyView);

        getPullProxy().getPullView().setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        // 显示第一进入dialog
        showGuideDlg();
    }

    private void showGuideDlg() {
        boolean isAlreadyShow = PrefUtil.Instance().getByKey(KEY_SHOW_GUIDE + PrefUtil.Instance().getUserId(), false);
        if (!isAlreadyShow && !isFromTask) {
            DialogUtil.dialogBecomeGuest(getActivity());
            PrefUtil.Instance().setKeyValue(KEY_SHOW_GUIDE + PrefUtil.Instance().getUserId(), true);
        }
    }

    @Override
    protected InviteRequestPresenter createPresenter() {
        InviteRequestPresenter presenter = new InviteRequestPresenter();
        InviteRequestModel model = new InviteRequestModel();
        presenter.setModel(model);
        return presenter;
    }

    @Override
    public void loadNormal() {
        presenter().loadNormal();
    }

    @Override
    public void loadMore(String nextId) {
        presenter().loadMore(nextId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initBottomHeight() {
        int height = DensityUtil.getHeight();
        if (height > 0) {
            //屏幕高的 1/3，减去蒙层的高度 50dp
            int bottomHeight = height / 3 - DensityUtil.dip2px(50);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, bottomHeight);
            viewHolder.llBottom.setLayoutParams(params);
        }
    }

    @Override
    protected String getPageName() {
        return "EntranceUpgradeHaiKe";
    }

    @Override
    public void setData(ZHPageData<InviteUser> datas) {
        getPullProxy().getAdapter().clearItems();
        getPullProxy().onLoadSucessfully(datas);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void showContentView() {
        if (isFromTask) {
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvDesc.setVisibility(View.VISIBLE);
            viewHolder.ivMask.setVisibility(View.VISIBLE);
            viewHolder.llBottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showEmptyView() {
        if (isFromTask) {
            viewHolder.tvTitle.setVisibility(View.GONE);
            viewHolder.tvDesc.setVisibility(View.GONE);
            viewHolder.ivMask.setVisibility(View.GONE);
            viewHolder.llBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void notifyDataChange() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showShortToast(String toast) {
        ToastUtil.showShort(toast);
    }

    @Override
    public void goToCallFriend() {
        // 跳转召唤好友
        FragFriendCall.invoke(getActivity());
    }

    @Override
    public void showProgress() {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(getActivity());
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void goToNoPower() {
        FragInviteRequestNoPower.invoke(getActivity(), isFromTask);
        getActivity().finish();
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    @Override
    public void loadDataSuccess(ZHPageData<InviteUser> datas) {
        getPullProxy().onLoadSucessfully(datas);
    }

    @Override
    public void onLoadError(Throwable e) {
        getPullProxy().onLoadFailed(e);
    }

    @Override
    public void showLeavingMsgDlg(final InviteUser item) {
        if (leavingDlg != null && leavingDlg.isShowing()) {
            return;
        }

        if (leavingDlg == null) {
            leavingDlg = new Dialog(getActivity(), R.style.DialogGuest);
            leavingDlg.setContentView(R.layout.dlg_request_invite_leaving_msg);
            leavingDlg.setCancelable(true);
            Window dialogWindow = leavingDlg.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = DensityUtil.getWidth() - DensityUtil.dip2px(64); // 宽度
            dialogWindow.setGravity(Gravity.CENTER);
            dialogWindow.setAttributes(lp);
        }

        User user = item.user;
        AvatarView ivUserAvatar = (AvatarView) leavingDlg.findViewById(R.id.ivUserAvatar);
        TextView tvUserName = (TextView) leavingDlg.findViewById(R.id.tvUserName);
        TextView tvUserCompany = (TextView) leavingDlg.findViewById(R.id.tvUserCompany);
        final EditText etLeavingMsg = (EditText) leavingDlg.findViewById(R.id.etLeavingMsg);
        etLeavingMsg.setText("");
        Button btnConfirm = (Button) leavingDlg.findViewById(R.id.btnConfirm);
        ivUserAvatar.fill(user, true);
        tvUserName.setText(user.name);
        tvUserCompany.setText(user.combineCompanyAndPosition());
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_UPGRADE_HAIKE_REQUEST_CONFIRM);
                String msg = etLeavingMsg.getText().toString().trim();
                presenter().requestInvite(item, msg);
            }
        });
//        leavingDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                ScreenTool.hideKeyboard(getActivity());
//            }
//        });

        leavingDlg.show();
    }

    @Override
    public void hideLeavingDlg() {
        if (leavingDlg != null && leavingDlg.isShowing()) {
            leavingDlg.dismiss();
        }
    }

    //================UI event start================

    @OnClick(R.id.tvBottomBtn)
    public void onClickBottomBtn() {
        presenter().onClickBottomBtn();
    }

    @Override
    public void onClickInviteRequest(InviteRequestItemHolder holder) {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_UPGRADE_HAIKE_REQUEST);
        presenter().onClickInviteRequest(holder.inviteUser);
    }

    @Override
    public void onClickRlInviteRequest(long userId) {
        ActProfileDetail.invokeNoHistory(getActivity(), userId);
    }
    //===============UI event end=============

    //==========viewpager adapter============
    class InviteRequestAdapter extends BaseListAdapter<InviteUser> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InviteRequestItemHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item_invite_request, parent, false);
                holder = new InviteRequestItemHolder(getActivity());
                convertView.setTag(holder);
            } else {
                holder = (InviteRequestItemHolder) convertView.getTag();
            }
            holder.setListener(FragInviteRequest.this);
            ButterKnife.inject(holder, convertView);
            holder.fill(getItem(position));
            return convertView;
        }

        @Override
        protected void recycleView(View view) {

        }
    }

}
