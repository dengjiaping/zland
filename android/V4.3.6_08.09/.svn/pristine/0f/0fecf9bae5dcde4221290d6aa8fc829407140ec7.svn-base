package com.zhisland.android.blog.info.view.impl.holder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.bean.CountCollect;
import com.zhisland.android.blog.info.bean.Reply;
import com.zhisland.android.blog.info.presenter.InfoSocialPresenter;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr.Tui on 2016/7/19.
 */
public class CommentHolder {

    private Activity context;

    @InjectView(R.id.ivFirstAvatar)
    ImageView ivFirstAvatar;

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

    InfoSocialPresenter presenter;

    public CommentHolder(Activity context, View view, InfoSocialPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
        ButterKnife.inject(this, view);
        holders = new ReplyHolder[5];
        holders[0] = new ReplyHolder(context, rlOne, presenter);
        holders[1] = new ReplyHolder(context, rlTwo, presenter);
        holders[2] = new ReplyHolder(context, rlThree, presenter);
        holders[3] = new ReplyHolder(context, rlFour, presenter);
        holders[4] = new ReplyHolder(context, rlFive, presenter);
    }

    public void fill(Comment comment, boolean isLast) {
        this.comment = comment;
        ImageWorkFactory.getCircleFetcher().loadImage(comment.publisher.userAvatar, ivFirstAvatar, R.drawable.avatar_default);
        tvTime.setText(comment.publishTime);
        tvName.setText(comment.publisher.name);
        tvPosition.setText((comment.publisher.userCompany == null ? ""
                : (comment.publisher.userCompany + " "))
                + (comment.publisher.userPosition == null ? "" : comment.publisher.userPosition));
        tvContent.setText(comment.content);
        tvLike.setText("" + comment.countCollect.likeCount);

        if (comment.countCollect != null && comment.countCollect.likedState == CountCollect.LIKE_STATE_DONE) {
            tvLike.setEnabled(false);
        } else {
            tvLike.setEnabled(true);
        }

        int replysSize = comment.replys == null ? 0 : comment.replys.size();
        if (replysSize > 0) {
            llComment.setVisibility(View.VISIBLE);
            vCommentLine.setVisibility(View.VISIBLE);
            for (int i = 0; i < holders.length; i++) {
                if (i < replysSize) {
                    holders[i].root.setVisibility(View.VISIBLE);
                    Reply reply = comment.replys.get(i);
                    holders[i].fill(comment, reply);
                } else {
                    holders[i].root.setVisibility(View.GONE);
                }
            }
            if (comment.countCollect.replyCount > replysSize || comment.countCollect.replyCount > holders.length) {
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
        presenter.commentLike(comment);
    }

    @OnClick(R.id.tvComment)
    void commentClick() {
        presenter.wantSendComment(SendCommentView.ToType.comment, comment.publisher.name, comment.commentId, null);
    }

    @OnClick(R.id.tvDelete)
    void deleteClick() {
        presenter.onDeleteCommentClick(comment);
    }

    @OnClick(R.id.tvMore)
    void moreClick() {
        presenter.lookMoreReply(comment);
    }

}
