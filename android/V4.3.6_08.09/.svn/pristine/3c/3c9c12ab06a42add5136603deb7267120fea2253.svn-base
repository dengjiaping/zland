package com.zhisland.android.blog.profile.controller.position;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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
import com.zhisland.android.blog.common.dto.City;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.util.AvatarUploader;
import com.zhisland.android.blog.common.util.AvatarUploader.OnUploaderCallback;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.CityPickDlg;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.CountEditText;
import com.zhisland.android.blog.common.view.FlowRadioGroup;
import com.zhisland.android.blog.common.view.select.FragSelectActivity;
import com.zhisland.android.blog.profile.api.ZHApiProfile;
import com.zhisland.android.blog.profile.dto.Company;
import com.zhisland.android.blog.profile.dto.CompanyState;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageLoadListener;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.text.ZHLink;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加或编辑公司信息
 */
public class FragUserCompanyCreateOrUpdate extends FragBase {

    /**
     * 添加公司信息的 type 标识
     */
    public final static int TYPE_ADD = 0x1000;

    /**
     * 编辑公司信息的 type 标识
     */
    public final static int TYPE_EDIT = 0x1100;

    public static final String INK_USER_COMPANY = "ink_user_position";

    public static final String INK_TYPE = "ink_type";

    /**
     * 标题返回 tagid
     */
    private final static int TAG_BACK = 410;

    /**
     * 标题保存 tagid
     */
    private final static int TAG_SAVE = 411;

    /**
     * 选logo图像的requestCode
     */
    private static final int REQ_IMAGE = 0;

    /**
     * 选行业的requestCode
     */
    private static final int REQ_INDUSTRY = 1;

    /**
     * 公司描述最大数量字符
     */
    private static final int MAX_COMPANY_DESC_COUNT = 500;

    @InjectView(R.id.tvCompanName)
    TextView tvCompanName;

    @InjectView(R.id.tvCompanyCity)
    TextView tvCompanyCity;

    @InjectView(R.id.tvCompanyIndustry)
    TextView tvCompanyIndustry;

    @InjectView(R.id.rgCompanyType)
    FlowRadioGroup rgCompanyType;

    @InjectView(R.id.rlCompanyShareCode)
    View rlCompanyShareCode;

    @InjectView(R.id.vDiv4)
    View vDiv4;

    @InjectView(R.id.rlEtCompanyDesc)
    CountEditText rlEtCompanyDesc;

    @InjectView(R.id.etCompanyShareCode)
    EditText etCompanyShareCode;

    @InjectView(R.id.etCompanyUrl)
    EditText etCompanyUrl;

    @InjectView(R.id.ivCompanyLogo)
    ImageView ivCompanyLogo;

    private Company company;

    private int type;
    /**
     * 图片上传时用到的进度加载框
     */
    private AProgressDialog dialog;

    /**
     * 城市选择
     */
    private CityPickDlg cityPickDlg;

    /**
     * 用户是否修改信息
     */
    private boolean hasEdited;

    /**
     * 公司类型是否初始化完毕
     */
    private boolean raidoButtonHasInited;

    private ArrayList<CompanyState> companyStates;

    private AProgressDialog progressDialog;

