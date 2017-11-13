package com.zhisland.android.blog.invitation.view.impl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.contacts.dto.InviteUser;
import com.zhisland.android.blog.invitation.view.impl.holder.InviteUserHolder;
import com.zhisland.lib.component.adapter.BaseListAdapter;

/**
 * Created by Mr.Tui on 2016/8/9.
 */
public class SearchAdapter extends BaseListAdapter<InviteUser> {

    InviteUserHolder.CallBack callBack;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InviteUserHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_search, null);
            holder = new InviteUserHolder(convertView);
            holder.setCallBack(callBack);
            convertView.setTag(holder);
        } else {
            holder = (InviteUserHolder) convertView.getTag();
        }
        holder.fill(getItem(position));
        return convertView;
    }

    @Override
    protected void recycleView(View view) {
        InviteUserHolder holder = (InviteUserHolder) view.getTag();
        holder.recycle();
    }

    public void setCallBack(InviteUserHolder.CallBack callBack) {
        this.callBack = callBack;
    }
}
