package com.zhisland.android.blog.profile.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleBtn;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleRunnable;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FragSelectTagsList extends FragBase {

	public static final String INTENT_KEY_TAG_ALL = "intent_key_tag_all";
	public static final String INTENT_KEY_TAG_SELECT = "intent_key_tag_select";
	public static final String INTENT_KEY_MAX_SELECT = "intent_key_max_select";
	public static final String INTENT_KEY_TITLE = "intent_key_title";
	public static final String INTENT_KEY_RETURN_LIST = "intent_key_return_list";
	private static final int TAG_ID_RIGHT = 101;

	private ListView listView;
	private ArrayList<ZHDicItem> tagsAll;
	private ArrayList<ZHDicItem> tagsSelected;
	private int maxSelect;
	private String title;
	private DataAdapter dataAdapter;
	
	static TitleRunnable titleRunnable = new TitleRunnable() {

		@Override
		protected void execute(Context context, Fragment fragment) {
			if (tagId == TAG_ID_RIGHT) {
				if (fragment != null && fragment instanceof FragSelectTagsList) {
					((FragSelectTagsList) fragment).saveTags();
				}
			}
		}
	};

	public static void invoke(Activity context, int reqCode,
			ArrayList<ZHDicItem> tagsAll, ArrayList<ZHDicItem> tagsSelected,
			String title, int maxSelect) {
		if (tagsAll == null || tagsAll.size() == 0 || maxSelect < 1) {
			return;
		}
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragSelectTagsList.class;
		if (title != null) {
			param.title = title;
		}
		param.enableBack = true;
		
		if(maxSelect > 1){
			param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
			param.runnable = titleRunnable;
			TitleBtn tb = new TitleBtn(TAG_ID_RIGHT,TitleBtn.TYPE_TXT);
			tb.btnText = "保存";
			tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.color_dc);
			param.titleBtns.add(tb);
		}
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INTENT_KEY_TAG_ALL, tagsAll);
		intent.putExtra(INTENT_KEY_MAX_SELECT, maxSelect);
		intent.putExtra(INTENT_KEY_TITLE, title);
		if (tagsSelected != null && tagsSelected.size() > 0) {
			intent.putExtra(INTENT_KEY_TAG_SELECT, tagsSelected);
		}
		context.startActivityForResult(intent, reqCode);
	}

	@Override
	public String getPageName() {
		return "FragSelectTagsList";
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		tagsAll = (ArrayList<ZHDicItem>) getActivity().getIntent()
				.getSerializableExtra(INTENT_KEY_TAG_ALL);
		maxSelect = getActivity().getIntent().getIntExtra(
				INTENT_KEY_MAX_SELECT, 1);
		Object titleObj = getActivity().getIntent().getStringExtra(
				INTENT_KEY_TITLE);
		if (titleObj != null && titleObj instanceof String) {
			title = (String) titleObj;
		}
		Object obj = getActivity().getIntent().getSerializableExtra(
				INTENT_KEY_TAG_SELECT);
		if (obj != null && obj instanceof ArrayList) {
			tagsSelected = (ArrayList<ZHDicItem>) obj;
		} else {
			tagsSelected = new ArrayList<ZHDicItem>();
		}
		listView = new ListView(getActivity());
		listView.setDivider(null);
		listView.setFastScrollEnabled(false);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setBackgroundColor(getResources().getColor(R.color.color_bg1));
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		listView.setLayoutParams(lp);
		listView.setVerticalScrollBarEnabled(false);
		dataAdapter = new DataAdapter();
		listView.setAdapter(dataAdapter);
		return listView;
	}

	private class DataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return tagsAll != null ? tagsAll.size() : 0;
		}

		@Override
		public ZHDicItem getItem(int position) {
			if (tagsAll != null && tagsAll.size() > position) {
				return tagsAll.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			DataHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.item_tag_list, null);
				holder = new DataHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (DataHolder) convertView.getTag();
			}

			holder.fill(getItem(position), position);
			return convertView;
		}
	}

	class DataHolder {
		ZHDicItem data;

		@InjectView(R.id.name)
		TextView name;
		@InjectView(R.id.checked)
		CheckBox checked;
		@InjectView(R.id.line)
		ImageView line;

		DataHolder(View view) {
			ButterKnife.inject(this, view);
			checked.setClickable(false);
			view.setOnClickListener(onClickListener);
		}

		public void fill(ZHDicItem d, int position) {
			data = d;
			name.setText(data.name);
			if (position == 0) {
				line.setVisibility(View.INVISIBLE);
			} else {
				line.setVisibility(View.VISIBLE);
			}
			boolean isChecked = false;

			for (ZHDicItem tag : tagsSelected) {
				if (tag.key.equals(data.key)) {
					isChecked = true;
				}
			}
			checked.setChecked(isChecked);
		}

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				ZHDicItem tagTem = null;
				for (ZHDicItem tag : tagsSelected) {
					if (tag.key.equals(data.key)) {
						tagTem = tag;
						break;
					}
				}
				if(tagTem != null){
					tagsSelected.remove(tagTem);
					checked.setChecked(false);
				}else{
					if(tagsSelected.size() < maxSelect){
						checked.setChecked(true);
						tagsSelected.add(data);
					}else{
						if(maxSelect == 1){
							tagsSelected.clear();
							tagsSelected.add(data);
							dataAdapter.notifyDataSetChanged();
						}
					}
				}
				
				if(maxSelect == 1){
					saveTags();
				}
			}
		};
	}

	public void saveTags() {
		if (tagsSelected.size() == 0) {
			if (!StringUtil.isNullOrEmpty(title)) {
                showToast("请选择" + title);
			}
			return;
		}
		Intent intent = new Intent();
		intent.putExtra(INTENT_KEY_RETURN_LIST, tagsSelected);
		getActivity().setResult(Activity.RESULT_OK, intent);
		getActivity().finish();
	}
}
