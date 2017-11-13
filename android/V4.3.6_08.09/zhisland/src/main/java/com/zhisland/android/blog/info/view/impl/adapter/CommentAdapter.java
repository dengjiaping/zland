package com.zhisland.android.blog.info.view.impl.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.presenter.InfoSocialPresenter;
import com.zhisland.android.blog.info.view.impl.holder.CommentHolder;
import com.zhisland.android.blog.info.view.impl.holder.SendCommentView;
import com.zhisland.lib.component.adapter.BaseListAdapter;

import java.util.ArrayList;

/**
 * Created by Mr.Tui on 2016/7/19.
 */
public class CommentAdapter extends BaseListAdapter<Comment> {

    private Activity context;

    private InfoSocialPresenter presenter;

    private boolean showEmptyView = false;

    public CommentAdapter(Activity context) {
        this.context = context;
    }

    public void setPresenter(InfoSocialPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public int getCount() {
        if (data == null) {
            data = new ArrayList<>();
        }
        return data.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    protected void recycleView(View view) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @Override
    public Comment getItem(int position) {
        if (data != null && position > 0 && position <= data.size()) {
            return data.get(position - 1);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        if (viewType == 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_info_comment_foot, null);
            }
            View empty = convertView.findViewById(R.id.tvEmpty);
            if ((data == null || data.size() == 0) && showEmptyView) {
                empty.setVisibility(View.VISIBLE);
                empty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.wantSendComment(SendCommentView.ToType.info, null, null, null);
                    }
                });
            } else {
                empty.setVisibility(View.GONE);
            }
            return convertView;
        } else {
            CommentHolder commentHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_info_comment, null);
                commentHolder = new CommentHolder(context, convertView, presenter);
                convertView.setTag(commentHolder);
            } else {
                commentHolder = (CommentHolder) convertView.getTag();
            }
            commentHolder.fill(getItem(position), position == getCount() - 1);
            return convertView;
        }
    }

    public void setShowEmptyView(boolean showEmptyView) {
        this.showEmptyView = showEmptyView;
    }
}
