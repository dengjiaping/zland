package com.zhisland.android.blog.profile.controller.honor;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.profile.dto.Honor;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;

import de.greenrobot.event.EventBus;

/**
 * 我的荣誉
 */
public class FragUserHonor extends FragBase {

	public static final String INTENT_KEY_HONOR_BLOCK = "intent_key_honor_block";

	public static void invoke(Context context, SimpleBlock<Honor> honorBlock) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragUserHonor.class;
		param.title = honorBlock.title;
		param.enableBack = true;
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INTENT_KEY_HONOR_BLOCK, honorBlock);
		context.startActivity(intent);
	}

	private ListView listView;

	HonorAdapter honorAdapter;

	@InjectView(R.id.ivUCHHead)
	ImageView ivUCHHead;

	@InjectView(R.id.tvUCHDesc)
	TextView tvUCHDesc;

	@InjectView(R.id.tvUCHBtnText)
	TextView tvUCHBtnText;

	EmptyView emptyView;

	SimpleBlock<Honor> honorBlock;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listView = new ListView(getActivity());
		listView.setDivider(null);
		listView.setFastScrollEnabled(false);
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		listView.setBackgroundColor(getResources().getColor(R.color.color_bg1));
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		listView.setLayoutParams(lp);
		listView.setVerticalScrollBarEnabled(false);
		listView.setPadding(DensityUtil.dip2px(15), 0, DensityUtil.dip2px(15),
				0);

		View headerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.user_common_head, null);
		listView.addHeaderView(headerView);

		setFootView();

		honorAdapter = new HonorAdapter();
		listView.setAdapter(honorAdapter);

		ButterKnife.inject(this, listView);
		EventBus.getDefault().register(this);
		initViews();
		return listView;
	}

	/**
	 * 设置footView，其中emptyView包含在footView中
	 * */
	private void setFootView() {
		LinearLayout footerView = new LinearLayout(getActivity());
		footerView.setGravity(Gravity.CENTER_HORIZONTAL);
		footerView.setPadding(0, 0, 0, DensityUtil.dip2px(30));
		ListView.LayoutParams lpFooter = new ListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		footerView.setLayoutParams(lpFooter);
		emptyView = new EmptyView(getActivity());
		emptyView.setImgRes(R.drawable.img_emptybox);
		emptyView.setPrompt("您还未添加荣誉");
		emptyView.setBtnVisibility(View.INVISIBLE);
		emptyView.setVisibility(View.GONE);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = DensityUtil.dip2px(100);
		footerView.addView(emptyView, lp);
		listView.addFooterView(footerView);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		honorBlock = (SimpleBlock<Honor>) getActivity().getIntent()
				.getSerializableExtra(INTENT_KEY_HONOR_BLOCK);
		honorAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	protected String getPageName() {
		return "ProfileMyHonor";
	}

	public void initViews() {
		ivUCHHead.setImageResource(R.drawable.img_profile_honor);
		tvUCHDesc.setText("你的荣誉将会提高你的信任度");
		tvUCHBtnText.setText("添加我的荣誉");
	}

	public void onEventMainThread(EBProfile eb) {
		switch (eb.getType()) {
		case EBProfile.TYPE_CHANGE_HONOR:
			getDataFromInternet();
		default:
			break;
		}
	}

	private void getDataFromInternet() {
		ZHApis.getProfileApi().getHonorList(getActivity(),
				new TaskCallback<ArrayList<Honor>>() {

					@Override
					public void onSuccess(ArrayList<Honor> content) {
						if (honorBlock.data == null) {
							honorBlock.data = new ArrayList<Honor>();
						} else {
							honorBlock.data.clear();
						}
						if (content != null) {
							honorBlock.data.addAll(content);
						}
						honorAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(Throwable error) {

					}
				});
	}

	@OnClick(R.id.btnUCH)
	void addClick() {
		FragHonorEdit.invoke(getActivity(), null);
	}

	private class HonorAdapter extends BaseAdapter {

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			if (getCount() == 0) {
				emptyView.setVisibility(View.VISIBLE);
			} else {
				emptyView.setVisibility(View.GONE);
			}
		}

		@Override
		public int getCount() {
			return honorBlock == null ? 0 : (honorBlock.data == null ? 0
					: honorBlock.data.size());
		}

		@Override
		public Honor getItem(int position) {
			if (honorBlock != null && honorBlock.data != null
					&& honorBlock.data.size() > position) {
				return honorBlock.data.get(position);
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
			UserHonorCell holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_honor, null);
				holder = new UserHonorCell(convertView, getActivity());
				convertView.setTag(holder);
			} else {
				holder = (UserHonorCell) convertView.getTag();
			}

			holder.fill(getItem(position), true);
			return convertView;
		}
	}

}