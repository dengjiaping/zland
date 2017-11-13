package com.zhisland.android.blog.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.common.view.NetErrorView;
import com.zhisland.lib.component.frag.FragBase;

/**
 * 网络错误通用Fragment
 */
public class FragNetError extends FragBase {

    private TextView tvReload;
    private OnClickReloadListener listener;

    public interface OnClickReloadListener {
        void onClickReload(View view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        LinearLayout parent = new LinearLayout(getActivity());

//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        parent.setLayoutParams(lp);
//        parent.setGravity(Gravity.CENTER);
        NetErrorView netErrorView = new NetErrorView(getActivity());
        tvReload = netErrorView.getTvReload();
        return netErrorView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClickReload(view);
                }
            }
        });
    }

    /**
     * 设置网络状态不好时监听事件
     */
    public void setOnClickReloadListener(OnClickReloadListener listener) {
        this.listener = listener;
    }

    @Override
    public String getPageName() {
        return "FragNetError";
    }
}
