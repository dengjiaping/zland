package com.zhisland.android.blog.profile.controller.position;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleBtn;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleRunnable;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.DialogUtil.OnCommonDialogBtnListener;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.UserCommonHead;
import com.zhisland.android.blog.profile.api.ZHApiProfile;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.profile.dto.CompanyType;
import com.zhisland.android.blog.profile.eb.EBCompany;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 添加任职
 */
public class FragUserCompanySimpleAdd extends FragBase {

	private final static int TAG_CANCEL = 410;
	private final static int TAG_SAVE = 411;

	@InjectView(R.id.rgTabLayout)
	RadioGroup rgTabLayout;

	@InjectView(R.id.tvCompanyTitle)
	TextView tvCompanyTitle;

	@InjectView(R.id.tvPositionTitle)
	TextView tvPositionTitle;

	@InjectView(R.id.tvEg)
	TextView tvEg;

	@InjectView(R.id.rlEg)
	View rlEg;

	@InjectView(R.id.etCompany)
	EditText etCompany;

	@InjectView(R.id.etPosition)
	EditText etPosition;

	@InjectView(R.id.btnSwitch)
	ImageView btnSwitch;

	@InjectView(R.id.btnContinue)
	UserCommonHead btnContinue;

	/**
	 * 当前任职是否为 <b>默认身份</b>
	 */
	private boolean isDefault;

	/**
	 * 添加任职，当前任职类型 </br>"商业" CompanyType.KEY_COMMERCE; </br>"非商业"
	 * CompanyType.KEY_UN_COMMERCE ; </br>"社会组织" CompanyType.KEY_SOCIETY;</br>
	 * 其它的都按"商业"来处理
	 */
	private String companyType = CompanyType.KEY_COMMERCE;

	/**
	 * 用户添加任职，判断当前任职是否保存
	 */
	private boolean hasEdited = false;

	private AProgressDialog progressDialog;

	/**
	 * 用户每保存一次， userPosition会被赋值，用于检查用户再次编辑信息后进行前后对照
	 */
	// private Company userCompany;

	public static void invoke(Context context) {
		Intent intent = CommonFragActivity.createIntent(context,
				createFragParams(context));
		context.startActivity(intent);
	}

	private static CommonFragParams createFragParams(Context context) {
		CommonFragParams param = new CommonFragParams();
		param.clsFrag = FragUserCompanySimpleAdd.class;
		param.swipeBackEnable = false;
		param.title = "添加任职";
		param.titleBtns = new ArrayList<TitleBtn>();
		TitleBtn leftBtn = new TitleBtn(TAG_CANCEL, TitleBtn.TYPE_TXT);
		leftBtn.btnText = "取消";
		leftBtn.isLeft = true;
		leftBtn.textColor = context.getResources().getColor(R.color.color_f2);
		TitleBtn rightBtn = new TitleBtn(TAG_SAVE, TitleBtn.TYPE_TXT);
		rightBtn.btnText = "保存";
		rightBtn.isLeft = false;
		rightBtn.textColor = context.getResources().getColor(R.color.color_dc);
		param.titleBtns.add(leftBtn);
		param.titleBtns.add(rightBtn);
		param.runnable = createRunnable();
		return param;
	}

