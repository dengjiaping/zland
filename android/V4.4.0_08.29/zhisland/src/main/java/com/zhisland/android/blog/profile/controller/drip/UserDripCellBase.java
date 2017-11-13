package com.zhisland.android.blog.profile.controller.drip;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.view.CityPickDlg;
import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;

/**
 * 用户点滴 cell
 */
public abstract class UserDripCellBase {

	protected LinearLayout llDripRoot;
	protected TextView tvDripTitle;
	protected EditText etDripContent;
	protected TextView tvDripContent;
	protected TextView tvDripRight;
	protected View dripViewLine;

	protected Activity context;
	protected CustomDict dict;
	/**
	 * 城市选择
	 */
	protected CityPickDlg cityPickDlg;

	public UserDripCellBase(View view, final Activity context) {
		this.context = context;
		initBaseView(view);
	}

	private void initBaseView(View view) {
		llDripRoot = (LinearLayout) view.findViewById(R.id.llDripRoot);
		tvDripTitle = (TextView) view.findViewById(R.id.tvDripTitle);
		etDripContent = (EditText) view.findViewById(R.id.etDripContent);
		tvDripContent = (TextView) view.findViewById(R.id.tvDripContent);
		tvDripRight = (TextView) view.findViewById(R.id.tvDripRight);
		dripViewLine = view.findViewById(R.id.dripViewLine);

		initChildView();
	}

	public void fill(CustomDict dict) {
		if (dict == null) {
			return;
		}
		this.dict = dict;
		tvDripTitle.setText(dict.name);

		fillData();

		if (StringUtil.isEquals(dict.alias, CustomDict.ALIAS_CITY)) {
			etDripContent.setVisibility(View.GONE);
			tvDripContent.setVisibility(View.VISIBLE);
			setCityTxt();
		} else if (StringUtil.isEquals(dict.alias, CustomDict.ALIAS_INDUSTRY)) {
			etDripContent.setVisibility(View.GONE);
			tvDripContent.setVisibility(View.VISIBLE);
			setIndustryTxt();
		}
	}

	protected abstract void initChildView();

	protected abstract void fillData();

	/**
	 * 设置行业
	 */
	private void setIndustryTxt() {
		ArrayList<ZHDicItem> items = Dict.getInstance().getProfileIndustry();
		if (items != null) {
			for (ZHDicItem item : items) {
				if (StringUtil.isEquals(item.key, dict.value)) {
					tvDripContent.setText(item.name);
				}
			}
		}
	}

	/**
	 * 根据event将城市设置到View上
	 * */
	private void setCityTxt() {
		String provinceName = "";
		String cityName = "";
		int proviceId = dict.getProvinceId();
		int cityId = dict.getCityId();
		if (proviceId > 0 && cityId > 0) {
			if (cityPickDlg == null) {
				cityPickDlg = new CityPickDlg(context,cityCallBack,"请选择您的" + dict.name);
			}
			cityPickDlg.setCity(proviceId, cityId);
			cityName = cityPickDlg.getCityName();
			provinceName = cityPickDlg.getProvinceName();
		} else {
			return;
		}
		if (provinceName.equals(cityName)) {
			tvDripContent.setText(provinceName);
		} else {
			tvDripContent.setText(provinceName + " " + cityName);
		}
	}

	CityPickDlg.CallBack cityCallBack = new CityPickDlg.CallBack() {

		@Override
		public void OkClick(int provinceId, String provinceName, int cityId, String cityName) {
			dict.setDictCity(provinceId, cityId);
			setCityTxt();
		}
	};

}