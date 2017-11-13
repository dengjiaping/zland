package com.zhisland.android.blog.profile.controller.position;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.CommonFragActivity.CommonFragParams;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleBtn;
import com.zhisland.android.blog.common.base.CommonFragActivity.TitleRunnable;
import com.zhisland.android.blog.common.dto.CacheDto;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.DialogUtil.OnCommonDialogBtnListener;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.profile.api.ZHApiProfile;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.profile.dto.CompanyType;
import com.zhisland.android.blog.profile.eb.EBCompany;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 编辑任职
 */
public class FragUserCompanySimpleEdit extends FragBase {

    public final static String INK_USER_POSITION = "ink_user_position";
    private final static int TAG_BACK = 410;
    private final static int TAG_SAVE = 411;

    @InjectView(R.id.tvEditHead)
    TextView tvEditHead;

    @InjectView(R.id.tvCompanyTitle)
    TextView tvCompanyTitle;

    @InjectView(R.id.tvPositionTitle)
    TextView tvPositionTitle;

    @InjectView(R.id.tvGotoCompanyDetail)
    TextView tvGotoCompanyDetail;

    @InjectView(R.id.etCompany)
    EditText etCompany;

    @InjectView(R.id.etPosition)
    EditText etPosition;

    @InjectView(R.id.btnSwitch)
    ImageView btnSwitch;

    @InjectView(R.id.ivIndentify)
    ImageView ivIndentify;

    @InjectView(R.id.llDefaultPosition)
    View llDefaultPosition;

    @InjectView(R.id.vDiv1)
    View vDiv1;

    @InjectView(R.id.vDiv2)
    View vDiv2;

    @InjectView(R.id.tvBtnText)
    TextView tvBtnText;

    @InjectView(R.id.rlBtn)
    View rlBtn;

    @InjectView(R.id.tvSumm)
    View tvSumm;

    @InjectView(R.id.bottomDiv)
    View bottomDiv;

    /**
     * 当前任职是否为 <b>默认身份</b>
     */
    private boolean isDefault;

    private Company company;

    /**
     * 用户添加任职，判断当前任职是否保存
     */
    private boolean hasEdited;

    /**
     * 任职对应的用户id
     */
    private boolean isSelf;

    private AProgressDialog progressDialog;

    public static void invoke(Context context, Company userPosition,
                              boolean isSelf) {
        Intent intent = CommonFragActivity.createIntent(context,
                createFragParams(context));
        intent.putExtra(INK_USER_POSITION, userPosition);
        intent.putExtra(ActCompanyDetail.INTENT_KEY_IS_SELF, isSelf);
        context.startActivity(intent);
    }

    private static CommonFragParams createFragParams(Context context) {
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragUserCompanySimpleEdit.class;
        param.swipeBackEnable = false;
        param.title = "编辑任职";
        param.titleBtns = new ArrayList<TitleBtn>();
        TitleBtn leftBtn = new TitleBtn(TAG_BACK, TitleBtn.TYPE_IMG);
        leftBtn.isLeft = true;
        leftBtn.imgResId = R.drawable.sel_btn_back;
        param.titleBtns.add(leftBtn);
        TitleBtn rightBtn = new TitleBtn(TAG_SAVE, TitleBtn.TYPE_TXT);
        rightBtn.btnText = "保存";
        rightBtn.isLeft = false;
        rightBtn.textColor = context.getResources().getColor(R.color.color_dc);
        param.titleBtns.add(rightBtn);
        param.runnable = createRunnable();
        return param;
    }

