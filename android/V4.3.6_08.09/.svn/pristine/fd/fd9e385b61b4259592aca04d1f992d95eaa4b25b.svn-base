package com.zhisland.android.blog.common.base;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.AnimUtils;
import com.zhisland.android.blog.common.util.IMUtil;
import com.zhisland.lib.component.act.BaseFragmentActivity;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.ScreenTool;
import com.zhisland.lib.view.title.OnTitleClickListner;
import com.zhisland.lib.view.title.TitleBar;

public abstract class FragBaseActivity extends BaseFragmentActivity implements
		OnTitleClickListner {

	protected LinearLayout rootLayout;
	protected RelativeLayout laytouForInfo;

	private TitleBarProxy titleBar;
	private DefaultTitleBarClickListener defaultTitleBarListener;

	public TitleBarProxy getTitleBar() {
		if (titleBar == null) {
			initTitleBar();
		}
		return titleBar;
	}

	public LinearLayout getRootLayout() {
		return rootLayout;
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		IMUtil.checkIM(this);
		buildFragView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			AnimUtils.configAnim(this, getClass().getName(), false);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * setup TitleBar according {@link TitleType}, and place a fragment cotainer
	 * with id R.id.frag_container
	 */
	@SuppressLint("InflateParams")
	private void buildFragView() {

		int customLayoutId = this.layResId();
		if (customLayoutId == 0) {
			View viewTitle = null;
			switch (titleType()) {
			case TitleType.TITLE_LAYOUT:
				viewTitle = inflater
						.inflate(R.layout.titlebar_with_image, null);
				break;
			}

			laytouForInfo = new RelativeLayout(this);
			laytouForInfo.setId(R.id.frag_container_rl);

			rootLayout = new LinearLayout(this);
			rootLayout.setId(R.id.frag_container);
			rootLayout.setOrientation(LinearLayout.VERTICAL);
			if (viewTitle != null) {
				viewTitle.setId(R.id.custom_titile);
				RelativeLayout.LayoutParams vtParams = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, getResources()
								.getDimensionPixelSize(R.dimen.title_height));
				vtParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				rootLayout.addView(viewTitle, vtParams);
			}

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			if (viewTitle != null) {
				params.addRule(RelativeLayout.BELOW, R.id.custom_titile);
			}
			laytouForInfo.addView(rootLayout, params);

			this.setContentView(laytouForInfo, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			this.setContentView(customLayoutId);
		}

		// 初始化titlebar
		initTitleBar();
	}

	private void initTitleBar() {
		titleBar = new TitleBarProxy();
		defaultTitleBarListener = new DefaultTitleBarClickListener(this);
		titleBar.configTitle(this.getWindow().getDecorView(), titleType(), this);
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		try {
			switch (tagId) {
			case TitleBarProxy.TAG_BACK:
				defaultTitleBarListener.onBack();
				break;
			case TitleBar.TAG_TITLE_TXT: {
				defaultTitleBarListener.onTitle();
				break;
			}
			default:
				break;
			}
		} catch (final Throwable e) {
			MLog.e("FragBaseActivity", "exception happened", e);
		}
	}

	protected int layResId() {
		return 0;
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		ComponentName comClass = intent.getComponent();
		if (comClass == null)
			return;// not zhisland's component or not an activty

		String className = comClass.getClassName();
		if (className.equals(CommonFragActivity.class.getName())) {
			className = CommonFragActivity.INCOME_FRAG_NAME;
		}
		AnimUtils.configAnim(this, className, true);
	}

	@Override
	public void finish() {
		super.finish();
		ScreenTool.HideInput(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_NONE;
	}

}
