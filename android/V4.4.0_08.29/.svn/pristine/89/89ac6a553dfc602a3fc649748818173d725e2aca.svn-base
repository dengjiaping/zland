package com.zhisland.android.blog.profile.view.block;

import android.app.Activity;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.profile.dto.SimpleBlock;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profilemvp.bean.UserPhoto;
import com.zhisland.android.blog.profilemvp.view.impl.ActAddPhoto;
import com.zhisland.android.blog.profilemvp.view.impl.FragMyPhoto;
import com.zhisland.android.blog.profilemvp.view.util.ProfilePhotoView;
import com.zhisland.lib.util.DensityUtil;

import java.util.List;

/**
 * 相册block
 */
public class UserPhotoBlock extends ProfileBaseCommonBlock<UserPhoto> {

    public UserPhotoBlock(Activity context, UserDetail userDetail,
                          SimpleBlock<UserPhoto> block) {
        super(context, userDetail, block);
    }

    @Override
    protected void loadData(List<UserPhoto> datas) {
        ProfilePhotoView profilePhotoView = new ProfilePhotoView(context);
        profilePhotoView.setPhoto(datas);
        profilePhotoView.setCallBack(new ProfilePhotoView.CallBack() {
            @Override
            public void onImageClick() {
                User user = getBlockUser();
                if (user != null) {
                    FragMyPhoto.invoke(context, user.uid);
                }
            }

            @Override
            public void onAddClick() {
                goToAddPhoto();
            }
        });
        profilePhotoView.setPadding(0, DensityUtil.dip2px(10), 0, 0);
        llBlockContent.addView(profilePhotoView);
    }

    @Override
    protected void initChildViews() {
        tvBlockContentRight.setText("添加");
        tvBlockContentRight.setVisibility(VISIBLE);
        ivBlockContentEdit.setVisibility(GONE);
        ivEmptyIcon.setImageResource(R.drawable.img_profile_photo);
        tvEmptyDesc.setText("这个看颜的世界，没有靓照怎么出来混");
        tvEmptyButton.setText("添加照片");
    }

    @Override
    protected void showRightView() {
        tvBlockContentRight.setVisibility(VISIBLE);
        ivBlockContentEdit.setVisibility(GONE);
    }

    @Override
    protected void onIvRightClick() {

    }

    @Override
    protected void ontvRightClick() {
        goToAddPhoto();
    }

    @Override
    protected void onTvEmptyButtonClick() {
        goToAddPhoto();
    }

    private void goToAddPhoto() {
        ActAddPhoto.invoke(context);
    }

}
