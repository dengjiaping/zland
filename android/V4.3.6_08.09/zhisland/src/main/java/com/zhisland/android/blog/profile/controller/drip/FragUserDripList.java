package com.zhisland.android.blog.profile.controller.drip;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.android.blog.common.view.select.FragSelectActivity;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import de.greenrobot.event.EventBus;

/**
 * 我的点滴
 */
@SuppressLint("InflateParams")
public class FragUserDripList extends FragBase {

	public static final int DRIP_REQ_CODE = 1;

	private static final String PAGE_NAME = "ProfileMyDrops";

	@InjectView(R.id.llDripContainer)
	LinearLayout llLinearLayout;

	// 展示的数据
	private List<CustomDict> dicts;

	private AProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.user_drip, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rootView.setLayoutParams(lp);
		ButterKnife.inject(this, rootView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	/**
	 * 填充数据
	 */
	@SuppressWarnings("unchecked")
	private void initData() {
		dicts = (List<CustomDict>) getActivity().getIntent()
				.getSerializableExtra(ActUserDripList.INK_DRIP_DATAS);
		fillView();

		boolean getDripTask = PrefUtil.Instance().isGetDripTask();
		if (!getDripTask) {
			getDefaultDrips();
		}
	}

	private void getDefaultDrips() {
		ZHApis.getProfileApi().getDefaultDrips(getActivity(),
				new TaskCallback<List<CustomDict>>() {

					@Override
					public void onSuccess(List<CustomDict> content) {
						if (content != null) {
							FragUserDripList.this.dicts = content;
							fillView();
						}
					}

					@Override
					public void onFailure(Throwable error) {
						ToastUtil.showShort(error.getMessage());
					}
				});
	}

	/**
	 * 填充view
	 */
	private void fillView() {
		if (getActivity() == null || dicts == null) {
			return;
		}
		llLinearLayout.removeAllViews();
		for (CustomDict dict : dicts) {
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.item_drip, null);
			UserDripCellBase holder = new UserDripEditCell(view, getActivity());
			holder.fill(dict);
			llLinearLayout.addView(view);
		}
	}

	/**
	 * 保存我的点滴
	 */
	public void saveDrip() {
		SoftInputUtil.hideInput(getActivity());
		if (!checkMust()) {
			return;
		}

		if (progressDialog == null) {
			progressDialog = new AProgressDialog(getActivity());
		}
		progressDialog.show();

		ZHApis.getProfileApi().updateDrip(getActivity(), getHasValueDrips(),
				new TaskCallback<Object>() {

					@Override
					public void onSuccess(Object content) {
						getActivity().finish();
					}

					@Override
					public void onFailure(Throwable error) {

					}

					@Override
					public void onFinish() {
						super.onFinish();
						if (progressDialog != null) {
							progressDialog.dismiss();
						}
					}
				});
	}

	/**
	 * 检测必填项不能为空
	 */
	private boolean checkMust() {
		boolean result = true;
		for (CustomDict dict : dicts) {
			if (dict.isMust() && StringUtil.isNullOrEmpty(dict.value)) {
				ToastUtil.showShort(dict.name + "不能为空！");
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * 请求接口时 如果value为空则删除该选项,只留有数据的Drip
	 */
	private List<CustomDict> getHasValueDrips() {
		List<CustomDict> result = new ArrayList<CustomDict>();
		result.addAll(dicts);
		Iterator<CustomDict> iterator = result.iterator();
		while (iterator.hasNext()) {
			CustomDict dict = iterator.next();
			// 不可编辑的字典不能反传服务器
			if (!dict.isCanEdit()) {
				iterator.remove();
			}
		}
		return result;
	}

	@OnClick(R.id.tvAddDrip)
	public void onClickAddDrip() {
		SelectDrips.launch(getActivity(), DRIP_REQ_CODE, dicts);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && data != null) {
			switch (requestCode) {
			case DRIP_REQ_CODE:
				List<CustomDict> selectedDicts = (List<CustomDict>) data
						.getSerializableExtra(FragSelectActivity.INK_RESULT);
				// 重新拼装我的点滴 固定的drips加上选择的drips
				if (selectedDicts != null) {
					dicts.clear();
					dicts.addAll(selectedDicts);
				}
				fillView();
				break;
			case UserDripEditCell.REQ_INDUSTRY:
				List<ZHDicItem> list = (List<ZHDicItem>) data
						.getSerializableExtra(FragSelectActivity.INK_RESULT);
				if (list != null && list.size() > 0) {
					ZHDicItem item = list.get(0);
					for (CustomDict dict : dicts) {
						if (dict.alias != null
								&& StringUtil.isEquals(dict.alias,
										CustomDict.ALIAS_INDUSTRY)) {
							dict.value = item.key;
						}
					}
					fillView();
				}
				break;
			}
		}
	}

	@Override
	protected String getPageName() {
		return PAGE_NAME;
	}

}
