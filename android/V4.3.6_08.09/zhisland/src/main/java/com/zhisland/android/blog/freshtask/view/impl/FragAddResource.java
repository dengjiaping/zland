package com.zhisland.android.blog.freshtask.view.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.CommonFragActivity;
import com.zhisland.android.blog.common.dto.ZHDicItem;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.util.SoftInputUtil;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.select.FragSelectActivity;
import com.zhisland.android.blog.freshtask.model.impl.AddResourceModel;
import com.zhisland.android.blog.freshtask.presenter.AddResourcePresenter;
import com.zhisland.android.blog.freshtask.view.IAddResourceView;
import com.zhisland.android.blog.freshtask.view.impl.holder.AddResourceHolder;
import com.zhisland.android.blog.profile.controller.resource.SelectResourcIndustryTag;
import com.zhisland.android.blog.profile.controller.resource.SelectSupplyCategoryTag;
import com.zhisland.android.blog.profile.dto.Resource;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Mr.Tui on 2016/5/28.
 */
public class FragAddResource extends FragBaseMvp<AddResourcePresenter> implements IAddResourceView, AddResourceHolder.ClickListener {

    private static final int TAG_ID_RIGHT = 101;

    private static final int REQ_INDUSTRY = 1987;
    private static final int REQ_CATEGORY = 1988;

    private static final int MAX_TEXT_COUNT = 200;

    private AddResourceHolder viewHolder = new AddResourceHolder();

    private AProgressDialog progressDialog;

    @Override
    protected String getPageName() {
        return "RookieTaskAddResource";
    }

    public static void invoke(Context context) {
        CommonFragActivity.CommonFragParams param = new CommonFragActivity.CommonFragParams();
        param.clsFrag = FragAddResource.class;
        param.titleBtns = new ArrayList<CommonFragActivity.TitleBtn>();
        param.enableBack = true;
        param.runnable = titleRunnable;
        param.titleColor = R.color.white;
        param.btnBackResID = R.drawable.sel_btn_back_white_two;
        param.titleBackground = R.drawable.task_background_tabbar;
        param.hideBottomLine = true;
        CommonFragActivity.TitleBtn tb = new CommonFragActivity.TitleBtn(TAG_ID_RIGHT, CommonFragActivity.TitleBtn.TYPE_TXT);
        tb.btnText = "保存";
        tb.textColor = ZhislandApplication.APP_RESOURCE.getColor(R.color.white);
        param.titleBtns.add(tb);
        param.title = "发现商业价值";
        Intent intent = CommonFragActivity.createIntent(context, param);
        context.startActivity(intent);
    }


    static CommonFragActivity.TitleRunnable titleRunnable = new CommonFragActivity.TitleRunnable() {

        @Override
        protected void execute(Context context, Fragment fragment) {
            if (tagId == TAG_ID_RIGHT) {
                if (fragment != null && fragment instanceof FragAddResource) {
                    ((FragAddResource) fragment).presenter().saveSupply();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_add_resource, null);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        root.setLayoutParams(lp);
        viewHolder.setClickListener(this);
        ButterKnife.inject(viewHolder, root);
        return root;
    }

    @Override
    protected AddResourcePresenter createPresenter() {
        AddResourcePresenter presenter = new AddResourcePresenter();
        AddResourceModel model = new AddResourceModel();
        presenter.setModel(model);
        presenter.setResource(new Resource());
        return presenter;
    }

    @Override
    public void initViews() {
        DensityUtil.setTextSize(viewHolder.tvTitle, R.dimen.txt_14);
        viewHolder.tvTitle.setTextColor(getResources().getColor(R.color.color_f2));
        SpannableStringBuilder style = new SpannableStringBuilder("简单描述你的资源\n让有需求的岛邻主动找上门");
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_f1)),
                0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new RelativeSizeSpan(1.4f),
                0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvTitle.setText(style);
        viewHolder.tvContentTitle.setText("资源介绍");
        viewHolder.etContent.setHint("请输入资源详情");
        viewHolder.tvCount.setText(String.valueOf(MAX_TEXT_COUNT));
        EditTextUtil.limitEditTextLengthChinese(viewHolder.etContent, MAX_TEXT_COUNT,
                viewHolder.tvCount);
    }

    @Override
    public String getContent() {
        return viewHolder.etContent.getText().toString().trim();
    }

    @Override
    public void showProgressDialog(String content) {
        if (progressDialog == null) {
            progressDialog = new AProgressDialog(getActivity());
        }
        progressDialog.show();
        progressDialog.setText(content);
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void finishSelf() {
        getActivity().finish();
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            List<ZHDicItem> tags = (List<ZHDicItem>) data
                    .getSerializableExtra(FragSelectActivity.INK_RESULT);
            if (tags != null && tags.size() > 0) {
                if (requestCode == REQ_INDUSTRY) {
                    presenter().setResourceIndustry(tags.get(0));
                } else if (requestCode == REQ_CATEGORY) {
                    presenter().setResourceCategory(tags.get(0));
                }
            }
        }
    }

    public void setIndustryTag(String tagId, String tagName) {
        viewHolder.tvIndustry.setText(tagName == null ? "" : tagName);
        viewHolder.tvIndustry.setTextColor(getResources().getColor(R.color.color_f1));
        viewHolder.tvIndustry.setCompoundDrawables(null, null, null, null);
    }

    public void setCategoryTag(String tagId, String tagName) {
        viewHolder.tvCategory.setText(tagName == null ? "" : tagName);
        viewHolder.tvCategory.setTextColor(getResources().getColor(R.color.color_f1));
        viewHolder.tvCategory.setCompoundDrawables(null, null, null, null);
    }

    @Override
    public void goToSelectIndustry(ZHDicItem selectedItem) {
        SoftInputUtil.hideInput(getActivity());
        SelectResourcIndustryTag.launch(getActivity(),
                selectedItem, REQ_INDUSTRY);
    }

    @Override
    public void goToSelectCategory(ZHDicItem selectedItem) {
        SoftInputUtil.hideInput(getActivity());
        SelectSupplyCategoryTag.launch(getActivity(),
                selectedItem, REQ_CATEGORY);
    }

    @Override
    public void industryClick() {
        presenter().onIndustryClick();
    }

    @Override
    public void categoryClick() {
        presenter().onCategoryClick();
    }


}
