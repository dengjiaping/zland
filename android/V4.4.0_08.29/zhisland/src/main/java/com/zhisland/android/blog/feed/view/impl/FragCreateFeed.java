package com.zhisland.android.blog.feed.view.impl;

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
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.EditPhoto;
import com.zhisland.android.blog.feed.bean.FeedPicture;
import com.zhisland.android.blog.feed.model.impl.CreateFeedModel;
import com.zhisland.android.blog.feed.presenter.CreateFeedPresenter;
import com.zhisland.android.blog.feed.view.IFeedCreateView;
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
public class FragCreateFeed extends FragBaseMvp<CreateFeedPresenter> implements IFeedCreateView {

    public static final String REQ_IMG = "req_img";
    private static int IMG_TYPE = 0;
    public static final int IMG_CAPTURE = 1; // 拍照TAB
    public static final int IMG_SELECT = 2;  // 选择图片

    @InjectView(R.id.etFeed)
    public EditText etFeed;
    @InjectView(R.id.llPhoto)
    public LinearLayout llPhoto;

    private EditPhoto epFeed;
    private CreateFeedPresenter presenter;

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
        epFeed = new EditPhoto(getActivity());
        epFeed.configRow(3, 3);
        epFeed.setBackgroundColor(Color.WHITE);
        llPhoto.addView(epFeed);

        etFeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                presenter().refreshRightBtnState();
            }
        });
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
        return "FragCreateFeed";
    }

    @OnTouch(R.id.svCreateFeed)
    public boolean onTouchScrollView() {
        ScreenTool.HideInput(getActivity());
        return false;
    }

    @Override
    protected CreateFeedPresenter createPresenter() {
        presenter = new CreateFeedPresenter();
        CreateFeedModel model = new CreateFeedModel();
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
        ((ActCreateFeed) getActivity()).tvComplete.setEnabled(state);
    }

    @Override
    public void onOkClicked(String tag, Object arg) {
        presenter.onConfirmOkClicked(tag, arg);
    }

    @Override
    public void onNoClicked(String tag, Object arg) {
        presenter.onConfirmNoClicked(tag, arg);
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
