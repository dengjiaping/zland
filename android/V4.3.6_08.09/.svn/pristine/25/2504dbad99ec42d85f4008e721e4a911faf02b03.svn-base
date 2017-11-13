package com.zhisland.android.blog.profile.controller.drip;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;

import com.zhisland.android.blog.profile.dto.CustomDict;
import com.zhisland.android.blog.profile.api.TaskGetDripList;
import com.zhisland.android.blog.common.view.select.FragParam;
import com.zhisland.android.blog.common.view.select.FragSelect.SelectCallback;
import com.zhisland.android.blog.common.view.select.FragSelectActivity;
import com.zhisland.android.blog.common.view.select.SelectDataListener;
import com.zhisland.android.blog.common.view.select.SelectUtil;
import com.zhisland.lib.async.http.task.TaskCallback;

/**
 * 选择要添加的水滴
 * <p>
 * 调起：SelectDrips.launch(this, 123);
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
			//do somethings
		}
	}
 * </code>
 * 
 * @author 向飞
 * 
 */
public class SelectDrips {

	private static final SelectDataListener DRIP_LISTENER = new SelectDataListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void loadNormal(Serializable tag, final SelectCallback callback) {

			TaskGetDripList task = new TaskGetDripList(null,
					new TaskCallback<List<CustomDict>>() {

						@Override
						public void onSuccess(List<CustomDict> content) {
							List<Serializable> results = null;
							if (content != null) {
								results = SelectUtil.converTo(content);
							}
							callback.onLoadSuccessFully(results);
						}

						@Override
						public void onFailure(Throwable error) {
							callback.onLoadFailture(null);
						}
					});
			task.execute();
		}

		@Override
		public String convertToString(Serializable item) {
			CustomDict dict = (CustomDict) item;
			return dict.name;
		}

		@Override
		public boolean isEqual(Serializable s1, Serializable s2) {
			CustomDict dict1 = (CustomDict) s1;
			CustomDict dict2 = (CustomDict) s2;
			if (dict1 != null && dict1.name != null && dict2 != null) {
				return dict1.name.equals(dict2.name);
			}
			return false;
		}
	};

	/**
	 * 调起点滴选择器
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void launch(Activity activity, int requestCode,
			List<CustomDict> selectedItes) {

		List<Serializable> ss = SelectUtil.converTo(selectedItes);

		FragParam param = new FragParam(ss);
		param.dataListener = DRIP_LISTENER;
		param.title = "添加点滴";
		param.max = 10000;
		param.showFilterBar = false;
		Intent intent = new Intent(activity, FragSelectActivity.class);
		intent.putExtra(FragSelectActivity.INK_PARAM, param);
		activity.startActivityForResult(intent, requestCode);
	}

}
