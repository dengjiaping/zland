package com.zhisland.android.blog.find.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zhisland.android.blog.find.dto.FilterItem;
import com.zhisland.lib.util.DensityUtil;

import java.util.ArrayList;

/**
 * 筛选View
 * Created by Mr.Tui on 2016/5/19.
 */
public class FilterView extends LinearLayout {

    private int margin5dp = DensityUtil.dip2px(5);
    private int margin15dp = DensityUtil.dip2px(15);

    public FilterView(Context context) {
        super(context);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
    }

    /**
     * 生成一行View
     */
    private LinearLayout makeRowViews(int index) {
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(HORIZONTAL);
        for (int i = index; i < index + 3; i++) {
            if (i < filterAdapter.getCount()) {
                final int position = i;
                View itemView = filterAdapter.getView(row, i, filterAdapter.getItem(i));
                filterAdapter.addChildView(itemView);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filterAdapter.onItemClick(view, filterAdapter.getItem(position));
                    }
                });
                row.addView(itemView, makeItemLayoutParams());
            } else {
                View emptyView = new View(getContext());
                row.addView(emptyView, makeItemLayoutParams());
            }
        }
        return row;
    }

    /**
     * 生成每一项View的LayoutParams
     * */
    private LayoutParams makeItemLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(margin5dp, 0, margin5dp, 0);
        lp.weight = 1;
        return lp;
    }

    /**
     * 生成行View的LayoutParams
     * */
    private LayoutParams makeRowLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, margin15dp);
        return lp;
    }

    private FilterAdapter filterAdapter;

    /**
     * 设置筛选项item适配器
     * */
    public synchronized void setFilterAdapter(FilterAdapter filterAdapter) {
        this.filterAdapter = filterAdapter;
        removeAllViews();
        if (filterAdapter == null) {
            return;
        }
        for (int i = 0; i < filterAdapter.getCount(); i = i + 3) {
            LinearLayout row = makeRowViews(i);
            addView(row, makeRowLayoutParams());
        }
    }

    /**
     * 筛选项item适配器
     * */
    public static abstract class FilterAdapter<T> {

        public int getCount() {
            return list == null ? 0 : list.size();
        }

        private ArrayList<T> list;

        private ArrayList<View> vList;

        public T getItem(int index) {
            if (list != null && index < list.size()) {
                return list.get(index);
            }
            return null;
        }

        public FilterAdapter(ArrayList<T> list) {
            this.list = list;
            vList = new ArrayList<View>();
        }

        public abstract View getView(LinearLayout row, int position, T data);

        public abstract void onItemClick(View v, T data);

        public abstract void refreshView(View v, T data);

        public void toRefreshItem() {
            for (int i = 0; i < vList.size(); i++) {
                refreshView(vList.get(i), list.get(i));
            }
        }

        private void addChildView(View v) {
            vList.add(v);
        }
    }

}