package com.zhisland.android.blog.feed.view.impl.holder;

import android.view.View;
import android.widget.ImageView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by arthur on 2016/9/6.
 */
public class RecommendHolder {

    @InjectView(R.id.ivRecommendPic)
    public ImageView ivRecommend;

    @InjectView(R.id.vRecommendDivider)
    public View vRecommendDivider;

    public View root;

    public RecommendHolder(View view) {
        ButterKnife.inject(this, view);

        this.root = view;

        int height = DensityUtil.getWidth() * 24 / 100;
        ivRecommend.getLayoutParams().height = height;
        ivRecommend.setBackgroundColor(0Xf8f8f8);
    }

}
