package com.zhisland.android.blog.common.view.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.StringUtil;

/**
 * 通用选择器，使用如下
 * 
 * @author 向飞
 *
 */
public class FragSelectActivity extends FragBaseActivity implements NavListener {

	public static final String INK_PARAM = "select_param";// 起始选择器参数
	public static final String INK_RESULT = "result_select";// 选择结果
	private static final int TAG_OK = 199;

	private FragmentManager fm;
	// 多级选择栈
	private Stack<Pair<FragParam, FragSelect>> frags = new Stack<Pair<FragParam, FragSelect>>();
	// 起始选择器参数
	private FragParam startParam;

	/**
	 * 启动选择器
	 * 
	 * @param activity
	 *            启动context
	 * @param dataListener
	 *            选择器名称
	 * @param title
	 *            选择器数据接口
	 * @param requestCode
	 *            返回值
	 */
	public static void launch(Activity activity,
			SelectDataListener dataListener, String title, int requestCode) {
		FragParam param = new FragParam(null);
		param.dataListener = dataListener;
		param.title = title;
		Intent intent = new Intent(activity, FragSelectActivity.class);
		intent.putExtra(FragSelectActivity.INK_PARAM, param);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	public boolean shouldContinueCreate(Bundle savedInstanceState) {
		startParam = (FragParam) getIntent().getSerializableExtra(INK_PARAM);
		if (startParam == null) {
			// 其实参数为空时，自动返回
			return false;
		}

		return true;
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);

		TextView okBtn = TitleCreator.Instance().createTextButton(this, "确定",
				R.color.color_dc);
		getTitleBar().addRightTitleButton(okBtn, TAG_OK);
		getTitleBar().hideTitleButton(TAG_OK);

		getTitleBar().addBackButton();
		getTitleBar().setTitle(startParam.title);

		fm = getFragmentManager();
		showParam(startParam, null, startParam.title, false);
	}

	@Override
	public void onTitleClicked(View view, int tagId) {
		switch (tagId) {
		case TitleBarProxy.TAG_BACK: {
			this.onBackPressed();
			break;
		}
		case TAG_OK: {
			Pair<FragParam, FragSelect> lastPair = frags.lastElement();
			FragParam curParam = lastPair.first;
			finishSelect(curParam.getAllSelectedItems());
			break;
		}
		default: {
			super.onTitleClicked(view, tagId);
			break;
		}
		}

	}

	@Override
	public void onBackPressed() {
		if (frags.size() <= 1) {
			super.onBackPressed();
		} else {
			popFrag();
		}
	}

	/**
	 * 弹出一级选择器
	 */
	private void popFrag() {
		Pair<FragParam, FragSelect> hidePair = frags.pop();
		Pair<FragParam, FragSelect> showPair = frags.lastElement();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out);
		ft.remove(hidePair.second);
		ft.show(showPair.second);
		ft.commit();
		hidePair.first.reset();
		setTitle(showPair.first.title);
	}

	@Override
	public boolean onSelected(Serializable item) {

		Pair<FragParam, FragSelect> lastPair = frags.lastElement();
		FragParam curParam = lastPair.first;

		boolean result = true;
		if (curParam.nextFrag != null) {
			String title = curParam.nextFrag.title;
			if (item != null && StringUtil.isNullOrEmpty(title)) {
				title = curParam.dataListener.convertToString(item);
			}
			showParam(curParam.nextFrag, item, title, true);
			result = false;
		} else {
			if (curParam.max <= 1) {
				// 单选时，选中任何一条数据都会自动结束选择并返回
				List<Serializable> selecteds = new ArrayList<Serializable>();
				selecteds.add(item);
				finishSelect(selecteds);
				result = false;
			} else {
				// 允许多选
				Serializable matched = SelectUtil.findEqualObj(curParam.getAllSelectedItems(), item,
						curParam.dataListener);
				if (matched!=null
						|| curParam.getAllSelectedItems().size() < curParam.max) {
					// 当取消选中或者还未达到选择上限时
					curParam.select(item);
					if (curParam.getAllSelectedItems().size() > 0) {
						getTitleBar().showTitleButton(TAG_OK);
					} else {
						getTitleBar().hideTitleButton(TAG_OK);
					}
				} else {
					// TODO 提示最多只能选择几项
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * 结束选择
	 * 
	 * @param items
	 *            最终选中的数据
	 */
	private void finishSelect(List<Serializable> items) {

		Intent intent = null;
		if (items != null) {
			intent = new Intent();
			intent.putExtra(INK_RESULT, (Serializable) items);
		}
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * 展示一个选择器
	 * 
	 * @param param
	 *            要展示的选择器参数
	 * @param parentArg
	 *            父级参数
	 * @param title
	 *            当前选择步骤名称
	 * @param showAnim
	 *            是否启用动画
	 */
	private void showParam(FragParam param, Serializable parentArg,
			String title, boolean showAnim) {
		setTitle(title);

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		FragSelect frag = new FragSelect();
		frag.set(param.dataListener, this, parentArg,
				param.max);
		frag.setSelectedItems(param.getAllSelectedItems());
		if (param.nextFrag == null) {
			frag.hideArrow();
		}
		if (!param.showFilterBar) {
			frag.hideFilter();
		}

		if (showAnim) {
			ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
		}

		ft.add(R.id.frag_container, frag);

		if (!frags.isEmpty()) {
			Pair<FragParam, FragSelect> lastPair = frags.lastElement();
			ft.hide(lastPair.second);
		}

		ft.commit();
		frags.add(new Pair<FragParam, FragSelect>(param, frag));

	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_LAYOUT;
	}

}
