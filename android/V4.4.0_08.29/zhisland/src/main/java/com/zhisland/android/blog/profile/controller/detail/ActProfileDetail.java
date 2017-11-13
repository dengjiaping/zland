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
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.ScrollTitleBar;
import com.zhisland.android.blog.common.view.pullzoom.PullToZoomScrollViewEx;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExPinnedListener;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExTitleListener;
import com.zhisland.android.blog.common.webview.ActionDialog.OnActionClick;
import com.zhisland.android.blog.feed.view.impl.FragMyPubFeedList;
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

    public static final String CACH_REPORT_REASON_LIST = "CACH_REPORT_REASON_LIST";

    private long uid;
    private UserDetail userDetail;
    private User user;
    private boolean isSelf;

    FragProfileMainUser fragProfileMainUser;
    FragProfileVisitorOther fragProfileVisitorOther;
    FragProfileVisitorSelf fragProfileVisitorSelf;

    private IProfileView cruFragView;

    //region invoke 方法

    /**
     * 唤起用户页面
     *
     * @param context
     * @param userId
     */
    public static void invoke(Context context, long userId) {
//        FragMyPubFeedList.Invoke(context);
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
    //endregion

    //region 生命周期方法

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
        fragProfileMainUser = new FragProfileMainUser();
        fragProfileVisitorOther = new FragProfileVisitorOther();
        fragProfileVisitorSelf = new FragProfileVisitorSelf();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.root, fragProfileMainUser);
        ft.add(R.id.root, fragProfileVisitorOther);
        ft.add(R.id.root, fragProfileVisitorSelf);
        ft.hide(fragProfileMainUser);
        ft.hide(fragProfileVisitorOther);
        ft.hide(fragProfileVisitorSelf);
        ft.commit();

        user = DBMgr.getMgr().getUserDao().getUserById(uid);
        userDetail = (UserDetail) DBMgr.getMgr().getCacheDao().get(UserDetail.CACH_USER_DETAIL + uid);

        switchShowType();
        loadUser(uid);
    }

    private void switchShowType() {
        if (user != null && user.isYuZhuCe()) {
            if (isSelf) {
                showProfileVisitorSelf();
            } else {
                showProfileVisitorOther();
            }
        } else {
            showProfileMainUser();
        }
        if (userDetail != null && cruFragView != null) {
            cruFragView.onUpdate(userDetail);
        }
    }

    private void switchShowError(Throwable error) {
        if (cruFragView != null) {
            cruFragView.onLoadError(error);
        }
    }

    private void showProfileMainUser() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.show(fragProfileMainUser);
        ft.hide(fragProfileVisitorOther);
        ft.hide(fragProfileVisitorSelf);
        ft.commit();
        cruFragView = fragProfileMainUser;
    }

    private void showProfileVisitorOther() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragProfileMainUser);
        ft.show(fragProfileVisitorOther);
        ft.hide(fragProfileVisitorSelf);
        ft.commit();
        cruFragView = fragProfileVisitorOther;
    }

    private void showProfileVisitorSelf() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragProfileMainUser);
        ft.hide(fragProfileVisitorOther);
        ft.show(fragProfileVisitorSelf);
        ft.commit();
        cruFragView = fragProfileVisitorSelf;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fragProfileMainUser != null) {
            fragProfileMainUser.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_NONE;
    }

    @Override
    protected int layResId() {
        return R.layout.act_profile_detail;
    }
    //endregion


    public void onEventMainThread(EBProfile eb) {
        loadUser(uid, true, eb.getType());
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
            showProgressDlg();
        }
        ZHApis.getUserApi().getUserDetail(this, id, new TaskCallback<UserDetail>() {

            @Override
            public void onSuccess(UserDetail content) {
                if (!isFinishing() && content != null) {
                    userDetail = content;
                    user = content.user;
                    switchShowType();
                }
            }

            @Override
            public void onFailure(Throwable error) {
                switchShowError(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDlg();
            }

        });
    }

}
