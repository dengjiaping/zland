package com.zhisland.android.blog.common.comment.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.comment.presenter.OnReplyListener;
import com.zhisland.android.blog.common.comment.bean.Comment;
import com.zhisland.android.blog.common.comment.bean.Reply;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.util.DensityUtil;

/**
 * 评论（观点）详情，回复列表Adapter
 * Created by Mr.Tui on 2016/7/22.
 */
public class ReplyAdapter extends BaseListAdapter<Reply> {

    private Activity context;
    private OnReplyListener onReplyListener;
    private Comment comment;

    public ReplyAdapter(Activity context) {
        this.context = context;
    }

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReplyHolder replyHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reply, null);
            replyHolder = new ReplyHolder(context, convertView, onReplyListener);
            convertView.setTag(replyHolder);
        } else {
            replyHolder = (ReplyHolder) convertView.getTag();
        }
        replyHolder.fill(comment, getItem(position));
        convertView.setPadding(DensityUtil.dip2px(64), DensityUtil.dip2px(5), DensityUtil.dip2px(20), DensityUtil.dip2px(5));
        return convertView;
    }

    @Override
    protected void recycleView(View view) {

    }
}
