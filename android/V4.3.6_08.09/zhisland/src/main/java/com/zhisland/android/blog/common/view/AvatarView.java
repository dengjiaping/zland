package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;

/**
 * APP默认头像。可根据用户身份显示不同身份图标，可根据用户性别显示不同默认头像
 */
public class AvatarView extends RelativeLayout {

    private static final int STATUS_ICON_WIDTH = DensityUtil.dip2px(24);
    //默认用户头像图片 resID
    private static final int defaultResID = R.drawable.avatar_default;
    // 头像 ImageView
    private ImageView ivAvatar;

    private ImageView ivMask;

    private ImageView ivStatus;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ivAvatar = new ImageView(getContext());
        ivAvatar.setImageResource(defaultResID);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(ivAvatar, lp);

        ivStatus = new ImageView(getContext());
        ivStatus.setVisibility(View.INVISIBLE);
        lp = new RelativeLayout.LayoutParams(STATUS_ICON_WIDTH,
                STATUS_ICON_WIDTH);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(ivStatus, lp);

        ivMask = new ImageView(getContext());
        ivMask.setVisibility(View.INVISIBLE);
        lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addView(ivMask, lp);
    }

    /**
     * 根据用户的类型、状态等，设置用户的头像显示。
     *
     * @param user
     * @param showUserType : 是否显示 蓝岛 绿岛 金岛的小狮子。
     */
    public void fill(User user, boolean showUserType) {
        if (user == null) {
            return;
        }
        if (user.activity != null
                && (user.activity == User.VALUE_ACTIVITY_BAD || user.activity == User.VALUE_ACTIVITY_DELETE)) {
            // TODO 默认样式
            loadAvatar("", defaultResID);
            return;
        }
        // 根据性别显示不同的默认图片
        int defaultId = defaultResID;
        if (user.sex != null) {
            if (user.sex == User.VALUE_SEX_MAN) {
                defaultId = R.drawable.avatar_default_man;
            } else if (user.sex == User.VALUE_SEX_WOMAN) {
                defaultId = R.drawable.avatar_default_woman;
            }
        }
        loadAvatar(user.userAvatar, defaultId);
        loadStatus(showUserType, user);
    }

    /**
     * 根据用户的类型、状态等，设置用户的头像显示。
     *
     * @param userAvatar   头像url
     * @param showUserType : 是否显示 蓝岛 绿岛 金岛的小狮子。
     */
    public void fill(String userAvatar, boolean showUserType) {
        loadAvatar(userAvatar, R.drawable.avatar_default);
        loadStatus(showUserType, null);
    }

    /**
     * 根据用户类型，显示类型图标，蓝岛、绿岛、海客、终身
     */
    public static void showUserType(ImageView ivStatus, User user) {
        int drawableId = user.getVipIconId();
        if (drawableId == -1) {
            ivStatus.setVisibility(View.GONE);
        } else {
            ivStatus.setVisibility(View.VISIBLE);
            ivStatus.setImageResource(drawableId);
        }
    }

    /**
     * 获取头像 ImageView
     */
    public ImageView getIvAvatar() {
        return ivAvatar;
    }

    /**
     * 加载用户头像
     */
    private void loadAvatar(String imgUrl, int defaultId) {
        ImageWorkFactory.getCircleFetcher().loadImage(imgUrl, ivAvatar, defaultId);
    }

    /**
     * 加载用户身份图标
     */
    private void loadStatus(boolean showUserType, User user) {
        if (showUserType) {
            showUserType(ivStatus, user);
        } else {
            ivStatus.setVisibility(View.GONE);
        }
    }
}
