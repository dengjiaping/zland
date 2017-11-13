package com.zhisland.android.blog.profile.controller.detail;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.eb.EBNotify;
import com.zhisland.android.blog.common.push.NotifyTypeConstants;
import com.zhisland.android.blog.common.retrofit.AppCall;
import com.zhisland.android.blog.common.retrofit.RetrofitFactory;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.IMUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.ScrollTitleBar;
import com.zhisland.android.blog.common.view.pullzoom.PullToZoomScrollViewEx;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExPinnedListener;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExTitleListener;
import com.zhisland.android.blog.common.webview.ActionDialog;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.dto.UserHeatReport;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.android.blog.profile.view.TabWrapper;
import com.zhisland.android.blog.profilemvp.bean.RelationBtnGroup;
import com.zhisland.android.blog.profilemvp.bean.RelationConstants;
import com.zhisland.android.blog.profilemvp.eb.EBRelation;
import com.zhisland.android.blog.profilemvp.model.remote.RelationApi;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.view.tab.TabBarView;
import com.zhisland.lib.view.tab.ZHTabInfo;

import org.apache.http.client.HttpResponseException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 非访客个人主页 fragment
 */
public class FragProfileMainUser extends FragBase implements IProfileView {

    //region 初始化view
    // --------框架------------
    @InjectView(R.id.svProfile)
    PullToZoomScrollViewEx scrollView;
    @InjectView(R.id.llScrollBody)
    LinearLayout llScrollBody;
    @InjectView(R.id.llProfileHead)
    LinearLayout llHead;

    // -------标题------------
    @InjectView(R.id.rlProfileTitle)
    ScrollTitleBar rlTitle;

    // -------tab view--------
    @InjectView(R.id.llInnerTab)
    LinearLayout llInner;
    @InjectView(R.id.ptInner)
    TabBarView tabInner;
    @InjectView(R.id.llPinnedTab)
    LinearLayout llPinned;
    @InjectView(R.id.ptPinned)
    TabBarView tabPinned;
    //endregion


    FragProfileDetailHeader fragHeader;
    FragProfileCover fragCover;
    FragProfileFriendImpression fragDynamic;
    FragProfileDetailBottom fragBottom;
    FragProfileNetError fragError;
    FragImpression fragImpression;
    Fragment curContentFrag;

    private long uid;
    private UserDetail userDetail;
    private User user;
    private boolean isSelf;

    // 看别人个人页点击右上角 action dialog
    private Dialog actionDialog;
    // 删除好友dialog
    private CommonDialog delFriendDlg;
    // 错误提示dialog
    private CommonDialog errorDialog;
    // 举报dialog
    private Dialog reportDlg;
    // 信任好友dialog
    private CommonDialog trustDlg;

    private ScrollViewExPinnedListener pinnedListener;
    private ScrollViewExTitleListener titleAlphaListener;

    private TabWrapper tabWrapper;

    private boolean viewInitOver = false;

    @Override
    public String getPageName() {
        return "FragProfileMainUser";
    }

