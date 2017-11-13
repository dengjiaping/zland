package com.zhisland.android.blog.info.view.impl.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.view.impl.holder.FeaturedInfoHolder;
import com.zhisland.lib.component.adapter.BaseListAdapter;

/**
 * Created by Mr.Tui on 2016/7/25.
 */
public class FeaturedInfoAdapter extends BaseListAdapter<ZHInfo> {

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FeaturedInfoHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_info, null);
            holder = new FeaturedInfoHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FeaturedInfoHolder) convertView.getTag();
            if (holder == null) {
                holder = new FeaturedInfoHolder(convertView);
            }
        }
        holder.fill(getItem(position));

        return convertView;
    }

    @Override
    public void recycleView(View view) {
    }

}