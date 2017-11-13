package com.zhisland.android.blog.common.comment.view;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.comment.presenter.OnReplyListener;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 评论（观点）详情，回复列表Holder
 * Created by Mr.Tui on 2016/7/22.
 */
public class ReplyHolder {

    private Activity context;
    private OnReplyListener onReplyListener;
    private Comment comment;
    private Reply reply;

    View root;

    @InjectView(R.id.ivAvatar)
    ImageView ivAvatar;

    @InjectView(R.id.tvTime)
    TextView tvTime;

    @InjectView(R.id.tvName)
    TextView tvName;

    @InjectView(R.id.tvContent)
    TextView tvContent;

    public ReplyHolder(Activity context, View view, OnReplyListener onReplyListener) {
        this.root = view;
        root.setOnClickListener(rootClick);
        this.context = context;
        this.onReplyListener = onReplyListener;
        ButterKnife.inject(this, view);
    }

    public void fill(Comment comment, Reply reply) {
        this.comment = comment;
        this.reply = reply;
        tvTime.setText(reply.publishTime);

        if (reply.fromUser != null) {
            ImageWorkFactory.getCircleFetcher().loadImage(reply.fromUser.userAvatar, ivAvatar, R.drawable.avatar_default);
            String toName = "";
            if (reply.toUser != null) {
                if (StringUtil.isNullOrEmpty(reply.toUser.name)) {
                    reply.toUser.name = "  ";
                }
                toName = " 回复 " + reply.toUser.name;
            }
            SpannableString ss = new SpannableString(reply.fromUser.name + toName);
            ss.setSpan(new NameClickSpan(reply.fromUser.uid), 0, reply.fromUser.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (reply.toUser != null && !StringUtil.isNullOrEmpty(toName)) {
                ss.setSpan(new NameClickSpan(reply.toUser.uid), reply.fromUser.name.length() + 4, reply.fromUser.name.length() + 4 + reply.toUser.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tvName.setMovementMethod(LinkMovementMethod.getInstance());
            tvName.setText(ss);
        }

        tvContent.setText(reply.content);
    }

    class NameClickSpan extends ClickableSpan {

        long uid;

        public NameClickSpan(long uid) {
            this.uid = uid;
        }

        @Override
        public void onClick(View widget) {
            ActProfileDetail.invoke(context, uid);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(context.getResources().getColor(R.color.color_f1));
            ds.setUnderlineText(false);
        }
    }

    @OnClick(R.id.ivAvatar)
    void onAvatarClick() {
        if (reply.fromUser != null) {
            ActProfileDetail.invoke(context, reply.fromUser.uid);
        }
    }

    View.OnClickListener rootClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onReplyListener.onReplyContentClick(comment, reply);
        }
    };

}