    public FragProfileMainUser() {
        fragHeader = new FragProfileDetailHeader();
        fragCover = new FragProfileCover();
        fragDynamic = new FragProfileFriendImpression();
        fragBottom = new FragProfileDetailBottom();
        fragError = new FragProfileNetError();
        fragImpression = new FragImpression();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        uid = getActivity().getIntent().getLongExtra(ActProfileDetail.INK_UID, -1);
        this.isSelf = uid == PrefUtil.Instance().getUserId();
        View rootView = inflater.inflate(R.layout.frag_profile_main_user, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(lp);
        ButterKnife.inject(this, rootView);
        viewInitOver = true;
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fragHeader != null) {
            fragHeader.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initView() {
        configView();
        fragError.setListener(new FragProfileNetError.ProfileNetErrorListener() {

            @Override
            public void onClickReload() {
                //TODO
//                loadUser(uid);
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.llProfileHead, fragHeader);
        ft.add(R.id.llBody, fragCover);
        ft.add(R.id.llBody, fragDynamic);
        ft.add(R.id.llBody, fragImpression);
        ft.add(R.id.llBody, fragError);
        ft.add(R.id.llProfileBottom, fragBottom);
        ft.hide(fragDynamic);
        ft.hide(fragImpression);
        ft.hide(fragError);
        ft.commit();
        this.curContentFrag = fragCover;
    }

    // 初始化view属性
    private void configView() {
        int headerHeight = DensityUtil.getWidth() * 3 / 4;
        int titleHeight = getResources().getDimensionPixelOffset(
                R.dimen.title_height);

        // 设置pullzoom的属性
        scrollView.reset();
        scrollView.setZoomView(llHead);
        scrollView.setScrollContentView(llScrollBody);
        scrollView.setHeaderViewSize(LayoutParams.MATCH_PARENT, headerHeight);

        // 设置tab悬浮效果
        pinnedListener = new ScrollViewExPinnedListener();
        pinnedListener.setPinnedOffset(headerHeight - titleHeight);
        pinnedListener.setPinnedView(llPinned);
        scrollView.addOnScrollChangedListener(pinnedListener);

        tabWrapper = new TabWrapper(getActivity()) {

            @Override
            public void selectTabBar(TabBarView view, ZHTabInfo tab, int atIndex) {

                if (atIndex == 0) {
                    showContentFrag(fragCover);
                } else if (atIndex == 1) {
                    showContentFrag(fragDynamic);
                } else {
                    showContentFrag(fragImpression);
                }
            }
        };
        tabWrapper.addTabView(tabInner);
        tabWrapper.addTabView(tabPinned);
        refreshTabView();

        // 设置标题栏渐变效果
        titleAlphaListener = new ScrollViewExTitleListener();
        titleAlphaListener.setTitledOffset(headerHeight - titleHeight);
        titleAlphaListener.setTitleView(rlTitle);
        rlTitle.setLeftRes(R.drawable.sel_btn_back_white,
                R.drawable.sel_btn_back);
        if (isSelf) {
            configSelfShareBtn();
        } else {
            rlTitle.setRightRes(R.drawable.sel_btn_feed_more,
                    R.drawable.sel_btn_more);
        }

        scrollView.addOnScrollChangedListener(titleAlphaListener);

        rlTitle.setLeftClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        // 设置标题栏点击监听事件
        rlTitle.setRightClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isSelf) {
                    // 看自己分享
                    DialogUtil.getInstatnce().showProfileShareDialog(
                            getActivity(), userDetail, fragCover.getPageName());
                } else {
                    // 看别人//TODO分享、举报、删除好友
                    createOtherSheet();
                }
            }
        });
    }

    /**
     * 右上角分享按钮
     */
    private void configSelfShareBtn() {
        if (isSelf) {
            if (userDetail != null && userDetail.share != null) {
                rlTitle.showRightButton();
                rlTitle.setRightRes(R.drawable.sel_btn_share, R.drawable.sel_btn_share_green);
            } else {
                rlTitle.hideRightButton();
            }
        } else {
            rlTitle.setRightRes(R.drawable.sel_btn_feed_more,
                    R.drawable.sel_btn_more);
        }
    }

    private void refreshTabView() {
        if (user == null || user.sex == null || user.sex == User.VALUE_SEX_MAN) {
            tabWrapper.refreshBtnName(isSelf, true);
        } else {
            tabWrapper.refreshBtnName(isSelf, false);
        }
    }

