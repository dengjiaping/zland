package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

/**
 * 网络错误时的view
 */
public class NetErrorView extends LinearLayout {

    //重新加载按钮
    TextView tvReload;

    public NetErrorView(Context context) {
        this(context, null);
    }

    public NetErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        ImageView img = new ImageView(getContext());
        LayoutParams imgParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        img.setLayoutParams(imgParams);
        img.setImageResource(R.drawable.wifi_wrong);
        addView(img);

        TextView tvPrompt = new TextView(getContext());
        LayoutParams promptParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        promptParams.topMargin = DensityUtil.dip2px(20);
        tvPrompt.setLayoutParams(promptParams);
        DensityUtil.setTextSize(tvPrompt, R.dimen.txt_20);
        tvPrompt.setTextColor(getResources().getColor(R.color.color_f1));
        tvPrompt.setText("您的手机网络不太顺畅");
        addView(tvPrompt);

        TextView textView = new TextView(getContext());
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.topMargin = DensityUtil.dip2px(8);
        textView.setLayoutParams(params);
        DensityUtil.setTextSize(textView, R.dimen.txt_16);
        textView.setTextColor(getResources().getColor(R.color.color_f2));
        textView.setText("请检查您的手机是否联网");
        addView(textView);

        tvReload = new TextView(getContext());
        LayoutParams btnParams = new LayoutParams(
                DensityUtil.dip2px(130), DensityUtil.dip2px(35));
        btnParams.topMargin = DensityUtil.dip2px(18);
        tvReload.setLayoutParams(btnParams);
        tvReload.setGravity(Gravity.CENTER);
        tvReload.setBackgroundResource(R.drawable.rect_bwhite_sdc_clarge);
        DensityUtil.setTextSize(tvReload, R.dimen.txt_16);
        tvReload.setTextColor(getResources().getColor(R.color.color_dc));
        tvReload.setText("重新加载");
        addView(tvReload);
    }

    public TextView getTvReload(){
        return tvReload;
    }

    /**
     * 重新加载按钮 listener
     */
    public void setBtnReloadClickListener(OnClickListener listener) {
        tvReload.setOnClickListener(listener);
    }
}