	private static TitleRunnable createRunnable() {
		return new TitleRunnable() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(Context context, Fragment fragment) {
				if (tagId == TAG_CANCEL) {
					((FragUserCompanySimpleAdd) fragment).cancel();
				} else if (tagId == TAG_SAVE) {
					((FragUserCompanySimpleAdd) fragment).create(true);
				}
			}
		};
	}

	/**
	 * 空构造函数得保留
	 */
	public FragUserCompanySimpleAdd() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.user_position_add, null);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		ButterKnife.inject(this, root);
		EventBus.getDefault().register(this);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		updateView();
	}

	/**
	 * 初始化头面板界面
	 */
	private void updateView() {
		btnContinue.btnOnly();
		btnContinue.setBtnText("继续填写公司信息");
		btnContinue.setBtnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 如果是"继续添加公司信息" 就走这一块儿
				create(false);
			}
		});
		// 添加时一开始肯定 都为空，所以保存为灰色
		((TextView) ((CommonFragActivity) getActivity()).getTitleBar()
				.getButton(TAG_SAVE)).setTextColor(getResources().getColor(
				R.color.color_f2));
		etCompany.addTextChangedListener(createEditTextChangedListener(40,
				etCompany));
		etPosition.addTextChangedListener(createEditTextChangedListener(20,
				etPosition));
		hasEdited = false;
		updateTabBar();
	}

	/**
	 * 更新TabBar(用RadioButton实现)，显示公司类型
	 */
	private void updateTabBar() {
		ArrayList<CompanyType> types = Dict.getInstance().getCompanyType();
		types = sortTypes(types);
		final ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();
		int w = getRadioButtonWitch(types.size());
		// radioButton 单选图像
		Drawable drawable = getResources().getDrawable(R.drawable.trans_dot);
		drawable.setBounds(0, 0, 1, 1);
		for (int i = 0; i < types.size(); i++) {
			RadioButton rb = new RadioButton(getActivity());
			int id = 0;
			if(Build.VERSION.SDK_INT >= 17) {
				id = View.generateViewId();
			}else{
				id = (int) (System.currentTimeMillis()*10000+new Random(10000).nextInt());
			}
			rb.setId(id);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(w,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
			llp.gravity = Gravity.CENTER;
			rb.setButtonDrawable(drawable);
			rb.setGravity(Gravity.CENTER_HORIZONTAL);
			DensityUtil.setTextSize(rb, R.dimen.txt_16);
			rb.setTextColor(getResources().getColor(R.color.color_f3));
			if (!StringUtil.isNullOrEmpty(types.get(i).tagName)) {
				rb.setText(types.get(i).tagName);
			} else {
				rb.setText("自定义类型");
			}

			buttons.add(rb);
			rgTabLayout.addView(rb, llp);
			updateCheckListener(types, buttons);
			if (types.get(i).tagId != null) {
				if (types.get(i).tagId.equals(CompanyType.KEY_UN_COMMERCE)) {
					changeDrawalbe(rb, R.drawable.btn_sel_radio_unbusiness);
				} else if (types.get(i).tagId.equals(CompanyType.KEY_SOCIETY)) {
					changeDrawalbe(rb, R.drawable.btn_sel_radio_socity);
				} else if (types.get(i).tagId.equals(CompanyType.KEY_COMMERCE)) {
					changeDrawalbe(rb, R.drawable.btn_sel_radio_business);
				} else {
					changeDrawalbe(rb, R.drawable.btn_sel_radio_other);
				}
			} else {
				changeDrawalbe(rb, R.drawable.btn_sel_radio_other);
			}
		}
		if (buttons.size() > 0) {
			buttons.get(0).setChecked(true);
		}
	}

	/**
	 * titlerbar 返回(放弃添加任职)
	 */
	public void cancel() {
		if (!hasEdited) {
			if(getActivity() != null)
				getActivity().finish();
			return;
		} else {
			showCancelDialog();
		}
	}

	/**
	 * titlerbar 创建任职
	 * 
	 * @param justSave
	 * <br>
	 *            <b>justSave</b>为true 为仅保存， justSave为false为保存并跳转到"继续添加公司信息"
	 */
	protected void create(boolean justSave) {
		final String position = etPosition.getText().toString();
		final String orgName = etCompany.getText().toString();
		if (StringUtil.isNullOrEmpty(position)
				|| StringUtil.isNullOrEmpty(orgName)) {
			DialogUtil.showDialog(getActivity(), "您的信息填写不全，请检查", "", "放弃",
					"继续填写", new OnCommonDialogBtnListener() {

						@Override
						public void onClick(CommonDialog dialog) {
							dialog.dismiss();
							if(getActivity() != null)
								getActivity().finish();
						}
					}, new OnCommonDialogBtnListener() {

						@Override
						public void onClick(CommonDialog dialog) {
							dialog.dismiss();
						}
					});
			return;
		}
		// 判断用户是否选择了此任职为 默认任职
		int isDefault = this.isDefault ? 1 : 0;
		// 保存当前任职信息到内存
		final Company company = new Company();
		company.name = orgName;
		company.position = position;
		company.isDefault = isDefault;
		company.type = companyType;
		if (!justSave) {
			FragUserCompanyCreateOrUpdate.invoke(getActivity(), company,
					FragUserCompanyCreateOrUpdate.TYPE_ADD);
			return;
		}
		createPosition(company, justSave);
	}

	/**
	 * 对RadioButton设置Check监听
	 * 
	 * @param types
	 * @param buttons
	 */
	private void updateCheckListener(final ArrayList<CompanyType> types,
			final ArrayList<RadioButton> buttons) {
		rgTabLayout.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				for (int i = 0; i < buttons.size(); i++) {
					if (buttons.get(i).getId() == checkedId) {
						if (types.get(i).tagId != null) {
							if (types.get(i).tagId
									.equals(CompanyType.KEY_UN_COMMERCE)) {
								onClickNoCommerce();
							} else if (types.get(i).tagId
									.equals(CompanyType.KEY_SOCIETY)) {
								onClickSociety();
							} else if (types.get(i).tagId
									.equals(CompanyType.KEY_COMMERCE)) {
								onClickCommerce();
							} else {
								onClickOther(types.get(i).tagId);
							}

						} else {
							onClickOther(null);
						}
					}
				}
			}
		});
	}

	/**
	 * 设置RadioButton的selector图像
	 * 
	 * @param rb
	 * @param res
	 */
	private void changeDrawalbe(RadioButton rb, int res) {
		Drawable drawable = getResources().getDrawable(res);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		rb.setCompoundDrawables(null, drawable, null, null);
	}

	/**
	 * 计算每个RadioButton的宽度
	 * 
	 * @param count
	 * @return
	 */
	private int getRadioButtonWitch(int count) {
		return (DensityUtil.getWidth() - ((LinearLayout.LayoutParams) (rgTabLayout
				.getLayoutParams())).leftMargin * 2) / count;
	}

	/**
	 * 对公司类型进行排序
	 * 
	 * @param types
	 * @return
	 */
	private ArrayList<CompanyType> sortTypes(ArrayList<CompanyType> types) {
		for (int i = 0; i < types.size() - 1; i++) {
			for (int j = i + 1; j < types.size(); j++) {
				if (types.get(j).custom != null
						&& (Integer.valueOf(types.get(i).custom) > Integer
								.valueOf(types.get(j).custom))) {
					CompanyType temp = new CompanyType(types.get(i));
					types.get(i).custom = types.get(j).custom;
					types.get(i).parentId = types.get(j).parentId;
					types.get(i).tagId = types.get(j).tagId;
					types.get(i).tagName = types.get(j).tagName;
					types.get(j).custom = temp.custom;
					types.get(j).parentId = temp.parentId;
					types.get(j).tagId = temp.tagId;
					types.get(j).tagName = temp.tagName;
				}
			}
		}
		return types;
	}

	/**
	 * 切换为商业机构 和其它类型的公司(除 非商业机构和社会组织的其它类型)
	 * 
	 * @param
	 */
	private void onClickCommerce() {
		tvCompanyTitle.setText("公司名称");
		tvPositionTitle.setText("当前职务");
		etCompany.setHint("请输入公司名称");
		rlEg.setVisibility(View.GONE);
		btnContinue.setVisibility(View.VISIBLE);
		companyType = CompanyType.KEY_COMMERCE;
	}

	/**
	 * 切换为非商业机构
	 * 
	 * @param
	 */
	public void onClickNoCommerce() {
		tvCompanyTitle.setText("机构名称");
		tvPositionTitle.setText("当前职务");
		etCompany.setHint("请输入机构名称");
		rlEg.setVisibility(View.VISIBLE);
		tvEg.setText("例如：北京大学  经济管理学院教授");
		companyType = CompanyType.KEY_UN_COMMERCE;
		btnContinue.setVisibility(View.GONE);
	}

	/**
	 * 切换为社会机构, 其它自定义的，也和社会机构一样，isNormal参数传入时为false
	 * 
	 * @param
	 */
	public void onClickSociety() {
		tvCompanyTitle.setText("组织名称");
		tvPositionTitle.setText("当前职务");
		etCompany.setHint("请输入组织名称");
		rlEg.setVisibility(View.VISIBLE);
		tvEg.setText("例如：全国人大政协委员会  委员");
		companyType = CompanyType.KEY_SOCIETY;
		btnContinue.setVisibility(View.GONE);
	}

	/**
	 * 其它类型点击
	 */
	public void onClickOther(String type) {
		tvCompanyTitle.setText("组织名称");
		tvPositionTitle.setText("当前职务");
		rlEg.setVisibility(View.GONE);
		companyType = type;
		btnContinue.setVisibility(View.GONE);
	}
	
	/**
	 * 默认身份开关
	 */
	@OnClick(R.id.btnSwitch)
	public void onClickSwitch() {
		isDefault = !isDefault;
		if (isDefault) {
			btnSwitch.setImageResource(R.drawable.switch_on);
		} else {
			btnSwitch.setImageResource(R.drawable.switch_off);
		}
	}

	/**
	 * 创建任职(组织好数据后调用接口)
	 * 
	 * @param company
	 * @param justSave
	 */
	private void createPosition(final Company company, final boolean justSave) {
		showProgressDialog("保存中...");
		// 然后调用接口
		ZHApiProfile.Instance().creatCompany(getActivity(), company, new TaskCallback<Long>() {

			@Override
			public void onSuccess(Long content) {
				company.companyId = content;
			}

			@Override
			public void onFailure(Throwable error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFinish() {
				super.onFinish();
				disMissDialog();
			}
		});
	}

	/**
	 * 返回二次确认
	 */
	private void showCancelDialog() {
		DialogUtil.showDialog(getActivity(), "是否保存修改内容", "",
				new OnCommonDialogBtnListener() {

					@Override
					public void onClick(CommonDialog dialog) {
						dialog.dismiss();
						if(getActivity() != null)
							getActivity().finish();
					}
				}, new OnCommonDialogBtnListener() {

					@Override
					public void onClick(CommonDialog dialog) {
						dialog.dismiss();
						create(true);
					}
				});
	}

	/**
	 * 检查 公司 和 职位 是否为空，并置灰或置亮保存 按钮
	 */
	private void checkEmpty() {
		if (StringUtil.isNullOrEmpty(etCompany.getText().toString())
				|| StringUtil.isNullOrEmpty(etPosition.getText().toString())) {
			((TextView) ((CommonFragActivity) getActivity()).getTitleBar()
					.getButton(TAG_SAVE)).setTextColor(getResources().getColor(
					R.color.color_f3));
			((CommonFragActivity) getActivity()).getTitleBar()
					.disableTitleButton(TAG_SAVE);
		} else {
			((TextView) ((CommonFragActivity) getActivity()).getTitleBar()
					.getButton(TAG_SAVE)).setTextColor(getResources().getColor(
					R.color.color_dc));
			((CommonFragActivity) getActivity()).getTitleBar()
					.enableTitleButton(TAG_SAVE);
		}
	}

	/**
	 * 对界面中的TextView 和 EditView 创建textChanged的监听，以便检验用户是否已编辑过信息
	 * 
	 * @return
	 */
	private TextWatcher createEditTextChangedListener(final int maxCount,
			final EditText et) {
		return new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				checkEmpty();
				hasEdited = true;
				String text = s.toString();
				if (!StringUtil.isNullOrEmpty(text)) {
					if (text.length() > maxCount) {
						text = text.substring(0, maxCount);
						et.setText(text);
						et.setSelection(text.length());
						ToastUtil.showShort("字数不得超过" + maxCount + "个");
					}
				}
			}
		};
	}

	protected String getPageName() {
		return "ProfileMyCompanyCreate";
	}

	@Override
	public boolean onBackPressed() {
		cancel();
		return true;
	}

	public void onEventMainThread(EBCompany eb) {
		if (eb.getType() == EBCompany.TYPE_COMPANY_ADD) {
			ToastUtil.showLong("保存成功");
			if(getActivity() != null)
				getActivity().finish();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	// 显示加载对话框
	private void showProgressDialog(String text) {
		if (progressDialog == null) {
			progressDialog = new AProgressDialog(getActivity());
			progressDialog.setText(text);
		}
		if (!progressDialog.isShowing())
			progressDialog.show();
	}

	private void disMissDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

}
