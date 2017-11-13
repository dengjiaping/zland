package com.zhisland.android.blog.find.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.find.dto.SearchHistory;
import com.zhisland.lib.util.DensityUtil;

import java.util.ArrayList;

/**
 * 搜索历史View
 * Created by Mr.Tui on 2016/5/19.
 */
public class SearchHistoryView extends LinearLayout {

    private ArrayList<SearchHistory> historyList;

    int paddingLeft = DensityUtil.dip2px(10);
    int paddingTop = DensityUtil.dip2px(15);

    public SearchHistoryView(Context context) {
        super(context);
        init(context);
    }

    public SearchHistoryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchHistoryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
    }

    /**
     * 设置历史数据，并生成view
     * */
    public synchronized void setHistory(ArrayList<SearchHistory> history) {
        this.historyList = history;
        removeAllViews();
        if (history == null) {
            return;
        }
        for (int i = 0; i < history.size(); i++) {
            addHistoryItem(i, history.get(i));
        }
    }

    /**
     * 添加一条历史记录View
     * */
    private void addHistoryItem(int index, SearchHistory searchHistory) {
        TextView textView = new TextView(getContext());
        DensityUtil.setTextSize(textView, R.dimen.txt_16);
        textView.setTextColor(getResources().getColor(R.color.color_f1));
        textView.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
        textView.setText(searchHistory.searchWord);
        textView.setTag(new Integer(index));
        textView.setOnClickListener(onItemClickListener);
        addView(textView);
        addLine();
    }

    /**
     * 添加一条分隔线
     * */
    private void addLine() {
        View vline = new View(getContext());
        vline.setBackgroundColor(getResources().getColor(R.color.color_div));
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(0.7f));
        addView(vline, lp);
    }

    OnClickListener onItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Object tag = view.getTag();
            if (tag != null && tag instanceof Integer && onHistoryClickListener != null) {
                int index = (Integer) tag;
                SearchHistory searchHistory = null;
                if (historyList != null && historyList.size() > index) {
                    searchHistory = historyList.get(index);
                }
                onHistoryClickListener.onItemClick(index, searchHistory);
            }
        }
    };

    public void setOnHistoryClickListener(OnHistoryClickListener onHistoryClickListener) {
        this.onHistoryClickListener = onHistoryClickListener;
    }

    private OnHistoryClickListener onHistoryClickListener;

    public interface OnHistoryClickListener {

        public void onItemClick(int index, SearchHistory searchHistory);
    }

}
