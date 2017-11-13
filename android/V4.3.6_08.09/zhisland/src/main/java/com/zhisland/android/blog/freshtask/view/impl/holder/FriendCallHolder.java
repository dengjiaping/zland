package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhisland.android.blog.R;

import butterknife.InjectView;

/**
 * 建立朋友圈 召唤好友 holder
 */
public class FriendCallHolder {

    @InjectView(R.id.tvTitle)
    public TextView tvTitle;

    @InjectView(R.id.tvDesc)
    public TextView tvDesc;

    @InjectView(R.id.lvData)
    public ListView lvData;

    @InjectView(R.id.ivMask)
    public ImageView ivMask;

    @InjectView(R.id.llBottom)
    public LinearLayout llBottom;

    @InjectView(R.id.tvBottomBtn)
    public TextView tvBottomBtn;

    @InjectView(R.id.tvJump)
    public TextView tvJump;

}
