package com.zhisland.android.blog.common.push;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhisland.android.blog.common.app.TabHome;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.uri.AUriMgr;

/**
 * 此Activity 用于推送点击 分发处理
 */
public class PushDispatchActivity extends FragBaseActivity{

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        String uriString = getIntent().getStringExtra(AUriMgr.URI_BROWSE);
        AUriMgr.instance().viewRes(this, uriString);
        finish();
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
}
