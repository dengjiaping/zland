package com.zhisland.android.blog.info.view.impl.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;

import butterknife.InjectView;

/**
 * Created by arthur on 2016/7/20.
 */
public class InfoSocialHolder {

    @InjectView(R.id.llBottom)
    public RelativeLayout llBottom;

    @InjectView(R.id.vLineBottom)
    public View vLineBottom;

    @InjectView(R.id.tvUp)
    public TextView tvUp;

    @InjectView(R.id.tvDown)
    public TextView tvDown;

    @InjectView(R.id.tvCommentCount)
    public TextView tvCommentCount;

    @InjectView(R.id.llComment)
    public LinearLayout llComment;

    @InjectView(R.id.llSendComment)
    public LinearLayout llSendComment;

    @InjectView(R.id.rlSendComment)
    public RelativeLayout rlSendComment;

    @InjectView(R.id.rlDown)
    public RelativeLayout rlDown;

    @InjectView(R.id.rlUp)
    public RelativeLayout rlUp;

    @InjectView(R.id.vLineUpDown)
    public View vLineUpDown;

    @InjectView(R.id.tvUpText)
    public TextView tvUpText;

    @InjectView(R.id.tvDownText)
    public TextView tvDownText;

    @InjectView(R.id.ivDown)
    public ImageView ivDown;

    @InjectView(R.id.ivUp)
    public ImageView ivUp;

}
