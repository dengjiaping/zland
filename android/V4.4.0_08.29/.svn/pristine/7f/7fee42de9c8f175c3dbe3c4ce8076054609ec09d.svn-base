package com.zhisland.android.blog.common.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

public class SignUpLayout extends ViewGroup {

	int count = 6;

	String moreText;

	List<AvatarView> imgViews = new ArrayList<AvatarView>();

	Context context;

	List<User> users;

	OnSignUpClickListener onClickListener;

	TextView tvMore;

	LinearLayout llMore;

	int rowW;

	int margin;

	int itemHW;

	int viewWidth;

	int viewHeight;

	int totalCount;

	public SignUpLayout(Context context) {
		this(context, null);
	}

	public SignUpLayout(Context context, AttributeSet attrs) {
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
			int wh = MeasureSpec.makeMeasureSpec(itemHW, MeasureSpec.AT_MOST);
			getChildAt(i).measure(wh, wh);
		}
	}

	public void setOnSignUpClickListener(OnSignUpClickListener onClickListener) {
		setListener();
		this.onClickListener = onClickListener;
	}

	public void setSignUpUsers(List<User> users) {
		this.users = users;
		totalCount = users.size();
		setDataToView();
	}

	public void setSignUpUsers(List<User> users, int totalCount) {
		this.users = users;
		this.totalCount = totalCount;
		setDataToView();
	}

	private void setDataToView() {
		if (imgViews == null || imgViews.size() == 0 || users == null
				|| users.size() == 0) {
			return;
		}
		for (int i = 0; i < count; i++) {
			if (users != null && users.size() > i) {
				setImageToView(imgViews.get(i), users.get(i));
			} else {
				imgViews.get(i).setVisibility(View.INVISIBLE);
			}
		}
		if (users.size() > count) {
			int leftCount = totalCount - count;
			if (StringUtil.isNullOrEmpty(moreText)) {
				tvMore.setText("+" + (leftCount > 99 ? 99 : leftCount));
			} else {
				tvMore.setText(moreText);
			}
			llMore.setVisibility(View.VISIBLE);
		} else {
			llMore.setVisibility(View.INVISIBLE);
		}
	}

	private void setImageToView(AvatarView avatarView, User user) {
		avatarView.setVisibility(View.VISIBLE);
		avatarView.fill(user, false);
	}

	private void init(AttributeSet attrs) {
		if (attrs != null) {
			TypedArray t = getContext().obtainStyledAttributes(attrs,
					R.styleable.ImgsLayout);
			count = t.getInt(R.styleable.ImgsLayout_imgCount, 6);
			moreText = t.getString(R.styleable.ImgsLayout_moreText);
			t.recycle();
		}
		makeAndAddView();
	}

	private void makeAndAddView() {
		imgViews.clear();
		removeAllViews();
		for (int i = 0; i < count; i++) {
			AvatarView iv = makeImageView(i);
			imgViews.add(iv);
			addView(iv);
		}
		addView(makeMoreView());
		setDataToView();
	}

	public void setCount(int count) {
		if (this.count != count) {
			this.count = count;
			makeAndAddView();
		}
	}

	private void setListener() {
		for (int i = 0; i < imgViews.size(); i++) {
			final int index = i;
			imgViews.get(index).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (SignUpLayout.this.onClickListener != null) {
						SignUpLayout.this.onClickListener.onItemClick(index, v);
					}
				}
			});
		}

		tvMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SignUpLayout.this.onClickListener != null) {
					SignUpLayout.this.onClickListener.moreClick(v);
				}
			}
		});
	}

	private AvatarView makeImageView(final int index) {
		AvatarView iv = new AvatarView(context);
		return iv;
	}

	private View makeMoreView() {
		llMore = new LinearLayout(getContext());
		llMore.setOrientation(LinearLayout.VERTICAL);
		llMore.setGravity(Gravity.CENTER);
		tvMore = new TextView(context);
		tvMore.setSingleLine(true);
		tvMore.setGravity(Gravity.CENTER);
		tvMore.setTextColor(context.getResources().getColor(R.color.color_f3));
        DensityUtil.setTextSize(tvMore, R.dimen.txt_12);
		llMore.setBackgroundResource(R.drawable.bg_sign_up_more);
		llMore.addView(tvMore);
		return llMore;
	}

	public interface OnSignUpClickListener {

		public void onItemClick(int index, View v);

		public void moreClick(View v);

	}
}
