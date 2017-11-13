package com.zhisland.android.blog.im.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.lib.util.DensityUtil;

public class AttachAdapter extends BaseAdapter {

	static class Attach {
		Attach(int id, int drawable, String title) {
			this.id = id;
			this.drawable = drawable;
			this.title = title;
		}

		int id;
		int drawable;
		String title;
	}

	public static final int EXPRESS = 100;
	public static final int PIC = 101;
	public static final int VIDEO = 102;
	public static final int VCARD = 103;
	public static final int LOCATION = 104;
	public static final int FEED = 105;
	public static final int INVEITE = 106;
	public static final int ACTIVITY = 107;
	public static final int QUANSHI = 108;
	public static final String TITLE_VCARD = "名片";
	public static final String TITLE_WEB = "发起活动";
	public static final String TITLE_LOCATION = "位置";
	public static final String TITLE_QUANSHI = "全时电话";

	/**
	 * attach focus and background's padding offset
	 */
	private static final int PADDNG_INNER = DensityUtil.dip2px(2);
	private final ArrayList<Attach> attaches;
	private final Context context;
	private final OnClickListener clickListener;

	public AttachAdapter(Context context, OnClickListener clickListener,
			boolean isFromGroup) {
		attaches = new ArrayList<AttachAdapter.Attach>();
		// attaches.add(new Attach(EXPRESS, R.drawable.sel_chat_attach_express,
		// "表情"));
		attaches.add(new Attach(PIC, R.drawable.sel_chat_attach_pic, "照片"));
		attaches.add(new Attach(VIDEO, R.drawable.sel_chat_attach_video,"拍摄"));
		// attaches.add(new Attach(VCARD, R.drawable.sel_chat_attach_vcard,
		// TITLE_VCARD));
		// attaches.add(new Attach(LOCATION,
		// R.drawable.sel_chat_attach_location,
		// TITLE_LOCATION));
		//
		// if (isFromGroup) {
		// attaches.add(new Attach(ACTIVITY,
		// R.drawable.sel_chat_attach_activity, TITLE_WEB));
		// attaches.add(new Attach(FEED, R.drawable.sel_chat_attach_feed,
		// "发文档"));
		// attaches.add(new Attach(INVEITE, R.drawable.sel_chat_attach_invite,
		// "邀请成员"));
		// }
		//
		// attaches.add(new Attach(QUANSHI, R.drawable.sel_chat_attach_quanshi,
		// TITLE_QUANSHI));

		this.context = context;
		this.clickListener = clickListener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		AttachTile tile = new AttachTile(context);
		Attach attach = attaches.get(position);
		tile.fill(attach, clickListener);

		return tile;
	}

	@Override
	public int getCount() {
		return attaches.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class AttachTile extends RelativeLayout {

		private ImageView iv;
		private ImageView ivNew;
		private TextView tv;

		public void fill(Attach attach, OnClickListener clickListener) {
			iv.setImageResource(attach.drawable);
			tv.setText(attach.title);
			ivNew.setVisibility(View.GONE);
			this.setOnClickListener(clickListener);
			this.setTag(attach);
		}

		public void hideNew() {
			ivNew.setVisibility(View.GONE);
		}

		public AttachTile(Context context) {
			super(context);
			setGravity(Gravity.CENTER);

			LinearLayout ll = new LinearLayout(context);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setGravity(Gravity.CENTER_HORIZONTAL);
			ll.setDuplicateParentStateEnabled(true);
			int paddingTop = ZhislandApplication.APP_RESOURCE.getDrawable(
					R.drawable.bg_dot).getIntrinsicHeight() / 4;
			int paddingRight = ZhislandApplication.APP_RESOURCE.getDrawable(
					R.drawable.bg_dot).getIntrinsicWidth() / 4;
			ll.setPadding(0, paddingTop, paddingRight, 0);

			LinearLayout.LayoutParams paramsIv = new LinearLayout.LayoutParams(
					AttachController.ICON_WIDTH, AttachController.ICON_WIDTH);
			iv = new ImageView(context);
			iv.setPadding(PADDNG_INNER, PADDNG_INNER, PADDNG_INNER,
					PADDNG_INNER);
			paramsIv.topMargin = DensityUtil.dip2px(10);
			iv.setDuplicateParentStateEnabled(true);
			ll.addView(iv, paramsIv);

			tv = new TextView(context);
			tv.setTextColor(getResources().getColor(R.color.color_f2));
			tv.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			paramsTv.setMargins(0, 0, 0, AttachController.VERTICAL_SPACING);
			ll.addView(tv, paramsTv);
			ll.setId(R.id.arg1);

			addView(ll);

			ivNew = new ImageView(context);
			ivNew.setImageResource(R.drawable.bg_dot);
			RelativeLayout.LayoutParams paramNew = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramNew.addRule(RelativeLayout.ALIGN_RIGHT, R.id.arg1);
			paramNew.addRule(RelativeLayout.ALIGN_TOP, R.id.arg1);
			addView(ivNew, paramNew);

		}
	}

}