package com.zhisland.android.blog.feed.view.impl.holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.feed.bean.AttachPraise;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedType;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 负责评论模版的展示
 * Created by zhuchuntao on 16/9/2.
 */
public class CommentHolder implements AttachHolder {

    private Context context;

    @InjectView(R.id.comment_content)
    TextView contentView;

    @InjectView(R.id.comment_sender_icon)
    ImageView iconView;

    @InjectView(R.id.comment_sender_name)
    TextView nameView;

    @InjectView(R.id.tvComAndPos)
    TextView tvComAndPos;

    @InjectView(R.id.ivUserType)
    ImageView ivUserType;

    private FeedViewListener listener;
    private Feed feed;
    private AttachPraise praise;

    private View layout;

    public CommentHolder(Context context) {
        this.context = context;
    }

    @Override
    public View getView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.layout_comment, null);
        ButterKnife.inject(this, layout);
        return layout;
    }

    @Override
    public void fillAttach(Feed feed, FeedViewListener feedViewListener) {
        this.feed = feed;
        this.listener = feedViewListener;
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
        lp.setMargins(DensityUtil.dip2px(16), 0, DensityUtil.dip2px(16), 0);
        layout.setLayoutParams(lp);
        if (null != feed && feed.type == FeedType.COMMENT) {
            praise = (AttachPraise) feed.attach;
            if (null != praise) {
                contentView.setText(praise.title);
                if (null != praise.toUser) {
                    ivUserType.setImageResource(praise.toUser.getVipIconId());
                    ImageWorkFactory.getCircleFetcher().loadImage(praise.toUser.userAvatar, iconView, R.drawable.avatar_default);
                    nameView.setText(praise.toUser.name);
                    tvComAndPos.setText(praise.toUser.combineCompanyAndPosition());
                }
            }
        }
    }

    @Override
    public void recycleAttach() {
        this.feed = null;
        this.listener = null;
        this.praise = null;
    }

    @OnClick(R.id.comment_content)
    void onContentClick() {
        if (listener != null) {
            listener.onAttachClicked(feed, null);
        }
    }

    @OnClick({R.id.tvComAndPos, R.id.comment_sender_name, R.id.comment_sender_icon})
    void onPraiseUserClick() {
        if (listener != null) {
            listener.onUserClicked(null, praise.toUser);
        }
    }
}
