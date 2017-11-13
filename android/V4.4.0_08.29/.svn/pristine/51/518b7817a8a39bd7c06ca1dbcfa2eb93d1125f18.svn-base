package com.zhisland.android.blog.feed.view.impl.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.view.impl.holder.AttachCreator;
import com.zhisland.android.blog.feed.view.impl.holder.AttachHolder;
import com.zhisland.android.blog.feed.view.impl.holder.FeedHolder;
import com.zhisland.android.blog.feed.view.impl.holder.FeedViewListener;
import com.zhisland.lib.component.adapter.BaseListAdapter;

/**
 * Created by arthur on 2016/8/31.
 */
public class FeedAdapter extends BaseListAdapter<Feed> {

    private static final int VIEW_COUNT = 4;

    private Context context;
    private FeedViewListener feedViewListener;
    private int showUser = 1;

    public FeedAdapter(Context context, FeedViewListener feedViewListener) {
        super(null);
        this.context = context;
        this.feedViewListener = feedViewListener;
    }

    @Override
    public int getItemViewType(int position) {
        Feed feed = getItem(position);
        return AttachCreator.Instance().getViewType(feed);
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            int type = getItemViewType(position);
            convertView = inflater.inflate(R.layout.item_feed, null);
            AttachHolder attachHolder = AttachCreator.Instance().getAttachHolder(context, type, false);
            FeedHolder feedHolder = new FeedHolder(context, convertView);
            feedHolder.addAttachView(attachHolder);
            convertView.setTag(feedHolder);
        }
        FeedHolder feedHolder = (FeedHolder) convertView.getTag();
        feedHolder.setShowUser(showUser);
        Feed feed = getItem(position);
        feedHolder.fill(feed, feedViewListener);

        return convertView;
    }

    @Override
    protected void recycleView(View view) {
        Object obj = view.getTag();
        if (obj instanceof FeedHolder) {
            FeedHolder holder = (FeedHolder) obj;
            holder.recycle();
        }

    }

    /**
     * 设置是否展示用户信息
     *
     * @param showHeader
     */
    public void setShowUser(int showHeader) {
        this.showUser = showHeader;
    }
}