    /**
     * 创建别人的分享dialog
     */
    private void createOtherSheet() {
        if (user == null) {
            return;
        }
        getReportReasonTask();
        if (actionDialog != null && actionDialog.isShowing()) {
            return;
        }
        ArrayList<ActionItem> actions = new ArrayList<ActionItem>();
        if (userDetail != null && userDetail.share != null) {
            actions.add(new ActionItem(1, R.color.color_dc, "分享个人主页"));
        }

        if (userDetail != null && userDetail.relationBtnGroup != null && userDetail.relationBtnGroup.followBtn != null &&
                RelationConstants.hadFollowedMe(userDetail.relationBtnGroup.followBtn.getState())) {
            actions.add(new ActionItem(3, R.color.color_dc, "移除粉丝"));
        }

        actions.add(new ActionItem(2, R.color.color_ac, "我要举报"));

        actionDialog = DialogUtil.createActionSheet(getActivity(), "",
                "取消", actions, new ActionDialog.OnActionClick() {

                    @Override
                    public void onClick(DialogInterface df, int id,
                                        ActionItem item) {
                        if (actionDialog != null && actionDialog.isShowing()) {
                            actionDialog.dismiss();
                        }
                        switch (id) {
                            case 1:
                                // create share dialog
                                DialogUtil.getInstatnce().showProfileShareDialog(
                                        getActivity(), userDetail, fragCover.getPageName());
                                break;
                            case 2:
                                // create report dialog
                                createReportDialog();
                                break;
                            case 3:
                                // create remove fans dialog
                                removeFans();
                                break;
                        }
                    }
                });

        actionDialog.show();
    }

    /**
     * 获取举报原因
     */
    private void getReportReasonTask() {
        ZHApis.getProfileApi().getReportReasonList(getActivity(),
                new TaskCallback<ArrayList<Country>>() {

                    @Override
                    public void onSuccess(ArrayList<Country> content) {
                        DBMgr.getMgr().getCacheDao().set(ActProfileDetail.CACH_REPORT_REASON_LIST, content);
                    }

                    @Override
                    public void onFailure(Throwable error) {

                    }
                });
    }

