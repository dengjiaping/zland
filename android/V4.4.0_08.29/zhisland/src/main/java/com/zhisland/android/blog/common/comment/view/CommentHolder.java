package com.zhisland.android.blog.common.comment.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.comment.presenter.OnCommentListener;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 评论（观点）Item Holder
 * Created by Mr.Tui on 2016/7/19.
 */
public class CommentHolder {

    private Activity context;

    @InjectView(R.id.ivFirstAvatar)
    ImageView ivFirstAvatar;

    @InjectView(R.id.ivUserType)
    ImageView ivUserType;

    @InjectView(R.id.tvTime)
    TextView tvTime;

    @InjectView(R.id.tvName)
    TextView tvName;

    @InjectView(R.id.tvPosition)
    TextView tvPosition;

    @InjectView(R.id.tvContent)
    TextView tvContent;

    @InjectView(R.id.tvLike)
    TextView tvLike;

    @InjectView(R.id.vCommentLine)
    View vCommentLine;

    @InjectView(R.id.llComment)
    LinearLayout llComment;

    @InjectView(R.id.tvDelete)
    TextView tvDelete;

    @InjectView(R.id.vLineDelete)
    View vLineDelete;

    @InjectView(R.id.rlOne)
    View rlOne;

    @InjectView(R.id.rlTwo)
    View rlTwo;

    @InjectView(R.id.rlThree)
    View rlThree;

    @InjectView(R.id.rlFour)
    View rlFour;

    @InjectView(R.id.rlFive)
    View rlFive;

    @InjectView(R.id.tvMore)
    TextView tvMore;

    @InjectView(R.id.vBottomLine)
    View vBottomLine;

    @InjectView(R.id.vBottomSpace)
    View vBottomSpace;

    ReplyHolder[] holders;

    Comment comment;

    OnCommentListener onCommentListener;

    /**
     * 评论详情页面的header
     */
    private boolean forCommentDetail = false;

    public CommentHolder(Activity context, View view, OnCommentListener onCommentListener) {
        this.context = context;
        this.onCommentListener = onCommentListener;
        ButterKnife.inject(this, view);
        holders = new ReplyHolder[5];
        holders[0] = new ReplyHolder(context, rlOne, onCommentListener);
        holders[1] = new ReplyHolder(context, rlTwo, onCommentListener);
        holders[2] = new ReplyHolder(context, rlThree, onCommentListener);
        holders[3] = new ReplyHolder(context, rlFour, onCommentListener);
        holders[4] = new ReplyHolder(context, rlFive, onCommentListener);
    }

    public void fill(Comment comment, boolean isLast) {
        this.comment = comment;
        if (comment == null) {
            return;
        }
        if (comment.publisher != null) {
            ImageWorkFactory.getCircleFetcher().loadImage(comment.publisher.userAvatar, ivFirstAvatar, R.drawable.avatar_default);
            ivUserType.setImageResource(comment.publisher.getVipIconId());
            tvName.setText(comment.publisher.name);
            tvPosition.setText((comment.publisher.userCompany == null ? ""
                    : (comment.publisher.userCompany + " "))
                    + (comment.publisher.userPosition == null ? "" : comment.publisher.userPosition));
        }
        tvTime.setText(comment.publishTime);
        tvContent.setText(comment.content);
        if (comment.likeCustomIcon != null) {
            tvLike.setText(comment.likeCustomIcon.quantity > 0 ? (comment.likeCustomIcon.quantity + "") : "赞");
        }

        if (comment.likeCustomIcon != null && comment.likeCustomIcon.operable == 1) {
            tvLike.setEnabled(true);
            if (comment.likeCustomIcon.clickState == 0) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.feed_btn_praise);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
                tvLike.setCompoundDrawables(drawable, null, null, null);
            } else {
                Drawable drawable = context.getResources().getDrawable(R.drawable.feed_btn_praise_selected);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
                tvLike.setCompoundDrawables(drawable, null, null, null);
            }
        } else {
            tvLike.setEnabled(false);
            Drawable drawable = context.getResources().getDrawable(R.drawable.feed_btn_praise_selected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());//必须设置图片大小，否则不显示
            tvLike.setCompoundDrawables(drawable, null, null, null);
        }

        int replysSize = comment.replyList == null ? 0 : comment.replyList.size();
        if (replysSize > 0) {
            llComment.setVisibility(View.VISIBLE);
            vCommentLine.setVisibility(View.VISIBLE);
            for (int i = 0; i < holders.length; i++) {
                if (i < replysSize) {
                    holders[i].root.setVisibility(View.VISIBLE);
                    Reply reply = comment.replyList.get(i);
                    holders[i].fill(comment, reply);
                } else {
                    holders[i].root.setVisibility(View.GONE);
                }
            }
            if (comment.replyCount > replysSize || comment.replyCount > holders.length) {
                tvMore.setVisibility(View.VISIBLE);
            } else {
                tvMore.setVisibility(View.GONE);
            }
        } else {
            llComment.setVisibility(View.GONE);
            vCommentLine.setVisibility(View.GONE);
        }

        if (comment.publisher.uid == PrefUtil.Instance().getUserId()) {
            tvDelete.setVisibility(View.VISIBLE);
            vLineDelete.setVisibility(View.VISIBLE);
        } else {
            tvDelete.setVisibility(View.GONE);
            vLineDelete.setVisibility(View.GONE);
        }

        if (isLast) {
            vBottomLine.setVisibility(View.GONE);
            vBottomSpace.setVisibility(View.VISIBLE);
        } else {
            vBottomLine.setVisibility(View.VISIBLE);
            vBottomSpace.setVisibility(View.GONE);
        }

        if (forCommentDetail) {
            vCommentLine.setVisibility(View.VISIBLE);
            vBottomLine.setVisibility(View.GONE);
            vBottomSpace.setVisibility(View.GONE);
        }
    }

    public void setForCommentDetail(boolean forCommentDetail) {
        this.forCommentDetail = forCommentDetail;
    }

    @OnClick(R.id.ivFirstAvatar)
    void avatarClick() {
        ActProfileDetail.invoke(context, comment.publisher.uid);
    }

    @OnClick(R.id.tvName)
    void nameClick() {
        ActProfileDetail.invoke(context, comment.publisher.uid);
    }

    @OnClick(R.id.tvPosition)
    void positionClick() {
        ActProfileDetail.invoke(context, comment.publisher.uid);
    }

    @OnClick(R.id.tvLike)
    void likeClick() {
        onCommentListener.onCommentLikeClick(comment);
    }

    @OnClick(R.id.tvComment)
    void commentClick() {
        onCommentListener.wantSendComment(SendCommentView.ToType.comment, comment.publisher.name, comment.commentId, null);
    }

    @OnClick(R.id.tvDelete)
    void deleteClick() {
        onCommentListener.onDeleteCommentClick(comment);
    }

    @OnClick(R.id.tvMore)
    void moreClick() {
        onCommentListener.lookMoreReply(comment);
    }

}
