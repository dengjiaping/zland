package com.zhisland.android.blog.common.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;

/**
 * 徽章
 */
public class BadgeImgsLayout extends ViewGroup {

	int count = 4;

	List<ImageView> imgViews = new ArrayList<ImageView>();

	Context context;

	List<String> badgeImgs;

	LinearLayout llMore;

	int rowW;

	int margin;

	int itemHW;

	int viewWidth;

	int viewHeight;
	
	int totalCount;

	public BadgeImgsLayout(Context context) {
		this(context, null);
	}

	public BadgeImgsLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed && (r - l) > 0) {
			layoutChildren();
		}
	}

	private void layoutChildren() {
		int left = 0;
		int right;
		for (int i = 0; i < imgViews.size(); i++) {
			right = left + itemHW;
			imgViews.get(i).layout(left, 0, right, itemHW);
			left += itemHW + margin * 2;
		}
		right = left + itemHW;
		llMore.layout(left, 0, right, itemHW);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int speSize = MeasureSpec.getSize(heightMeasureSpec);
		int speMode = MeasureSpec.getMode(heightMeasureSpec);
		int height = speSize;
		int viewWidth = getMeasuredWidth();
		rowW = viewWidth - getPaddingLeft() - getPaddingRight();
		margin = DensityUtil.dip2px(5);
		itemHW = (rowW - margin * (count + 1 - 1) * 2) / (count + 1);

		if (speMode == MeasureSpec.AT_MOST) {
			height = itemHW < speSize ? itemHW : speSize;
		}
		if (speMode == MeasureSpec.UNSPECIFIED) {
			height = itemHW;
		}
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			int wh = MeasureSpec.makeMeasureSpec(itemHW,
					MeasureSpec.UNSPECIFIED);
			getChildAt(i).measure(wh, wh);
		}
	}

	public void setImgs(ArrayList<String> imgs){
		this.badgeImgs = imgs;
		totalCount = imgs.size();
		setDataToView();
	}
	

	private void setDataToView() {
		if (imgViews == null || imgViews.size() == 0 || badgeImgs == null
				|| badgeImgs.size() == 0) {
			return;
		}
		for (int i = 0; i < count; i++) {
			if (badgeImgs != null && badgeImgs.size() > i) {
				setImageToView(imgViews.get(i), badgeImgs.get(i));
			} else {
				imgViews.get(i).setVisibility(View.INVISIBLE);
			}
		}
	}

	private void setImageToView(ImageView iv, String url) {
		iv.setVisibility(View.VISIBLE);
		ImageWorkFactory.getCircleFetcher().loadImage(url, iv,
				R.drawable.avatar_default);
	}

	private void init(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray t = getContext().obtainStyledAttributes(attrs,
					R.styleable.ImgsLayout);
			count = t.getInt(R.styleable.ImgsLayout_imgCount, 4);
			t.recycle();
		}
		addView();
	}

	private void addView() {
		imgViews.clear();
		removeAllViews();
		for (int i = 0; i < count; i++) {
			ImageView iv = makeImageView(i);
			imgViews.add(iv);
			addView(iv);
		}
		setDataToView();
	}

	private ImageView makeImageView(final int index) {
		ImageView iv = new ImageView(context);
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return iv;
	}
}
