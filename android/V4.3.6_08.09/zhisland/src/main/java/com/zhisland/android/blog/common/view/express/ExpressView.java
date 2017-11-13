package com.zhisland.android.blog.common.view.express;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.view.PageControl;
import com.zhisland.lib.view.SwipeView;

public class ExpressView extends RelativeLayout {

	public static interface OnExpressListener {
		void onExpressClicked(String text);
	}

	private OnExpressListener expressListener;

	private SwipeView sv;
	private PageControl pc;
	private boolean isFilled = false;
	private boolean showDel = false;
	private ExpressParser parser;

	public ExpressView(Context context) {
		super(context);
		initViews(context);
	}

	public ExpressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context);
	}

	public ExpressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context);
	}

	/**
	 * will affect in next measure
	 */
	public void setShowDel(boolean showDel) {
		this.showDel = showDel;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		if (width > 0 && !isFilled) {
			fill(width);
			invalidate();
			isFilled = true;
		}
	}

	private void initViews(Context context) {
		parser = ExpressParser.getInstance(context);
		sv = new SwipeView(context);
		sv.setId(R.id.sv_express);
		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		this.addView(sv, param);

		pc = new PageControl(context);
		pc.setId(R.id.pc_express);
		pc.setGravity(Gravity.CENTER);
		param = new LayoutParams(LayoutParams.MATCH_PARENT,
				DensityUtil.dip2px(20));
		param.addRule(ALIGN_BOTTOM, R.id.sv_express);
		this.addView(pc, param);
	}

	private void fill(int totalWidth) {
		int columnNum = (totalWidth / ExpressAdapter.CELL_SIZE);
		int pageCount = columnNum * 3 - (showDel ? 1 : 0);
		int pageNum = (parser.count() + pageCount - 1) / pageCount;

		OnClickListener clickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = v.getTag().toString();
				if (expressListener != null) {
					expressListener.onExpressClicked(text);
				}
			}
		};

		for (int i = 0; i < pageNum; i++) {
			GridView gv = new GridView(getContext());
			ExpressAdapter adapter = new ExpressAdapter(getContext(), gv,
					i + 1, columnNum, clickListener);

			gv.setGravity(Gravity.CENTER);
			gv.setNumColumns(columnNum);
			gv.setAdapter(adapter);
			gv.setSelector(R.color.transparent);

			int padding = (DensityUtil.getWidth() - columnNum
					* ExpressAdapter.EXP_SIZE)
					/ (columnNum + 2);
			gv.setPadding(padding, padding, padding, padding);
			sv.addView(gv);
		}
		sv.setPageControl(pc);
	}

	public void setOnExpressListener(OnExpressListener listener) {
		this.expressListener = listener;
	}

}
