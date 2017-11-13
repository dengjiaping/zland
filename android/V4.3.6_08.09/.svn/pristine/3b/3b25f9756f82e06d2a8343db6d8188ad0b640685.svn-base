package com.zhisland.android.blog.info.view.impl;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.TitleCreator;
import com.zhisland.android.blog.info.bean.ZHInfo;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.DensityUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 资讯详情页面
 * Created by Mr.Tui on 2016/7/11.
 */
public class ActInfoDetail extends FragBaseActivity implements FragInfoDetail.SwitchBtnCallBack {

    FragInfoDetail fragInfoDetail;

    private static final int TAG_ID_RIGHT = 100;

    View switchView;

    @InjectView(R.id.tvReadMode)
    TextView tvReadMode;

    @InjectView(R.id.tvUrlMode)
    TextView tvUrlMode;

    /**
     * 启动资讯详情页面。
     */
    public static void invoke(Context context, long infoId) {
        Intent intent = new Intent(context, ActInfoDetail.class);
        ZHInfo info = new ZHInfo();
        info.newsId = infoId;
        intent.putExtra(FragInfoDetail.INTENT_KEY_INFO, info);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        getTitleBar().addBackButton();
        ImageView vRight = TitleCreator.Instance()
                .createImageButton(this, R.drawable.sel_btn_share_green);
        getTitleBar().addRightTitleButton(vRight, TAG_ID_RIGHT);
        switchView = inflater.inflate(R.layout.layout_info_detail_title_btn, null);
        switchView.setVisibility(View.GONE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        ((RelativeLayout) getTitleBar().getRootView()).addView(switchView, lp);
        ButterKnife.inject(this);

        fragInfoDetail = new FragInfoDetail();
        fragInfoDetail.setSwitchBtnCallBack(this);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, fragInfoDetail);
        ft.commit();
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_LAYOUT;
    }

    @Override
    public void onTitleClicked(View view, int tagId) {
        if (tagId == TAG_ID_RIGHT) {
            shareInfo();
        }
        super.onTitleClicked(view, tagId);
    }

    /**
     * 资讯分享的url
     */
    private void shareInfo() {
        fragInfoDetail.onShareClicked();
    }

    @OnClick(R.id.tvReadMode)
    void onReadModeClick() {
        fragInfoDetail.onReadModeClick();
    }

    @OnClick(R.id.tvUrlMode)
    void onUrlModeClick() {
        fragInfoDetail.onUrlModeClick();
    }

    @Override
    public void setVisibility(boolean visibility) {
        if (visibility) {
            switchView.setVisibility(View.VISIBLE);
        } else {
            switchView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSwitchRead() {
        tvReadMode.setTextColor(getResources().getColor(R.color.white));
        tvReadMode.setBackgroundResource(R.drawable.tab_left_f3);
        tvUrlMode.setTextColor(getResources().getColor(R.color.color_f3));
        tvUrlMode.setBackgroundResource(R.drawable.tab_right_white_f3);
    }

    @Override
    public void setSwitchUrl() {
        tvReadMode.setTextColor(getResources().getColor(R.color.color_f3));
        tvReadMode.setBackgroundResource(R.drawable.tab_left_white_f3);
        tvUrlMode.setTextColor(getResources().getColor(R.color.white));
        tvUrlMode.setBackgroundResource(R.drawable.tab_right_f3);
    }
}
