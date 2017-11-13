package com.zhisland.android.blog.profile.controller.detail;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.lib.util.DensityUtil;

/**
 * 举报原因adapter
 */
public class ReportReasonAdapter extends BaseAdapter {

	private Context context;
	private List<Country> datas;

	public ReportReasonAdapter(Context context, List<Country> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Country getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView = new TextView(context);
		textView.setTextColor(context.getResources().getColor(R.color.color_dc));
		DensityUtil.setTextSize(textView, R.dimen.txt_16);
		textView.setGravity(Gravity.CENTER);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				DensityUtil.dip2px(50));
		textView.setLayoutParams(params);
		textView.setText(getItem(position).name);

		return textView;
	}
}
