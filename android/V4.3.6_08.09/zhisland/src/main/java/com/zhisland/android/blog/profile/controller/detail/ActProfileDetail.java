package com.zhisland.android.blog.profile.controller.detail;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.IMUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.ScrollTitleBar;
import com.zhisland.android.blog.common.view.pullzoom.PullToZoomScrollViewEx;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExPinnedListener;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExTitleListener;
import com.zhisland.android.blog.common.webview.ActionDialog.OnActionClick;
import com.zhisland.android.blog.profile.controller.detail.FragProfileNetError.ProfileNetErrorListener;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.android.blog.profile.view.TabWrapper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.act.TitleType;
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

/**
 * 个人页
 */
public class ActProfileDetail extends FragBaseActivity {

    public static final String INK_UID = "ink_uid";

    public static final String CACH_USER_DETAIL = "CACH_USER_DETAIL";
    private static final String CACH_REPORT_REASON_LIST = "CACH_REPORT_REASON_LIST";

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

    private AProgressDialog progressDialog;

    FragProfileDetailHeader fragHeader;
    FragProfileCover fragCover;
    FragProfileFriendImpression fragImpression;
    FragProfileDetailBottom fragBottom;
    FragProfileNetError fragError;
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

    /**
     * 唤起用户页面
     *
     * @param context
     * @param userId
     */
    public static void invoke(Context context, long userId) {
        if (userId < 0) {
            return;
        }
        Intent intent = new Intent(context, ActProfileDetail.class);
        intent.putExtra(INK_UID, userId);
        context.startActivity(intent);
    }

    /**
     * 唤起用户页面，并且在启动其他页面时，关闭自己。
     *
     * @param context
     * @param userId
     */
    public static void invokeNoHistory(Context context, long userId) {
        if (userId < 0) {
            return;
        }
        Intent intent = new Intent(context, ActProfileDetail.class);
        intent.putExtra(INK_UID, userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);
    }

    // 带requestCode
    public static void invoke(Activity context, long userId, int reqCode) {
        Intent intent = new Intent(context, ActProfileDetail.class);
        if (userId <= 0) {
            return;
        }
        intent.putExtra(INK_UID, userId);
        context.startActivityForResult(intent, reqCode);
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_NONE;
    }

    @Override
    public boolean shouldContinueCreate(Bundle savedInstanceState) {
        uid = getIntent().getLongExtra(INK_UID, -1);
        isSelf = uid == PrefUtil.Instance().getUserId();
        if (uid < 0) {
            Toast.makeText(this, "用户ID不能为空", Toast.LENGTH_LONG).show();
        }
        return uid > 0;
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);

        setSwipeBackEnable(false);

        configView();

        fragHeader = new FragProfileDetailHeader();
        fragCover = new FragProfileCover();
        fragImpression = new FragProfileFriendImpression();
        fragBottom = new FragProfileDetailBottom();
        fragError = new FragProfileNetError();
        fragError.setListener(new ProfileNetErrorListener() {

            @Override
            public void onClickReload() {
                loadUser(uid);
            }
        });

        user = DBMgr.getMgr().getUserDao().getUserById(uid);
        if (user != null) {
            userDetail = (UserDetail) DBMgr.getMgr().getCacheDao()
                    .get(CACH_USER_DETAIL + user.uid);
            updateFrag(userDetail);
            updateView(user);
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.llProfileHead, fragHeader);
        ft.add(R.id.llBody, fragCover);
        ft.add(R.id.llBody, fragImpression);
        ft.add(R.id.llBody, fragError);
        ft.add(R.id.llProfileBottom, fragBottom);
        ft.hide(fragImpression);
        ft.hide(fragError);
        ft.commit();

        this.curContentFrag = fragCover;

