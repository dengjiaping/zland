package com.zhisland.lib.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhisland.lib.R;
import com.zhisland.lib.util.DensityUtil;

public class PopupList extends PopupWindow {

	private static final int MAX_POP_HEIGHT = DensityUtil.getHeight() / 2;
	public static final int POP_WIDTH = DensityUtil.dip2px(220);
	private static final int ITEM_HEIGHT = DensityUtil.dip2px(40);
	private static final int CATE_HEIGHT = DensityUtil.dip2px(30);
	private static final int PADDING_TOP = DensityUtil.dip2px(14);
	private static final int PADDING = DensityUtil.dip2px(8);
	private static final int TEXT_SIZE = 20;
	private static final int CAT_TEXT_SIZE = 18;

	private final HashMap<PopItem, View> views = new HashMap<PopupList.PopItem, View>();

	public static interface PopListener extends OnDismissListener {

		void onPopItemClick(View v, PopItem item);
	}

	public static class PopItem {

		public static final int NORMAL = 1;
		public static final int CATEGORY = 5;

		public PopItem(int id, String text) {
			this.id = id;
			this.text = text;
			this.type = NORMAL;
		}

		public PopItem(int id, String text, int type) {
			this.id = id;
			this.text = text;
			this.type = type;
		}

		public PopItem(int id, String text, Object tag) {
			this.id = id;
			this.text = text;
			this.type = NORMAL;
			this.tag = tag;
		}

		public int id;
		public String text;
		public int type;
		public Object tag;
	}

	private Context context;
	private PopListener listener;
	private LinearLayout container;
	private Holder curHolder = null;
	private ArrayList<PopItem> items;

	public PopupList(Context context) {
		super(context);
	}

	public PopupList(final Context context, Handler handler,
			final PopListener listener) {
		super(context);

		this.setOnDismissListener(listener);
		this.context = context;
		this.listener = listener;

		setContentView();

	}

	static final class Holder {
		LinearLayout container;
		TextView tv;
	}

	private View getLayout(PopItem item) {
		switch (item.type) {
		case PopItem.NORMAL:
			return buildNormal(item);
		case PopItem.CATEGORY:
			return buildCategory(item);
		}

		return null;
	}

	private View buildNormal(final PopItem item) {

		LinearLayout ll = new LinearLayout(context);
		android.view.ViewGroup.LayoutParams param = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, ITEM_HEIGHT);
		ll.setLayoutParams(param);
		ll.setGravity(Gravity.CENTER_VERTICAL);
		ll.setBackgroundResource(R.drawable.sel_bg_popitem);
		ll.setPadding(0, 0, DensityUtil.dip2px(10), 0);

		TextView tv = new TextView(context);
		tv.setTextSize(TEXT_SIZE);
		tv.setTextColor(Color.WHITE);
		tv.setPadding(DensityUtil.dip2px(20), 0, 0, 0);
		tv.setBackgroundDrawable(null);
		ll.addView(tv);

		final Holder holder = new Holder();
		holder.container = ll;
		holder.tv = tv;

		holder.tv.setText(item.text);
		holder.container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupList.this.dismiss();

				Holder holder = (Holder) v.getTag();
				if (curHolder != null) {
					if (curHolder != holder) {
						curHolder.tv.setTextColor(Color.WHITE);
					} else {
						return;
					}
				}

				holder.tv.setTextColor(context.getResources().getColor(
						R.color.drop_text_focus));
				listener.onPopItemClick(holder.container, item);
				curHolder = holder;
			}
		});

		ll.setTag(holder);
		views.put(item, ll);
		return ll;

	}

	private View buildCategory(final PopItem item) {
		int lineHeight = DensityUtil.dip2px(2);
		LinearLayout ll = new LinearLayout(context);
		android.view.ViewGroup.LayoutParams param = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, CATE_HEIGHT);
		ll.setLayoutParams(param);
		ll.setGravity(Gravity.CENTER);
		ll.setPadding(0, 0, DensityUtil.dip2px(10), 0);

		View left = new View(context);
		param = new LayoutParams(DensityUtil.dip2px(20), lineHeight);
		left.setLayoutParams(param);
		left.setBackgroundResource(R.drawable.line_category);
		ll.addView(left);

		TextView tv = new TextView(context);
		tv.setTextSize(CAT_TEXT_SIZE);
		tv.setTextColor(context.getResources().getColor(R.color.title_drop));
		tv.setShadowLayer((float) 0.6, 0, (float) 0.6, Color.GRAY);
		tv.setText(item.text);
		tv.setPadding(DensityUtil.dip2px(10), 0, DensityUtil.dip2px(10),
				DensityUtil.dip2px(2));
		ll.addView(tv);

		View right = new View(context);
		param = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, lineHeight);
		right.setLayoutParams(param);
		right.setBackgroundResource(R.drawable.line_category);
		ll.addView(right);

		final Holder holder = new Holder();
		holder.container = ll;
		holder.tv = tv;

		holder.tv.setText(item.text);

		return ll;

	}

	private void configSize(int count) {
		this.setWidth(POP_WIDTH);
		int height = calcTotalHeight(count);
		this.setHeight(height);
	}

	private int calcTotalHeight(int itemCount) {
		int height = itemCount * ITEM_HEIGHT + PADDING_TOP + PADDING;
		return height > MAX_POP_HEIGHT ? MAX_POP_HEIGHT : height;
	}

	private void setContentView() {

		container = new LinearLayout(context);
		container.setOrientation(LinearLayout.VERTICAL);
		container.setBackgroundDrawable(null);

		ScrollView sv = new ScrollView(context);
		sv.setVerticalScrollBarEnabled(false);
		sv.setBackgroundDrawable(null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		sv.addView(container, layoutParams);

		LinearLayout bg = new LinearLayout(context);
		bg.setBackgroundResource(R.drawable.bg_poplist);
		bg.setPadding(PADDING, PADDING_TOP, PADDING, PADDING);
		layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		bg.addView(sv, layoutParams);

		this.setContentView(bg);
		this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	public void updateItems(ArrayList<PopItem> items) {

		container.removeAllViews();
		views.clear();
		if (items == null) {
			configSize(0);
			return;
		}
		this.items = items;
		configSize(items.size());
		for (int i = 0; i < items.size(); i++) {
			container.addView(getLayout(items.get(i)));
		}
	}

	public void selectItem(int index) {
		if (index < 0 || items == null || items.size() <= index) {
			return;
		}
		View v = views.get(items.get(index));
		v.performClick();
	}

	public void selectItemById(int id) {
		for (PopItem item : items) {
			if (item.id == id) {
				View v = views.get(item);
				v.performClick();
				break;
			}
		}
	}

	public void selectItemByText(String text) {
		for (PopItem item : items) {
			if (item.text.equals(text)) {
				View v = views.get(item);
				v.performClick();
				break;
			}
		}
	}
}
