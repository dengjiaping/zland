package com.zhisland.android.blog.profile.controller.detail;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.aa.dto.Country;
import com.zhisland.android.blog.common.base.ZHApis;
import com.zhisland.android.blog.common.dto.ActionItem;
import com.zhisland.android.blog.common.dto.DBMgr;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.android.blog.common.util.CodeUtil;
import com.zhisland.android.blog.common.util.DialogUtil;
import com.zhisland.android.blog.common.util.IMUtil;
import com.zhisland.android.blog.common.view.CommonDialog;
import com.zhisland.android.blog.common.view.ScrollTitleBar;
import com.zhisland.android.blog.common.view.pullzoom.PullToZoomScrollViewEx;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExPinnedListener;
import com.zhisland.android.blog.common.view.pullzoom.ScrollViewExTitleListener;
import com.zhisland.android.blog.common.webview.ActionDialog;
import com.zhisland.android.blog.common.webview.FragWebview;
import com.zhisland.android.blog.profile.dto.UserDetail;
import com.zhisland.android.blog.profile.view.TabWrapper;
import com.zhisland.lib.async.http.task.TaskCallback;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.tab.TabBarView;
import com.zhisland.lib.view.tab.ZHTabInfo;

import org.apache.http.client.HttpResponseException;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 个人主页 印象tab fragment
 */
public class FragImpression extends FragBase {

    @Override
    public String getPageName() {
        return "FragImpression";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        TextView tv = new TextView(getActivity());
        tv.setTextColor(getResources().getColor(R.color.color_f3));
        DensityUtil.setTextSize(tv, R.dimen.txt_16);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        tv.setPadding(0, DensityUtil.dip2px(100), 0, 0);
        tv.setText("开发中，敬请期待");
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        return tv;
    }
}
