package com.zhisland.android.blog.profilemvp.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.zhisland.android.blog.common.eb.EbAction;
import com.zhisland.android.blog.common.util.AvatarUploader;
import com.zhisland.android.blog.feed.bean.AttachImg;
import com.zhisland.android.blog.feed.bean.Feed;
import com.zhisland.android.blog.feed.bean.FeedPicture;
import com.zhisland.android.blog.feed.model.impl.CreateFeedModel;
import com.zhisland.android.blog.feed.view.IFeedCreateView;
import com.zhisland.android.blog.profile.eb.EBProfile;
import com.zhisland.android.blog.profilemvp.model.IAddPhotoModel;
import com.zhisland.android.blog.profilemvp.view.IAddPhotoView;
import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.component.lifeprovider.PresenterEvent;
import com.zhisland.lib.mvp.presenter.BasePresenter;
import com.zhisland.lib.rxjava.RxBus;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.dialog.AProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import rx.Subscriber;

/**
 * 个人主页添加照片 Presenter
 */
public class AddPhotoPresenter extends BasePresenter<IAddPhotoModel, IAddPhotoView> {

    /**
     * 上传图片池，所有上传过的图片都在这个池子里
     * key 代表本地fileName, value 代表上传后返回的服务器地址
     */
    private HashMap<String, String> uploadPool = new HashMap<>();
    // EditPhoto 已选的图片
    private ArrayList<FeedPicture> selectedFiles;
    //当前正在上传的图片位置
    private int curUploadPosition = -1;
    //当前正在上传的图片 fileName
    private String curUploadFileName = "";

    // 当前是否正在上传状态
    private boolean isUploading;
    // 当前是否在发布feed的状态
    private boolean isCreateFeedState;

    @Override
    protected void updateView() {
        super.updateView();
    }

    /**
     * 刷新右边发布按钮的状态
     * 规则： 发布（按钮）：正文或图片任意不为空时，则可高亮点击；
     */
    public void refreshRightBtnState() {
        String content = view().getFeedContent();
        boolean hasPicture = view().hasPicture();
        if (!TextUtils.isEmpty(content) || hasPicture) {
            view().setRightBtnEnable(true);
        } else {
            view().setRightBtnEnable(false);
        }
    }

    /**
     * 取消发布feed，如果填写了内容或者选择了图片，则弹出确认对话框，否则直接关闭界面
     */
    public void onQuitClicked() {
        stopUpload();
        String content = view().getFeedContent();
        boolean hasPicture = view().hasPicture();
        if (!TextUtils.isEmpty(content) || hasPicture) {
            // 显示取消发布 confirm 对话框
            view().showQuiteConfirmDlg();
        } else {
            view().finish();
        }
    }

    /**
     * 点击发布feed的按钮
     */
    public void onPublishClicked() {
        isCreateFeedState = true;
        view().showProgressDlg();
        createFeedTask();
        AProgressDialog progressDialog = view().getProgressDlg();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isCreateFeedState = false;
            }
        });
    }

    /**
     * 界面从不可见变成可见时，检查是否有没有上传过的图片，如果有则开始上传
     * 包括选择图片后，删除图片后，前后台切换
     */
    public void checkUpload() {
        curUploadPosition = -1;
        // 获取当前已选择图片的地址
        selectedFiles = view().getSelectedLocalPhotos();
        if (!isUploading) {
            startUpload();
        }
    }

    /**
     * 上传 task 回调
     */
    AvatarUploader.OnUploaderCallback onUploaderCallback = new AvatarUploader.OnUploaderCallback() {

        @Override
        public void callBack(String backUrl) {
            if (!TextUtils.isEmpty(backUrl)) {
                MLog.e("feed", "上传的位置：" + curUploadPosition + "..." + "本地图片地址：" + curUploadFileName + "..." + "返回图片地址：" + backUrl);
                // 上传成功后，放入已成功的池子里
                uploadPool.put(curUploadFileName, backUrl);
                if (curUploadPosition == selectedFiles.size() - 1) {
                    //所有图片全部上传成功
                    stopUpload();
                    // 如果此时是发布feed的状态，则调用发布feed的接口
                    if (isCreateFeedState) {
                        createFeedTask();
                    }
                } else {
                    // 有未上传过的图片，继续上传
                    startUpload();
                }
            } else {
                MLog.e("feed", "上传图片失败。。。");
                // 某张图片上传失败，则停止所有上传
                stopUpload();
                // 如果此时是发布feed的状态，则隐藏loading框
                if (isCreateFeedState) {
                    view().hideProgressDlg();
                }
            }
        }
    };

    /**
     * 开始遍历上传未传过的图片
     */
    private void startUpload() {
        if (selectedFiles != null) {
            for (int i = 0; i < selectedFiles.size(); i++) {
                String fileName = selectedFiles.get(i).localPath;
                if (uploadPool.containsKey(fileName)) {
                    // 代表此张图片已经上传过，检查下一张
                    continue;
                } else {
                    // 此张图片没有上传过，开始上传
                    isUploading = true;
                    curUploadPosition = i;
                    curUploadFileName = selectedFiles.get(i).localPath;
                    AvatarUploader.instance().uploadAvatar(curUploadFileName, onUploaderCallback);
                    break;
                }
            }
        }
    }

    /**
     * 停止上传
     */
    private void stopUpload() {
        curUploadPosition = -1;
        curUploadFileName = "";
        isUploading = false;
        AvatarUploader.instance().loadFinish();
    }

    /**
     * 发布feed task
     */
    private void createFeedTask() {
        // 选择的图片，服务器返回的地址
        ArrayList<FeedPicture> photos = view().getSelectedLocalPhotos();
        if (photos != null) {
            for (FeedPicture photo : photos) {
                if (uploadPool.containsKey(photo.localPath)) {
                    photo.url = uploadPool.get(photo.localPath);
                } else {
                    // 如果发现已选择的图片中有未上传过的图片，则开始上传
                    if (!isUploading) {
                        startUpload();
                    }
                    return;
                }
            }
        }


        // 发布的内容
        String desc = view().getFeedContent();
        ArrayList<String> urls = new ArrayList<>();
        for (FeedPicture photo : photos) {
            urls.add(photo.url);
        }

        String imgGson = GsonHelper.GetCommonGson().toJson(urls);

        model().addPhoto(desc, imgGson)
                .observeOn(getSchedulerObserver())
                .subscribeOn(getSchedulerSubscribe())
                .compose(this.<Void>bindUntilEvent(PresenterEvent.UNBIND_VIEW))
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view().hideProgressDlg();
                    }

                    @Override
                    public void onNext(Void feed) {
                        //TODO
                        EBProfile eBProfile = new EBProfile(EBProfile.TYPE_CHANGE_PHOTO, null);
                        EventBus.getDefault().post(eBProfile);

                        view().hideProgressDlg();
                        view().showToast("上传成功");
                        view().finish();
                    }
                });
    }

}
