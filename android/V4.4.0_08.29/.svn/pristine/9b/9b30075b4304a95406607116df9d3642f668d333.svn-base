package com.zhisland.android.blog.common.upapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.lib.load.HttpDownloadInfo;
import com.zhisland.lib.load.LoadStatus;
import com.zhisland.lib.load.ZHLoadManager;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.file.FileMgr;
import com.zhisland.lib.util.file.FileMgr.DirType;
import com.zhisland.lib.util.file.FileUtil;

import java.io.File;

import de.greenrobot.event.EventBus;

public class ActUpdateDialog extends FragBaseActivity {

	private static final String IS_FORCE_UPDATE_KEY = "is_force_update";
	private static final String VERSION_NAME_KEY = "vname_from_server";
	private static final String DOWNLOAD_URL_KEY = "downurl_from_server";
	private static final String CHANGE_LOG_KEY = "changelog_from_server";

	public String mVername;
	public String mDownurl;
	public String mChangelog;
	private boolean mIsForceUpdate = false;

	private boolean mIsDownloading = false;
	private boolean mDownloadFinished = false;

	private Button btnUpdate;
	private TextView updateTitle;
	private TextView mUpdateHintTextview;
	private TextView mProgressText;
	private ProgressBar mProgressBar;

	private UpdateDialog mUpdateDialog;

	private String percent;

	private long downToken;

	public static void invoke(Context context, ZHUpgrade content, boolean isFromSplash) {
		if (content == null) {
			return;
		}
		Intent intent = new Intent(context, ActUpdateDialog.class);
		Bundle b = new Bundle();
		b.putString(VERSION_NAME_KEY, content.version);
		b.putString(DOWNLOAD_URL_KEY, content.url);
		b.putString(CHANGE_LOG_KEY, content.msg);
		if (content.isForce == ZHUpgrade.FORCE_UPDATE) {
			b.putBoolean(IS_FORCE_UPDATE_KEY, true);
		} else {
			b.putBoolean(IS_FORCE_UPDATE_KEY, false);
		}
		intent.putExtras(b);
        if (isFromSplash) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
		context.startActivity(intent);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);

		EventBus.getDefault().register(this);

		Intent intent = getIntent();

		Bundle b = intent.getExtras();
		mIsForceUpdate = b.getBoolean(IS_FORCE_UPDATE_KEY, false);
		mVername = b.getString(VERSION_NAME_KEY);
		mDownurl = b.getString(DOWNLOAD_URL_KEY);
		mChangelog = b.getString(CHANGE_LOG_KEY);

