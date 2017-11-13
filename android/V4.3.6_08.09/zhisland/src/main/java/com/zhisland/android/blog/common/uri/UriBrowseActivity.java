package com.zhisland.android.blog.common.uri;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.zhisland.android.blog.aa.controller.SplashActivity;
import com.zhisland.android.blog.common.app.AppUtil;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;

/**
 * 用来监听正和岛schema（zhisland）的点击
 *
 * @author 向飞
 */
public class UriBrowseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = getIntent().getData();
        if (uri != null && AUriMgr.instance().isValid(uri)) {
            String uriString = uri.toString();
            if (AppUtil.appIsRunning(UriBrowseActivity.class.getName()) && PrefUtil.Instance().hasLogin()) {
                AUriMgr.instance().viewRes(this, uriString);
            } else {
                Intent intent = new Intent(ZhislandApplication.APP_CONTEXT, SplashActivity.class);
                intent.putExtra(AUriMgr.URI_BROWSE, uriString);
                startActivity(intent);
            }
        }
        finish();
    }
}
