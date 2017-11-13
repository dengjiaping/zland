package com.zhisland.android.blog.profile.view.block;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.controller.resource.FragUserResource;
import com.zhisland.android.blog.profile.controller.resource.UserResourceMergeCell;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

/**
 * 资源需求合并 block
 */
public class UserResourceBlock extends ProfileBaseCommonBlock<Resource> {

    public UserResourceBlock(Activity context, UserDetail userDetail,
                             SimpleBlock<Resource> block) {
        super(context, userDetail, block);
    }

    @Override
    protected void initChildViews() {
        ivEmptyIcon.setImageResource(R.drawable.img_profile_resource);
        tvEmptyDesc.setText("共享经济，共创利益，大家在等你");
        tvEmptyButton.setText("添加资源需求");
    }

    @Override
    protected void loadData(List<Resource> datas) {
        if (datas == null) {
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.item_block_resource, null);
            UserResourceMergeCell cell = new UserResourceMergeCell(view, context);
            cell.fill(datas.get(i));
            llBlockContent.addView(view);
            if (i < datas.size() - 1) {
                View bottomline = new View(getContext());
                bottomline.setBackgroundColor(getResources()
                        .getColor(R.color.color_div));
                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, getResources()
                        .getDimensionPixelSize(R.dimen.app_line_height));
                lineParams.setMargins(0, DensityUtil.dip2px(10), 0, DensityUtil.dip2px(10));
                llBlockContent.addView(bottomline, lineParams);
            }
        }
    }

    @Override
    protected void onIvRightClick() {
        FragUserResource.invoke(context, getUserDetail());
    }

    @Override
    protected void ontvRightClick() {
    }

    @Override
    protected void onTvEmptyButtonClick() {
        FragUserResource.invoke(context, getUserDetail());
    }

}
