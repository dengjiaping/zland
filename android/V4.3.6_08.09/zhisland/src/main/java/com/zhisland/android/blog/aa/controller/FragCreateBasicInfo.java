package com.zhisland.android.blog.aa.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.City;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.Dict;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.util.AvatarUploader;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.CityPickDlg;
import com.zhisland.android.blog.profile.controller.FragSelectTagsList;
import com.zhisland.android.blog.setting.eb.EBLogout;
import com.zhisland.android.blog.tracker.bean.TrackerAlias;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 创建正和岛微名片
 */
public class FragCreateBasicInfo extends FragBase {

    private static final String INK_CONTINUE_REGISTER = "ink_continue_register";

    private static final int REQ_CATEGORY = 1;
    private static final int REQ_IMAGE = 115;

    @InjectView(R.id.ivUserAvatar)
    ImageView ivUserAvatar;
    //姓名
    @InjectView(R.id.tvUserName)
    public TextView tvUserName;
    @InjectView(R.id.etUserName)
    public EditText etUserName;
    @InjectView(R.id.lineName)
    public View lineName;
    //性别
    @InjectView(R.id.rbSexMan)
    public RadioButton rbSexMan;
    @InjectView(R.id.rbSexWoman)
    public RadioButton rbSexWoman;
    //公司
    @InjectView(R.id.etUserCompany)
    public EditText etUserCompany;
    @InjectView(R.id.lineCompany)
    public View lineCompany;
    //职位
    @InjectView(R.id.etUserPosition)
    public EditText etUserPosition;
    @InjectView(R.id.linePosition)
    public View linePosition;
    //行业
    @InjectView(R.id.tvUserIndustryContent)
    public TextView tvUserIndustryContent;
    //城市
    @InjectView(R.id.tvCityContent)
    public TextView tvCityContent;
    //认证icon
    @InjectView(R.id.ivAuth)
    public ImageView ivAuth;

    private User userSelf;
    private ArrayList<ZHDicItem> tagIndustry = new ArrayList<>();
    private CityPickDlg cityPickDlg;
    private AProgressDialog progressDialog;

