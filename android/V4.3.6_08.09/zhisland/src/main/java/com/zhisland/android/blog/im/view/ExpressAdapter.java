package com.zhisland.android.blog.im.view;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.lib.util.DensityUtil;

public class ExpressAdapter extends BaseAdapter {

	public static final String TAG_DELETE = "TAG_DELETE";
	public static final int PAGE_INDICATOR_HEIGHT = DensityUtil.dip2px(15);
	public static int EXP_SIZE = ZhislandApplication.APP_RESOURCE
			.getDimensionPixelOffset(R.dimen.chat_express_size);
	public static int CELL_SIZE = EXP_SIZE * 5 / 3;
	public static int EXP_PANNEL_HEIGH = CELL_SIZE * 5;

	private final Context context;
	private final GridView gv;
	private final int page, columnNum, count;
	private final ExpressParser parser;
	private final int expCount;
	private final OnClickListener clickListener;

	public ExpressAdapter(Context context, GridView gv, int page,
			int columnNum, OnClickListener clickListener) {
		this.context = context;
		this.page = page;
		this.columnNum = columnNum;
		this.count = columnNum * 3 - 1;
		parser = ExpressParser.getInstance(context);
		expCount = parser.count();
		this.clickListener = clickListener;
		this.gv = gv;
	}

	@Override
	public int getCount() {
		if (page * count > expCount) {
			return expCount - (page - 1) * count + 1;
		}
		return count + 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ImageView iv = new ImageView(context);

		if (position == getCount() - 1) { // 删除键
			iv.setImageResource(R.drawable.sel_btn_delete);
			iv.setTag(TAG_DELETE);
		} else {
			int pos = this.getItem(position);
			iv.setImageBitmap(parser.res(pos));
			iv.setTag(parser.text(pos));
		}
		int paddingTop = EXP_SIZE / 4;
		int padding = EXP_SIZE / 3;
		iv.setPadding(0, paddingTop, 0, padding);
		iv.setOnClickListener(clickListener);

		AbsListView.LayoutParams params = new LayoutParams(EXP_SIZE, EXP_SIZE
				+ padding + paddingTop);
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setLayoutParams(params);

		return iv;
	}

	@Override
	public Integer getItem(int position) {
		return (page - 1) * count + position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}