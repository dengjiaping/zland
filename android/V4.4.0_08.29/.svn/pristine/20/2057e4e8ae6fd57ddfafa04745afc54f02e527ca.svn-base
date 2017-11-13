package com.zhisland.android.blog.im.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.view.SwipeView;

public class BigEmotionAdapter extends BaseAdapter {

	public static final int EMOTION_WIDTH = DensityUtil.dip2px(74);
	public static final int EMOTION_HEIGH = DensityUtil.dip2px(67);
	public static final int LINE_TOTAL_NUM = 2;

	private final Context context;
	private final GridView gv;
	private final int page, columnNum, count;
	private final int expCount;
	private final OnClickListener clickListener;
	private final BigEmotionParser parser;

	private int padding;

	public BigEmotionAdapter(Context context, GridView gv, int page,
			int columnNum, OnClickListener clickListener) {
		this.context = context;
		this.page = page;
		this.columnNum = columnNum;
		this.count = columnNum * LINE_TOTAL_NUM;
		parser = BigEmotionParser.getInstance(context);
		expCount = parser.count();
		this.clickListener = clickListener;
		this.gv = gv;

	}

	public void init(SwipeView sv) {
		gv.setGravity(Gravity.CENTER);
		gv.setNumColumns(columnNum);
		gv.setAdapter(this);

		int gvPadding = (DensityUtil.getWidth() - columnNum * EMOTION_WIDTH)
				/ (columnNum + 2);
		gv.setPadding(gvPadding, gvPadding, gvPadding, gvPadding);
		sv.addView(gv);

		padding = (ExpressAdapter.EXP_PANNEL_HEIGH - gvPadding * 2 - LINE_TOTAL_NUM
				* EMOTION_HEIGH) / 4;
	}

	@Override
	public int getCount() {
		if (page * count > expCount) {
			return expCount - (page - 1) * count;
		}
		return count;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		int pos = this.getItem(position);

		ImageView iv = new ImageView(context);

		iv.setPadding(0, padding, 0, padding);
		iv.setImageBitmap(parser.res(pos));
		iv.setTag(parser.text(pos));
		iv.setOnClickListener(clickListener);

		AbsListView.LayoutParams params = new LayoutParams(EMOTION_WIDTH,
				EMOTION_HEIGH);
		iv.setScaleType(ScaleType.CENTER_CROP);
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