    public static void invoke(Context context, Company company, int type) {
        Intent intent = CommonFragActivity.createIntent(context,
                createFragParams(context, type));
        intent.putExtra(INK_USER_COMPANY, company);
        intent.putExtra(INK_TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 创建TiterBar的Frag参数
     *
     * @param context
     * @param type
     * @return
     */
    private static CommonFragParams createFragParams(Context context, int type) {
        CommonFragParams param = new CommonFragParams();
        param.clsFrag = FragUserCompanyCreateOrUpdate.class;
        param.swipeBackEnable = false;
        //两种情况的方案待定，暂时用"公司信息"
        if (type == TYPE_EDIT)
            param.title = "编辑公司信息";
        else if (type == TYPE_ADD)
            param.title = "添加公司信息";
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

    /**
     * titerBar中的按钮点击事件
     *
     * @return
     */
    private static TitleRunnable createRunnable() {
        return new TitleRunnable() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void execute(Context context, Fragment fragment) {
                if (tagId == TAG_SAVE) {
                    ((FragUserCompanyCreateOrUpdate) fragment).save();
                } else if (tagId == TAG_BACK) {
                    ((FragUserCompanyCreateOrUpdate) fragment).back();
                }
            }
        };
    }

    /**
     * 空构造函数得保留
     */
    public FragUserCompanyCreateOrUpdate() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        company = (Company) getActivity().getIntent().getSerializableExtra(
                INK_USER_COMPANY);
        companyStates = Dict.getInstance().getCompanyState();
        type = getActivity().getIntent().getIntExtra(INK_TYPE, 0);
        View root = inflater.inflate(R.layout.user_company_add, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        ButterKnife.inject(this, root);
        initView();
        initData();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        updateCountEditText();
        updateChanged();
        updateRaidoGroup();
    }

    /**
     * 对公司描述自定义EditText进行各种属性的配置
     */
    private void updateCountEditText() {
        rlEtCompanyDesc.setMaxCount(MAX_COMPANY_DESC_COUNT);
        rlEtCompanyDesc.setGravity(Gravity.TOP);
        rlEtCompanyDesc.setHint("介绍下公司吧");
        rlEtCompanyDesc.setEditTextColor(getResources().getColor(
                R.color.color_f1));
        rlEtCompanyDesc.getEditText().setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.txt_16));
        rlEtCompanyDesc.getEditText().setHintTextColor(
                getResources().getColor(R.color.color_div));
    }

    /**
     * 为所有的TextView 和 EditView加入 Text改变监听
     */
    private void updateChanged() {
        etCompanyShareCode
                .addTextChangedListener(createEditTextChangedListener());
        etCompanyUrl.addTextChangedListener(createEditTextChangedListener());
        rlEtCompanyDesc.getEditText().addTextChangedListener(
                createEditTextChangedListener());
        tvCompanyCity.addTextChangedListener(createEditTextChangedListener());
        tvCompanyIndustry
                .addTextChangedListener(createEditTextChangedListener());
    }

