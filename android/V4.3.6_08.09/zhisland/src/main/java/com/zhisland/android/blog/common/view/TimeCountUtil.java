package com.zhisland.android.blog.common.view;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.zhisland.android.blog.R;

/**
 * 验证码计时器
 */
public class TimeCountUtil extends CountDownTimer {
	private Activity mActivity;
	private TextView tvCode;

    //可点击时的颜色
    private int enableTvColor = R.color.color_dc;
    //不可点击时的颜色
    private int disableTvColor = R.color.color_div;

	public TimeCountUtil(Activity mActivity, long millisInFuture,
			long countDownInterval, TextView btn) {
		super(millisInFuture, countDownInterval);
		this.mActivity = mActivity;
		this.tvCode = btn;
	}

    /**
     * 设置可点击时的字体颜色
     */
    public void setEnableTvColor(int color){
        this.enableTvColor = color;
    }

    /**
     * 设置不可点击时的字体颜色
     */
    public void setDisableTvColor(int color){
        this.disableTvColor = color;
    }

	@Override
	public void onTick(long millisUntilFinished) {
		tvCode.setClickable(false);// 设置不能点击
		tvCode.setText(millisUntilFinished / 1000 + "秒");// 设置倒计时时间
        tvCode.setEnabled(false);
		// 设置按钮为灰色，这时是不能点击的
		tvCode.setTextColor(mActivity.getResources()
				.getColor(disableTvColor));
	}

	@Override
	public void onFinish() {
        tvCode.setEnabled(true);
		tvCode.setText("获取验证码");
		tvCode.setClickable(true);// 重新获得点击
		tvCode.setTextColor(mActivity.getResources().getColor(enableTvColor));
	}

}