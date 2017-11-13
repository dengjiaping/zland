package com.zhisland.android.blog.freshtask.view.impl.holder;

import android.widget.EditText;
import android.widget.TextView;

import com.zhisland.android.blog.R;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Mr.Tui on 2016/5/28.
 */
public class AddResourceHolder {

    @InjectView(R.id.tvTitle)
    public TextView tvTitle;

    @InjectView(R.id.tvContentTitle)
    public TextView tvContentTitle;

    @InjectView(R.id.etContent)
    public EditText etContent;

    @InjectView(R.id.tvCount)
    public TextView tvCount;

    @InjectView(R.id.tvIndustry)
    public TextView tvIndustry;

    @InjectView(R.id.tvCategory)
    public TextView tvCategory;

    ClickListener clickListener;

    @OnClick(R.id.llIndustry)
    public void industryClick() {
        if (clickListener != null) {
            clickListener.industryClick();
        }
    }

    @OnClick(R.id.llCategory)
    public void categoryClick() {
        if (clickListener != null) {
            clickListener.categoryClick();
        }
    }

    public interface ClickListener {

        void industryClick();

        void categoryClick();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
