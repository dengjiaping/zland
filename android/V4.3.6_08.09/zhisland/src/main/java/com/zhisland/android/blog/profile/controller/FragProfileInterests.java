package com.zhisland.android.blog.profile.controller;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.api.TaskUpdateUser;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.util.LoadDataFromAssert;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.flowlayout.FlowLayout;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout.OnTagClickListener;
import com.zhisland.android.blog.common.util.MobileUtil;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * 兴趣爱好
 */
public class FragProfileInterests extends FragBase {

	private static final int MAX_SEL_SIZE = 3;

	@InjectView(R.id.tvTitle)
	public TextView tvTitle;

	@InjectView(R.id.tvDesc)
	public TextView tvDesc;

	@InjectView(R.id.tvErrorPrompt)
	public TextView tvErrorPrompt;

	@InjectView(R.id.id_flowlayout)
	public TagFlowLayout flowLayout;

	@InjectView(R.id.btnNext)
	public Button btnNext;

	private List<String> tags = new ArrayList<String>();
	/**
	 * 已选标签
	 */
	private List<String> selTags = new ArrayList<String>();

	private TagAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_profile_interests, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initViews();
		return root;
	}

	/**
	 * 获取已选择tags
	 */
	public List<String> getSelTags() {
		return selTags;
	}

	@Override
	protected String getPageName() {
		return "fragProfileInterests";
	}

	@OnClick(R.id.btnNext)
	public void onClickNext() {
		((ActSelTags) getActivity()).showFragSpecialy();
	}

	public void initViews() {
		tags.clear();
		tags.addAll(LoadDataFromAssert.getInstance().loadInterestTags());
		addTags();

		tvTitle.setVisibility(View.GONE);
		tvDesc.setVisibility(View.GONE);
		tvErrorPrompt.setVisibility(View.INVISIBLE);
		btnNext.setVisibility(View.GONE);
		fillUser();
	}

	private void fillUser() {
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		List<String> interestTags = user.getInterestTags();
		if (interestTags.size() > 0) {
			// 设置默认值
			selTags.addAll(interestTags);
			for (String tag : selTags) {
				for (int i = 0; i < tags.size(); i++) {
					if (tags.get(i).equals(tag)) {
						tags.set(i, tag);
						adapter.setSelectedList(i);
					}
				}
			}
		}
	}

	/**
	 * 添加tags
	 */
	private void addTags() {
		flowLayout.setMaxSelectCount(MAX_SEL_SIZE);
		flowLayout.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent) {
				String tag = tags.get(position);
				if (selTags.size() < MAX_SEL_SIZE) {
					tvErrorPrompt.setVisibility(View.INVISIBLE);
					if (selTags.contains(tag)) {
						selTags.remove(tag);
					} else {
						selTags.add(tag);
					}
				} else {
					if (selTags.contains(tag)) {
						tvErrorPrompt.setVisibility(View.INVISIBLE);
						selTags.remove(tag);
					} else {
						tvErrorPrompt.setVisibility(View.VISIBLE);
					}
				}
				return false;
			}
		});

		adapter = new TagAdapter<String>(tags) {
			@Override
			public View getView(
					com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
					int position, String tag) {
				TextView textView = (TextView) LayoutInflater.from(
						getActivity()).inflate(R.layout.tag_text, null);
				textView.setBackgroundResource(R.drawable.bg_tag_gold);
				int tagHeight = getResources().getDimensionPixelSize(
						R.dimen.tag_height);
				MarginLayoutParams params = new MarginLayoutParams(
						MarginLayoutParams.WRAP_CONTENT, tagHeight);
				params.rightMargin = DensityUtil.dip2px(10);
				params.topMargin = DensityUtil.dip2px(10);
				textView.setPadding(DensityUtil.dip2px(15), 0,
						DensityUtil.dip2px(15), 0);
				DensityUtil.setTextSize(textView, R.dimen.txt_12);
				textView.setLayoutParams(params);
				textView.setText(tag);
				return textView;
			}
		};

		flowLayout.setAdapter(adapter);
	}

	public void saveIntroduction() {
		String interests = MobileUtil.listToString(getSelTags());
		User user = new User();
		user.uid = PrefUtil.Instance().getUserId();
		user.interests = interests == null ? "" : interests;
		ZHApis.getUserApi().updateUser(getActivity(), user, TaskUpdateUser.TYPE_UPDATE_OTHER,
				new TaskCallback<Object>() {

					@Override
					public void onSuccess(Object content) {
						ToastUtil.showShort("兴趣爱好修改成功");
					}

					@Override
					public void onFailure(Throwable error) {
					}
				});
		getActivity().finish();
	}
}