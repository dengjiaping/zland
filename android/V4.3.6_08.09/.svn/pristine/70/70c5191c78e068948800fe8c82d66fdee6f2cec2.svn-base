package com.zhisland.android.blog.profile.view.block;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.profile.controller.comment.ActUserCommentList;
import com.zhisland.android.blog.profile.controller.comment.ActWriteUserComment;
import com.zhisland.android.blog.profile.controller.comment.FragUserCommentEdit;
import com.zhisland.android.blog.profile.controller.comment.UserCommentCell;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserComment;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ToastUtil;

import org.apache.http.client.HttpResponseException;

import java.util.List;

/**
 * 神评论 block
 */
public class UserCommentBlock extends ProfileBaseCommonBlock<UserComment> {

    /**
     * 是否可以评论，默认为可以，只有接口返回987才置为不可以。
     */
    private boolean commentEnable = true;

    public UserCommentBlock(Activity context, UserDetail userDetail,
                            SimpleBlock<UserComment> block) {
        super(context, userDetail, block);
    }

    @Override
    protected void initChildViews() {

        if (isUserSelf()) {
            tvEmptyDesc.setText("暂时还没有人评价过您");
        } else {
            tvEmptyDesc.setText("暂时还没有人评价过TA");
            tvBlockContentRight.setText("去评价");
            tvEmptyButton.setText("给TA第一条评价");
            tvEmptyButton
                    .setBackgroundResource(R.drawable.rect_bwhite_sdc_clargest);
            tvEmptyButton.setPadding(DensityUtil.dip2px(55),
                    DensityUtil.dip2px(8), DensityUtil.dip2px(55),
                    DensityUtil.dip2px(8));
            tvEmptyButton.setCompoundDrawables(null, null, null, null);
        }

        LinearLayout.LayoutParams llContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llContainerParams.topMargin = DensityUtil.dip2px(15);
        llBlockContent.setLayoutParams(llContainerParams);
    }

    @Override
    protected void showContentView(boolean isUpdate) {
        super.showContentView(isUpdate);
        if (!isUserSelf()) {
            tvBlockContentRight.setVisibility(View.VISIBLE);
            tvEmptyButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void showEmptyView() {
        super.showEmptyView();
        if (!isUserSelf()) {
            tvBlockContentRight.setVisibility(View.GONE);
            tvEmptyButton.setVisibility(View.VISIBLE);
        } else {
            ivBlockEmptyEdit.setVisibility(View.VISIBLE);
            tvEmptyButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void loadData(List<UserComment> datas) {
        for (UserComment data : datas) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.item_user_comment, null);
            UserCommentCell cell = new UserCommentCell(view, context,
                    isUserSelf());
            cell.fill(data, UserCommentCell.FROM_PROFILE);

            llBlockContent.addView(view);
            llBlockContent.addView(getDividerView());
        }
        addBottomView();
    }

    private View getDividerView() {
        View dividerView = new View(context);
        dividerView.setBackgroundColor(getResources().getColor(R.color.white));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(15));
        dividerView.setLayoutParams(layoutParams);
        return dividerView;
    }

    @Override
    protected void onIvRightClick() {
        FragUserCommentEdit.invoke(context, getBlockUser(), null);
    }

    @Override
    protected void ontvRightClick() {
        goToWriteComment();
    }

    @Override
    protected void onTvEmptyButtonClick() {
        goToWriteComment();
    }

    private void goToWriteComment() {
        if (commentEnable) {
            ActWriteUserComment.invoke(context, getBlockUser());
        } else {
            ToastUtil.showShort("已经写过神评价，不能重复评价");
        }
    }

    /**
     * 添加底部全部评价view
     */
    public void addBottomView() {
        View bottomline = new View(getContext());
        bottomline.setBackgroundColor(getResources()
                .getColor(R.color.color_div));
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getResources()
                .getDimensionPixelSize(R.dimen.app_line_height));
        llBlockContent.addView(bottomline, lineParams);

        TextView tvBottom = new TextView(getContext());
        tvBottom.setTextColor(getResources().getColor(R.color.color_dc));
        DensityUtil.setTextSize(tvBottom, R.dimen.txt_16);
        tvBottom.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams tvBottomParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvBottomParams.topMargin = DensityUtil.dip2px(15);
        llBlockContent.addView(tvBottom, tvBottomParams);
        tvBottom.setText("全部评价(" + getBlockDataTotal() + ") >");
        tvBottom.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ActUserCommentList.invoke(context, getBlockUser());
            }
        });
    }

    public void setCommentEnable(boolean commentEnable){
        this.commentEnable = commentEnable;
    }
}
