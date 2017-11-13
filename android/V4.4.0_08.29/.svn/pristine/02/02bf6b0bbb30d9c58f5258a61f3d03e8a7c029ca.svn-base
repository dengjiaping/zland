package com.zhisland.android.blog.info.view.impl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.view.impl.holder.ReportTypeHolder;

import java.util.ArrayList;

/**
 * Created by Mr.Tui on 2016/7/26.
 */
public class ReportTypeAdapter extends BaseAdapter {

    private ArrayList<ReportType> data;

    private CallBack callBack;

    public ReportTypeAdapter(CallBack callBack) {
        this.callBack = callBack;
    }

    public void setData(ArrayList<ReportType> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public ReportType getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_report_type, null);
            ReportTypeHolder holder = new ReportTypeHolder(convertView, callBack);
            convertView.setTag(holder);
        }
        ReportTypeHolder holder = (ReportTypeHolder) convertView.getTag();
        boolean isLast = (getCount() - 1) == position;
        holder.fill(getItem(position), isLast);
        return convertView;
    }

    public interface CallBack {

        void onItemClick(ReportType reportType);

    }

}