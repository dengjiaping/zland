package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.igexin.sdk.PushBuildConfig;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.IntentUtil;

/**
 * 通讯录授权被拒 view
 */
public class ContactRefuseView extends LinearLayout {

    TextView tvContent;
    /**
     * 统计pagename
     */
    private String trackerPageName;
    /**
     * 统计别名
     */
    private String trackerAlias;

    public ContactRefuseView(Context context) {
        super(context);
        initView(context);
    }

    public ContactRefuseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ContactRefuseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.white));
        setGravity(Gravity.CENTER);

        tvContent = new TextView(context);
        tvContent.setTextColor(getResources().getColor(R.color.color_f2));
        DensityUtil.setTextSize(tvContent, R.dimen.txt_18);
        tvContent.setGravity(Gravity.CENTER);
        tvContent.setText("你尚未授权通讯录");
        addView(tvContent);

        Button btnOpenPermissions = new Button(context);
        btnOpenPermissions.setText("开启通讯录访问权限");
        btnOpenPermissions.setBackgroundResource(R.drawable.rect_bwhite_sdiv_clarge);
        btnOpenPermissions.setTextColor(getResources().getColor(R.color.color_f2));
        btnOpenPermissions.setGravity(Gravity.CENTER);
        DensityUtil.setTextSize(btnOpenPermissions, R.dimen.txt_18);
        LayoutParams btnParams = new LayoutParams(DensityUtil.dip2px(250), DensityUtil.dip2px(44));
        btnParams.topMargin = DensityUtil.dip2px(24);
        btnOpenPermissions.setLayoutParams(btnParams);
        addView(btnOpenPermissions);
        btnOpenPermissions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(trackerPageName)) {
                    ZhislandApplication.trackerClickEvent(trackerPageName, trackerAlias);
                }
                //去打开通信录授权
                IntentUtil.intentToSystemSetting(context);
            }
        });
    }

    /**
     * 设置通讯录被拒绝时文案
     */
    public void setContent(String content) {
        if (tvContent != null && !TextUtils.isEmpty(content)) {
            tvContent.setText(content);
        }
    }

    /**
     * 设置通讯录被拒绝时文案 字体颜色
     */
    public void setContentColor(int color) {
        if (tvContent != null) {
            tvContent.setTextColor(getResources().getColor(color));
        }
    }

    /**
     * 设置统计 pageName 和 别名，用于统计点击开启通讯录按钮事件
     */
    public void setTracker(String pageName, String alias){
        this.trackerPageName = pageName;
        this.trackerAlias = alias;
    }

}
