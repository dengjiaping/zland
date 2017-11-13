package com.zhisland.android.blog.profile.controller;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.api.TaskUpdateUser;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.flowlayout.FlowLayout;
import com.zhisland.android.blog.common.view.flowlayout.TagAdapter;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout;
import com.zhisland.android.blog.common.view.flowlayout.TagFlowLayout.OnTagClickListener;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.MobileUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * 愿意结识
 */
public class FragProfileWantFriend extends FragBase {

	@InjectView(R.id.tvTitle)
	public TextView tvTitle;

	@InjectView(R.id.tvDesc)
	public TextView tvDesc;

	@InjectView(R.id.id_flowlayout)
	public TagFlowLayout flowLayout;

	@InjectView(R.id.btnNext)
	public Button btnNext;

	private List<String> tags = new ArrayList<String>();

	private TagAdapter<String> adapter;

	private CommonDialog addDialog;
	private CommonDialog delDialog;

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
		List<String> tmpTags = new ArrayList<String>();
		tmpTags.addAll(tags);
		tmpTags.remove(tmpTags.size() - 1);
		return tmpTags;
	}

	@Override
	protected String getPageName() {
		return "fragProfileWantFriend";
	}

	@OnClick(R.id.btnNext)
	public void onClickNext() {
		((ActSelTags) getActivity()).updateUser();
	}

	public void initViews() {
		tvTitle.setVisibility(View.GONE);
		tvDesc.setVisibility(View.GONE);
		btnNext.setVisibility(View.GONE);
		fillUser();

		initTags();
		refreshTags();
	}

	private void fillUser() {
		User user = DBMgr.getMgr().getUserDao().getSelfUser();
		List<String> wantFriendsTags = user.getWantFriendsTags();
		if (user != null && wantFriendsTags.size() > 0) {
			tags.addAll(wantFriendsTags);
		}
	}

	private void initTags() {
		tags.add("添加");
	}

	private void showAddDialog() {
		if (addDialog == null)
			addDialog = new CommonDialog(getActivity(),
					CommonDialog.SOFT_KEY_UP);
		if (!addDialog.isShowing()) {
			addDialog.show();
			addDialog.setTitle("请输入标签名称");
			addDialog.setEditHint("输入标签。。。");
			addDialog.setMaxEditCount(24);
			addDialog.clearEditText();
			addDialog.tvDlgConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String editTextContent = addDialog.getEditTextContent();
					if (StringUtil.isNullOrEmpty(editTextContent)) {
						ToastUtil.showShort("标签名字不能为空");
						return;
					}
					for (int i = 0; i < editTextContent.length(); i++) {
						if (!StringUtil.isNormalChar(editTextContent.substring(
								i, i + 1))) {
							ToastUtil.showShort("标签名字不能包括特殊字符");
							return;
						}
					}
					int len = StringUtil.getLength2(editTextContent);
					if (len > 24) {
						DialogUtil.singleToast(ZhislandApplication.APP_CONTEXT,
								"setMaxEditCountInCommonDlg", "不能输入超过" + 24 / 2
										+ "个中文或" + 24 + "个英文字符");
					}

					else {
						addDialog.dismiss();
						addTagTask(editTextContent);
					}
				}
			});
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void showDelDialog(final String tag) {
		if (delDialog == null) {
			delDialog = new CommonDialog(getActivity());
		}
		if (!delDialog.isShowing()) {
			delDialog.show();
			delDialog.setTitle("是否要删除此标签？");
			delDialog.tvDlgConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					delDialog.dismiss();
					delTagTask(tag);
				}
			});
		}
	}

	private void addTagTask(String tagName) {
		tags.add(0, tagName);
		refreshTags();
	}

	private void delTagTask(String tag) {
		tags.remove(tag);
		refreshTags();
	}

	private void refreshTags() {
		flowLayout.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public boolean onTagClick(View view, int position, FlowLayout parent) {
				String tag = tags.get(position);
				if (position == tags.size() - 1) {
					showAddDialog();
				} else {
					showDelDialog(tag);
				}
				return false;
			}
		});

		adapter = new TagAdapter<String>(tags) {
			@Override
			public View getView(
					com.zhisland.android.blog.common.view.flowlayout.FlowLayout parent,
					int position, String tag) {
				int tagHeight = getResources().getDimensionPixelSize(
						R.dimen.tag_height);
				if (position == tags.size() - 1) {
					LinearLayout llTagAdd = (LinearLayout) LayoutInflater.from(
							getActivity()).inflate(R.layout.tag_add, null);
					MarginLayoutParams params = new MarginLayoutParams(
							MarginLayoutParams.WRAP_CONTENT, tagHeight);
					params.rightMargin = DensityUtil.dip2px(10);
					params.topMargin = DensityUtil.dip2px(10);
					llTagAdd.setLayoutParams(params);
					return llTagAdd;
				} else {
					TextView textView = (TextView) LayoutInflater.from(
							getActivity()).inflate(R.layout.tag_text, null);
					MarginLayoutParams params = new MarginLayoutParams(
							MarginLayoutParams.WRAP_CONTENT, tagHeight);
					textView.setTextColor(getResources().getColor(
							R.color.color_bg1));
					textView.setBackgroundResource(R.drawable.img_label_selected);
					params.rightMargin = DensityUtil.dip2px(10);
					params.topMargin = DensityUtil.dip2px(10);
					textView.setPadding(DensityUtil.dip2px(15), 0,
							DensityUtil.dip2px(15), 0);
					textView.setLayoutParams(params);
					textView.setText(tag);
					DensityUtil.setTextSize(textView, R.dimen.txt_12);
					return textView;
				}
			}
		};

		flowLayout.setAdapter(adapter);
	}

	public void saveIntroduction() {
		String wantFriends = MobileUtil.listToString(getSelTags());
		User user = new User();
		user.uid = PrefUtil.Instance().getUserId();
		user.wantFriends = wantFriends == null ? "" : wantFriends;
		ZHApis.getUserApi().updateUser(getActivity(), user, TaskUpdateUser.TYPE_UPDATE_OTHER,
				new TaskCallback<Object>() {

					@Override
					public void onSuccess(Object content) {
						ToastUtil.showShort("愿意结识修改成功");
						if (getActivity() != null) {
							getActivity().finish();
						}
					}

					@Override
					public void onFailure(Throwable error) {
					}
				});
	}
}