package com.zhisland.android.blog.info.view.impl.holder;

import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.info.bean.ReportType;
import com.zhisland.android.blog.info.view.impl.adapter.ReportTypeAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Mr.Tui on 2016/7/26.
 */
public class ReportTypeHolder {

    @InjectView(R.id.tvTypeName)
    TextView tvTypeName;

    @InjectView(R.id.vline)
    View vline;

    ReportType item;

    ReportTypeAdapter.CallBack callBack;

    public ReportTypeHolder(View convertView, ReportTypeAdapter.CallBack callBack) {
        convertView.setOnClickListener(clickListener);
        this.callBack = callBack;
        ButterKnife.inject(this, convertView);
    }

    public void fill(ReportType item, boolean isLast) {
        this.item = item;
        tvTypeName.setText(item.name);
        if (isLast) {
            vline.setVisibility(View.VISIBLE);
        } else {
            vline.setVisibility(View.INVISIBLE);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(callBack != null){
                callBack.onItemClick(item);
            }
        }
    };


}