    /**
     * 举报dialog
     */
    private void createReportDialog() {
        final ArrayList<Country> datas = (ArrayList<Country>) DBMgr.getMgr().getCacheDao().get(ActProfileDetail.CACH_REPORT_REASON_LIST);
        if (datas == null) {
            showToast("举报原因拉取失败");
            return;
        }
        if (reportDlg == null) {
            reportDlg = new Dialog(getActivity(), R.style.DialogGuest);
            reportDlg.setContentView(R.layout.profile_report_user);
            reportDlg.setCancelable(true);

            ListView listView = (ListView) reportDlg
                    .findViewById(R.id.lvReportReason);
            listView.setAdapter(new ReportReasonAdapter(getActivity(),
                    datas));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    reportUserTask(datas.get(position).code);
                }
            });

            TextView tvReportTitle = (TextView) reportDlg
                    .findViewById(R.id.tvReportTitle);
            tvReportTitle.setText("我要举报 " + user.name);

            Window dialogWindow = reportDlg.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = DensityUtil.getWidth() - DensityUtil.dip2px(70); // 宽度
            dialogWindow.setGravity(Gravity.CENTER);
            dialogWindow.setAttributes(lp);
        }
        if (!reportDlg.isShowing()) {
            reportDlg.show();
        }
    }

    /**
     * 移除粉丝dialog
     */
    private void removeFans() {
        showProgressDlg("正在请求服务器。。。");
        Observable.create(new AppCall<Void>() {
            @Override
            protected Response<Void> doRemoteCall() throws Exception {
                Call<Void> call = RetrofitFactory.getInstance().getHttpsApi(RelationApi.class).removeFans(uid);
                return call.execute();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showShort("移除粉丝失败");
                        hideProgressDlg();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        hideProgressDlg();
                        ToastUtil.showShort("成功移除粉丝");
                        if (userDetail != null && userDetail.relationBtnGroup != null && userDetail.relationBtnGroup.followBtn != null) {
                            if (userDetail.relationBtnGroup.followBtn.getState() == RelationConstants.BE_FOLLOWED) {
                                userDetail.relationBtnGroup.followBtn.setState(RelationConstants.UNFOLLOW_TA);
                            }
                            if (userDetail.relationBtnGroup.followBtn.getState() == RelationConstants.BOTH_FLLOWED) {
                                userDetail.relationBtnGroup.followBtn.setState(RelationConstants.FOLLOWED_TA);
                            }
                        }
                        fragBottom.updateUser(userDetail);
                        EBRelation ebRelation = new EBRelation(EBRelation.TYPE_REMOVE_FANS, null);
                        RxBus.getDefault().post(ebRelation);
                    }
                });
    }

    // 更新fragments
    private void updateFrag() {
        if (userDetail == null) {
            return;
        }
        fragHeader.updateUser(userDetail.user);
        fragCover.updateUser(userDetail);
        fragDynamic.updateUser(userDetail);
        fragBottom.updateUser(userDetail);
        showContentFrag(curContentFrag);
    }

    // 更新view
    private void updateView() {
        String title = user != null && !StringUtil.isNullOrEmpty(user.name) ? user.name
                : "个人主页";
        rlTitle.setTitle(title);
        refreshTabView();
        configSelfShareBtn();
        loadAvatarToView();
    }

    /**
     * 显示内容去的fragment
     *
     * @param frag
     */
    private void showContentFrag(Fragment frag) {
        if (frag == null || (!isDetached() && frag.isVisible()))
            return;
        if (frag != fragError) {
            this.curContentFrag = frag;
            llInner.setVisibility(View.VISIBLE);
        } else {
            llInner.setVisibility(View.GONE);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragCover);
        ft.hide(fragError);
        ft.hide(fragDynamic);
        ft.hide(fragImpression);
        ft.show(frag);
        ft.commitAllowingStateLoss();
    }

    /**
     * 举报用户task
     */
    private void reportUserTask(String reason) {
        showProgressDlg();
        ZHApis.getProfileApi().reportUser(getActivity(), user.uid, reason,
                new TaskCallback<Object>() {

                    @Override
                    public void onSuccess(Object content) {
                        showToast("举报成功");
                        if (reportDlg != null && reportDlg.isShowing()) {
                            reportDlg.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        showToast("举报失败");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        hideProgressDlg();
                    }
                });
    }

    /**
     * 错误dialog
     */
    private void showErrorDialog(String title, String content) {
        if (errorDialog == null) {
            errorDialog = new CommonDialog(getActivity());
        }
        errorDialog.show();
        errorDialog.setTitle(title);
        errorDialog.setContent(content);
        errorDialog.setLeftBtnGone();
        errorDialog.setRightBtnColor(getResources().getColor(R.color.color_f2));
        errorDialog.setRightBtnContent("确 定");
    }

    @Override
    public boolean onBackPressed() {
        back();
        return true;
    }

    /**
     * 关闭个人页 判断是否加过好友，用于卡片翻转动画
     */
    private void back() {
        if (fragBottom.isAddFriend) {
            getActivity().setResult(Activity.RESULT_OK);
        }
        getActivity().finish();
    }

    /**
     * 默认加载头像到缓存，用于分享头像
     */
    private void loadAvatarToView() {
        if (user != null && !StringUtil.isNullOrEmpty(user.userAvatar)) {
            ImageView imageView = new ImageView(getActivity());
            ImageWorkFactory.getFetcher().loadImage(user.userAvatar, imageView,
                    ImageWorker.ImgSizeEnum.SMALL);
        }
    }

    private void getCommentEnable() {
        if (user == null) {
            return;
        }
        ZHApis.getProfileApi().getCommentEnable(getActivity(), user.uid, new TaskCallback<Object>() {
            @Override
            public void onSuccess(Object content) {
            }

            @Override
            public void onFailure(Throwable error) {
                if (error != null && error instanceof HttpResponseException) {
                    HttpResponseException exception = (HttpResponseException) error;
                    int code = exception.getStatusCode();
                    if (code == CodeUtil.CODE_UNENABLE_COMMENT) {
                        fragDynamic.setCommentEnable(false);
                        fragCover.setCommentEnable(false);
                    }
                }
            }
        });
    }

    private void onUpdate() {
        updateFrag();
        updateView();
        getCommentEnable();
    }

    @Override
    public void onUpdate(UserDetail userDetail) {
        this.userDetail = userDetail;
        this.user = userDetail.user;
        fragBottom.updateUser(userDetail);
        if (viewInitOver) {
            onUpdate();
        }
    }

    @Override
    public void onLoadError(Throwable error) {
        if (userDetail == null) {
            showContentFrag(fragError);
        }
    }
}
