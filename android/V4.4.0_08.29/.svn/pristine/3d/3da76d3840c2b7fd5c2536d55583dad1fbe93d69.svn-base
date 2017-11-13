package com.zhisland.android.blog.profilemvp.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.lib.component.act.TitleType;

import butterknife.ButterKnife;

/**
 * Created by Mr.Tui on 2016/9/12.
 */
public class ActAddPhoto extends FragBaseActivity {

    private final int TAG_LEFT = 100;
    private final int TAG_NEXT = 101;

    private TextView tvCancel;
    public TextView tvComplete;

    private FragAddPhoto fragAddPhoto;

    public static void invoke(Context context) {
        Intent intent = new Intent(context, ActAddPhoto.class);
        context.startActivity(intent);
    }


    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);

        ButterKnife.inject(this);
        getTitleBar().setTitle("上传照片");

        tvCancel = TitleCreator.Instance().createTextButton(this, "取消",
                R.color.txt_light_gray);
        tvComplete = TitleCreator.Instance().createTextButton(this, "发布",
                getResources().getColorStateList(R.color.sel_color_dc));
        tvComplete.setEnabled(false);
        getTitleBar().addLeftTitleButton(tvCancel, TAG_LEFT);
        getTitleBar().addRightTitleButton(tvComplete, TAG_NEXT);

        fragAddPhoto = new FragAddPhoto();
        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragAddPhoto);
        ft.commit();
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        super.onTitleClicked(view, tagId);
        switch (tagId) {
            case TAG_LEFT:
                // 取消发布
                back();
                break;
            case TAG_NEXT:
                if (fragAddPhoto != null) {
                    fragAddPhoto.onPublishClicked();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            default: {
                if (fragAddPhoto != null) {
                    fragAddPhoto.onActivityResult(requestCode, resultCode, data);
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (fragAddPhoto != null) {
            fragAddPhoto.back();
        }
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

}
