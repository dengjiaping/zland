package com.zhisland.lib.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhisland.lib.R;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.frag.FragPullGrid;
import com.zhisland.lib.image.ImgPicData.ImageBucket;
import com.zhisland.lib.image.ImgPicData.ImageItem;
import com.zhisland.lib.util.DensityUtil;

/**
 * @See MultiImgPickerActivity 图片选择
 */
public class FragImgPicker extends FragPullGrid<ImageItem> {

	static interface OnPickerListener {

		static int STU_SELECT = 1;
		static int STU_UNSELECT = 2;
		static int STU_REACH_MAX = 3;

		/**
		 * 
		 * @return {@link OnPickerListener#STU_SELECT
		 *         ,OnPickerListener#STU_UNSELECT
		 *         ,OnPickerListener#STU_REACH_MAX}
		 */
		int onImgClicked(ImageBucket bucket, String imgpath);
	}

	private static final int WIDTH = DensityUtil.getWidth() * 2 / 7;
	private static final int HEIGHT = WIDTH - DensityUtil.dip2px(10);
	private static final int PAD = DensityUtil.dip2px(1);

	private ImageBucket bucket;
	private ImgBucketAdapter imgAdapter;
	private OnPickerListener pickListener;
	private GridView internalView;

	/**
	 * 
	 */
	public void setData(ImageBucket bucket) {
		this.bucket = bucket;
		if (imgAdapter != null) {
			imgAdapter.clearItems();
			imgAdapter.add(bucket.imageList);
		}
	}

	public void setOnPickListener(OnPickerListener pickListener) {
		this.pickListener = pickListener;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		imgAdapter = new ImgBucketAdapter(getActivity());
		getPullProxy().setAdapter(imgAdapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		pullView.setMode(Mode.DISABLED);
		this.internalView = getPullProxy().getPullView().getRefreshableView();
		int pad = DensityUtil.dip2px(5);
		internalView.setPadding(pad, 0, pad, 0);
		internalView.setNumColumns(3);
		internalView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
		internalView.setVerticalSpacing(DensityUtil.dip2px(10));
		internalView.setHorizontalSpacing(DensityUtil.dip2px(10));
		pullView.setMode(Mode.DISABLED);
	}

	@Override
	public void loadNormal() {
		getPullProxy().onRefreshFinished();
	}

	class ImgBucketAdapter extends BaseListAdapter<ImageItem> {

		private Context context;

		public ImgBucketAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new ImgPickerView(context);
			}
			ImageItem item = getItem(position);
			ImgPickerView bv = (ImgPickerView) convertView;
			bv.fill(item);
			return convertView;
		}

		@Override
		protected void recycleView(View view) {
			if (view instanceof ImgPickerView) {
				ImgPickerView bv = (ImgPickerView) view;
				bv.recycle();
			}
		}
	}

	class ImgPickerView extends RelativeLayout implements
			android.view.View.OnClickListener {

		private static final int ID_IMG = 1;
		private static final int ID_SELECT = 2;
		private ImageView iv;
		private ImageView ivSelect;
		private ImageItem curItem;

		public ImgPickerView(Context context) {
			super(context);

			setOnClickListener(this);
			setPadding(PAD, PAD, PAD, PAD);

			iv = new ImageView(context);
			iv.setId(ID_IMG);
			iv.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams paramImg = new LayoutParams(WIDTH, HEIGHT);
			paramImg.addRule(ALIGN_PARENT_TOP);
			paramImg.addRule(CENTER_HORIZONTAL);
			addView(iv, paramImg);
			iv.setOnClickListener(this);

			ivSelect = new ImageView(context);
			ivSelect.setId(ID_SELECT);
			LayoutParams paramSelect = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramSelect.addRule(ALIGN_TOP, ID_IMG);
			paramSelect.addRule(ALIGN_RIGHT, ID_IMG);
			paramSelect.topMargin = DensityUtil.dip2px(2);
			paramSelect.rightMargin = DensityUtil.dip2px(2);
			addView(ivSelect, paramSelect);

		}

		public void fill(ImageItem item) {
			this.curItem = item;
			ImageWorkFactory.getFetcher().loadImage(item.imagePath, iv,
					R.drawable.news_pic_default);
			refreshSelectStatus();
		}

		public void recycle() {
			iv.setImageBitmap(null);
			ivSelect.setImageBitmap(null);
			curItem = null;
		}

		@Override
		public void onClick(View arg0) {
			if (pickListener == null)
				return;
			int status = pickListener.onImgClicked(bucket, curItem.imagePath);
			switch (status) {
			case OnPickerListener.STU_SELECT: {
				curItem.isSelected = true;
				break;
			}
			case OnPickerListener.STU_UNSELECT: {
				curItem.isSelected = false;
				break;
			}
			case OnPickerListener.STU_REACH_MAX: {
				curItem.isSelected = false;
				break;
			}
			}
			refreshSelectStatus();
		}

		private void refreshSelectStatus() {
			if (!curItem.isSelected) {
				setBackgroundColor(Color.TRANSPARENT);
				ivSelect.setImageResource(R.drawable.chb_unselect);
			} else {
				ivSelect.setImageResource(R.drawable.chb_select);
				setBackgroundColor(Color.TRANSPARENT);
			}
		}
	}

	@Override
	protected String getPageName() {
		return null;
	}

}
