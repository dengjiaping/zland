package com.zhisland.android.blog.profile.controller.resource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的供求 cell
 */
public class UserResourceMergeCell {

    private Context context;

    @InjectView(R.id.ivType)
    ImageView ivType;

    @InjectView(R.id.tvContent)
    TextView tvContent;

    Resource resource;

    public UserResourceMergeCell(View view, Context context) {
        ButterKnife.inject(this, view);
        this.context = context;
    }

    public void fill(Resource resource) {
        this.resource = resource;
        if (resource != null) {
            if (resource.type == Resource.TYPE_SUPPLY) {
                ivType.setImageResource(R.drawable.img_profile_supply);
            } else {
                ivType.setImageResource(R.drawable.img_profile_demand);
            }
            tvContent.setText(resource.content == null ? "" : resource.content);
        }
    }

}
