package com.zhisland.android.blog.profile.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.api.TaskUpdateUser;
import com.zhisland.android.blog.common.app.Config;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.TabHome;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.eb.EBNotify;
import com.zhisland.android.blog.common.eb.EBUser;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.common.util.AvatarUploader;
import com.zhisland.android.blog.common.util.AvatarUploader.OnUploaderCallback;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.common.view.AvatarView;
import com.zhisland.android.blog.common.webview.FragWebViewActivity;
import com.zhisland.android.blog.contacts.controller.FragContactList;
import com.zhisland.android.blog.contacts.eb.EBContacts;
import com.zhisland.android.blog.event.controller.ActSignUpEvents;
import com.zhisland.android.blog.event.controller.FragInitiatedEvents;
import com.zhisland.android.blog.freshtask.view.impl.FragInviteRequest;
import com.zhisland.android.blog.invitation.view.impl.FragHaikeConfirm;
import com.zhisland.android.blog.invitation.view.impl.FragInviteRegist;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.android.blog.profile.dto.UserHeatReport;
import com.zhisland.android.blog.profilemvp.model.remote.RelationApi;
import com.zhisland.android.blog.profilemvp.view.impl.FragMyAttention;
import com.zhisland.android.blog.profilemvp.view.impl.FragMyFans;
import com.zhisland.android.blog.profilemvp.view.impl.FragMyHot;
import com.zhisland.android.blog.setting.controller.FragSettings;
import com.zhisland.android.blog.tabhome.eb.EBTabHome;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.ZHPageData;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.util.StringUtil;

import org.apache.http.client.HttpResponseException;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人页tab
 */
public class FragProfileTab extends FragBase {

    private static final String KEY_REQUEST_HAIKE_NUM = "key_request_haike_num";

    private static final int REQ_IMAGE = 115;

    private User user;

    //region 组件声明
    @InjectView(R.id.ivAvatar)
    AvatarView avatarView;

    @InjectView(R.id.ivUserType)
    ImageView ivUserType;

    @InjectView(R.id.ivGoldFire)
    ImageView ivGoldFire;

    @InjectView(R.id.tvName)
    TextView tvName;

    @InjectView(R.id.tvCompany)
    TextView tvCompany;

    @InjectView(R.id.tvPosition)
    TextView tvPosition;

    @InjectView(R.id.llFanHot)
    LinearLayout llFanHot;

    @InjectView(R.id.tvNumAttention)
    TextView tvNumAttention;

    @InjectView(R.id.tvNumFans)
    TextView tvNumFans;

    @InjectView(R.id.tvNumHot)
    TextView tvNumHot;

    @InjectView(R.id.ivInitiatedEvents)
    ImageView ivInitiatedEvents;

    @InjectView(R.id.ivSignUpEvents)
    ImageView ivSignUpEvents;

    @InjectView(R.id.tvMemberRightDesc)
    TextView tvMemberRightDesc;

    @InjectView(R.id.tvMemberName)
    TextView tvMemberName;

    @InjectView(R.id.llMyPermissions)
    LinearLayout llMyPermissions;

    @InjectView(R.id.llAllowHaiKe)
    LinearLayout llAllowHaiKe;

    @InjectView(R.id.tvRequestHaiKe)
    TextView tvRequestHaiKe;

    @InjectView(R.id.ivAllowHaiKeNew)
    ImageView ivAllowHaiKeNew;

    @InjectView(R.id.ivAllowHaiKeLock)
    ImageView ivAllowHaiKeLock;

    @InjectView(R.id.tvRequestNum)
    TextView tvRequestNum;

    @InjectView(R.id.llHeadInfo)
    LinearLayout rlHeadInfo;
    //endregion