    private static TitleRunnable createRunnable() {
        return new TitleRunnable() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void execute(Context context, Fragment fragment) {
                if (tagId == TAG_SAVE) {
                    ((FragUserCompanySimpleEdit) fragment).updata();
                } else if (tagId == TAG_BACK) {
                    ((FragUserCompanySimpleEdit) fragment).cancel();
                }
            }
        };
    }

    /**
     * 空构造函数得保留
     */
    public FragUserCompanySimpleEdit() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        company = (Company) getActivity().getIntent().getSerializableExtra(
                INK_USER_POSITION);
        isSelf = getActivity().getIntent().getBooleanExtra(
                ActCompanyDetail.INTENT_KEY_IS_SELF, false);
        View root = inflater.inflate(R.layout.user_position_edit, null);
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
        initView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    /**
     * 为界面控件填充数据
     */
    private void initView() {
        initBaseInfo();
        initHeadView();
        initChange();
    }

    /**
     * 基本信息赋值
     */
    private void initBaseInfo() {
        // 是否为认证公司
        if ((company.isAuthentication != null && company.isAuthentication == 1)
                || (company.isClientAuth != null && company.isClientAuth == 1)) {
            setAuthCompany();
        }
        // 公司名
        etCompany.setText(company.name);
        // 职位
        etPosition.setText(company.position);
        // 默认认职
        if (company.isDefault != null && company.isDefault == 1) {
            isDefault = true;
            btnSwitch.setImageResource(R.drawable.switch_on);
            llDefaultPosition.setVisibility(View.INVISIBLE);
        } else {
            isDefault = false;
            btnSwitch.setImageResource(R.drawable.switch_off);
            btnSwitch.setVisibility(View.VISIBLE);
            llDefaultPosition.setVisibility(View.VISIBLE);
        }
        // 只有商业公司才能查看详情
        if (company.type != null
                && company.type.equals(CompanyType.KEY_COMMERCE)) {
            tvGotoCompanyDetail.setVisibility(View.VISIBLE);
        } else {
            tvGotoCompanyDetail.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化头板界面，仿tabbar
     */
    private void initHeadView() {
        ArrayList<CompanyType> list = Dict.getInstance().getCompanyType();
        int res = R.drawable.profile_img_business_selected;
        if (company.type != null && list != null) {
            if (company.type.equals(CompanyType.KEY_COMMERCE)) {
                res = R.drawable.profile_img_business_selected;
                tvCompanyTitle.setText("公司名称");
                etCompany.setHint("请输入公司名称");
            } else if (company.type.equals(CompanyType.KEY_UN_COMMERCE)) {
                res = R.drawable.profile_img_nonbusiness_selected;
                tvCompanyTitle.setText("机构名称");
                etCompany.setHint("请输入机构名称");
            } else if (company.type.equals(CompanyType.KEY_SOCIETY)) {
                res = R.drawable.profile_img_community_selected;
                tvCompanyTitle.setText("组织名称");
                etCompany.setHint("请输入组织名称");
            }
            // 正和岛组织机构不会进入编辑页
            else if (company.type.equals(CompanyType.KEY_ZHISLAND_ORG)) {
                res = R.drawable.profile_img_zhisland_selected;
            }
        } else {
            res = R.drawable.profile_img_other_selected;
            tvCompanyTitle.setText("公司名称");
        }

        int draw = getRes(company.type, list);
        if (draw != -1) {
            Drawable drawable = getResources().getDrawable(draw);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tvEditHead.setCompoundDrawables(null, drawable, null, null);
        }
        tvEditHead.setText(getTextForCompanyTypeList(company.type, list));
    }

    /**
     * 为EditText设置变更监听
     */
    private void initChange() {
        etCompany.addTextChangedListener(createEditTextChangedListener(40,
                etCompany));
        etPosition.addTextChangedListener(createEditTextChangedListener(20,
                etPosition));
        hasEdited = false;
    }

    /**
     * titbar返回
     */
    public void cancel() {
        if (etCompany != null && (etCompany.getKeyListener() == null)) {
            if (getActivity() != null)
                getActivity().finish();
            return;
        }
        if (hasEdited) {
            DialogUtil.showDialog(getActivity(), "是否保存修改内容", "",
                    new OnCommonDialogBtnListener() {

                        @Override
                        public void onClick(CommonDialog dialog) {
                            dialog.dismiss();
                            if (getActivity() != null)
                                getActivity().finish();
                        }
                    }, new OnCommonDialogBtnListener() {

                        @Override
                        public void onClick(CommonDialog dialog) {
                            updata();
                            dialog.dismiss();
                        }
                    });
        } else {
            if (getActivity() != null)
                getActivity().finish();
        }
    }

    /**
     * titerbar编辑任职(提交变更)
     */
    protected void updata() {
        company.position = etPosition.getText().toString();
        company.name = etCompany.getText().toString();
        if (StringUtil.isNullOrEmpty(company.position)
                || StringUtil.isNullOrEmpty(company.name)) {
            DialogUtil.showDialog(getActivity(), "您的信息填写不全，请检查", "", null,
                    new OnCommonDialogBtnListener() {

                        @Override
                        public void onClick(CommonDialog dialog) {
                            dialog.dismiss();
                        }
                    });
            return;
        }
        updatePosition(company);
    }

    @Override
    public boolean onBackPressed() {
        cancel();
        return true;
    }

    /**
     * 从公司类型列表中，通过公司的类型取出对应的类型名称
     */
    private String getTextForCompanyTypeList(String type,
                                             ArrayList<CompanyType> list) {
        if (type == null) {
            return "";
        }
        if (list == null) {
            return "";
        }
        for (CompanyType ct : list) {
            if (ct.tagId.equals(type)) {
                return ct.tagName;
            }
        }
        return "";
    }

    /**
     * 删除任职(调用接口)
     */
    private void deletePosition() {
        if (company != null) {
            showProgressDialog("删除中...");
            ZHApiProfile.Instance().deleteCompany(getActivity(), company.companyId,
                    new TaskCallback<Object>() {

                        @Override
                        public void onSuccess(Object content) {
                            EBCompany eb = new EBCompany(
                                    EBCompany.TYPE_COMPANY_DELETE, company);
                            EventBus.getDefault().post(eb);
                            EventBus.getDefault().post(
                                    new EBProfile(
                                            EBProfile.TYPE_CHANGE_POSITION));
                            CacheDto cacheDto = new CacheDto();
                            cacheDto.key = company.companyId + Company.POSTFIX;
                            cacheDto.value = company;
                            try {
                                DBMgr.getMgr().getCacheDao().delete(cacheDto);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (getActivity() != null)
                                getActivity().finish();
                            ToastUtil.showShort("删除成功");

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
    }

    /**
     * 编辑任职(调用接口)
     *
     * @param positionId
     * @param position
     * @param orgName
     * @param isTop
     */
    private void updatePosition(final Company company) {
        showProgressDialog("保存中...");
        ZHApiProfile.Instance().updateCompany(getActivity(), company,
                new TaskCallback<Object>() {

                    @Override
                    public void onSuccess(Object content) {
                        hasEdited = false;
                        DBMgr.getMgr()
                                .getCacheDao()
                                .set(company.companyId + Company.POSTFIX,
                                        company);
                        ToastUtil.showLong("保存成功");
                        if (getActivity() != null)
                            getActivity().finish();
                        // 如果是选 switch的话 不提交是不会保存的
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

    protected String getPageName() {
        return "ProfileMyCompanyEdit";
    }

    /**
     * 认证公司界面
     */
    private void setAuthCompany() {

        if (company.isAuthentication != null && company.isAuthentication == 1) {
            ivIndentify.setImageResource(R.drawable.profile_img_authentication_full);
        } else {
            ivIndentify.setImageResource(R.drawable.profile_img_haike_auth_full);
        }

        ivIndentify.setVisibility(View.VISIBLE);
        tvSumm.setVisibility(View.VISIBLE);
        bottomDiv.setVisibility(View.GONE);
        tvBtnText.setVisibility(View.GONE);
        vDiv1.setVisibility(View.GONE);
        vDiv2.setVisibility(View.GONE);
        etCompany.setKeyListener(null);
        etPosition.setKeyListener(null);
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

    /**
     * 检查 公司 和 职位 是否为空，并置灰或置亮保存 按钮
     */
    private void checkEmpty() {
        if (StringUtil.isNullOrEmpty(etCompany.getText().toString())
                || StringUtil.isNullOrEmpty(etPosition.getText().toString())) {
            ((TextView) ((CommonFragActivity) getActivity()).getTitleBar()
                    .getButton(TAG_SAVE)).setTextColor(getResources().getColor(
                    R.color.color_f2));
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
     * 点击"编辑公司信息"跳转到公司详情
     */
    @OnClick(R.id.tvGotoCompanyDetail)
    public void onClickBtnCDetail() {
        if (StringUtil.isNullOrEmpty(etCompany.getText().toString())) {
            ToastUtil.showShort("公司名称不能为空");
            return;
        }
        if (StringUtil.isNullOrEmpty(etPosition.getText().toString())) {
            ToastUtil.showShort("公司职务不能为空");
            return;
        }
        company.name = etCompany.getText().toString();
        company.position = etPosition.getText().toString();
        FragUserCompanyCreateOrUpdate.invoke(getActivity(), company,
                FragUserCompanyCreateOrUpdate.TYPE_EDIT);
    }

    /**
     * 点击删除任职按钮
     */
    @OnClick(R.id.tvBtnText)
    public void onClickBtnText() {
        DialogUtil.showDialog(getActivity(), "确定删除此任职吗", "",
                new OnCommonDialogBtnListener() {

                    @Override
                    public void onClick(CommonDialog dialog) {
                        dialog.dismiss();
                    }
                }, new OnCommonDialogBtnListener() {

                    @Override
                    public void onClick(CommonDialog dialog) {
                        dialog.dismiss();
                        deletePosition();
                    }
                });
    }

    /**
     * 点击 SwitchButton 设为默认任职，并调用接口直接提交这个变更
     */
    @OnClick(R.id.btnSwitch)
    public void onClickSwitch() {
        hasEdited = true;
        isDefault = !isDefault;
        if (isDefault) {
            btnSwitch.setImageResource(R.drawable.switch_on);
        } else {
            btnSwitch.setImageResource(R.drawable.switch_off);
        }
        company.isDefault = isDefault ? 1 : 0;
    }

    /**
     * 根据公司的type(商业，非商业，社会组织)来获取对应的logo
     *
     * @param type
     * @return
     */
    private int getRes(String type, ArrayList<CompanyType> types) {
        if (types == null || type == null) {
            return R.drawable.profile_img_other_selected;
        }
        for (CompanyType ct : types) {
            if (ct.tagId.equals(type)) {
                if (CompanyType.KEY_COMMERCE.equals(ct.tagId)) {
                    return R.drawable.profile_img_business_selected;
                } else if (CompanyType.KEY_UN_COMMERCE.equals(ct.tagId)) {
                    return R.drawable.profile_img_nonbusiness_selected;
                } else if (CompanyType.KEY_SOCIETY.equals(ct.tagId)) {
                    return R.drawable.profile_img_community_selected;
                } else if (CompanyType.KEY_ZHISLAND_ORG.equals(ct.tagId)) {
                    return R.drawable.profile_img_zhisland_selected;
                } else {
                    return R.drawable.profile_img_other_selected;
                }
            }

        }
        return -1;
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

    public void onEventMainThread(EBCompany eb) {
        if (eb.getType() == EBCompany.TYPE_COMPANY_EDIT) {
            if (getActivity() != null)
                getActivity().finish();
        }
    }
}
