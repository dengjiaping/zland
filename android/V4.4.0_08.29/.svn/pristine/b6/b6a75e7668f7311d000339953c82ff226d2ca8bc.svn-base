package com.zhisland.android.blog.common.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;
import android.webkit.WebChromeClient.CustomViewCallback;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.webview.WVWrapper.WebListener;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.text.ZHLink;

public class FragWebViewActivity extends FragBaseActivity implements
		WebListener {

	private static final long serialVersionUID = 1L;
	private static final int TAG_SHARE = 301;
	private static final int TAG_PRO = 105;

	private FragWebview frag;
	private boolean refreshState = false;

	/**
	 * 打开URL
	 */
	public static void launch(Context context, String url, String title) {
		Intent intent = new Intent();
		intent.setClass(context, FragWebViewActivity.class);
		intent.putExtra(FragWebview.INK_URL, url);
		intent.putExtra(FragWebview.INK_TITLE, title);
		context.startActivity(intent);
	}
	/**
	 * 打开URL 带cookies
	 */
	public static void launch(Context context, String url, String title, String host, String value) {
		Intent intent = new Intent();
		intent.setClass(context, FragWebViewActivity.class);
		intent.putExtra(FragWebview.INK_URL, url);
		intent.putExtra(FragWebview.INK_TITLE, title);
		intent.putExtra(FragWebview.INK_COOKIES_HOST, host);
		intent.putExtra(FragWebview.INK_COOKIES_VALUE, value);
		context.startActivity(intent);
	}
	
	public static void launch(Context context, String url, String title, String host, String value, boolean isDialogShow, boolean enableCache){
		Intent intent = new Intent();
		intent.setClass(context, FragWebViewActivity.class);
		intent.putExtra(FragWebview.INK_URL, url);
		intent.putExtra(FragWebview.INK_TITLE, title);
		intent.putExtra(FragWebview.INK_COOKIES_HOST, host);
		intent.putExtra(FragWebview.INK_COOKIES_VALUE, value);
		intent.putExtra(FragWebview.INK_DIALOG_SHOW, isDialogShow);
		intent.putExtra(FragWebview.INK_CACHE, enableCache);
		context.startActivity(intent);
	}
	/**
	 * 打开html data
	 */
	public static void launchData(Context context, String content, String title) {
		Intent intent = new Intent();
		intent.setClass(context, FragWebViewActivity.class);
		intent.putExtra(FragWebview.INK_CONTENT, content);
		intent.putExtra(FragWebview.INK_TITLE, title);
		context.startActivity(intent);
	}

	@Override
	public boolean shouldContinueCreate(Bundle savedInstanceState) {
		String url = getIntent().getStringExtra(FragWebview.INK_URL);
		String content = getIntent().getStringExtra(FragWebview.INK_CONTENT);
		if (StringUtil.isNullOrEmpty(url) && StringUtil.isNullOrEmpty(content))
			return false;

		return true;
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		setSwipeBackEnable(false);
		frag = new FragWebview();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.frag_container, frag);
		ft.commit();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		
		if (frag.canGoBack()) {
			frag.goBack();
		} else {
			this.finish();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		default: {
			if (frag != null) {
				frag.onActivityResult(requestCode, resultCode, data);
			} else {
				super.onActivityResult(requestCode, resultCode, data);
			}
		}
		}
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		switch (tagId) {
		case TitleBarProxy.TAG_BACK: {
			this.onBackPressed();
			break;
		}
		case TAG_SHARE: {
			showShare();
			break;
		}
		default:
			super.onTitleClicked(view, tagId);
		}
	}

	// ==========web listener metods===
	@Override
	public void onPageStart() {
		getTitleBar().hideTitleButton(TAG_SHARE);
		getTitleBar().showTitleButton(TAG_PRO);
		if (refreshState == false) {
			frag.refresh();
			refreshState = true;
		}

	}

	@Override
	public void onPageFinish() {
	}

	@Override
	public void onReceivedTitle(String title) {
		if(ZHLink.isValidTitleAndIsUrl(title)){
			return;
		}
		this.setTitle(title);
		if(frag.titleText!=null){
			frag.titleText.setVisibility(View.VISIBLE);
			frag.titleText.setText(title);
			frag.titleBar.getRootView().findViewById(R.id.ivTitle).setVisibility(View.GONE);
		}
	}

	private void updateTitleClose() {
		if (frag.canGoBack()) {
			getTitleBar().showTitleButton(TitleBarProxy.TAG_BACK);
		} else {
			getTitleBar().hideTitleButton(TitleBarProxy.TAG_BACK);
		}
	}

	@Override
	public void showShare() {
		String title = "更多操作";
	}

	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {

	}

	@Override
	public void onHideCustomView() {

	}

	@Override
	public void loadError() {
		
	}
}
