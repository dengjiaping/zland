package com.zhisland.android.blog.profile.controller.honor;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleBtn;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleRunnable;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.profile.dto.Honor;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加我的荣誉或编辑我的荣誉
 */
public class FragHonorEdit extends FragBase {

	public static final String INTENT_KEY_HONOR = "intent_key_honor";

	private static final int TAG_ID_RIGHT = 101;
	private static final int TAG_ID_LEFT = 102;

	private static final int MAX_TEXT_COUNT = 20;

	public static void invoke(Context context, Honor honor) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragHonorEdit.class;
		param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
		param.runnable = titleRunnable;
		TitleBtn tb = new TitleBtn(TAG_ID_RIGHT, TitleBtn.TYPE_TXT);
		tb.btnText = "保存";
		tb.textColor = ZhislandApplication.APP_RESOURCE
				.getColor(R.color.color_dc);
		param.titleBtns.add(tb);
		if (honor == null) {
			param.title = "添加荣誉";
			param.enableBack = false;
			TitleBtn tbLeft = new TitleBtn(TAG_ID_LEFT, TitleBtn.TYPE_TXT);
			tbLeft.btnText = "取消";
			tbLeft.isLeft = true;
			tbLeft.textColor = ZhislandApplication.APP_RESOURCE
					.getColor(R.color.color_f2);
			param.titleBtns.add(tbLeft);
		} else {
			param.title = "编辑荣誉";
			param.enableBack = true;
		}
		Intent intent = CommonFragActivity.createIntent(context, param);
		intent.putExtra(INTENT_KEY_HONOR, honor);
		context.startActivity(intent);
	}

	static TitleRunnable titleRunnable = new TitleRunnable() {

		@Override
		protected void execute(Context context, Fragment fragment) {
			if (tagId == TAG_ID_RIGHT) {
				if (fragment != null && fragment instanceof FragHonorEdit) {
					((FragHonorEdit) fragment).saveHonor();
				}
			} else if (tagId == TAG_ID_LEFT) {
				fragment.getActivity().finish();
			}
		}
	};

	@InjectView(R.id.etHonor)
	EditText etHonor;

	@InjectView(R.id.tvCount)
	TextView tvCount;

	@InjectView(R.id.llDelete)
	LinearLayout llDelete;

	Honor honor;

	private CommonDialog deleteDialog;

	boolean isCreate = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_honor_edit, null);
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
	public String getPageName() {
		return "FragHonorEdit";
	}

	public void initViews() {
		tvCount.setText(String.valueOf(MAX_TEXT_COUNT));
		EditTextUtil.limitEditTextLengthChinese(etHonor, MAX_TEXT_COUNT,
				tvCount);
	}

	private void setDataToView() {
		honor = (Honor) getActivity().getIntent().getSerializableExtra(
				INTENT_KEY_HONOR);
		if (honor == null) {
			isCreate = true;
			honor = new Honor();
		} else {
			isCreate = false;
		}
		if (isCreate) {
			llDelete.setVisibility(View.GONE);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		} else {
			llDelete.setVisibility(View.VISIBLE);
			getActivity().getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
			etHonor.setText(honor.honorTitle == null ? "" : honor.honorTitle);
			etHonor.setSelection(etHonor.getText().length());
		}
	}

	@OnClick(R.id.tvDelete)
	void deleteClick() {
		if (deleteDialog == null) {
			deleteDialog = new CommonDialog(getActivity());
		}
		if (!deleteDialog.isShowing()) {
			deleteDialog.show();

			deleteDialog.setTitle("确定删除此荣誉？");
			deleteDialog.setLeftBtnContent("取消");
			deleteDialog.setRightBtnContent("确定删除");
			deleteDialog.setRightBtnColor(getActivity().getResources()
					.getColor(R.color.color_ac));
			deleteDialog.tvDlgConfirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					deleteDialog.dismiss();
					showProgressDlg("请求中...");
					ZHApis.getProfileApi().deleteHonor(getActivity(), honor.id,
							new TaskCallback<Object>() {

								@Override
								public void onSuccess(Object content) {
									if (isAdded() || !isDetached()) {
										showToast("删除荣誉成功");
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
                                        hideProgressDlg();
									}
								}
							});
				}
			});
		}
	}

	public void saveHonor() {
		String content = etHonor.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(content)) {
			showToast("请输入你所取得的荣誉");
		} else {
			honor.honorTitle = content;
			showProgressDlg("请求中...");
			if (isCreate) {
				ZHApis.getProfileApi().createHonor(getActivity(), honor,
						new TaskCallback<String>() {

							@Override
							public void onSuccess(String content) {
								if (isAdded() || !isDetached()) {
									showToast("添加荣誉成功");
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
                                    hideProgressDlg();
								}
							}
						});
			} else {
				ZHApis.getProfileApi().updateHonor(getActivity(), honor,
						new TaskCallback<Object>() {

							@Override
							public void onSuccess(Object content) {
								if (isAdded() || !isDetached()) {
									showToast("编辑荣誉成功");
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
									hideProgressDlg();
								}
							}
						});
			}
		}
	}
	

}