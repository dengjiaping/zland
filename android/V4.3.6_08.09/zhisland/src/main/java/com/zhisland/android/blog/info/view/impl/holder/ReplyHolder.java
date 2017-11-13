package com.zhisland.android.blog.info.view.impl.holder;

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
import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.bean.Reply;
import com.zhisland.android.blog.info.presenter.IReply;
import com.zhisland.android.blog.profile.controller.detail.ActProfileDetail;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 观点详情，回复列表Holder
 * Created by Mr.Tui on 2016/7/22.
 */
public class ReplyHolder {

    private Activity context;
    private IReply presenter;
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

    public ReplyHolder(Activity context, View view, IReply presenter) {
        this.root = view;
        root.setOnClickListener(rootClick);
        this.context = context;
        this.presenter = presenter;
        ButterKnife.inject(this, view);
    }

    public void fill(Comment comment, Reply reply) {
        this.comment = comment;
        this.reply = reply;
        tvTime.setText(reply.pulblishTime);

        if (reply.publisher != null) {
            ImageWorkFactory.getCircleFetcher().loadImage(reply.publisher.userAvatar, ivAvatar, R.drawable.avatar_default);
            String toName = StringUtil.isNullOrEmpty(reply.reviewedName) ? "" : (" 回复 " + reply.reviewedName);
            SpannableString ss = new SpannableString(reply.publisher.name + toName);
            ss.setSpan(new NameClickSpan(reply.publisher.uid), 0, reply.publisher.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (!StringUtil.isNullOrEmpty(toName)) {
                ss.setSpan(new NameClickSpan(reply.reviewedUid), reply.publisher.name.length() + 4, reply.publisher.name.length() + 4 + reply.reviewedName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        if (reply.publisher != null) {
            ActProfileDetail.invoke(context, reply.publisher.uid);
        }
    }

    View.OnClickListener rootClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            presenter.onReplyContentClick(comment, reply);
        }
    };

}
