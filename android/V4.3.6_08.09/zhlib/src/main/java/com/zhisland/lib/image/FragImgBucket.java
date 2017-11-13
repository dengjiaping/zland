package com.zhisland.lib.image;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhisland.lib.R;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.frag.FragPullGrid;
import com.zhisland.lib.image.ImgPicData.ImageBucket;
import com.zhisland.lib.util.DensityUtil;

/**
 * @See MultiImgPickerActivity 相册选择
 */
public class FragImgBucket extends FragPullGrid<ImageBucket> {

	public static interface OnBucketListener {
		void onBucketClicked(ImageBucket bucket);
	}

	protected static final String PIC_CAP = "拍照";
	private ImgBucketAdapter bucketAdapter;
	private OnBucketListener bucketListener;
	private GridView internalView;

	public void setBucketListener(OnBucketListener bucketListener) {
		this.bucketListener = bucketListener;
	}

	public void refreshUI() {
		bucketAdapter.notifyDataSetChanged();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		bucketAdapter = new ImgBucketAdapter(activity);
		getPullProxy().setAdapter(bucketAdapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.internalView = getPullProxy().getPullView().getRefreshableView();
		int pad = DensityUtil.dip2px(10);
		internalView.setPadding(pad, 0, pad, 0);
		internalView.setNumColumns(2);
		internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		internalView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
		internalView.setVerticalSpacing(DensityUtil.dip2px(20));
		internalView.setHorizontalSpacing(DensityUtil.dip2px(20));
		pullView.setMode(Mode.PULL_FROM_START);

		pullView.setRefreshing();

	}

	@Override
	public void loadNormal() {
		List<ImageBucket> dataList = AlbumHelper.instance()
				.getImagesBucketList(true);

		ImageBucket im = new ImageBucket();
		im.bucketName = PIC_CAP;

		if (dataList == null) {
			dataList = new ArrayList<ImgPicData.ImageBucket>();
		}
		dataList.add(0, im);

		getPullProxy().onLoadSucessfully(dataList);
		pullView.setMode(Mode.DISABLED);
	}

	class ImgBucketAdapter extends BaseListAdapter<ImageBucket> {

		private Context context;

		public ImgBucketAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new BucketView(context);
			}
			ImageBucket item = getItem(position);
			BucketView bv = (BucketView) convertView;
			bv.fill(item);
			return convertView;
		}

		@Override
		protected void recycleView(View view) {
			if (view instanceof BucketView) {
				BucketView bv = (BucketView) view;
				bv.recycle();
			}
		}
	}

	class BucketView extends RelativeLayout implements
			android.view.View.OnClickListener {

		final int WIDTH = DensityUtil.getWidth() * 2 / 5;
		final int HEIGHT = WIDTH * 5 / 5;
		final int PAD = DensityUtil.dip2px(4);

		private static final int ID_IMG = 1;
		private static final int ID_NAME = 2;
		private ImageView iv;
		private TextView name;
		private LayoutParams paramImg;
		private ImageBucket bucket;

		public BucketView(Context context) {
			super(context);

			setPadding(PAD, PAD, PAD, PAD);
			setOnClickListener(this);

			iv = new ImageView(context);
			iv.setId(ID_IMG);
			iv.setScaleType(ScaleType.CENTER_CROP);
			paramImg = new LayoutParams(WIDTH, HEIGHT);
			paramImg.addRule(ALIGN_PARENT_TOP);
			paramImg.addRule(CENTER_HORIZONTAL);
			addView(iv, paramImg);

			name = new TextView(context);
			name.setId(ID_NAME);
			name.setTextColor(Color.DKGRAY);
			name.setTextSize(16);
			name.setSingleLine();
			LayoutParams paramName = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			paramName.addRule(BELOW, ID_IMG);
			paramName.addRule(CENTER_HORIZONTAL);
			addView(name, paramName);
		}

		public void fill(ImageBucket item) {

			this.bucket = item;

			if (item.bucketName.equals(PIC_CAP)) {
				name.setText(PIC_CAP);
				iv.setImageResource(R.drawable.icon_cap_in_select_img);
				iv.setScaleType(ScaleType.CENTER);
				// paramImg.width = LayoutParams.WRAP_CONTENT;
				// paramImg.height = LayoutParams.WRAP_CONTENT;
				paramImg.addRule(RelativeLayout.CENTER_IN_PARENT);
				setBackgroundColor(0xfff8f8f8);
				return;
			}
			paramImg.width = WIDTH;
			paramImg.height = WIDTH;
			String title = String.format("(%d/%d)%s", item.selectCount,
					item.count, item.bucketName);
			name.setText(title);
			if (item.imageList != null && item.imageList.size() > 0) {
				String imgPath = item.imageList.get(0).imagePath;
				ImageWorkFactory.getFetcher().loadImage(imgPath, iv);
			}

			if (item.selectCount > 0) {
				setBackgroundColor(0xfff0f0f0);
			} else {
				setBackgroundColor(0xfff8f8f8);
			}
		}

		public void recycle() {
			iv.setImageBitmap(null);
		}

		@Override
		public void onClick(View v) {
			bucketListener.onBucketClicked(bucket);
		}
	}

	@Override
	protected String getPageName() {
		return "select_img";
	}

}
