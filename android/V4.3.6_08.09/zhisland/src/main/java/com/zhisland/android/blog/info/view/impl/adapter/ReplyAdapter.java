package com.zhisland.android.blog.info.view.impl.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.info.bean.Comment;
import com.zhisland.android.blog.info.bean.Reply;
import com.zhisland.android.blog.info.presenter.CommentDetailPresenter;
import com.zhisland.android.blog.info.view.impl.holder.ReplyHolder;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

/**
 * 观点详情，回复列表Adapter
 * Created by Mr.Tui on 2016/7/22.
 */
public class ReplyAdapter extends BaseAdapter {

    private List<Reply> replys;
    private Activity context;
    private CommentDetailPresenter presenter;
    private Comment comment;

    public ReplyAdapter(Activity context) {
        this.context = context;
    }

    public void setPresenter(CommentDetailPresenter presenter) {
        this.presenter = presenter;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public int getCount() {
        if (replys == null) {
            return 0;
        }
        return replys.size();
    }

    @Override
    public Reply getItem(int position) {
        if (replys != null && position < replys.size()) {
            return replys.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReplyHolder replyHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_reply, null);
            replyHolder = new ReplyHolder(context, convertView, presenter);
            convertView.setTag(replyHolder);
        } else {
            replyHolder = (ReplyHolder) convertView.getTag();
        }
        replyHolder.fill(comment, getItem(position));
        convertView.setPadding(DensityUtil.dip2px(64), DensityUtil.dip2px(5), DensityUtil.dip2px(20), DensityUtil.dip2px(5));
        return convertView;
    }

    public void setData(List<Reply> replys) {
        this.replys = replys;
    }
}
