package com.zhisland.android.blog.profile.controller.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.lib.component.frag.FragBase;

/**
 * 个人中心网络错误 fragment
 */
public class FragProfileNetError extends FragBase {

	private static final String PAGE_NAME = "FragProfileNetError";

	private ProfileNetErrorListener listener;

	public static interface ProfileNetErrorListener {
		void onClickReload();
	}

	public FragProfileNetError() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_profile_net_error, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rootView.setLayoutParams(lp);
		ButterKnife.inject(this, rootView);
		return rootView;
	}

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}

	/**
	 * 设置回调lisener
	 */
	public void setListener(ProfileNetErrorListener listener) {
		this.listener = listener;
	}

	/**
	 * 重新加载
	 */
	@OnClick(R.id.btnProfileReload)
	public void onClickProfileReload() {
		if (listener != null) {
			listener.onClickReload();
		}
	}

}
