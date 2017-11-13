package com.zhisland.android.blog.profile.controller.drip;

import android.app.Activity;
import android.content.Intent;

import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.view.select.FragParam;
import com.zhisland.android.blog.common.view.select.FragSelect.SelectCallback;
import com.zhisland.android.blog.common.view.select.FragSelectActivity;
import com.zhisland.android.blog.common.view.select.SelectDataListener;
import com.zhisland.android.blog.common.view.select.SelectUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择个人行业
 */
public class SelectProfileIndustry {
	private static final SelectDataListener CITY_SELECT_LISTENER = new SelectDataListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void loadNormal(Serializable tag, SelectCallback callback) {
			List<ZHDicItem> items = Dict.getInstance().getProfileIndustry();
			ArrayList<Serializable> data = SelectUtil.converTo(items);
			callback.onLoadSuccessFully(data);
		}

		@Override
		public String convertToString(Serializable item) {
			ZHDicItem dict = (ZHDicItem) item;
			return dict.name;
		}

		@Override
		public boolean isEqual(Serializable s1, Serializable s2) {
			ZHDicItem dict1 = (ZHDicItem) s1;
			ZHDicItem dict2 = (ZHDicItem) s2;
			return dict1.key.equals(dict2.key);
		}
	};

	public static void launch(Activity activity, Serializable selectedItem,
			int requestCode) {
		FragParam param = new FragParam(null);
		param.dataListener = CITY_SELECT_LISTENER;
		param.title = "行业选择";
		param.max = 1;
		Intent intent = new Intent(activity, FragSelectActivity.class);
		intent.putExtra(FragSelectActivity.INK_PARAM, param);
		activity.startActivityForResult(intent, requestCode);
	}
}
