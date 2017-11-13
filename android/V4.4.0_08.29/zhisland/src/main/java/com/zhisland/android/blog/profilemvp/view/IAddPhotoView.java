package com.zhisland.android.blog.profilemvp.view;

import com.zhisland.android.blog.common.view.EditPhoto;
import com.zhisland.android.blog.feed.bean.FeedPicture;
import com.zhisland.lib.mvp.view.IMvpView;
import com.zhisland.lib.view.dialog.AProgressDialog;

import java.util.ArrayList;

/**
 * 发布新鲜事
 */
public interface IAddPhotoView extends IMvpView {

    /**
     * 获取输入的feed 内容
     */
    String getFeedContent();

    /**
     * 获取所选的图片本地地址
     */
    ArrayList<FeedPicture> getSelectedLocalPhotos();

    /**
     * 是否添加了图片
     */
    boolean hasPicture();

    /**
     * 设置发布按钮状态
     */
    void setRightBtnEnable(boolean state);

    /**
     * 关闭activity
     */
    void finish();

    /**
     * 显示 关闭确认对话框
     */
    void showQuiteConfirmDlg();

    /**
     * 获取 editPhoto 实例
     */
    EditPhoto getEditPhoto();

    /**
     * 获取progress dialog
     */
    AProgressDialog getProgressDlg();
}
