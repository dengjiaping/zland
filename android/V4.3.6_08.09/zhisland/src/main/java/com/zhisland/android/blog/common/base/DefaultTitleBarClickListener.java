package com.zhisland.android.blog.common.base;

import android.app.Activity;
import android.view.View;

import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.title.OnTitleClickListner;
import com.zhisland.lib.view.title.TitleBar;

/**
 * 标题栏默认点击监听器
 * 
 * @author 向飞
 *
 */
public class DefaultTitleBarClickListener implements OnTitleClickListner{

	private Activity activity;

	public DefaultTitleBarClickListener(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 点击回退
	 */
	public void onBack() {
		activity.onBackPressed();
	}

	/**
	 * 长按标题
	 */
	public void onTitle() {
		HomeUtil.NavToHome(activity);
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		try {
			switch (tagId) {
			case TitleBarProxy.TAG_BACK:
				onBack();
				break;
			case TitleBar.TAG_TITLE_TXT: {
				onTitle();
				break;
			}
			default:
				break;
			}
		} catch (final Throwable e) {
			MLog.e("FragBaseActivity", "exception happened", e);
		}
		
	}

}