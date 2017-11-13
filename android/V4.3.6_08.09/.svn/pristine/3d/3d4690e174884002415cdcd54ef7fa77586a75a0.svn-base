package com.zhisland.android.blog.common.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

public class ShareDialog extends Dialog {

	public static final int ID_CANCEL = -1;

	private String mTitle;
	private String mCancelTitle;
	private List<ActionItem> actionItems;
	private int numColumns;

	private TextView tvTitle;
	private TextView tvCancel;

	private Context context;

	public static interface OnShareActionClick {
		public void onClick(int id, ActionItem item);
	}

	private final OnShareActionClick mClickListener;

	public ShareDialog(Context context, int theme,
			List<ActionItem> actionItems,int numColumns, OnShareActionClick mClickListener) {
		super(context, theme);
		this.context = context;
		this.actionItems = actionItems;
		this.mClickListener = mClickListener;
		this.numColumns = numColumns;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public void setCancel(String mCancelTitle) {
		this.mCancelTitle = mCancelTitle;
	}

	private void initTitle() {
		if (!StringUtil.isNullOrEmpty(mTitle)) {
			if (tvTitle == null) {
				tvTitle = (TextView) findViewById(R.id.tvShareTitle);
			}
			tvTitle.setText(mTitle);
		}
	}

	private void initCancel() {
		if (tvCancel == null) {
			tvCancel = (TextView) findViewById(R.id.tvShareCancel);
		}
		if (!StringUtil.isNullOrEmpty(mCancelTitle)) {
			tvCancel.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mClickListener.onClick(ID_CANCEL, null);
				}
			});
			tvCancel.setText(mCancelTitle);
			tvCancel.setVisibility(View.VISIBLE);
		} else {
			tvCancel.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.share);

		initTitle();
		initCancel();

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = DensityUtil.getWidth(); // 宽度
		dialogWindow.setAttributes(lp);
		
		GridView gvShare = (GridView) findViewById(R.id.gvShare);
		gvShare.setNumColumns(numColumns);
		ActionAdapter mAdapter = new ActionAdapter(context);
		gvShare.setAdapter(mAdapter);

		mAdapter.add(actionItems);
	}

	private class ActionAdapter extends BaseListAdapter<ActionItem> {

		private Context context;

		public ActionAdapter(Context context) {
			super();
			this.context = context;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_share, null);
				holder = new Holder(convertView, mClickListener);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			holder.fill(getItem(position));

			return convertView;
		}

		@Override
		protected void recycleView(View view) {

		}

	}

	public static class Holder implements View.OnClickListener {

		@InjectView(R.id.llShare)
		public LinearLayout llShare;

		@InjectView(R.id.ivShare)
		public ImageView ivShare;

		@InjectView(R.id.tvShare)
		public TextView tvShare;

		private ActionItem item;
		private OnShareActionClick actionClick;

		public Holder(View v, OnShareActionClick actionClick) {
			this.actionClick = actionClick;
			ButterKnife.inject(this, v);
			this.llShare.setOnClickListener(this);
		}

		public void fill(ActionItem item) {
			this.item = item;
			ivShare.setImageResource(item.resId);
			tvShare.setText(item.name);
		}

		@Override
		public void onClick(View v) {
			if (actionClick != null) {
				actionClick.onClick(item.id, item);
			}
		}
	}

}
