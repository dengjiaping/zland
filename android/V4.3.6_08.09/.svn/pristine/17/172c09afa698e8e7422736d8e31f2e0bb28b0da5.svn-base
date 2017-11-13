package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.AvatarView;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 建立好友圈，加好友 item holder
 */
public class FriendAddItemHolder {

    @InjectView(R.id.ivUserAvatar)
    public AvatarView ivUserAvatar;

    @InjectView(R.id.ivRight)
    public ImageView ivRight;

    @InjectView(R.id.tvUserName)
    public TextView tvUserName;

    @InjectView(R.id.tvComAndPos)
    public TextView tvComAndPos;

    public User item;
    private FriendAddItemHolderListener listener;

    public void fill(User item) {
        this.item = item;
        ivUserAvatar.fill(item, true);
        tvUserName.setText(item.name);
        if (item.sendApplyFriendRequest != null && item.sendApplyFriendRequest) {
            ivRight.setImageResource(R.drawable.task_btn_add_success);
            ivRight.setEnabled(false);
        } else {
            ivRight.setImageResource(R.drawable.bg_task_add_friend);
            ivRight.setEnabled(true);
        }
        tvComAndPos.setText(item.combineCompanyAndPosition());
    }

    /**
     * 更新UI
     */
    public void refresh() {
        if (item != null) {
            fill(item);
        }
    }

    @OnClick(R.id.ivRight)
    public void onClickRight() {
        if (listener != null) {
            listener.onClickAddFriend(this);
        }
    }


    public void setListener(FriendAddItemHolderListener listener) {
        this.listener = listener;
    }
}
