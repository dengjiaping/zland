package com.zhisland.android.blog.profilemvp.view.impl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.util.EditTextUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.EditPhoto;
import com.zhisland.android.blog.feed.bean.FeedPicture;
import com.zhisland.android.blog.profilemvp.model.impl.AddPhotoModel;
import com.zhisland.android.blog.profilemvp.presenter.AddPhotoPresenter;
import com.zhisland.android.blog.profilemvp.view.IAddPhotoView;
import com.zhisland.lib.mvp.view.FragBaseMvp;
import com.zhisland.lib.util.ScreenTool;
import com.zhisland.lib.view.dialog.AProgressDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnTouch;

/**
 * 发新鲜事
 */
public class FragAddPhoto extends FragBaseMvp<AddPhotoPresenter> implements IAddPhotoView {

    private static final int TAG_ID_RIGHT = 100;

    public static final String REQ_IMG = "req_img";
    private static int IMG_TYPE = 0;
    public static final int IMG_CAPTURE = 1; // 拍照TAB
    public static final int IMG_SELECT = 2;  // 选择图片

    @InjectView(R.id.etFeed)
    public EditText etFeed;
    @InjectView(R.id.llPhoto)
    public LinearLayout llPhoto;

    private EditPhoto epFeed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.frag_create_feed, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        IMG_TYPE = getActivity().getIntent().getIntExtra(REQ_IMG, 0);
        if (IMG_TYPE == IMG_CAPTURE || IMG_TYPE == IMG_SELECT) {
            epFeed.selectImage();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter().refreshRightBtnState();
        presenter().checkUpload();
    }

    private void initView() {
        etFeed.setHint("请输入照片说明，最多200字...");
        EditTextUtil.limitEditTextLengthChinese(etFeed, 200, null);
        epFeed = new EditPhoto(getActivity());
        epFeed.configRow(3, 3);
        epFeed.setBackgroundColor(Color.WHITE);
        llPhoto.addView(epFeed);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EditPhoto.REQ_CAPTURE_PHOTO:
                if (resultCode == Activity.RESULT_CANCELED)
                    this.getActivity().finish();
                break;
        }
        if (EditPhoto.isPhotoRequest(requestCode)) {
            this.epFeed.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public String getPageName() {
        return "FragAddPhoto";
    }

    @OnTouch(R.id.svCreateFeed)
    public boolean onTouchScrollView() {
        ScreenTool.HideInput(getActivity());
        return false;
    }

    @Override
    protected AddPhotoPresenter createPresenter() {
        AddPhotoPresenter presenter = new AddPhotoPresenter();
        AddPhotoModel model = new AddPhotoModel();
        presenter.setModel(model);
        return presenter;
    }

    @Override
    public String getFeedContent() {
        return etFeed.getText().toString().trim();
    }

    @Override
    public ArrayList<FeedPicture> getSelectedLocalPhotos() {
        return epFeed.getSelectedFiles();
    }

    @Override
    public boolean hasPicture() {
        ArrayList<FeedPicture> selectedFiles = epFeed.getSelectedFiles();
        return selectedFiles.size() > 0;
    }

    @Override
    public void setRightBtnEnable(boolean state) {
        ((ActAddPhoto) getActivity()).tvComplete.setEnabled(state);
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void showQuiteConfirmDlg() {
        final CommonDialog commonDialog = new CommonDialog(getActivity());
        commonDialog.show();
        commonDialog.setTitle("取消此次编辑？");
        commonDialog.setLeftBtnContent("取消");
        commonDialog.setRightBtnContent("确定");
        commonDialog.tvDlgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public EditPhoto getEditPhoto() {
        return epFeed;
    }

    @Override
    public AProgressDialog getProgressDlg() {
        return getProgressDialog();
    }

    /**
     * 点击取消或者back
     */
    public void back() {
        presenter().onQuitClicked();
    }

    /**
     * 发布feed
     */
    public void onPublishClicked() {
        presenter().onPublishClicked();
    }

}
