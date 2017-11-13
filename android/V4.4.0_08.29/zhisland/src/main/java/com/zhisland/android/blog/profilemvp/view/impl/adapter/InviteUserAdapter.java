package com.zhisland.android.blog.profilemvp.view.impl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.profilemvp.view.impl.holder.InviteUserHolder;
import com.zhisland.lib.component.adapter.BaseListAdapter;

/**
 * Created by Mr.Tui on 2016/9/13.
 */
public class InviteUserAdapter extends BaseListAdapter<InviteUser> {

    Context context;

    InviteUserHolder.OnRightClickListener onRightClickListener;

    public InviteUserAdapter(Context context, InviteUserHolder.OnRightClickListener onRightClickListener) {
        this.context = context;
        this.onRightClickListener = onRightClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InviteUserHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_my_attention, null);
            holder = new InviteUserHolder(convertView, context, onRightClickListener);
            convertView.setTag(holder);
        } else {
            holder = (InviteUserHolder) convertView.getTag();
        }
        holder.fill(getItem(position), position);
        return convertView;
    }

    @Override
    protected void recycleView(View view) {

    }
}
