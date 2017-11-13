package com.zhisland.android.blog.setting.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.AppUtil;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.upapp.UpgradeMgr;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.info.view.impl.FragInfoDetail;
import com.zhisland.android.blog.setting.eb.EBSetting;
import com.zhisland.lib.bitmap.ImageCache;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.file.FileUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class FragSettings extends FragBase {

	public static final String KEY_DEBUG_LOC_INFO = "key_debug_loc_info";

	@InjectView(R.id.tvInvisible)
	TextView tvInvisible;

	@InjectView(R.id.tvCacheSize)
	TextView tvCacheSize;

	@InjectView(R.id.tvVersion)
	TextView tvVersion;

	@InjectView(R.id.ivVersionNew)
	ImageView ivVersionNew;

	private CommonDialog dialog;

	public static void invoke(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragSettings.class;
		param.enableBack = true;
		param.title = "设置";
		Intent intent = CommonFragActivity.createIntent(context, param);
		context.startActivity(intent);
	}

	@Override
	public String getPageName() {
		return "SettingHome";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_settings, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initView();
		EventBus.getDefault().register(this);
		return root;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}

	private void initView() {
		setCacheSize();
		tvVersion.setText(AppUtil.Instance().getVersionName());

		setVisibleView();

		refreshVersionIcon();

	}

	private void setVisibleView() {
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		if (user == null) {
			getActivity().finish();
		} else {
			if (user.invisible != null
					&& user.invisible == User.VALUE_INVISIBLE) {
				tvInvisible.setText("关闭");
			} else {
				tvInvisible.setText("开启");
			}
		}
	}

	public void onEventMainThread(EBSetting eb) {
		switch (eb.getType()) {
		case EBSetting.TYPE_REFRESH_VERSION_ICON:
			refreshVersionIcon();
			break;
		case EBSetting.TYPE_VISIBLE_SETTING:
			setVisibleView();
			break;
		}
	}

	private void refreshVersionIcon() {
		boolean latestVersion = PrefUtil.Instance().isLatestVersion();
		if (latestVersion) {
			ivVersionNew.setVisibility(View.GONE);
		} else {
			ivVersionNew.setVisibility(View.VISIBLE);
		}
	}

	@OnClick(R.id.tvChangePswd)
	void changePswdClick() {
		ActModifyPwd.invoke(getActivity());
	}

	@OnClick(R.id.llInvisible)
	void invisibleClick() {
		FragSetVisiable.invoke(getActivity());
	}

	@OnClick(R.id.tvAboutUs)
	void aboutUsClick() {
		FragAboutUs.invoke(getActivity());
	}

	@OnClick(R.id.tvFeedBack)
	void feedBackClick() {
		FragFeedBack.invoke(getActivity());
	}

	@OnClick(R.id.llClearCache)
	void clearCacheClick() {
		asyncClearCache(true);
	}

	@OnClick(R.id.llVersionUpdate)
	void versionUpdateClick() {
		UpgradeMgr upgradeManager = new UpgradeMgr(getActivity());
		upgradeManager.checkUpgrade(false);
	}

	@OnClick(R.id.tvLogout)
	void logoutClick() {
		showLogoutDialog();
	}

	// 退出Dialog
	private void showLogoutDialog() {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		dialog = new CommonDialog(getActivity());
		dialog.show();
		dialog.setTitle("是否确定退出登录");
		dialog.setContent("确定退出登录后，将无法收到系统的推送通知");
		dialog.tvDlgConfirm.setText("确定退出");
		dialog.tvDlgConfirm.setTextColor(getActivity().getResources().getColor(
				R.color.red));
		dialog.tvDlgConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog != null) {
					dialog.dismiss();
				}
				logout();
			}
		});
	}

	private void logout() {
		ZHApis.getAAApi().logout(null, null);
		asyncClearCache(false);
		HomeUtil.logout();
	}

	private void setCacheSize() {
		long cachSize = ImageCache.getInstance().getCachSize();
		long cachSize2 = ImageCache.getHeaderCacheInstance().getCachSize();
		long webSize = FileUtil.getFileOrDirSize(FragInfoDetail.CACHE_PATH);
		String fileSize = FileUtil.formatFileSize(cachSize + cachSize2
				+ webSize);
		tvCacheSize.setText(fileSize);
	}

	private void asyncClearCache(final boolean isShowClearToast) {
		if (isShowClearToast) {
            showProgressDlg("请稍候，正在清除缓存...");
            getProgressDialog().setCanceledOnTouchOutside(false);
		}
		AsyncTask<Void, Void, Long> task = new AsyncTask<Void, Void, Long>() {

			@Override
			protected Long doInBackground(Void... params) {
				ImageCache.getInstance().clearCaches();
				ImageCache.getHeaderCacheInstance().clearCaches();
				FileUtil.clearDir(FragInfoDetail.CACHE_PATH);
				return (long) 0;
			}

			@Override
			public void onPostExecute(Long params) {
				hideProgressDlg();
				if (isShowClearToast) {
                    showToast("清除成功");
					setCacheSize();
				}
			}

		};
		task.execute();
	}

}
