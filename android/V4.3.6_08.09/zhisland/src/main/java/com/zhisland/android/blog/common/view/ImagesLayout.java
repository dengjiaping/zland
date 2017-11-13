package com.zhisland.android.blog.common.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;

public class ImagesLayout extends LinearLayout {

	int count = 4;

	boolean isCircle = false;

	List<ImageView> imgViews = new ArrayList<ImageView>();

	Context context;

	List<String> imgs;

	OnItemClickListener onClickListener;

	int rowW;

	int margin;

	int itemHW;

	int rightArrow;

	public ImagesLayout(Context context) {
		this(context, null);
	}

	public ImagesLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	public void setOnItemClickListener(OnItemClickListener onClickListener) {
		this.onClickListener = onClickListener;
		for (int i = 0; i < imgViews.size(); i++) {
			final int index = i;
			imgViews.get(index).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (ImagesLayout.this.onClickListener != null) {
						ImagesLayout.this.onClickListener.onItemClick(index, v);
					}
				}
			});
		}
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
		for (int i = 0; i < count; i++) {
			if (imgs != null && imgs.size() > i) {
				setImageToView(imgViews.get(i), imgs.get(i));
			} else {
				imgViews.get(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	private void setImageToView(ImageView iv, String url) {
		iv.setVisibility(View.VISIBLE);
		if (isCircle) {
			ImageWorkFactory.getCircleFetcher().loadImage(url, iv);
		} else {
			ImageWorkFactory.getFetcher().loadImage(url, iv);
		}
	}

	private void init(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray t = getContext().obtainStyledAttributes(attrs,
					R.styleable.ImgsLayout);
			count = t.getInt(R.styleable.ImgsLayout_imgCount, 4);
			isCircle = t.getBoolean(R.styleable.ImgsLayout_isCircle, false);
			t.recycle();
		}
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
		rowW = DensityUtil.getWidth() - DensityUtil.dip2px(30);
		rightArrow = DensityUtil.dip2px(20);
		margin = DensityUtil.dip2px(5);
		itemHW = (rowW - rightArrow - margin * (count - 1) * 2) / count;
		initViews();
	}

	private void initViews() {
		for (int i = 0; i < count; i++) {
			ImageView iv = makeImageView(i);
			imgViews.add(iv);
			addView(iv);
		}
		addView(makeArrowView());
	}

	private ImageView makeImageView(final int index) {
		ImageView iv = new ImageView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemHW,
				itemHW);
		lp.leftMargin = index == 0 ? 0 : margin;
		lp.rightMargin = index == count - 1 ? 0 : margin;
		iv.setLayoutParams(lp);
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return iv;
	}

	private ImageView makeArrowView() {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ImageView.ScaleType.FIT_END);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		iv.setImageResource(R.drawable.img_arrow_right);
		iv.setLayoutParams(lp);
		return iv;
	}

	public interface OnItemClickListener {

		public void onItemClick(int index, View v);

	}
}
