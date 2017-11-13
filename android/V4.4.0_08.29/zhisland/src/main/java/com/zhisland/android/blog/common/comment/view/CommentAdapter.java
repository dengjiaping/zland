package com.zhisland.android.blog.common.comment.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.presenter.OnCommentListener;
import com.zhisland.lib.component.adapter.BaseListAdapter;

import java.util.ArrayList;

/**
 * 评论（观点）listView 适配器
 * Created by Mr.Tui on 2016/7/19.
 */
public class CommentAdapter extends BaseListAdapter<Comment> {

    private Activity context;

    private OnCommentListener onCommentListener;

    private View emptyView;

    public CommentAdapter(Activity context) {
        this.context = context;
    }

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
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
            if ((data == null || data.size() == 0) && emptyView != null) {
                return emptyView;
            } else {
                View v = new View(context);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 1);
                v.setLayoutParams(lp);
                return v;
            }
        } else {
            CommentHolder commentHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
                commentHolder = new CommentHolder(context, convertView, onCommentListener);
                convertView.setTag(commentHolder);
            } else {
                commentHolder = (CommentHolder) convertView.getTag();
            }
            commentHolder.fill(getItem(position), position == getCount() - 1);
            return convertView;
        }
    }

    public void setEmptyView(View v) {
        this.emptyView = v;
        notifyDataSetChanged();
    }
}
