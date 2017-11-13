package com.zhisland.android.blog.profile.controller.resource;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的供求 cell
 */
public class UserResourceCell {

	private Context context;

	@InjectView(R.id.tflTag)
	TagFlowLayout tflTag;

	@InjectView(R.id.ivResourceEdit)
	ImageView ivResourceEdit;

	@InjectView(R.id.tvContent)
	TextView tvContent;

	Resource resource;

	private List<String> tags = new ArrayList<String>();

	public UserResourceCell(View view, Context context) {
		ButterKnife.inject(this, view);
		tflTag.setAdapter(selectAdapter);
		this.context = context;
	}

	public void fill(Resource resource, boolean showEdit) {
		this.resource = resource;
		tags.clear();
		if (resource != null) {
			tvContent.setText(resource.content == null ? "" : resource.content);
			setTagStringToList(resource.getIndustryName(), tags);
			setTagStringToList(resource.getCategoryName(), tags);
		} else {
			tvContent.setText("");
		}
		selectAdapter.notifyDataChanged();
		ivResourceEdit.setVisibility(showEdit ? View.VISIBLE : View.GONE);
	}

	@OnClick(R.id.ivResourceEdit)
	void onEditClick() {
		if (resource.type == null) {
			return;
		}
		if (resource.type == Resource.TYPE_DEMAND) {
			FragDemandEdit.invoke(context, resource);
		} else {
			FragSupplyEdit.invoke(context, resource);
		}
	}

	private void setTagStringToList(String tag, List<String> tags) {
		if (!StringUtil.isNullOrEmpty(tag) && tags != null) {
			tags.add(tag.trim());
		}
	}

	TagAdapter selectAdapter = new TagAdapter<String>(tags) {
		@Override
		public View getView(
				com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
				int position, String tag) {
			TextView textView = (TextView) LayoutInflater.from(context)
					.inflate(R.layout.tag_text, null);
			MarginLayoutParams params = new MarginLayoutParams(
					MarginLayoutParams.WRAP_CONTENT,
					MarginLayoutParams.WRAP_CONTENT);
			textView.setTextColor(context.getResources().getColor(
					R.color.color_f2));
			DensityUtil.setTextSize(textView, R.dimen.txt_12);
			textView.setBackgroundResource(R.drawable.rect_sf2_clarge);
			int dpi10 = DensityUtil.dip2px(8);
			int dpi5 = DensityUtil.dip2px(2);
			params.rightMargin = dpi10;
			params.topMargin = dpi10;
			textView.setPadding(dpi10, dpi5, dpi10, dpi5);
			textView.setLayoutParams(params);
			textView.setText(tag);
			return textView;
		}
	};
}
