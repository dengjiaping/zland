package com.zhisland.android.blog.feed.view.impl.holder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.android.blog.im.view.ChatViewUtil;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.text.ZHLink;
import com.zhisland.lib.util.text.ZHLinkBuilder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 负责具体一条资讯的展现
 * Created by arthur on 2016/9/1.
 */
public class FeedHolder {

    //上部分
    @InjectView(R.id.rlFeedUser)
    RelativeLayout rlFeedUser;
    @InjectView(R.id.ivFeedAvatar)
    ImageView ivFeedAvatar;
    @InjectView(R.id.tvFeedTime)
    TextView tvFeedTime;
    @InjectView(R.id.tvFeedRecommendTag)
    TextView tvFeedRecommendTag;

    @InjectView(R.id.ivFeedRank)
    ImageView ivFeedRank;
    @InjectView(R.id.tvFeedName)
    TextView tvFeedName;
    @InjectView(R.id.tvFeedPosition)
    TextView tvFeedPosition;
    @InjectView(R.id.tvFeedContent)
    TextView tvFeedContent;
    @InjectView(R.id.tvFeedContentMore)
    TextView tvFeedContentMore;

    //附件
    @InjectView(R.id.llFeed)
    LinearLayout llFeed;

    //底部
    @InjectView(R.id.tvFeedTransmitDesc)
    TextView tvFeedTransmitDesc;
    @InjectView(R.id.tvFeedPraise)
    TextView tvFeedPraise;
    @InjectView(R.id.tvFeedComment)
    TextView tvFeedComment;
    @InjectView(R.id.tvFeedTransmit)
    TextView tvFeedTransmit;
    @InjectView(R.id.llFeedSns)
    RelativeLayout llFeedSns;
    @InjectView(R.id.vFeedLine)
    View vFeedLine;

    @InjectView(R.id.vFeedDivider)
    View vFeedDivider;


    private Feed curFeed;
    private Context context;
    private FeedViewListener feedViewListener;
    private AttachHolder attachHolder;

    private boolean isDetail = false;
    private int showUser = 1;

