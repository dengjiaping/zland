package com.zhisland.android.blog.profile.controller.position;

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
 * 选择公司行业
 * <p>
 * 调起：SelectCompanyIndustry.launch(this, null, 123);
 * <p>
 * 回调：
 * <p>
 * <code>
 * public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 123:
			List<?> objs = (List<?>) data
					.getSerializableExtra(FragSelectActivity.INK_RESULT);
			if (objs != null) {
				Toast.makeText(this, "sada", Toast.LENGTH_LONG);
			}
		}
	}
 * </code>
 * 
 * @author 向飞
 * 
 */
public class SelectCompanyIndustry {

	private static final SelectDataListener CITY_SELECT_LISTENER = new SelectDataListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void loadNormal(Serializable tag, SelectCallback callback) {

			List<ZHDicItem> items = Dict.getInstance().getCompanyIndustry();
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
			if (dict1 != null && dict1.key != null && dict2 != null
					&& dict2.key != null) {
				return dict1.key.equals(dict2.key);
			}
			return false;
		}
	};

	public static void launch(Activity activity, List<ZHDicItem> selectedItem,
			int requestCode) {
		List<Serializable> list = SelectUtil.converTo(selectedItem);
		FragParam param = new FragParam(list);
		param.dataListener = CITY_SELECT_LISTENER;
		param.title = "公司行业";
		param.max = 1;
		Intent intent = new Intent(activity, FragSelectActivity.class);
		intent.putExtra(FragSelectActivity.INK_PARAM, param);
		activity.startActivityForResult(intent, requestCode);
	}

}