    //region 生命周期
    @Override
    public String getPageName() {
        return "ProfileHome";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_profile_tab, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        setDataToView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case REQ_IMAGE:
                    List<String> paths = (List<String>) data
                            .getSerializableExtra(MultiImgPickerActivity.RLT_PATHES);
                    String localUrl = paths.get(0);
                    avatarView.fill(localUrl, false);
                    showProgress();
                    AvatarUploader.instance().uploadAvatar(localUrl,
                            onUploaderCallback);
                    break;
            }

        }
    }

    @Override
    protected boolean reportUMOnCreateAndOnDestory() {
        return false;
    }

    public void pageStart() {
        ZhislandApplication.trackerPageStart(getPageName());
        getRelationReport();
        getInvitationCodesTask();
    }

    public void pageEnd() {
        ZhislandApplication.trackerPageEnd(getPageName());
    }
    //endregion

    //region fill view
    private void setDataToView() {
        user = DBMgr.getMgr().getUserDao().getSelfUser();
        if (user == null || !isAdded()) {
            return;
        }
        fillUserInfo();
        fillFansHotView(null);
        fillPermissionsView();
        updateEventRedHot();
    }

    private void fillUserInfo() {
        tvName.setText(user.name);
        tvCompany.setText(user.userCompany == null ? "" : user.userCompany);
        tvPosition.setText(user.userPosition == null ? "" : user.userPosition);
        avatarView.fill(user, false);
        AvatarView.showUserType(ivUserType, user);
        if (user.isGoldFire()) {
            ivGoldFire.setVisibility(View.VISIBLE);
        } else {
            ivGoldFire.setVisibility(View.INVISIBLE);
        }
        if (user.isVip()) {
            rlHeadInfo.setBackgroundResource(R.drawable.img_profile_e);
            if (user.isLifelong != null && user.isLifelong == User.VALUE_TYPE_VIP_LIFE) {
                rlHeadInfo.setBackgroundResource(R.drawable.img_profile_c);
            } else {
                if (user.zhislandType != null && user.zhislandType == User.VALUE_TYPE_VIP_GREEN) {
                    rlHeadInfo.setBackgroundResource(R.drawable.img_profile_b);
                } else if (user.zhislandType != null && user.zhislandType == User.VALUE_TYPE_VIP_BLUE) {
                    rlHeadInfo.setBackgroundResource(R.drawable.img_profile_d);
                }
            }
        } else if (user.isHaiKe()) {
            rlHeadInfo.setBackgroundResource(R.drawable.img_profile_a);
        } else if (user.isDaoDing()) {
            rlHeadInfo.setBackgroundColor(getResources().getColor(R.color.color_dc));
        } else {
            rlHeadInfo.setBackgroundResource(R.drawable.img_profile_e);
        }
    }

    private void fillPermissionsView() {
        tvMemberRightDesc.setText(user.memberRight == null ? ""
                : user.memberRight);
        tvMemberName.setText(user.getUserTypeDesc());
        if (user.isYuZhuCe()) {
            llMyPermissions.setEnabled(false);
            llAllowHaiKe.setVisibility(View.GONE);
            tvRequestHaiKe.setVisibility(View.VISIBLE);
            tvMemberName.setTextColor(getResources().getColor(R.color.color_f2));
            Drawable drawable = getResources().getDrawable(R.drawable.img_arrow_right);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvMemberName.setCompoundDrawables(null, null, null, null);
        } else {
            llMyPermissions.setEnabled(true);
            llAllowHaiKe.setVisibility(View.VISIBLE);
            tvRequestHaiKe.setVisibility(View.GONE);
            tvMemberName.setTextColor(getResources().getColor(R.color.color_dc));
            Drawable drawable = getResources().getDrawable(R.drawable.img_arrow_right);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvMemberName.setCompoundDrawables(null, null, drawable, null);
            fillRequestHaiKeNum(getRequestHaiKeNum());
        }
    }

    private void fillFansHotView(UserHeatReport userHeatReport) {
        if (user == null || user.isYuZhuCe()) {
            llFanHot.setVisibility(View.GONE);
        } else {
            llFanHot.setVisibility(View.VISIBLE);
        }
        if (userHeatReport == null) {
            Object obj = DBMgr.getMgr().getCacheDao().get(UserHeatReport.CACHE_KEY + PrefUtil.Instance().getUserId());
            if (obj != null && obj instanceof UserHeatReport) {
                userHeatReport = (UserHeatReport) obj;
            }
        }
        if (userHeatReport != null) {
            tvNumAttention.setText("" + userHeatReport.attentionCount);
            tvNumFans.setText("" + userHeatReport.fansCount);
            tvNumHot.setText(userHeatReport.heatVal + "℃");
        }
    }

    private void fillRequestHaiKeNum(int haiKeRequestNum) {
        if (PermissionsMgr.getInstance().canInviteFriend()) {
            if (PrefUtil.Instance().hasVisitedFragHaikeConfirm()) {
                ivAllowHaiKeNew.setVisibility(View.GONE);
                if (haiKeRequestNum > 0) {
                    tvRequestNum.setVisibility(View.VISIBLE);
                    tvRequestNum.setText("" + (haiKeRequestNum > 99 ? 99 : haiKeRequestNum));
                } else {
                    tvRequestNum.setVisibility(View.GONE);
                }
            } else {
                ivAllowHaiKeNew.setVisibility(View.VISIBLE);
                tvRequestNum.setVisibility(View.GONE);
            }
            ivAllowHaiKeLock.setImageResource(R.drawable.img_arrow_right);
        } else {
            ivAllowHaiKeNew.setVisibility(View.GONE);
            tvRequestNum.setVisibility(View.GONE);
            ivAllowHaiKeLock.setImageResource(R.drawable.img_contact_lock);
        }

    }

    /**
     * 更新个人页面活动相关的小红点
     */
    private void updateEventRedHot() {
        Integer myEvent = PrefUtil.Instance().getByKey(
                NotifyTypeConstants.PREF_MY_EVENT, 0);
        Integer signEvent = PrefUtil.Instance().getByKey(
                NotifyTypeConstants.PREF_SIGN_EVENT, 0);
        if (myEvent > 0) {
            ivInitiatedEvents.setVisibility(View.VISIBLE);
        } else {
            ivInitiatedEvents.setVisibility(View.INVISIBLE);
        }
        if (signEvent > 0) {
            ivSignUpEvents.setVisibility(View.VISIBLE);
        } else {
            ivSignUpEvents.setVisibility(View.INVISIBLE);
        }

    }

    private void showProgress() {
        showProgressDlg("正在上传您的头像...");
        getProgressDialog().setCanceledOnTouchOutside(false);
        getProgressDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                AvatarUploader.instance().loadFinish();
            }
        });
    }
    //endregion

    //region 业务逻辑
    private void getRelationReport() {
        Observable.create(new AppCall<UserHeatReport>() {
            @Override
            protected Response<UserHeatReport> doRemoteCall() throws Exception {
                Call<UserHeatReport> call = RetrofitFactory.getInstance().getHttpsApi(RelationApi.class).getUserHeatReport(user.uid);
                return call.execute();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .compose(this.<UserHeatReport>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Subscriber<UserHeatReport>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(UserHeatReport userHeatReport) {
                        DBMgr.getMgr().getCacheDao().set(UserHeatReport.CACHE_KEY + PrefUtil.Instance().getUserId(), userHeatReport);
                        fillFansHotView(userHeatReport);
                    }
                });
    }

    private int getRequestHaiKeNum() {
        Object obj = DBMgr.getMgr().getCacheDao().get(KEY_REQUEST_HAIKE_NUM + user.uid);
        if (obj != null && obj instanceof Integer) {
            return (int) obj;
        }
        return 0;
    }

    private void cacheRequestHaiKeNum(int num) {
        int numBefore = getRequestHaiKeNum();
        if (num > numBefore) {
            EventBus.getDefault().post(
                    new EBTabHome(EBTabHome.TYPE_TAB_RED_DOT, TabHome.TAB_ID_PROFILE));
        }
        DBMgr.getMgr().getCacheDao().set(KEY_REQUEST_HAIKE_NUM + +user.uid, num);
    }

    OnUploaderCallback onUploaderCallback = new OnUploaderCallback() {

        @Override
        public void callBack(String backUrl) {
            if (StringUtil.isNullOrEmpty(backUrl)) {
                hideProgressDlg();
                showToast("上传头像失败");
                avatarView.fill(user, false);
            } else {
                updateUserAvatar(backUrl);
            }
        }
    };

    private void updateUserAvatar(String url) {
        final User user = new User();
        user.uid = this.user.uid;
        user.userAvatar = url;
        ZHApis.getUserApi().updateUser(getActivity(), user, TaskUpdateUser.TYPE_UPDATE_OTHER,
                new TaskCallback<Object>() {

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideProgressDlg();
                    }

                    @Override
                    public void onSuccess(Object content) {
                        FragProfileTab.this.user.userAvatar = user.userAvatar;
                        showToast("上传头像成功");
                        avatarView.fill(user.userAvatar, false);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        avatarView.fill(FragProfileTab.this.user.userAvatar, false);
                    }
                });
    }

    private void getInvitationCodesTask() {
        if (PermissionsMgr.getInstance().canInviteFriend()) {
            ZHApis.getUserApi().getInvitationRequests(getActivity(), null,
                    new TaskCallback<ZHPageData<User>>() {

                        @Override
                        public void onSuccess(ZHPageData<User> content) {
                            if (content != null && content.data != null) {
                                cacheRequestHaiKeNum(content.data.size());
                                fillRequestHaiKeNum(content.data.size());
                            } else {
                                cacheRequestHaiKeNum(0);
                                fillRequestHaiKeNum(0);
                            }
                        }

                        @Override
                        public void onFailure(Throwable error) {
                            if (error instanceof HttpResponseException) {
                                HttpResponseException e = (HttpResponseException) error;
                                int statusCode = e.getStatusCode();
                                if (statusCode == CodeUtil.CODE_NO_PERMISSION) {
                                    cacheRequestHaiKeNum(0);
                                    fillRequestHaiKeNum(0);
                                }
                            }
                        }
                    });
        } else {
            cacheRequestHaiKeNum(0);
            fillRequestHaiKeNum(0);
        }
    }
    //endregion

    //region 点击事件
    @OnClick(R.id.ivAvatar)
    void avatarClick() {
        MultiImgPickerActivity.invoke(getActivity(), 1, 1, REQ_IMAGE);
    }

    @OnClick(R.id.tvRequestHaiKe)
    void requestHaiKeClick() {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_UPGRADE_HAIKE_FROM_PROFILE);
        FragInviteRequest.invoke(getActivity(), false);
    }

    @OnClick(R.id.llMyPermissions)
    void myPermissionsClick() {
        FragWebViewActivity.launch(getActivity(), Config.getUserPermissionUrl(user), "会员权限");
    }

    @OnClick(R.id.llHeadInfo)
    void headInfoClick() {
        ActProfileDetail.invoke(getActivity(), PrefUtil.Instance().getUserId());
    }

    @OnClick(R.id.llNumAttention)
    void onAttentionClick() {
        FragMyAttention.invoke(getActivity());
    }

    @OnClick(R.id.llNumFans)
    void onFansClick() {
        FragMyFans.invoke(getActivity());
    }

    @OnClick(R.id.llNumHot)
    void onHotClick() {
        FragMyHot.invoke(getActivity());
    }

    @OnClick(R.id.llAllowHaiKe)
    void allowHaiKeClick() {
        if (PermissionsMgr.getInstance().canInviteFriend()) {
            FragHaikeConfirm.invoke(getActivity());
            PrefUtil.Instance().setVisitedFragHaikeConfirm(true);
            fillPermissionsView();
            ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_APPROVE_HAIKE_CLICK_PERMISSION_DENIED, "有权限");
        } else {
            DialogUtil.showPermissionsDialog(getActivity(), getPageName());
            ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_APPROVE_HAIKE_CLICK, "无权限");

        }
    }

    @OnClick(R.id.llInvite)
    void onInviteClick() {
        FragInviteRegist.invoke(getActivity());
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_INVITE_PROFILECENTER_CLICK);
    }

    @OnClick(R.id.llFriend)
    void onFriendClick() {
        FragContactList.invoke(getActivity(),
                FragContactList.INTENT_VALUE_TYPE_FRIEND);
    }

    @OnClick(R.id.llInitiatedEvents)
    void initiatedEventsClick() {
        ivInitiatedEvents.setVisibility(View.INVISIBLE);
        PrefUtil.Instance().setKeyValue(NotifyTypeConstants.PREF_MY_EVENT, 0);
        FragInitiatedEvents.invoke(getActivity());
    }

    @OnClick(R.id.llSignUpEvents)
    void signUpEventsClick() {
        ivSignUpEvents.setVisibility(View.INVISIBLE);
        PrefUtil.Instance().setKeyValue(NotifyTypeConstants.PREF_SIGN_EVENT, 0);
        ActSignUpEvents.invoke(getActivity());
    }

    @OnClick(R.id.tvSetting)
    void onSettingClick() {
        FragSettings.invoke(getActivity());
    }
    //endregion

    //region event bus 消息处理
    public void onEventMainThread(EBNotify eb) {
        if (NotifyTypeConstants.isEventNotify(eb.notifyType)) {
            updateEventRedHot();
        } else if (eb.notifyType == NotifyTypeConstants.INVITATION_REQUEST) {
            getInvitationCodesTask();
        }
    }

    public void onEventMainThread(EBUser eb) {
        switch (eb.getType()) {
            case EBUser.TYPE_USER_SELF_INFO_CHANGED:
                setDataToView();
                break;
            default:
                break;
        }
    }

    public void onEventMainThread(EBContacts eb) {
        switch (eb.getType()) {
            //本地拒绝或者同意请求
            case EBContacts.TYPE_INVITATION_REQUEST_CHANGE:
                getInvitationCodesTask();
                break;
        }
    }
    //endregion

}