    /**
     * 初始化流式布局的RaidoGroup
     */
    private void updateRaidoGroup() {
        rlCompanyShareCode.setVisibility(View.GONE);
        final ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<RadioButton> buttons = new ArrayList<RadioButton>();
        for (int i = 0; i < companyStates.size(); i++) {
            RadioButton rbutton = new RadioButton(getActivity());
            rbutton.setText(companyStates.get(i).tagName);
            rbutton.setButtonDrawable(R.drawable.radio_selector);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.leftMargin = DensityUtil.dip2px(10);
            llp.topMargin = DensityUtil.dip2px(5);
            rbutton.setLayoutParams(llp);
            rbutton.setPadding(DensityUtil.dip2px(4), DensityUtil.dip2px(5),
                    DensityUtil.dip2px(10), DensityUtil.dip2px(5));
            rbutton.setTextColor(getResources().getColor(R.color.color_f2));
            DensityUtil.setTextSize(rbutton, R.dimen.txt_14);
            int id = 0;
            if (Build.VERSION.SDK_INT >= 17) {
                id = View.generateViewId();
            } else {
                id = (int) (System.currentTimeMillis() * 10000 + new Random(10000).nextInt());
            }
            ids.add(id);
            rbutton.setId(id);
            rgCompanyType.addView(rbutton);
            buttons.add(rbutton);
        }
        rgCompanyType.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = ids.indexOf(checkedId);
                company.stage = companyStates.get(index).tagId;
                if (CompanyState.FLAG_LAUNCHED.equals(companyStates.get(index).custom)) {
                    rlCompanyShareCode.setVisibility(View.VISIBLE);
                    if (company.stockCode != null) {
                        etCompanyShareCode.setText(company.stockCode);
                    }
                } else {
                    rlCompanyShareCode.setVisibility(View.GONE);
                }
            }
        });
        if (company != null && company.stage != null) {
            for (int i = 0; i < companyStates.size(); i++) {
                if (companyStates.get(i).tagId.equals(company.stage)) {
                    buttons.get(i).setChecked(true);
                    if (CompanyState.FLAG_LAUNCHED
                            .equals(companyStates.get(i).custom)) {
                        rlCompanyShareCode.setVisibility(View.VISIBLE);
                        if (company.stockCode != null) {
                            etCompanyShareCode.setText(company.stockCode);
                            break;
                        }
                    } else {
                        rlCompanyShareCode.setVisibility(View.GONE);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 初始化公司的信息
     */
    private void initData() {
        // 公司名字
        tvCompanName.setText(company.name == null ? "" : company.name);
        // 公司描述
        rlEtCompanyDesc.setText(company.desc == null ? "" : company.desc);
        // 公司官网
        etCompanyUrl.setText(company.website == null ? "" : company.website);
        // 公司行业
        if (company.industry != null) {
            tvCompanyIndustry.setText(company.industry.name);
        }
        // 公司所在地
        if (company.provinceId != null && company.cityId != null) {
            String cityName = City.getNameByCode(company.cityId);
            String provinceName = City.getNameByCode(company.provinceId);
            if (provinceName != null && provinceName.equals(cityName)) {
                tvCompanyCity.setText(provinceName);
            } else {
                tvCompanyCity.setText((provinceName == null ? ""
                        : (provinceName + " "))
                        + (cityName == null ? "" : cityName));
            }

        }
        // 公司Logo
        if (company.logo != null) {
            ImageWorkFactory.getFetcher().loadImage(company.logo,
                    ivCompanyLogo, R.drawable.img_campany_default_logo,
                    new ImageLoadListener() {

                        @Override
                        public void onLoadFinished(String url, int status) {
                            hasEdited = false;
                        }
                    }, true);
        }
        hasEdited = false;
    }

    /**
     * 点titerbar保存
     */
    private void save() {
        if (!check()) {
            return;
        }
        company.name = tvCompanName.getText().toString();
        company.desc = rlEtCompanyDesc.getText().toString();
        company.website = etCompanyUrl.getText().toString();
        company.stockCode = etCompanyShareCode.getText().toString();

        final CommonDialog saveDlg = new CommonDialog(getActivity());
        saveDlg.show();
        saveDlg.setTitle("确定要保存公司信息");
        saveDlg.setLeftBtnContent("取消");
        saveDlg.setRightBtnContent("确定");
        saveDlg.tvDlgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDlg.dismiss();
            }
        });
        saveDlg.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCompany();
                saveDlg.dismiss();
            }
        });
    }

    /**
     * 点titerbar返回
     */
    private void back() {
        if (!check()) {
            return;
        }
        if (hasEdited) {
            final CommonDialog saveDlg = new CommonDialog(getActivity());
            saveDlg.show();
            saveDlg.setTitle("是否要保存公司信息");
            saveDlg.setLeftBtnContent("取消");
            saveDlg.setRightBtnContent("确定");
            saveDlg.tvDlgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDlg.dismiss();
                    if (getActivity() != null)
                        getActivity().finish();
                }
            });
            saveDlg.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveCompany();
                    saveDlg.dismiss();
                    if (getActivity() != null)
                        getActivity().finish();
                }
            });
        } else {
            if (getActivity() != null)
                getActivity().finish();
        }
    }

        /**
         * 提交(保存或修改)公司信息 根据type去调用接口 并将信息状态变为已保存
         */

    private void saveCompany() {
        if (type == TYPE_ADD) {
            createCompany(company);
        } else if (type == TYPE_EDIT) {
            updateCompany();
        }
        hasEdited = false;
    }

    @Override
    public boolean onBackPressed() {
        back();
        return true;
    }

    /**
     * 将城市设置到View上
     */
    private void setCityTxt(String city, String province) {
        if (city != null && province != null && hasCityAndProince()) {
            if (cityPickDlg == null) {
                cityPickDlg = new CityPickDlg(getActivity(), cityCallBack, "请选择公司所在城市");
            }
            if (company.provinceId != null && company.cityId != null) {
                cityPickDlg.setCity(company.provinceId, company.cityId);
                city = cityPickDlg.getCityName();
                province = cityPickDlg.getProvinceName();
            }
        } else {
            return;
        }
        if (city.equals(province)) {
            tvCompanyCity.setText(province);
        } else {
            tvCompanyCity.setText(province + " " + city);
        }
    }

    CityPickDlg.CallBack cityCallBack = new CityPickDlg.CallBack() {

        @Override
        public void OkClick(int provinceId, String provinceName, int cityId, String cityName) {
            company.cityId = cityId;
            company.provinceId = provinceId;
            setCityTxt(cityName, provinceName);
        }
    };

    /**
     * 判断公司是否有 城市和省份数据
     *
     * @return
     */
    private boolean hasCityAndProince() {
        if (company != null && company.provinceId != null
                && company.cityId != null && company.provinceId > 0
                && company.cityId > 0) {
            return true;
        }
        return false;
    }

    /**
     * 对界面中的TextView 和 EditView 创建textChanged的监听，以便检验用户是否已编辑过信息
     *
     * @return
     */
    private TextWatcher createEditTextChangedListener() {
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
                hasEdited = true;
            }
        };
    }

    /**
     * 创建图片上传的回调方法
     *
     * @return
     */
    private OnUploaderCallback createCallback() {
        return new OnUploaderCallback() {

            @Override
            public void callBack(String backUrl) {
                cancelDialog();
                if (StringUtil.isNullOrEmpty(backUrl)) {
                    ToastUtil.showShort("上传Logo图片失败");

                    if (StringUtil.isNullOrEmpty(company.logo)) {
                        ivCompanyLogo.setImageResource(R.drawable.bg_add_photo);
                    } else {
                        ImageWorkFactory.getFetcher().loadImage(company.logo,
                                ivCompanyLogo,
                                R.drawable.img_campany_default_logo);
                    }
                } else {
                    company.logo = backUrl;
                }
            }
        };
    }

    private void cancelDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }

    private void showProgress() {
        if (dialog == null) {
            dialog = new AProgressDialog(getActivity());
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    AvatarUploader.instance().loadFinish();
                }
            });
        }
        if (!dialog.isShowing()) {
            dialog.show();
            dialog.setText("正在上传您的Logo图片...");
        }
    }

    /**
     * 提交前检查用户所填写的信息
     *
     * @return
     */
    private boolean check() {
        String ret = "";
        if (StringUtil.isNullOrEmpty(tvCompanyCity.getText().toString())) {
            ret = "坐落城市不能为空";
        } else if (StringUtil.isNullOrEmpty(tvCompanyIndustry.getText()
                .toString())) {
            ret = "经营行业不能为空";
        }
        // 判断RadioGroup是否一个都没有选中
        else if (rgCompanyType.getCheckedRadioButtonId() == -1) {
            ret = "公司类型不能为空，例如是否为上市";
        } else if (StringUtil.isNullOrEmpty(rlEtCompanyDesc.getText().trim())) {
            ret = "公司介绍不能为空";
        } else if (!StringUtil.isNullOrEmpty(etCompanyUrl.getText().toString()
                .trim())) {
            String url = etCompanyUrl.getText().toString();
            if (!ZHLink.isValidTitleAndIsUrl(url) && !ZHLink.isValidUrl(url)) {
                ret = "公司官网网址格式有误";
            }
        }
        if (!StringUtil.isNullOrEmpty(ret)) {
            final CommonDialog dlg = new CommonDialog(getActivity());
            dlg.show();
            dlg.setTitle(ret);
            dlg.setLeftBtnContent("放弃");
            dlg.setRightBtnContent("继续完善信息");
            dlg.tvDlgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
                    if (getActivity() != null)
                        getActivity().finish();
                }
            });
            dlg.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.dismiss();
                }
            });
            return false;
        }
        return true;
    }

    /**
     * 编辑更新公司(接口调用)
     */
    private void updateCompany() {
        showProgressDialog("保存中...");
        ZHApiProfile.Instance().updateCompany(getActivity(), company,
                new TaskCallback<Object>() {

                    @Override
                    public void onSuccess(Object content) {
                        ToastUtil.showShort("公司信息修改成功。");
                        hasEdited = false;
                        if (getActivity() != null)
                            getActivity().finish();
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
     * 创建任职(组织好数据后调用接口)
     *
     * @param company
     */
    private void createCompany(final Company company) {
        showProgressDialog("保存中...");
        // 然后调用接口
        ZHApiProfile.Instance().creatCompany(getActivity(), company, new TaskCallback<Long>() {

            @Override
            public void onSuccess(Long content) {
                if (getActivity() != null)
                    getActivity().finish();
                hasEdited = false;
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
     * 用户通过RadioButton改变公司类型后，信息的编辑状态设置为已编辑
     */
    private void checkCompanyTypeChanged() {
        if (!raidoButtonHasInited) {
            raidoButtonHasInited = true;
        } else {
            hasEdited = true;
        }
    }

    @OnClick(R.id.tvCompanyIndustry)
    public void onEtIndustryClick() {
        ArrayList<ZHDicItem> list = new ArrayList<ZHDicItem>();
        list.add(company.industry);
        SelectCompanyIndustry.launch(getActivity(), list, REQ_INDUSTRY);
    }

    @OnClick(R.id.ivCompanyLogo)
    public void onLogoClick() {
        MultiImgPickerActivity.invoke(getActivity(), ivCompanyLogo.getWidth(),
                ivCompanyLogo.getHeight(), REQ_IMAGE);
    }

    @OnClick(R.id.tvCompanyCity)
    public void onClickCity() {
        if (cityPickDlg == null) {
            cityPickDlg = new CityPickDlg(getActivity(), cityCallBack, "请选择公司所在城市");
        }
        if (company != null && company.provinceId != null
                && company.cityId != null) {
            cityPickDlg.setCity(company.provinceId, company.cityId);
        }
        cityPickDlg.show();
    }

    @Override
    protected String getPageName() {
        String pageName = "";
        switch (type){
            case TYPE_ADD:
                pageName = "ProfileCompanyCreate";
                break;
            case TYPE_EDIT:
                pageName = "ProfileCompanyEdit";
                break;
        }
        return pageName;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                // 选取公司logo
                case REQ_IMAGE:
                    List<String> paths = (List<String>) data
                            .getSerializableExtra(MultiImgPickerActivity.RLT_PATHES);
                    String localUrl = paths.get(0);
                    ImageWorkFactory.getFetcher()
                            .loadImage(localUrl, ivCompanyLogo);
                    showProgress();
                    AvatarUploader.instance().uploadAvatar(localUrl,
                            createCallback());
                    break;
                // 选取公司的行业类型
                case REQ_INDUSTRY:
                    List<ZHDicItem> list = (List<ZHDicItem>) data
                            .getSerializableExtra(FragSelectActivity.INK_RESULT);
                    if (list != null && list.size() > 0) {
                        company.industry = list.get(0);
                        tvCompanyIndustry.setText(company.industry.name);
                    }
                    break;
            }

        }
    }

//	/**
//	 * 城市选择界面内部类
//	 *
//	 * @author zhangxiang
//	 *
//	 */
//	class CityHolder {
//
//		private Dialog cityDialog;
//
//		@InjectView(R.id.startCityPicker)
//		CityPicker cityPicker;
//
//		@InjectView(R.id.tvCityPickerNote)
//		TextView tvCityPickerNote;
//
//		@InjectView(R.id.btnCityOk)
//		TextView tvCityPickerOk;
//
//		private Company company;
//
//		public CityHolder(final Context context) {
//			View cityPickerView = LayoutInflater.from(context).inflate(
//					R.layout.city_picker, null);
//			cityDialog = new Dialog(context, R.style.ActionDialog);
//			cityDialog.setContentView(cityPickerView);
//			WindowManager.LayoutParams wmlp = cityDialog.getWindow()
//					.getAttributes();
//			wmlp.gravity = Gravity.BOTTOM;
//			wmlp.width = DensityUtil.getWidth();
//			ButterKnife.inject(this, cityDialog);
//			tvCityPickerNote.setText("请选择活动所在城市");
//		}
//
//		@OnClick(R.id.btnCityOk)
//		void OkClick() {
//			company.cityId = cityPicker.getCityId();
//			company.provinceId = cityPicker.getProvinceId();
//			setCityTxt(cityPicker.getCityName(), cityPicker.getProvinceName());
//			cityDialog.dismiss();
//		}
//
//		public void show(Company company) {
//			this.company = company;
//			setCity(company);
//			cityDialog.show();
//		}
//
//		public void setCity(Company company) {
//			if (company != null && company.provinceId != null
//					&& company.cityId != null) {
//				cityPicker.setCity(company.provinceId, company.cityId);
//			}
//		}
//	}

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
