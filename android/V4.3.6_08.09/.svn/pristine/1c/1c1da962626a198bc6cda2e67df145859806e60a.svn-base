package com.zhisland.android.blog.profile.controller.position;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.view.EmptyView;
import com.zhisland.android.blog.common.view.UserCommonHead;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.profile.dto.CompanyType;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.eb.EBCompany;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.adapter.BaseListAdapter;
import com.zhisland.lib.component.frag.FragPullList;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 任职列表
 * 
 * @author zhangxiang
 * 
 */
public class FragUserCompany extends FragPullList<Company> {

	/**
	 * 从user中取出来人 任职 数据
	 */
	private ArrayList<Company> userCompanyList;

	/**
	 * 公司类型列表
	 */
	private ArrayList<CompanyType> types;

	private boolean isSelf;

	/**
	 * 空构造函数得保留
	 */
	public FragUserCompany() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		userCompanyList = (ArrayList<Company>) getActivity().getIntent()
				.getSerializableExtra(ActUserCompany.INK_USER_COMPANY);
		types = Dict.getInstance().getCompanyType();
		isSelf = getActivity().getIntent().getBooleanExtra(
				ActUserCompany.INK_USER_IS_SELF, false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		EventBus.getDefault().register(this);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		getPullProxy().setAdapter(new ProfileJobAdapter());
	}

	private void initView() {
		initHeadView();
		initPullView();
	}

	/**
	 * 初始化pullview的顶头界面
	 */
	private void initHeadView() {
		// 初始化头图面板
		UserCommonHead ucHead = new UserCommonHead(getActivity());
		ucHead.setLayoutParams(new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		ucHead.setDesc("各个领域 都有我的足迹");
		ucHead.setBtnText("添加当前任职");
		ucHead.setHeadImage(R.drawable.profile_img_job);
		ucHead.setBtnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragUserCompanySimpleAdd.invoke(getActivity());
			}
		});
		pullView.getRefreshableView().addHeaderView(ucHead);
	}

	/**
	 * 初始配置pullview
	 */
	private void initPullView() {
		getPullProxy().getPullView().setBackgroundColor(
				getResources().getColor(R.color.color_bg2));

		internalView.setFastScrollEnabled(false);
		internalView.setDividerHeight(0);
		internalView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		internalView.setHeaderDividersEnabled(false);
		internalView.setFooterDividersEnabled(false);
		internalView.setBackgroundColor(getResources().getColor(R.color.white));
		// 初始化EmptyView
		EmptyView ev = new EmptyView(getActivity());
		ev.setImgRes(R.drawable.img_company_empty);
		ev.setPrompt("没有任职");
		ev.setBtnVisibility(View.INVISIBLE);
		getPullProxy().getPullView().setEmptyView(ev);
		getPullProxy().getPullView().setMode(Mode.DISABLED);
		loadNormal();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(EBCompany eb) {
		switch (eb.getType()) {
		// 添加任职
		case EBCompany.TYPE_COMPANY_ADD:
			if (eb.getCompany().isDefault != null
					&& eb.getCompany().isDefault == 1)
				userCompanyList.add(0, eb.getCompany());
			else {
				userCompanyList.add(eb.getCompany());
			}
			break;
		case EBCompany.TYPE_COMPANY_DELETE:
			for (Company company : userCompanyList) {
				if (company.companyId == eb.getCompany().companyId) {
					userCompanyList.remove(company);
					break;
				}
			}
			break;
		// 编辑任职
		case EBCompany.TYPE_COMPANY_EDIT:
			for (int i = 0; i < userCompanyList.size(); i++) {
				if (userCompanyList.get(i).companyId == eb.getCompany().companyId) {
					userCompanyList.set(i, eb.getCompany());
					break;
				}
			}
			break;
		}
		// 只有一个默认身份,所以要遍历列表来确认只有一个默认的身份
		if (eb.getCompany().isDefault != null && eb.getCompany().isDefault == 1) {
			int authIndex = 0;
			for (int i = 0; i < userCompanyList.size(); i++) {
				if (userCompanyList.get(i).companyId != eb.getCompany().companyId) {
					userCompanyList.get(i).isDefault = 0;
				} else {
					authIndex = i;
				}
			}
			// 将默认身份移到第一列
			userCompanyList.add(0, userCompanyList.remove(authIndex));
		}
		//先把公司加入到列表中，以防正在添加的时候断网了，公司列表最基本的变化都没有
		getPullProxy().getAdapter().notifyDataSetChanged();
		//再拉接口与后台同步
		loadNormal();
	}

	/**
	 * 任职Adapter
	 * 
	 * @author zhangxiang
	 * 
	 */
	class ProfileJobAdapter extends BaseListAdapter<Company> {

		@Override
		public int getCount() {
			return userCompanyList.size();
		}

		@Override
		public Company getItem(int position) {
			return userCompanyList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.item_user_position, null);
				UserCompanyCell holder = new UserCompanyCell(getActivity(),
						convertView);
				convertView.setTag(holder);
			}
			UserCompanyCell holder = (UserCompanyCell) convertView.getTag();
			holder.fill(getItem(position), isSelf, false, types);
			return convertView;
		}

		@Override
		protected void recycleView(View view) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 加载公司列表
	 */
	public void loadNormal() {
		ZHApis.getUserApi().getUserDetail(getActivity(), PrefUtil.Instance().getUserId(), new TaskCallback<UserDetail>() {

			@Override
			public void onSuccess(UserDetail content) {
				userCompanyList = content.user.companies;
				getPullProxy().getAdapter().notifyDataSetChanged();
				getPullProxy().getPullView().setMode(Mode.DISABLED);
			}

			@Override
			public void onFailure(Throwable error) {
				getPullProxy().onLoadFailed(error);
				getPullProxy().getPullView().setMode(Mode.PULL_FROM_START);
			}
		});
	};

	@Override
	protected String getPageName() {
		return "ProfileMyCompanyList";
	}
}
