package com.zhisland.android.blog.profile.controller.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.IMUtil;
import com.zhisland.android.blog.common.util.PermissionsMgr;
import com.zhisland.android.blog.im.controller.ActChat;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.ToastUtil;

/**
 * 个人页bottom
 */
public class FragProfileDetailBottom extends FragBase {

    private static final String PAGE_NAME = "FragProfileDetailBottom";
    @InjectView(R.id.llBottom)
    LinearLayout llBottom;
    @InjectView(R.id.llBottomBtn)
    LinearLayout llBottomBtn;
    @InjectView(R.id.tvBottomBtn)
    TextView tvBottomBtn;

    private User userSelf;
    private User userFrom;
    // 是否执行加好友的操作
    public boolean isAddFriend;

    private boolean isFriend = false;

    private CommonDialog errorDialog;
    private CommonDialog commonDialog;

    public FragProfileDetailBottom() {
        userSelf = DBMgr.getMgr().getUserDao().getSelfUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_profile_detail_bottom,
                null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setDataToView();
    }

    /**
     * 底部按钮view
     */
    private void setDataToView() {
        if (userFrom == null) {
            return;
        }
        if (userFrom.uid == userSelf.uid) {
            llBottom.setVisibility(View.GONE);
        } else {
            llBottom.setVisibility(View.VISIBLE);
        }

        if (isFriend) {
            tvBottomBtn.setText("发消息");
            if (userFrom.isTrustFriend()) {
                llBottomBtn.setBackgroundResource(R.drawable.sel_bg_btn_bsc);
            } else {
                llBottomBtn.setBackgroundResource(R.drawable.sel_bg_btn_bgreen);
            }
            tvBottomBtn.setCompoundDrawables(null, null, null, null);
        } else {
            tvBottomBtn.setText("加好友");
            llBottomBtn.setBackgroundResource(R.drawable.sel_bg_btn_bgreen);
            Drawable drawable = getResources().getDrawable(
                    R.drawable.profile_changecard);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tvBottomBtn.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @OnClick(R.id.llBottomBtn)
    void bottomClick() {
        if (isFriend) {
            ActChat.invoke(getActivity(), userFrom.uid);
        } else {
            if (PermissionsMgr.getInstance().canAddFriend(userFrom)) {
                if (commonDialog == null) {
                    commonDialog = new CommonDialog(getActivity());
                }
                if (!commonDialog.isShowing()) {
                    commonDialog.show();
                    commonDialog.setTitle("确认您要添加" + userFrom.name + "为好友");
                    commonDialog.setLeftBtnContent("取消");
                    commonDialog.setRightBtnColor(getResources().getColor(
                            R.color.color_dc));
                    commonDialog.setRightBtnContent("确认");
                    commonDialog.setRightBtnColor(getActivity().getResources()
                            .getColor(R.color.color_dc));
                    commonDialog.tvDlgConfirm
                            .setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    commonDialog.dismiss();
                                    addFriend();
                                }
                            });
                }
            } else {
                DialogUtil.showPermissionsDialog(getActivity(), getPageName());
            }
        }
    }

    /**
     * 加好友
     */
    private void addFriend() {
        boolean result = IMUtil.addFriend(userFrom.uid);
        isAddFriend = true;
        if (result) {
            ToastUtil.showShort("好友请求已发送");
        } else {
            showErrorDialog("发送失败", "发送好友申请失败，请重新发送");
        }
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

    /**
     * profile框架 传过来的user
     */
    public void updateUser(User user) {
        this.userFrom = user;
        isFriend = com.zhisland.im.data.DBMgr.getHelper().getContactDao()
                .isFriend(userFrom.uid);
        if (this.isAdded()) {
            setDataToView();
        }
    }

    @Override
    protected String getPageName() {
        return PAGE_NAME;
    }

}
