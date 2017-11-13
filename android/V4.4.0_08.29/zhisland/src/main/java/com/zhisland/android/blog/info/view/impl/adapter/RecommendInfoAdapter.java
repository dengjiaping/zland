package com.zhisland.android.blog.info.view.impl.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.android.blog.info.view.impl.holder.RecommendInfoHolder;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.util.StringUtil;

/**
 * Created by Mr.Tui on 2016/7/25.
 */
public class RecommendInfoAdapter extends BaseListAdapter<ZHInfo> {

    private int[] itemLayoutId = new int[]{R.layout.item_info_one, R.layout.item_info_two, R.layout.item_info_three};

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        ZHInfo info = getItem(position);
        if (!StringUtil.isNullOrEmpty(info.coverLarge)) {
            return 0;
        } else if (!StringUtil.isNullOrEmpty(info.coverSmall)) {
            return 1;
        } else {
            return 2;
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecommendInfoHolder holder;
        int curType = getItemViewType(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    itemLayoutId[curType], null);
            holder = new RecommendInfoHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (RecommendInfoHolder) convertView.getTag();
            if (holder == null) {
                holder = new RecommendInfoHolder(convertView);
            }
        }
        holder.fill(getItem(position), curType);
        return convertView;
    }

    @Override
    public void recycleView(View view) {
    }

}
