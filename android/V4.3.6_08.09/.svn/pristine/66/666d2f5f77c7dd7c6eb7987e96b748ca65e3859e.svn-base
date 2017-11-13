package com.zhisland.android.blog.aa.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.SplashData;
import com.zhisland.android.blog.common.app.HomeUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.base.FragBaseActivity;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.aa.eb.EBSplashStop;
import com.zhisland.android.blog.common.upapp.UpgradeMgr;
import com.zhisland.android.blog.common.util.FontsUtil;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import de.greenrobot.event.EventBus;

public class SplashActivity extends FragBaseActivity {

    public static final String TAG = "splash";

    public static final String INTENT_KEY_SPLASH_DATA = "intent_key_splash_data";

    public static final int FROM_LAUNCHER = 0;
    public static final int FROM_HOME = 1;

    private static final int splashImgDelay = 3000;// 图片最短的显示时间
    private static final int splashEmptyDelay = 1500;// 图片最短的显示时间

    SplashData splashData;

    @InjectView(R.id.tvContent)
    TextView tvContent;

    @InjectView(R.id.tvAuthor)
    TextView tvAuthor;

    @InjectView(R.id.ivBg)
    ImageView ivBg;

    @InjectView(R.id.tvJumpOver)
    TextView tvJumpOver;

    @InjectView(R.id.vLine)
    View vLine;

    private int from;

    /**
     * 页面开始显示的时间
     */
    private long startTime;

    /**
     * 页面显示多久.默认值为splashImgDelay
     */
    private long showTime;

    public static void invoke(Context context, SplashData splashData) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(INTENT_KEY_SPLASH_DATA, splashData);
        context.startActivity(intent);
    }

    @Override
    public void onContinueCreate(Bundle savedInstanceState) {
        super.onContinueCreate(savedInstanceState);
        MLog.d(TAG, "create splash activity");
        Object obj = getIntent().getSerializableExtra(INTENT_KEY_SPLASH_DATA);
        if (obj != null && obj instanceof SplashData) {
            splashData = (SplashData) obj;
            from = FROM_HOME;
        } else {
            splashData = SplashData.getValidity();
            from = FROM_LAUNCHER;
        }
        setSwipeBackEnable(false);
        ShareSDK.initSDK(this);
        setContentView(R.layout.splash);
        ButterKnife.inject(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runnableFillData);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().post(new EBSplashStop(from));
    }

    private Runnable runnableFillData = new Runnable() {

        @Override
        public void run() {
            fillView();
            getDataFromInternet();
            getAllDict();
        }
    };

    /**
     * 获取所有字典
     */
    private void getAllDict() {
        ZHApis.getUserApi().getAllDict();
    }

    private Runnable runnableToAdjust = new Runnable() {

        @Override
        public void run() {
            jump();
        }
    };

    @OnClick(R.id.tvJumpOver)
    public void jump() {
        if (from == FROM_HOME) {
            finish();
        } else {
            HomeUtil.dispatchJump(SplashActivity.this);
        }
    }

    private Runnable runnableTimer = new Runnable() {

        @Override
        public void run() {
            int timeLeft = (int) ((showTime - (System.currentTimeMillis() - startTime)) / 1000 + 1);
            tvJumpOver.setText("跳过\n" + timeLeft + "S");
            if (timeLeft > 0) {
                handler.postDelayed(runnableTimer, 100);
            }
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnableToAdjust);
        handler.removeCallbacks(runnableTimer);
        handler.removeCallbacks(runnableFillData);
        super.onDestroy();
        checkUpdate();
    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        UpgradeMgr upgradeManager = new UpgradeMgr(ZhislandApplication.APP_CONTEXT);
        upgradeManager.checkUpgrade(true);
    }

    private void initView() {
        Typeface typeFace = FontsUtil.getFzltsjwTypeface();
        if (typeFace != null) {
            tvContent.setTypeface(typeFace);
        } else {
            FontsUtil.unZipFzltsjwAsync();
        }
    }

    private void fillView() {
        if (splashData != null) {
            tvJumpOver.setVisibility(View.VISIBLE);
            handler.removeCallbacks(runnableToAdjust);
            handler.removeCallbacks(runnableTimer);
            showTime = splashData.delay > 0 ? splashData.delay : splashImgDelay;
            handler.postDelayed(runnableToAdjust, showTime);
            startTime = System.currentTimeMillis();
            handler.post(runnableTimer);

            tvContent.setText(splashData.content == null ? "" : splashData.content);
            if (StringUtil.isNullOrEmpty(splashData.author)) {
                vLine.setVisibility(View.GONE);
                tvAuthor.setText("");
            } else {
                vLine.setVisibility(View.VISIBLE);
                tvAuthor.setText(splashData.author);
            }
            ImageWorkFactory.getFetcher().loadImage(splashData.picUrl, ivBg, ImageWorker.ImgSizeEnum.LARGE);
        } else {
            handler.removeCallbacks(runnableToAdjust);
            handler.postDelayed(runnableToAdjust, splashEmptyDelay);
        }
    }

    private void getDataFromInternet() {
        ZHApis.getAAApi().getSplashData(this, new TaskCallback<ArrayList<SplashData>>() {
            @Override
            public void onSuccess(ArrayList<SplashData> content) {
                if (splashData == null) {
                    splashData = SplashData.getValidity();
                    fillView();
                }
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });
    }

    @Override
    protected int titleType() {
        return TitleType.TITLE_NONE;
    }

}