		percent = getString(R.string.download_percent);
		showUpdateDialog();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void showUpdateDialog() {
		mDownloadFinished = false;
		mIsDownloading = false;

		prepareForDialog();

		btnUpdate = (Button) mUpdateDialog.findViewById(R.id.update_btn);
		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String[] info = UpdateUtil
						.getClientUpdateApkPathAndVcode(ActUpdateDialog.this);
				String path = info[0];
				String oldVerName = info[1];
				if (mVername.equals(oldVerName)) {
					if (FileUtil.isExternalMediaMounted()) {
						File f = new File(path);
						if (f.exists()) {
							installApk(path);
						} else {
							UpdateUtil.setClientUpdateApkPathAndVcode(
									ActUpdateDialog.this, "", "0");
							startDownload();
						}
					}
				} else {
					startDownload();
				}
				mUpdateHintTextview.setVisibility(View.GONE);
				mProgressText.setVisibility(View.VISIBLE);
				mProgressText.setText(getString(R.string.download_waiting));
				mProgressBar.setVisibility(View.VISIBLE);
				btnUpdate.setVisibility(View.GONE);
				if (mUpdateDialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
					mUpdateDialog.getButton(AlertDialog.BUTTON_POSITIVE)
							.setVisibility(View.GONE);
				}

				if (mUpdateDialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
					mUpdateDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
							.setVisibility(View.GONE);
				}
			}
		});

		Button updateClose = (Button) mUpdateDialog
				.findViewById(R.id.update_close);
		updateClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mIsDownloading) {
					stopDownload();
				}
				if (mIsForceUpdate) {
					if (mProgressBar.getVisibility() != View.GONE) {
						resetDialog();
					} else {
						closeApp();
					}
				} else {
					dialogDismiss();
				}

			}
		});

		mUpdateDialog.setCanceledOnTouchOutside(false);
		mUpdateDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {

					if (!mIsForceUpdate) {
						if (mIsDownloading) {
							stopDownload();
						}

						dialogDismiss();
						return false;
					} else {
						return true;
					}
				}
				return false;
			}
		});

		if (mIsForceUpdate) {
			if (btnUpdate != null) {
				btnUpdate.setBackgroundResource(R.drawable.sel_bg_btn_borangered);
			}

			if (updateTitle != null) {
				updateTitle.setTextColor(Color.rgb(0x32, 0xa4, 0x31));
			}
		}
	}

	protected void installApk(String path) {
		UpdateUtil.setClientUpdateApkPathAndVcode(ActUpdateDialog.this,
				getDownloadPath(), mVername);
		UpdateUtil.installClientUpdateApk(ActUpdateDialog.this, path);
		if (mIsForceUpdate) {
			closeApp();
		} else {
			dialogDismiss();
		}
	}

	private void prepareForDialog() {
		mUpdateDialog = new UpdateDialog(this, R.style.UpdateDialog);

		mUpdateDialog.show();

		mProgressText = (TextView) mUpdateDialog.findViewById(R.id.progress);
		mProgressBar = (ProgressBar) mUpdateDialog
				.findViewById(R.id.progress_horizontal);
		mUpdateHintTextview = (TextView) mUpdateDialog
				.findViewById(R.id.updatehint);
		updateTitle = (TextView) mUpdateDialog.findViewById(R.id.txt_title);
		mProgressText.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);

		String s = getString(R.string.update_info_not_auto);
		s = String.format(s, mVername);
		if (mChangelog != null) {
			mUpdateHintTextview.setText(mChangelog);
		} else {
			mUpdateHintTextview.setText(s);
		}
	}

	private void resetDialog() {
		if (mUpdateDialog != null) {
			mUpdateHintTextview.setVisibility(View.VISIBLE);
			mProgressText.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.GONE);
			btnUpdate.setVisibility(View.VISIBLE);
			if (mIsForceUpdate) {
				if (btnUpdate != null) {
					btnUpdate.setBackgroundResource(R.drawable.sel_bg_btn_borangered);
				}
				if (updateTitle != null) {
					updateTitle.setTextColor(Color.rgb(0xd0, 0x13, 0x34));
				}
			} else {
				if (btnUpdate != null) {
					btnUpdate
							.setBackgroundResource(R.drawable.sel_bg_btn_bgreen);
				}
				if (updateTitle != null) {
					updateTitle.setTextColor(Color.rgb(0x32, 0xa4, 0x31));
				}
			}
		} else {
			showUpdateDialog();
		}
	}

	private void startDownload() {
		File apkDir = FileMgr.Instance().getDir(DirType.APK);
		FileUtil.clearDir(apkDir);
		downToken = ZHLoadManager.download(this, 0, mDownurl,
				getDownloadPath(), 0);
		mIsDownloading = true;
	}

	private String getDownloadPath() {
		return FileMgr.Instance().getFilepath(DirType.APK,
				"zhinfo_" + mVername + ".apk");
	}

	public void onEvent(HttpDownloadInfo info) {
		long totalSize = info.totalSize;
		int curPercent = 0;
		if (totalSize != 0) {
			curPercent = (int) (info.endIndex * 100 / info.totalSize);
		}
		switch (info.status) {
		case LoadStatus.LOADING:
			String s = String.format(percent, curPercent) + "%";
			mProgressText.setText(s);
			mProgressBar.setProgress(curPercent);
			break;
		case LoadStatus.PAUSE:
			resetDialog();
			break;
		case LoadStatus.FINISH:
			mDownloadFinished = true;
			installApk(getDownloadPath());
			break;
		case LoadStatus.ERROR:
			popupToast(getString(R.string.update_download_error));
			resetDialog();
			break;
		}

	}

	private void stopDownload() {
		mIsDownloading = false;
		if (mDownloadFinished) {
			UpdateUtil.setClientUpdateApkPathAndVcode(ActUpdateDialog.this,
					getDownloadPath(), mVername);
		} else {
			if (!TextUtils.isEmpty(getDownloadPath())) {
				ZHLoadManager.Instance().getHttpDownMgr()
						.stopByToken(downToken);
				try {
					File file = new File(getDownloadPath());
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void popupToast(final String hint) {
        showToast(hint);
	}

	private void closeApp() {
		HomeUtil.closeApp(ActUpdateDialog.this);
		dialogDismiss();
	}

	private void dialogDismiss() {
		if (mUpdateDialog != null) {
			mUpdateDialog.dismiss();
		}
		finish();
	}

}