        loadUser(uid);
        getCommentEnable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EBProfile eb) {
        loadUser(uid, true, eb.getType());
        getCommentEnable();
    }

    @Override
    protected int layResId() {
        return R.layout.act_profile_detail;
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

        tabWrapper = new TabWrapper(this) {

            @Override
            public void selectTabBar(TabBarView view, ZHTabInfo tab, int atIndex) {

                if (atIndex == 0) {
                    showContentFrag(fragCover);
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

        rlTitle.setLeftClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                back();
            }
        });

        // 设置标题栏点击监听事件
        rlTitle.setRightClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isSelf) {
                    // 看自己分享
                    DialogUtil.getInstatnce().showProfileShareDialog(
                            ActProfileDetail.this, userDetail, fragCover.getPageName());
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
        if (userDetail != null && userDetail.share != null) {
            rlTitle.showRightButton();
            rlTitle.setRightRes(R.drawable.sel_btn_share, R.drawable.sel_btn_share_green);
        } else {
            rlTitle.hideRightButton();
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
            actions.add(new ActionItem(1, R.color.color_dc, "分享到..."));
        }
        boolean isFriend = com.zhisland.im.data.DBMgr.getHelper()
                .getContactDao().isFriend(user.uid);
        if (isFriend) {
            if (user.isTrustFriend()) {
                actions.add(new ActionItem(4, R.color.color_dc, "标为普通好友"));
            } else {
                actions.add(new ActionItem(4, R.color.color_dc, "标为信任好友"));
            }
        }
        actions.add(new ActionItem(2, R.color.color_dc, "我要举报"));
        if (isFriend) {
            actions.add(new ActionItem(3, R.color.color_ac, "删除好友"));
        }

        actionDialog = DialogUtil.createActionSheet(ActProfileDetail.this, "",
                "取消", actions, new OnActionClick() {

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
                                        ActProfileDetail.this, userDetail, fragCover.getPageName());
                                break;
                            case 2:
                                // create report dialog
                                createReportDialog();
                                break;
                            case 3:
                                // create delete friend dialog
                                showDeleteFriendDialog();
                                break;
                            case 4:
                                // create trust friend dialog
                                showTrustFriendDialog();
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
        ZHApis.getProfileApi().getReportReasonList(ActProfileDetail.this,
                new TaskCallback<ArrayList<Country>>() {

                    @Override
                    public void onSuccess(ArrayList<Country> content) {
                        DBMgr.getMgr().getCacheDao()
                                .set(CACH_REPORT_REASON_LIST, content);
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
        final ArrayList<Country> datas = (ArrayList<Country>) DBMgr.getMgr()
                .getCacheDao().get(CACH_REPORT_REASON_LIST);
        if (datas == null) {
            ToastUtil.showShort("举报原因拉取失败");
            return;
        }
        if (reportDlg == null) {
            reportDlg = new Dialog(ActProfileDetail.this, R.style.DialogGuest);
            reportDlg.setContentView(R.layout.profile_report_user);
            reportDlg.setCancelable(true);

            ListView listView = (ListView) reportDlg
                    .findViewById(R.id.lvReportReason);
            listView.setAdapter(new ReportReasonAdapter(ActProfileDetail.this,
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
     * 删除好友dialog
     */
    private void showDeleteFriendDialog() {
        if (delFriendDlg == null) {
            delFriendDlg = new CommonDialog(ActProfileDetail.this);
        }
        if (!delFriendDlg.isShowing()) {
            delFriendDlg.show();
            delFriendDlg.setTitle("确认删除 " + user.name + " 吗？");
            delFriendDlg.setContent("删除后将不能查看对方的联系方式与发送消息");
            delFriendDlg.setEditTextVisibility(View.GONE);
            delFriendDlg.setLeftBtnContent("取消");
            delFriendDlg.setRightBtnColor(getResources().getColor(
                    R.color.color_f2));
            delFriendDlg.setRightBtnContent("确认删除");
            delFriendDlg.setRightBtnColor(getResources().getColor(
                    R.color.color_ac));
            delFriendDlg.tvDlgConfirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    delFriendDlg.dismiss();
                    blockFriend();
                }
            });
        }
    }

    /**
     * 信任好友dialog
     */
    private void showTrustFriendDialog() {
        if (trustDlg == null) {
            trustDlg = new CommonDialog(ActProfileDetail.this);
        }
        if (!trustDlg.isShowing()) {
            trustDlg.show();
            String target = user.isTrustFriend() ? "普通好友" : "信任好友";
            trustDlg.setContent("确认您要标记" + user.name + "为" + target);
            trustDlg.setLeftBtnContent("取消");
            trustDlg.setEditTextVisibility(View.GONE);
            trustDlg.setRightBtnColor(getResources().getColor(R.color.color_dc));
            trustDlg.setRightBtnContent("确认");
            trustDlg.setRightBtnColor(getResources().getColor(R.color.color_dc));
            trustDlg.tvDlgConfirm.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    changeFriendGrade();
                    trustDlg.dismiss();
                }
            });
        }
    }

    /**
     * 修改好友关系
     */
    private void changeFriendGrade() {
        if (user.isTrustFriend()) {
            boolean result = IMUtil.downgradeFriend(user.uid);
            if (result) {
                ToastUtil.showShort("成功标记为普通好友！");
                user.relationLevel = User.VALUE_RELATION_LEVEL_NORMAL;
                onFriendGradechanged();
            } else {
                showErrorDialog("操作失败", "标记普通好友失败，请重新操作");
            }
        } else {
            boolean result = IMUtil.promoteFriend(user.uid);
            if (result) {
                ToastUtil.showShort("成功标记为信任好友！");
                user.relationLevel = User.VALUE_RELATION_LEVEL_TRUST;
                onFriendGradechanged();
            } else {
                showErrorDialog("操作失败", "标记信任好友失败，请重新操作");
            }
        }
    }

    /**
     * 好友关系发生变化
     */
    private void onFriendGradechanged() {
        User userTemp = new User();
        userTemp.uid = user.uid;
        userTemp.relationLevel = user.relationLevel;
        DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(userTemp);
        fragBottom.updateUser(user);
    }

    /**
     * 删除好友
     */
    private void blockFriend() {
        boolean result = IMUtil.blockFriend(user.uid);
        if (result) {
            ToastUtil.showShort("您已成功移除好友 " + user.name);
            // 刷新bottom view
            fragBottom.updateUser(user);
        } else {
            showErrorDialog("操作失败", "移除好友失败，请重新操作");
        }
    }

    // 更新fragments
    private void updateFrag(UserDetail userDetail) {
        if (userDetail == null) {
            return;
        }
        fragHeader.updateUser(userDetail.user);
        fragCover.updateUser(userDetail);
        fragImpression.updateUser(userDetail);
        fragBottom.updateUser(userDetail.user);
    }

    // 更新view
    private void updateView(User user) {
        String title = user != null && !StringUtil.isNullOrEmpty(user.name) ? user.name
                : "个人主页";
        rlTitle.setTitle(title);
        refreshTabView();
    }

    /**
     * 显示内容去的fragment
     *
     * @param frag
     */
    private void showContentFrag(Fragment frag) {
        if (frag.isVisible())
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
        ft.hide(fragImpression);
        ft.show(frag);
        ft.commitAllowingStateLoss();
    }

    // 显示加载对话框
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(this);
            progressDialog.setText("加载中...");
        }
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    /**
     * 加载用户数据
     */
    private void loadUser(long userId) {
        loadUser(userId, false, -1);
    }

    /**
     * 加载用户数据
     *
     * @param id       用户id
     * @param isUpdate 是否来自于修改block
     * @param type     修改的type
     */
    private void loadUser(long id, final boolean isUpdate, int type) {
        if (userDetail == null && !isUpdate) {
            showProgressDialog();
        }
        ZHApis.getUserApi().getUserDetail(ActProfileDetail.this, id, new TaskCallback<UserDetail>() {

            @Override
            public void onSuccess(UserDetail content) {
                if (!ActProfileDetail.this.isFinishing() && content != null && ActProfileDetail.this != null) {
                    ActProfileDetail.this.userDetail = content;
                    ActProfileDetail.this.user = content.user;
                    com.zhisland.im.data.DBMgr.getHelper().getChatDao()
                            .updateAvatar(content.user.uid, user.userAvatar);
                    DBMgr.getMgr().getCacheDao()
                            .set(CACH_USER_DETAIL + user.uid, content);
                    if (!isUpdate) {
                        updateFrag(content);
                        updateView(user);

                        showContentFrag(curContentFrag);
                        if (isSelf){
                            configSelfShareBtn();
                        }
                    } else {
                        updateFrag(content);
                    }
                    loadAvatarToView();
                }
            }

            @Override
            public void onFailure(Throwable error) {
                if (userDetail == null) {
                    showContentFrag(fragError);
                }
            }

            @Override
            public void onFinish() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                super.onFinish();
            }

        });
    }

    /**
     * 举报用户task
     */
    private void reportUserTask(String reason) {
        showProgressDialog();
        ZHApis.getProfileApi().reportUser(ActProfileDetail.this, user.uid, reason,
                new TaskCallback<Object>() {

                    @Override
                    public void onSuccess(Object content) {
                        ToastUtil.showShort("举报成功");
                        if (reportDlg != null && reportDlg.isShowing()) {
                            reportDlg.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        ToastUtil.showShort("举报失败");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (progressDialog != null
                                && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    /**
     * 错误dialog
     */
    private void showErrorDialog(String title, String content) {
        if (errorDialog == null) {
            errorDialog = new CommonDialog(this);
        }
        errorDialog.show();
        errorDialog.setTitle(title);
        errorDialog.setContent(content);
        errorDialog.setLeftBtnGone();
        errorDialog.setRightBtnColor(getResources().getColor(R.color.color_f2));
        errorDialog.setRightBtnContent("确 定");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fragHeader != null) {
            fragHeader.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    /**
     * 关闭个人页 判断是否加过好友，用于卡片翻转动画
     */
    private void back() {
        if (fragBottom.isAddFriend) {
            ActProfileDetail.this.setResult(RESULT_OK);
        }
        ActProfileDetail.this.finish();
    }

    /**
     * 默认加载头像到缓存，用于分享头像
     */
    private void loadAvatarToView() {
        ImageView imageView = new ImageView(this);
        ImageWorkFactory.getFetcher().loadImage(user.userAvatar, imageView,
                ImageWorker.ImgSizeEnum.SMALL);
    }

    private void getCommentEnable() {
        ZHApis.getProfileApi().getCommentEnable(this, uid, new TaskCallback<Object>() {
            @Override
            public void onSuccess(Object content) {
            }

            @Override
            public void onFailure(Throwable error) {
                if (error != null && error instanceof HttpResponseException) {
                    HttpResponseException exception = (HttpResponseException) error;
                    int code = exception.getStatusCode();
                    if (code == CodeUtil.CODE_UNENABLE_COMMENT) {
                        fragImpression.setCommentEnable(false);
                        fragCover.setCommentEnable(false);
                    }
                }
            }
        });
    }

}
