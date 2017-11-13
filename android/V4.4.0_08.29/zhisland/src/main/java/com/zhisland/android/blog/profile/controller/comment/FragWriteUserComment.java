package com.zhisland.android.blog.profile.controller.comment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.view.CountEditText;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 写神评论
 */
@SuppressLint("InflateParams")
public class FragWriteUserComment extends FragBase {

	private static final int EDIT_TEXT_COUNT = 150;

	@InjectView(R.id.tvDesc)
	public TextView tvDesc;

	@InjectView(R.id.etWriteUC)
	public CountEditText etWriteUC;

	private User userFrom;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.frag_write_user_comment, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		initViews();
		return root;
	}

	@Override
	public String getPageName() {
		return "FragWriteUserComment";
	}

	public void initViews() {
		etWriteUC.setMaxCount(EDIT_TEXT_COUNT);
		userFrom = ((ActWriteUserComment) getActivity()).userFrom;
		tvDesc.setText("评价一下 " + userFrom.name);
	}

	/**
	 * 获取评论内容
	 */
	public String getContent() {
		return etWriteUC.getEditText().getText().toString().trim();
	}

	// 提交评论
	public void submit() {
		String content = etWriteUC.getEditText().getText().toString().trim();
		if (StringUtil.isNullOrEmpty(content)) {
			showToast("评价不能为空");
		} else {
			showProgressDlg();
			ZHApis.getProfileApi().createUserComment(getActivity(), userFrom.uid, content,
					new TaskCallback<Object>() {

						@Override
						public void onSuccess(Object content) {
							showToast("评论成功");
							getActivity().finish();
						}

						@Override
						public void onFailure(Throwable error) {
						}

						@Override
						public void onFinish() {
							super.onFinish();
							hideProgressDlg();
						}
					});
		}
	}
}
