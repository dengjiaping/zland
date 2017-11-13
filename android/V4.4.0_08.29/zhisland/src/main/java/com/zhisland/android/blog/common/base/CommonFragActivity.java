package com.zhisland.android.blog.common.base;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.AnimUtils;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.component.frag.FragBase;

/**
 * this class is hold for commen frag launch
 * 此通用Activity默认不会主动弹出键盘，如有需要，就在attach()里加上弹出代码
 */
public class CommonFragActivity extends FragBaseActivity {

	private static final String PARAM = "common_frag_param";
	private static final String FRAG_TAG_CURFRAG = "frag_tag_curFrag";
	public static String INCOME_FRAG_NAME;
	CommonFragParams param;
	private Fragment curFrag = null;
	private int titleType = TitleType.TITLE_LAYOUT;

	public static void invoke(Context context, CommonFragParams param) {
		Intent intent = new Intent(context, CommonFragActivity.class);
		intent.putExtra(PARAM, param);
		INCOME_FRAG_NAME = param.clsFrag.getName();
		context.startActivity(intent);
	}

	public static Intent createIntent(Context context, CommonFragParams param) {
		Intent intent = new Intent(context, CommonFragActivity.class);
		intent.putExtra(PARAM, param);
		INCOME_FRAG_NAME = param.clsFrag.getName();
		return intent;
	}

	@Override
	public boolean shouldContinueCreate(Bundle savedInstanceState) {
		param = (CommonFragParams) getIntent().getSerializableExtra(PARAM);
		if (param == null || !param.isValid()) {
			return false;
		}
		//titleType的赋值需要在super.onContinueCreate(savedInstanceState);方法前执行。
		if (param.noTitle) {
			titleType = TitleType.TITLE_NONE;
		}
		if (!param.swipeBackEnable) {
			setSwipeBackEnable(false);
		}
		return super.shouldContinueCreate(savedInstanceState);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);
		FragmentManager fragmentManager = getFragmentManager();
		//获取curFrag，如果被回收后系统自动创建则能够取到。
		curFrag = fragmentManager.findFragmentByTag(FRAG_TAG_CURFRAG);
		if (curFrag == null) {
			try {
				curFrag = (Fragment) param.clsFrag.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (curFrag == null) {
				finish();
				return;
			}
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.add(R.id.frag_container, curFrag, FRAG_TAG_CURFRAG);
			ft.commit();
		}

		if (titleType == TitleType.TITLE_LAYOUT) {
			getTitleBar().setTitle(param.title);
			if (param.enableBack) {
                if (param.btnBackResID > 0){
                    getTitleBar().addBackButton(param.btnBackResID);
                }else {
                    getTitleBar().addBackButton();
                }
			}
            if (param.hideBottomLine){
                getTitleBar().hideBottomLine();
            }
            if (param.titleBackground > 0){
                getTitleBar().setTitleBackground(param.titleBackground);
            }
            if (param.titleColor > 0){
                getTitleBar().setTitleColor(param.titleColor);
            }

			if (param.titleBtns != null) {
				param.runnable.setContext(this);
				param.runnable.setFragment(curFrag);
				for (TitleBtn btn : param.titleBtns) {
					View v = null;
					if (btn.type == TitleBtn.TYPE_TXT) {
						TextView vRight = TitleCreator.Instance()
								.createTextButton(this, btn.btnText);
						if (btn.textColor != null) {
							vRight.setTextColor(btn.textColor);
						}
						v = vRight;
					} else {
						ImageView vRight = TitleCreator.Instance()
								.createImageButton(this, btn.imgResId);
						v = vRight;
					}
					if (btn.isLeft) {
						getTitleBar().addLeftTitleButton(v, btn.tagId);
					} else {
						getTitleBar().addRightTitleButton(v, btn.tagId);
					}

				}
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (curFrag != null) {
			curFrag.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		boolean clicked = false;
		if (param.titleBtns != null) {
			for (TitleBtn btn : param.titleBtns) {
				if (btn.tagId == tagId) {
					param.runnable.tagId = tagId;
					handler.post(param.runnable);
					clicked = true;
				}
			}
		}
		if (!clicked) {
			super.onTitleClicked(view, tagId);
		}
	}

	@Override
	protected int titleType() {
		return titleType;
	}

	// =================

	@Override
	protected void onPause() {
		super.onPause();
		if (this.isFinishing()) {
			AnimUtils.configAnim(this, param.clsFrag.getName(), false);
		}
	}

	// ======================

	/**
	 * the info to launch CommonFragActivity
	 *
	 */
	public static final class CommonFragParams implements Serializable {

		private static final long serialVersionUID = 1L;
		public Class<?> clsFrag;
		public String title;
        // 标题背景图 resId
        public int titleBackground;
		public boolean enableBack;
        public boolean hideBottomLine;
		public List<TitleBtn> titleBtns;
		public TitleRunnable runnable;
		public boolean noTitle = false;
		public boolean swipeBackEnable = true;
        // 标题颜色
        public int titleColor;
        // back按钮图片 resId
        public int btnBackResID;


        public boolean isValid() {
			return clsFrag != null;
		}

	}

	public static final class TitleBtn implements Serializable {

		private static final long serialVersionUID = 1L;

		public static final int TYPE_TXT = 0;
		public static final int TYPE_IMG = 1;

		public TitleBtn(int tagId, int type) {
			this.tagId = tagId;
			this.type = type;
		}

		public int tagId;
		public boolean isLeft;
		public int type;
		public int imgResId;
		public String btnText;
		public Integer textColor = null;
	}

	public static abstract class TitleRunnable implements Runnable,
			Serializable {

		private static final long serialVersionUID = 1L;
		private Context context;
		private Fragment fragment;
		protected int tagId;

		private void setContext(Context context) {
			this.context = context;
		}

		private void setFragment(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void run() {
			execute(context, fragment);
		}

		protected abstract void execute(Context context, Fragment fragment);
	}

	@Override
	public void onBackPressed() {
		if (curFrag instanceof FragBase) {
			if (((FragBase) curFrag).onBackPressed()) {
				return;
			}
		}
		super.onBackPressed();
	}

}
