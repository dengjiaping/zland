package com.zhisland.android.blog.profile.controller.resource;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleBtn;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleRunnable;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.view.select.FragSelectActivity;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

/**
 * 添加我的需求或编辑我的需求
 */
public class FragDemandEdit extends FragBase {

	public static final String INTENT_KEY_DEMAND = "intent_key_demand";

	private static final int TAG_ID_RIGHT = 101;
	private static final int TAG_ID_LEFT = 102;

	private static final int MAX_TEXT_COUNT = 200;

	private static final int REQ_INDUSTRY = 1987;
	private static final int REQ_CATEGORY = 1988;

	public static void invoke(Context context, Resource resource) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragDemandEdit.class;
		param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
		param.runnable = titleRunnable;
		TitleBtn tb = new TitleBtn(TAG_ID_RIGHT, TitleBtn.TYPE_TXT);
		tb.btnText = "保存";
		tb.textColor = ZhislandApplication.APP_RESOURCE
				.getColor(R.color.color_dc);
		param.titleBtns.add(tb);
		if (resource == null) {
			param.title = "添加需求";
			param.enableBack = false;
			TitleBtn tbLeft = new TitleBtn(TAG_ID_LEFT, TitleBtn.TYPE_TXT);
			tbLeft.btnText = "取消";
			tbLeft.isLeft = true;
			tbLeft.textColor = ZhislandApplication.APP_RESOURCE
					.getColor(R.color.color_f2);
			param.titleBtns.add(tbLeft);
		} else {
			param.title = "编辑需求";
			param.enableBack = true;
		}
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INTENT_KEY_DEMAND, resource);
		context.startActivity(intent);
	}

	static TitleRunnable titleRunnable = new TitleRunnable() {

		@Override
		protected void execute(Context context, Fragment fragment) {
			if (tagId == TAG_ID_RIGHT) {
				if (fragment != null && fragment instanceof FragDemandEdit) {
					((FragDemandEdit) fragment).saveDemand();
				}
			} else if (tagId == TAG_ID_LEFT) {
				fragment.getActivity().finish();
			}
		}
	};

	@InjectView(R.id.tvTitle)
	TextView tvTitle;

	@InjectView(R.id.tvContentTitle)
	TextView tvContentTitle;

	@InjectView(R.id.etContent)
	EditText etContent;

	@InjectView(R.id.tvCount)
	TextView tvCount;

	@InjectView(R.id.llDelete)
	LinearLayout llDelete;

	@InjectView(R.id.tvDelete)
	TextView tvDelete;

	@InjectView(R.id.llIndustry)
	LinearLayout llIndustry;

	@InjectView(R.id.tvIndustry)
	TextView tvIndustry;

	@InjectView(R.id.llCategory)
	LinearLayout llCategory;

	@InjectView(R.id.tvCategory)
	TextView tvCategory;

	Resource resource;

	private CommonDialog deleteDialog;
	private AProgressDialog progressDialog;

	boolean isCreate = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_resource_edit, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initViews();
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setDataToView();
	}

	@Override
	protected String getPageName() {
        if (resource == null){
            return "ProfileDemandCreate";
        }else {
            return "ProfileDemandEdit";
        }
	}

	public void initViews() {
		tvTitle.setText("希望在岛上满足的需求");
		tvContentTitle.setText("需求介绍");
		etContent.setHint("请输入需求详情");
		tvDelete.setText("删除该需求");
		tvCount.setText(String.valueOf(MAX_TEXT_COUNT));
		EditTextUtil.limitEditTextLengthChinese(etContent, MAX_TEXT_COUNT,
				tvCount);
	}

	private void setDataToView() {
		resource = (Resource) getActivity().getIntent().getSerializableExtra(
				INTENT_KEY_DEMAND);
		if (resource == null) {
			isCreate = true;
			resource = new Resource();
			resource.type = Resource.TYPE_DEMAND;
		} else {
			isCreate = false;
		}
		if (isCreate) {
			llDelete.setVisibility(View.GONE);
		} else {
			llDelete.setVisibility(View.VISIBLE);
			etContent.setText(resource.content == null ? "" : resource.content);
			etContent.setSelection(etContent.getText().length());
			setIndustryTag(resource.industryTags, resource.getIndustryName());
			setCategoryTag(resource.categoryTags, resource.getCategoryName());
		}
	}

	@OnClick(R.id.tvDelete)
	void deleteClick() {
		if (deleteDialog == null) {
			deleteDialog = new CommonDialog(getActivity());
		}
		if (!deleteDialog.isShowing()) {
			deleteDialog.show();

			deleteDialog.setTitle("确定删除此需求？");
			deleteDialog.setLeftBtnContent("取消");
			deleteDialog.setRightBtnContent("确定删除");
			deleteDialog.setRightBtnColor(getActivity().getResources()
					.getColor(R.color.color_ac));
			deleteDialog.tvDlgConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteDialog.dismiss();
					showProgressDialog("请求中...");
					ZHApis.getProfileApi().deleteResource(getActivity(), resource,
							new TaskCallback<Object>() {

								@Override
								public void onSuccess(Object content) {
									if (isAdded() || !isDetached()) {
										ToastUtil.showShort("删除需求成功");
										getActivity().finish();
									}
								}

								@Override
								public void onFailure(Throwable error) {

								}

								@Override
								public void onFinish() {
									super.onFinish();
									if (isAdded() || !isDetached()) {
										if (progressDialog != null) {
											progressDialog.dismiss();
										}
									}
								}
							});
				}
			});
		}
	}

	public void saveDemand() {
		String content = etContent.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(content)) {
			ToastUtil.showShort("请输入需求介绍");
			return;
		}
		if (resource.industryTags == null) {
			ToastUtil.showShort("请选择行业标签");
			return;
		}
		if (resource.categoryTags == null) {
			ToastUtil.showShort("请选择类别标签");
			return;
		}
		resource.content = content;
		showProgressDialog("请求中...");
		if (isCreate) {
			ZHApis.getProfileApi().createResource(getActivity(), resource,
					new TaskCallback<Long>() {

						@Override
						public void onSuccess(Long content) {
							if (isAdded() || !isDetached()) {
								ToastUtil.showShort("添加需求成功");
								getActivity().finish();
							}
						}

						@Override
						public void onFailure(Throwable error) {

						}

						@Override
						public void onFinish() {
							super.onFinish();
							if (isAdded() || !isDetached()) {
								if (progressDialog != null) {
									progressDialog.dismiss();
								}
							}
						}
					});
		} else {
			ZHApis.getProfileApi().updateResource(getActivity(), resource,
					new TaskCallback<Object>() {

						@Override
						public void onSuccess(Object content) {
							if (isAdded() || !isDetached()) {
								ToastUtil.showShort("编辑需求成功");
								getActivity().finish();
							}
						}

						@Override
						public void onFailure(Throwable error) {

						}

						@Override
						public void onFinish() {
							super.onFinish();
							if (isAdded() || !isDetached()) {
								if (progressDialog != null) {
									progressDialog.dismiss();
								}
							}
						}
					});
		}
	}

	@OnClick(R.id.llIndustry)
	void industryClick() {
		SoftInputUtil.hideInput(getActivity());
		SelectResourcIndustryTag.launch(getActivity(),
				resource.getIndustryObj(), REQ_INDUSTRY);
	}

	@OnClick(R.id.llCategory)
	void categoryClick() {
		SoftInputUtil.hideInput(getActivity());
		SelectDemandCategoryTag.launch(getActivity(),
				resource.getCategoryObj(), REQ_CATEGORY);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK && data != null) {
			List<ZHDicItem> tags = (List<ZHDicItem>) data
					.getSerializableExtra(FragSelectActivity.INK_RESULT);
			if (tags != null && tags.size() > 0) {
				if (requestCode == REQ_INDUSTRY) {
					setIndustryTag(tags.get(0).key, tags.get(0).name);
				} else if (requestCode == REQ_CATEGORY) {
					setCategoryTag(tags.get(0).key, tags.get(0).name);
				}
			}
		}
	}

	private void setIndustryTag(String tagId, String tagName) {
		if (StringUtil.isNullOrEmpty(tagId)) {
			return;
		}
		resource.industryTags = tagId;
		tvIndustry.setText(tagName == null ? "" : tagName);
		tvIndustry.setTextColor(getResources().getColor(R.color.color_f1));
		tvIndustry.setCompoundDrawables(null, null, null, null);
	}

	private void setCategoryTag(String tagId, String tagName) {
		if (StringUtil.isNullOrEmpty(tagId)) {
			return;
		}
		resource.categoryTags = tagId;
		tvCategory.setText(tagName == null ? "" : tagName);
		tvCategory.setTextColor(getResources().getColor(R.color.color_f1));
		tvCategory.setCompoundDrawables(null, null, null, null);
	}

	private void showProgressDialog(String content) {
		if (progressDialog == null) {
			progressDialog = new AProgressDialog(getActivity());
		}
		progressDialog.show();
		progressDialog.setText(content);
	}

}