package com.zhisland.android.blog.common.base;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.view.title.OnTitleClickListner;
import com.zhisland.lib.view.title.TitleBar;

/**
 * titlebar代理
 */
public class TitleBarProxy {

	public static final int TAG_BACK = 601;

	private TitleBar titleBar;

	public void configTitle(View root, int titleType,
			OnTitleClickListner titleListener) {
		switch (titleType) {
		case TitleType.TITLE_LAYOUT: {
			View view = root.findViewById(R.id.custom_titile);
			if (view == null) {
				throw new UnsupportedOperationException();
			}
			titleBar = new TitleBar(root, titleListener);
			titleBar.setBackGroup(R.color.white);
			titleBar.setTextStyle(R.style.title_bar);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 返回button
	 */
	public void addBackButton() {
		if (titleBar != null) {
			View backBtn = TitleCreator.Instance().createImageButton(
					ZhislandApplication.APP_CONTEXT, R.drawable.sel_btn_back);
			titleBar.addLeftTitleButton(backBtn, TAG_BACK);
		}
	}

    /**
	 * 返回button
	 */
	public void addBackButton(int btnBackResID) {
		if (titleBar != null) {
			View backBtn = TitleCreator.Instance().createImageButton(
					ZhislandApplication.APP_CONTEXT, btnBackResID);
			titleBar.addLeftTitleButton(backBtn, TAG_BACK);
		}
	}



	/**
	 * 添加标题
	 */
	public void setTitle(String title) {
		if (titleBar != null) {
			titleBar.setTitle(title);
		}
	}

	/**
	 * 添加标题
	 */
	public void setImageTitle(int resId) {
		if (titleBar != null) {
			titleBar.setImageTitle(resId);
		}
	}

    /**
     * 添加标题
     */
    public void setTitleBackground(int resId) {
        if (titleBar != null) {
            titleBar.setBackGroup(resId);
        }
    }

	/**
	 * 获取标题
	 */
	public void getTitle() {
		if (titleBar != null) {
			titleBar.getTitle();
		}
	}

	/**
	 * 获取标题TextView控件
	 */
	public TextView getTitleTextView() {
		if (titleBar != null) {
			return titleBar.getTitleTextView();
		}
		return null;
	}

	/**
	 * 添加titlebar左边button
	 */
	public void addLeftTitleButton(View view, int tagId) {
		if (titleBar != null) {
			titleBar.addLeftTitleButton(view, tagId);
		}
	}

	/**
	 * 添加titlebar左边button,可配置LayoutParams
	 */
	public void addLeftTitleButton(View view, int tagId,
			LinearLayout.LayoutParams param) {
		if (titleBar != null) {
			titleBar.addLeftTitleButton(view, tagId, param);
		}
	}

	/**
	 * 添加titlebar右边button
	 */
	public void addRightTitleButton(View view, int tagId) {
		if (titleBar != null) {
			titleBar.addRightTitleButton(view, tagId);
		}
	}

	/**
	 * 添加titlebar右边button,可配置LayoutParams
	 */
	public void addRightTitleButton(View view, int tagId,
			LinearLayout.LayoutParams param) {
		if (titleBar != null) {
			titleBar.addRightTitleButton(view, tagId, param);
		}
	}

	/**
	 * 隐藏指定id的button
	 */
	public void hideTitleButton(int tagId) {
		if (titleBar != null) {
			titleBar.hideTitleButton(tagId);
		}
	}

	/**
	 * 隐藏所有button
	 */
	public void hideAllButtons() {
		if (titleBar != null) {
			titleBar.hideAllButtons();
		}
	}

	/**
	 * 显示指定id的button
	 */
	public void showTitleButton(int tagId) {
		if (titleBar != null) {
			titleBar.showTitleButton(tagId);
		}
	}

	/**
	 * enable指定id的button
	 */
	public void enableTitleButton(int tagId) {
		if (titleBar != null) {
			titleBar.enableTitleButton(tagId);
		}
	}

	/**
	 * disable指定id的button
	 */
	public void disableTitleButton(int tagId) {
		if (titleBar != null) {
			titleBar.disableTitleButton(tagId);
		}
	}

	/**
	 * 显示title下面的Line
	 */
	public void showBottomLine() {
		if (titleBar != null) {
			titleBar.showBottomLine();
		}
	}

	/**
	 * 隐藏title下面的Line
	 */
	public void hideBottomLine() {
		if (titleBar != null) {
			titleBar.hideBottomLine();
		}
	}

	public View getRootView() {
		return titleBar.getRootView();
	}

	/**
	 * 根据tagId来获取button
	 * @param tagId
	 * @return
	 */
	public View getButton(int tagId) {
		if (titleBar != null)
			return titleBar.getButton(tagId);
		return null;
	}

    /**
     * 设置标题颜色
     */
    public void setTitleColor(int titleColor) {
        titleBar.setTitleColor(titleColor);
    }
}
