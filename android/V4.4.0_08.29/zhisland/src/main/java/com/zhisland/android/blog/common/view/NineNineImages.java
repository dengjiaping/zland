package com.zhisland.android.blog.common.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;

public class NineNineImages extends LinearLayout {

	List<LinearLayout> llRows = new ArrayList<LinearLayout>();

	List<ImageView> imgViews = new ArrayList<ImageView>();

	ImageView SingleImg;

	Context context;

	List<String> imgs;

	OnItemClickListener onClickListener;

	int rowW;

	int margin;

	int itemHW;

	public NineNineImages(Context context) {
		this(context, null);
	}

	public NineNineImages(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public void setImagesUrl(List<String> imgs,
			OnItemClickListener onClickListener) {
		this.imgs = imgs;
		this.onClickListener = onClickListener;
		removeAllViews();
		make();
	}

	private void init() {
		setOrientation(LinearLayout.VERTICAL);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(lp);
		rowW = DensityUtil.getWidth() - DensityUtil.dip2px(30);
		margin = DensityUtil.dip2px(2);
		itemHW = (rowW - margin * 6) / 3;
		initViews();
	}

	private void initViews() {
		for (int i = 0; i < 3; i++) {
			llRows.add(makeRowView());
		}
		for (int i = 0; i < 9; i++) {
			imgViews.add(makeImageView(i));
			llRows.get(i / 3).addView(imgViews.get(i));
		}
		SingleImg = makeSingleImageView();
	}

	private ImageView makeImageView(final int index) {
		ImageView iv = new ImageView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemHW,
				itemHW);
		lp.leftMargin = margin;
		lp.rightMargin = margin;
		iv.setLayoutParams(lp);
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickListener.onItemClick(index, v, imgs.get(index));
			}
		});
		return iv;
	}

	private ImageView makeSingleImageView() {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(rowW, rowW);
		iv.setLayoutParams(lp);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickListener.onSingleClick();
			}
		});
		return iv;
	}

	private LinearLayout makeRowView() {
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, itemHW);
		lp.topMargin = margin;
		lp.bottomMargin = margin;
		ll.setLayoutParams(lp);
		return ll;
	}

	private void make() {
		if (imgs != null && imgs.size() > 0) {
			if (imgs.size() == 1) {
				makeSingleImg();
			} else {
				makeArrImg();
			}
		}
	}

	private void makeSingleImg() {
		removeAllViews();
		addView(SingleImg);
		ImageWorkFactory.getFetcher().loadImage(imgs.get(0), SingleImg,
				R.drawable.feed_bg_default);
	}

	private void makeArrImg() {
		removeAllViews();
		for (int i = 0; i < 9; i++) {
			if (i < imgs.size()) {
				if (i % 3 == 0) {
					addView(llRows.get(i / 3));
				}
				imgViews.get(i).setVisibility(View.VISIBLE);
				ImageWorkFactory.getFetcher().loadImage(imgs.get(i),
						imgViews.get(i));
			} else {
				imgViews.get(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	public interface OnItemClickListener {

		public void onItemClick(int index, View v, String imgs);

		public void onSingleClick();

	}
}
