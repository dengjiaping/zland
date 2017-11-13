package com.zhisland.android.blog.profile.view.block;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.profile.controller.drip.ActUserDripList;
import com.zhisland.android.blog.profile.controller.drip.UserDripProfileCell;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.lib.util.StringUtil;

import java.util.List;

/**
 * 用户点滴 block
 */
public class UserDripBlock extends ProfileBaseCommonBlock<CustomDict> implements UserDripProfileCell.CallBack {

    public UserDripBlock(Activity context, UserDetail userDetail,
                         SimpleBlock<CustomDict> block) {
        super(context, userDetail, block);
    }

    @Override
    protected void initChildViews() {
        tvEmptyDesc.setText("您暂时还没有点滴");
        tvEmptyButton.setText("添加我的点滴");
    }

    @Override
    protected void loadData(List<CustomDict> datas) {
        int index = 0;
        for (CustomDict data : datas) {
            if (data.isVisitDisplayable()) {
                View view = LayoutInflater.from(context).inflate(
                        R.layout.item_block_drip, null);
                UserDripProfileCell cell = new UserDripProfileCell(view, context, this);
                cell.fill(data, index);
                index++;
                llBlockContent.addView(view);
            }
        }
    }

    @Override
    protected void onIvRightClick() {
        ActUserDripList.invoke(context, getBlockDatas());
    }

    @Override
    protected void ontvRightClick() {
    }

    @Override
    protected void onTvEmptyButtonClick() {
        ActUserDripList.invoke(context, getBlockDatas());
    }

    @Override
    public void onEmptyClick() {
        ActUserDripList.invoke(context, getBlockDatas());
    }
}
