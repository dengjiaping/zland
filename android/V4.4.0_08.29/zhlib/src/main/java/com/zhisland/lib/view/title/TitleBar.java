package com.zhisland.lib.view.title;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.lib.R;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;

/**
 * app 统一标题，支持标题左右增加,隐藏button按钮，按钮点击事件
 *
 * addRightTitleButton(),addLeftTitileButton()，hideTitleButton()
 * onTitleClicked();
 */
public class TitleBar implements OnClickListener {

	public static final int TAG_TITLE_TXT = -9;

	private static final int MARGIN_DEFAULT = DensityUtil.dip2px(10);
	private static final String TAG = "TitleBar";

	private SparseArray<View> buttons;

	protected OnTitleClickListner titleListener;

	protected View titleLayout;
	protected LinearLayout rightContainer;
	protected LinearLayout leftContainer;
	protected TextView titleText;
	protected ImageView ivTitle;
	protected String curTitle;
	protected View titleLine;

	protected View root;

	public TitleBar(final View view, final OnTitleClickListner titleListener) {
		this.root = view;
		this.titleListener = titleListener;
		this.titleLayout = this.root.findViewById(R.id.custom_titile);
		this.init();
	}

	public TitleBar(final View view, View titleLayout,
			OnTitleClickListner titleListener) {
		this.root = view;
		this.titleListener = titleListener;
		this.titleLayout = titleLayout;
		this.init();
	}

	private void init() {
		this.titleText = (TextView) this.titleLayout
				.findViewById(R.id.title_text);
		this.ivTitle = (ImageView) this.titleLayout.findViewById(R.id.ivTitle);
		this.rightContainer = (LinearLayout) this.titleLayout
				.findViewById(R.id.titlebar_image_right_layout);
		this.leftContainer = (LinearLayout) this.titleLayout
				.findViewById(R.id.titlebar_image_left_layout);
		this.titleLine = this.titleLayout.findViewById(R.id.titleLine);
		if (titleText != null) {
			titleText.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					if (titleListener != null) {
						titleListener.onTitleClicked(v, TAG_TITLE_TXT);
					}
					return true;
				}
			});
		}
		buttons = new SparseArray<View>();
	}

	public void addRightTitleButton(View view, int tagId) {
		if (view == null)
			return;
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		view.setPadding(MARGIN_DEFAULT * 2, 0, MARGIN_DEFAULT, 0);

		this.addRightTitleButton(view, tagId, param);
	}

	public void addRightTitleButton(View view, int tagId,
			LinearLayout.LayoutParams param) {
		if (view == null)
			return;
		try {
			view.setTag(tagId);
			view.setOnClickListener(this);

			view.setLayoutParams(param);
			this.rightContainer.addView(view);
			this.buttons.put(tagId, view);

			MLog.d(TAG, "add right title layout changed");
		} catch (final Exception ex) {
			MLog.e(TAG, ex.getMessage(), ex);
		} catch (final Throwable e) {
			MLog.e(TAG, "exception happened", e);
		}
	}

	public void addLeftTitleButton(View view, int tagId) {
		if (view == null)
			return;
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		view.setPadding(MARGIN_DEFAULT, 0, MARGIN_DEFAULT * 2, 0);

		this.addLeftTitleButton(view, tagId, param);

	}

	public void addLeftTitleButton(View view, int tagId,
			LinearLayout.LayoutParams param) {
		if (view == null)
			return;
		try {
			view.setTag(tagId);
			view.setOnClickListener(this);

			view.setLayoutParams(param);
			this.leftContainer.addView(view);
			this.buttons.put(tagId, view);

			MLog.d(TAG, "add left title layout changed");
		} catch (final Exception ex) {
			MLog.e(TAG, ex.getMessage(), ex);
		} catch (final Throwable e) {
			MLog.e(TAG, "exception happened", e);
		}
	}

	public void hideAllButtons() {
		for (int i = 0, size = buttons.size(); i < size; i++) {
			int index = buttons.keyAt(i);
			View view = buttons.get(index);
			view.setVisibility(View.GONE);
		}

		MLog.d(TAG, "hide title layout changed");
	}

	public void hideTitleButton(int tagId) {

		View view = this.buttons.get(tagId);
		if (view != null) {
			view.setVisibility(View.GONE);
		}

		MLog.d(TAG, "hide title layout changed");
	}

	public void disableTitleButton(int tagId) {

		View view = this.buttons.get(tagId);
		if (view != null) {
			view.setEnabled(false);
		}
	}

	public void enableTitleButton(int tagId) {

		View view = this.buttons.get(tagId);
		if (view != null) {
			view.setEnabled(true);
		}
	}

	public void showTitleButton(int tagId) {
		View view = this.buttons.get(tagId);
		if (view != null) {
			view.setVisibility(View.VISIBLE);
		}
		MLog.d(TAG, "show title layout changed");
	}

	//
	// =========================================
	// 基类标题栏的监听器
	// ==========================================
	//

	@Override
	public void onClick(final View v) {
		if (TitleBar.this.titleListener != null) {
			try {
				final int tagId = (Integer) v.getTag();
				TitleBar.this.titleListener.onTitleClicked(v, tagId);
			} catch (final Throwable e) {
				MLog.e(TAG, "exception happened", e);
			}
		}
	}

	public void setTitle(final String title) {
		MLog.d(TAG, "set title: " + title);
		try {
			if (this.titleText != null) {
				this.curTitle = title;
				titleText.setVisibility(View.VISIBLE);
				ivTitle.setVisibility(View.GONE);
				this.titleText.setText(title);
			}
		} catch (final Throwable e) {
			MLog.e(TAG, "exception happened", e);
		}
	}

	public String getTitle() {
		String title = null;

		if (this.titleText != null) {
			title = this.titleText.getText().toString();
		}

		return title;
	}

	public TextView getTitleTextView() {
		return titleText;
	}

	public void setBackGroup(int bgResId) {
		this.titleLayout.setBackgroundResource(bgResId);
	}

	public void setTextStyle(int style) {
		if (titleText != null) {
			titleText.setTextAppearance(root.getContext(), style);
		}
	}

    public void setTitleColor(int titleColor) {
        if (titleText != null){
            titleText.setTextColor(root.getContext().getResources().getColor(titleColor));
        }
    }

	public void showBottomLine() {
		titleLine.setVisibility(View.VISIBLE);
	}

	public void hideBottomLine() {
		titleLine.setVisibility(View.GONE);
	}

	public void setImageTitle(int resId) {
		titleText.setVisibility(View.GONE);
		ivTitle.setVisibility(View.VISIBLE);
		ivTitle.setImageResource(resId);
	}

	/**
	 * 根据tagId来获取title button
	 * @param tagId
	 * @return
	 */
	public View getButton(int tagId){
		return buttons.get(tagId);
	}

	static ImageView getSimpleImageView(final Context context) {
		final ImageView imageView = new ImageView(context);
		imageView.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		return imageView;
	}

	public View getRootView() {
		return titleLayout;
	}
}