    /**
     * @param isShowContinueRegister 是否显示继续注册dialog
     */
    public static void invoke(Context context, boolean isShowContinueRegister) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragCreateBasicInfo.class;
        param.title = "创建正和岛名片";
        param.enableBack = false;
        param.swipeBackEnable = false;
        Intent intent = CommonFragActivity.createIntent(context, param);
        intent.putExtra(INK_CONTINUE_REGISTER, isShowContinueRegister);
        context.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_basic_info, container, false);
        ButterKnife.inject(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (!PrefUtil.Instance().getShowGuideBasicInfo()) {
            GuideInfo.invoke(getActivity());
            getActivity().finish();
        }
        super.onActivityCreated(savedInstanceState);
        KillSelfMgr.getInstance().setCurrentPage(KillSelfMgr.FRAG_CREATE_BASIC_INFO);
        boolean isShowContinueRegister = getActivity().getIntent().getBooleanExtra(INK_CONTINUE_REGISTER, false);
        if (isShowContinueRegister) {
            DialogUtil.getInstatnce().showContinueRegisterDialog(getActivity(), "个人信息填写未完成", "完整的个人信息，有利于系统为您自动匹配同行与需求", "返回登录", "继续完善信息");
        }

        userSelf = DBMgr.getMgr().getUserDao().getSelfUser();

        initView();
        fillUser();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        etUserPosition.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    SoftInputUtil.hideInput(getActivity());
                    return true;
                }
                return false;
            }
        });
    }

    private void fillUser() {
        if (userSelf != null) {
            ImageWorkFactory.getCircleFetcher().loadImage(userSelf.userAvatar, ivUserAvatar, R.drawable.avatar_default);

            if (!StringUtil.isNullOrEmpty(userSelf.name)) {
                etUserName.setText(userSelf.name);
            }

            if (userSelf.sex != null) {
                if (userSelf.sex == User.VALUE_SEX_WOMAN) {
                    rbSexMan.setChecked(false);
                    rbSexWoman.setChecked(true);
                } else {
                    rbSexMan.setChecked(true);
                    rbSexWoman.setChecked(false);
                }
            }

            if (!StringUtil.isNullOrEmpty(userSelf.userCompany)) {
                etUserCompany.setText(userSelf.userCompany);
            }

            if (!StringUtil.isNullOrEmpty(userSelf.userPosition)) {
                etUserPosition.setText(userSelf.userPosition);
            }

            if (userSelf.industry != null) {
                tagIndustry.add(userSelf.industry);
            }
            setIndustryContent();

            if (userSelf.cityId != null && userSelf.provinceId != null) {
                String cityName = City.getNameByCode(userSelf.cityId);
                String provinceName = City.getNameByCode(userSelf.provinceId);
                setCityText(provinceName, cityName);

                if (cityPickDlg == null) {
                    cityPickDlg = new CityPickDlg(getActivity(), cityCallBack, "请选择您的常驻城市");
                }
                cityPickDlg.setCity(userSelf.provinceId, userSelf.cityId);
            }

            //用户是认证用户，姓名、公司、职位不可修改,并显示已认证图标
            if (userSelf.isVip() || userSelf.isVipBefore()) {
                etUserName.setEnabled(false);
                lineName.setVisibility(View.GONE);
                etUserCompany.setEnabled(false);
                lineCompany.setVisibility(View.GONE);
                etUserPosition.setEnabled(false);
                linePosition.setVisibility(View.GONE);
                ivAuth.setVisibility(View.VISIBLE);
            } else {
                ivAuth.setVisibility(View.GONE);
            }
        }
    }

    CityPickDlg.CallBack cityCallBack = new CityPickDlg.CallBack() {

        @Override
        public void OkClick(int provinceId, String provinceName, int cityId, String cityName) {
            userSelf.provinceId = provinceId;
            userSelf.cityId = cityId;
            setCityText(provinceName, cityName);
        }
    };

    //设置城市text
    private void setCityText(String provinceName, String cityName) {
        if (provinceName != null
                && provinceName.equals(cityName)) {
            tvCityContent.setText(provinceName);
        } else {
            tvCityContent.setText(provinceName + " " + cityName);
        }
    }

    @OnClick(R.id.ivUserAvatar)
    void onAvatarClick() {
        MultiImgPickerActivity.invoke(getActivity(), 1, 1, REQ_IMAGE);
    }


    @OnClick(R.id.rbSexMan)
    void sexNanClick() {
        rbSexMan.setChecked(true);
        rbSexWoman.setChecked(false);
    }

    @OnClick(R.id.rbSexWoman)
    void sexNvClick() {
        rbSexMan.setChecked(false);
        rbSexWoman.setChecked(true);
    }

    @OnClick(R.id.tvUserIndustryContent)
    void industryClick() {
        FragSelectTagsList.invoke(getActivity(), REQ_CATEGORY, Dict.getInstance().getProfileIndustry(), tagIndustry, "行业类型", 1);
    }

    @OnClick(R.id.tvCityContent)
    void cityClick() {
        if (cityPickDlg == null) {
            cityPickDlg = new CityPickDlg(getActivity(), cityCallBack, "请选择您的常驻城市");
        }
        cityPickDlg.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null)
                return;
            switch (requestCode) {
                case REQ_CATEGORY:
                    ArrayList<ZHDicItem> tags = (ArrayList<ZHDicItem>) data
                            .getSerializableExtra(FragSelectTagsList.INTENT_KEY_RETURN_LIST);
                    if (tags != null) {
                        tagIndustry = tags;
                        setIndustryContent();
                    }
                    break;
                case REQ_IMAGE:
                    List<String> paths = (List<String>) data
                            .getSerializableExtra(MultiImgPickerActivity.RLT_PATHES);
                    String localUrl = paths.get(0);
                    ImageWorkFactory.getCircleFetcher().loadImage(localUrl,
                            ivUserAvatar, R.drawable.avatar_default);
                    showProgress("正在上传您的头像...");
                    AvatarUploader.instance().uploadAvatar(localUrl, onUploaderCallback);
                    break;
            }
        }

    }

    AvatarUploader.OnUploaderCallback onUploaderCallback = new AvatarUploader.OnUploaderCallback() {

        @Override
        public void callBack(String backUrl) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (StringUtil.isNullOrEmpty(backUrl)) {
                ToastUtil.showShort("上传头像失败");
            } else {
                userSelf.userAvatar = backUrl;
            }
        }
    };

    private void showProgress(String content) {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            AvatarUploader.instance().loadFinish();
                            ImageWorkFactory.getCircleFetcher().loadImage(
                                    userSelf.userAvatar, ivUserAvatar, R.drawable.avatar_default);
                        }
                    });
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
            progressDialog.setText(content);
        }
    }


    //设置行业Text
    private void setIndustryContent() {
        if (tagIndustry != null && tagIndustry.size() > 0) {
            tvUserIndustryContent.setText(tagIndustry.get(0).name);
        } else {
            tvUserIndustryContent.setText("");
        }
    }

    @OnClick(R.id.tvNext)
    void nextClick() {
        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_BASE_INFO_CONFIRM);
        if (checkSubmit()) {
            final User tmpUser = new User();
            tmpUser.uid = this.userSelf.uid;
            tmpUser.userAvatar = this.userSelf.userAvatar;
            tmpUser.name = etUserName.getText().toString().trim();
            tmpUser.sex = rbSexMan.isChecked() ? User.VALUE_SEX_MAN : User.VALUE_SEX_WOMAN;
            tmpUser.userCompany = etUserCompany.getText().toString().trim();
            tmpUser.userPosition = etUserPosition.getText().toString().trim();

            if (tagIndustry != null && tagIndustry.size() > 0) {
                tmpUser.industry = tagIndustry.get(0);
            }
            tmpUser.provinceId = this.userSelf.provinceId;
            tmpUser.cityId = this.userSelf.cityId;

            final AProgressDialog progressDialog = new AProgressDialog(
                    getActivity());
            progressDialog.show();
            ZHApis.getAAApi().updateBasicInfo(getActivity(), tmpUser, new TaskCallback<Integer>() {
                @Override
                public void onSuccess(Integer uploadState) {
                    DBMgr.getMgr().getUserDao().creatOrUpdateUserNotNull(tmpUser);
                    if (uploadState == User.UPLOAD_CONTACT) {
                        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_BASE_INFO_CLICK_RESULT, "已上传通讯录");
                        // 已上传过通讯录，直接进主页
                        PrefUtil.Instance().setIsCanLogin(true);
                        HomeUtil.NavToHome(getActivity());
                        getActivity().finish();
                    } else {
                        ZhislandApplication.trackerClickEvent(getPageName(), TrackerAlias.CLICK_BASE_INFO_CLICK_RESULT, "未上传通讯录");
                        // 未上传过通讯录跳转授权通讯录页面
                        FragVisitContact.invoke(getActivity(), false);
                        getActivity().finish();
                    }
                }

                @Override
                public void onFailure(Throwable error) {

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    progressDialog.dismiss();
                }
            });

        }
    }

    /**
     * 检测是否可提交,并提示没有填写的项
     */
    private boolean checkSubmit() {
        boolean ret = false;

        String userName = etUserName.getText().toString().trim();
        String userCompany = etUserCompany.getText().toString().trim();
        String userPosition = etUserPosition.getText().toString().trim();

        if (StringUtil.isNullOrEmpty(userSelf.userAvatar)) {
            ToastUtil.showShort("请上传您的头像");
        } else if (StringUtil.isNullOrEmpty(userName)) {
            ToastUtil.showShort("请输入您的姓名");
        } else if (!rbSexMan.isChecked() && !rbSexWoman.isChecked()) {
            ToastUtil.showShort("请选择您的称呼");
        } else if (StringUtil.isNullOrEmpty(userCompany)) {
            ToastUtil.showShort("请输入您的公司名称");
        } else if (StringUtil.isNullOrEmpty(userPosition)) {
            ToastUtil.showShort("请输入您的当前职位");
        } else if (tagIndustry == null || tagIndustry.size() == 0) {
            ToastUtil.showShort("请选择您的所在行业");
        } else if (userSelf.cityId == null && userSelf.provinceId == null) {
            ToastUtil.showShort("请选择您的常驻城市");
        } else {
            ret = true;
        }

        return ret;
    }

    /**
     * 监听退出登录命令
     */
    public void onEventMainThread(EBLogout eb) {
        HomeUtil.toRegisterAndFinishSelf(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected String getPageName() {
        return "EntranceBaseInfo";
    }
}