    View.OnClickListener feedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (feedViewListener != null) {
                feedViewListener.onFeedCicked(curFeed);
            }
        }
    };


    public FeedHolder(Context context, View convertView) {
        this.context = context;
        ButterKnife.inject(this, convertView);
        convertView.setOnClickListener(feedClickListener);
        tvFeedContent.setOnClickListener(feedClickListener);

        tvFeedContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvFeedTransmitDesc.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //增加附件view
    public void addAttachView(AttachHolder attachHolder) {

        if (attachHolder == null || attachHolder.getView() == null)
            return;

        this.attachHolder = attachHolder;
        View view = attachHolder.getView();
        llFeed.addView(view);
    }


    //region 点击事件

    @OnClick({R.id.ivFeedAvatar, R.id.tvFeedName, R.id.tvFeedPosition, R.id.ivFeedRank})
    public void onUserClicked() {
        feedViewListener.onUserClicked(this, curFeed.user);
    }

    @OnClick(R.id.tvFeedPraise)
    public void onPraiseClicked() {
        feedViewListener.onPraiseClicked(this, curFeed);
    }

    @OnClick(R.id.tvFeedComment)
    public void onCommentClicked() {
        feedViewListener.onCommentClicked(this, curFeed);
    }

    @OnClick(R.id.tvFeedTransmit)
    public void onTransmitClicked() {
        feedViewListener.onTransemitClicked(this, curFeed);
    }


    //endregion


    //region 填充回收逻辑


    //填充附件view
    public void fill(Feed feed, FeedViewListener feedViewListener) {

        this.curFeed = feed;
        this.feedViewListener = feedViewListener;

        if (feed == null) {
            return;
        }

        fillUser();

        fillText();

        fillAttach();

        fillBottom();


    }

    //回收资源
    public void recycle() {
        this.curFeed = null;
        this.feedViewListener = null;
        if (attachHolder != null) {
            attachHolder.recycleAttach();
        }
    }

    //设置底部Divider是否显示
    public void setBottomDividerVisibility(int visibility) {
        vFeedDivider.setVisibility(visibility);
    }

    //填充上部分
    private void fillUser() {

        if (showUser <= 0) {
            rlFeedUser.setVisibility(View.GONE);
            return;
        }

        rlFeedUser.setVisibility(View.VISIBLE);
        User user = curFeed.user;

        if (!isDetail) {
            tvFeedTime.setVisibility(View.GONE);
            if (!StringUtil.isNullOrEmpty(curFeed.recommendTag)) {
                tvFeedRecommendTag.setVisibility(View.VISIBLE);
                tvFeedRecommendTag.setText(curFeed.recommendTag);
            } else {
                tvFeedRecommendTag.setVisibility(View.GONE);
            }
        } else {
            tvFeedRecommendTag.setVisibility(View.GONE);
            tvFeedTime.setVisibility(View.VISIBLE);
            tvFeedTime.setText(StringUtil.convertFrom(curFeed.time * 1000));
        }
        if (user != null) {
            ImageWorkFactory.getCircleFetcher().loadImage(user.userAvatar, ivFeedAvatar, R.drawable.avatar_default);
            tvFeedName.setText(user.name);
            tvFeedPosition.setText(user.combineCompanyAndPosition());
            ivFeedRank.setImageResource(user.getVipIconId());
        }
    }

    private void fillText() {

        if (curFeed.type == FeedType.IMG) {
            tvFeedContent.setTextColor(ZhislandApplication.APP_RESOURCE.getColor(R.color.color_f1));
        } else {
            tvFeedContent.setTextColor(ZhislandApplication.APP_RESOURCE.getColor(R.color.color_f2));
        }

        if (!isDetail) {
            tvFeedContent.setMaxLines(4);
            if (tryMeasureHeight(4)) {
                tvFeedContentMore.setVisibility(View.VISIBLE);
            } else {
                tvFeedContentMore.setVisibility(View.GONE);
            }
        } else {
            tvFeedContent.setMaxLines(10000);
            tvFeedContentMore.setVisibility(View.GONE);
        }

        if (!StringUtil.isNullOrEmpty(curFeed.title)) {
            CharSequence csContent = ChatViewUtil.instance().formatText(context, curFeed.title, new ZHLink.OnLinkClickListener() {
                @Override
                public void onLinkClicked(Context context, String regex, String text) {
                    if (regex.equals(ZHLinkBuilder.REGEX_URL)) {
                        AUriMgr.instance().viewRes(context, text);
                    } else if (regex.equals(ZHLinkBuilder.REGEX_NUMBER)) {
                        IntentUtil.dialTo(context, text);
                    }
                }
            }, tvFeedContent.getLineHeight());
            tvFeedContent.setText(csContent);
            tvFeedContent.setVisibility(View.VISIBLE);
        } else {
            tvFeedContent.setVisibility(View.GONE);
        }

    }

    private boolean tryMeasureHeight(int maxLines) {
        if (curFeed.title == null) {
            return false;
        }
        String[] lines = curFeed.title.split("\n");
        MLog.e("asd", "行数 " + lines.length);
        if (lines.length > maxLines) {
            return true;
        }

        int mWidth = tvFeedContent.getMeasuredWidth();
        int textViewWidth = mWidth < 1 ? DensityUtil.getWidth() : mWidth;
        int totoal = 0;
        for (String line : lines) {
            float length = tvFeedContent.getPaint().measureText(line);
            totoal += (length / textViewWidth + 1);
        }

        MLog.e("asd", "total line is  " + totoal);
        return totoal > maxLines;
    }

    //填充底部
    private void fillBottom() {

        //填充赞
        Drawable drawable;
        if (curFeed.like.clickState > 0) {
            drawable = ZhislandApplication.APP_RESOURCE.getDrawable(R.drawable.feed_btn_praise_selected);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        } else {
            drawable = ZhislandApplication.APP_RESOURCE.getDrawable(R.drawable.feed_btn_praise);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        tvFeedPraise.setCompoundDrawables(drawable, null, null, null);
        String text = curFeed.like.quantity > 0 ? curFeed.like.quantity + "" : curFeed.like.name;
        tvFeedPraise.setText(text);

        //填充评论
        text = curFeed.comment.quantity > 0 ? curFeed.comment.quantity + "" : curFeed.comment.name;
        tvFeedComment.setText(text);

        //填充转播按钮
        if (curFeed.forward != null) {

            if (curFeed.forward.clickState > 0) {
                drawable = ZhislandApplication.APP_RESOURCE.getDrawable(R.drawable.feed_btn_transmit_selected);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                text = "已转";
                tvFeedTransmit.setTextColor(ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc));
            } else {
                drawable = ZhislandApplication.APP_RESOURCE.getDrawable(R.drawable.feed_btn_transmit);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                text = "转播";
                tvFeedTransmit.setTextColor(ZhislandApplication.APP_RESOURCE.getColor(R.color.color_f3));
            }

            tvFeedTransmit.setCompoundDrawables(drawable, null, null, null);
            tvFeedTransmit.setText(text);
        }

        fillTransmitDesc();
    }

    //填充附件
    private void fillAttach() {

        if (attachHolder != null) {
            attachHolder.fillAttach(curFeed, feedViewListener);
        }
    }

    //填充转播的文案
    private void fillTransmitDesc() {
        if (curFeed.forwardUser != null) {

            tvFeedTransmitDesc.setVisibility(View.VISIBLE);

            String text;
            if (curFeed.forward.quantity > 1) {
                text = String.format("%s 等%d人转播", curFeed.forwardUser.name, curFeed.forward.quantity);
            } else {
                text = String.format("%s 转播", curFeed.forwardUser.name);
            }
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    feedViewListener.onUserClicked(FeedHolder.this, curFeed.forwardUser);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc));
                    ds.setUnderlineText(false);
                }
            }, 0, curFeed.forwardUser.name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvFeedTransmitDesc.setText(ssb);
        } else {
            tvFeedTransmitDesc.setVisibility(View.GONE);
        }
    }

    public void setDetail(boolean detail) {
        isDetail = detail;
    }

    /**
     * 设置是否展示用户信息
     *
     * @param showUser
     */
    public void setShowUser(int showUser) {
        this.showUser = showUser;
    }

    //endregion
}
