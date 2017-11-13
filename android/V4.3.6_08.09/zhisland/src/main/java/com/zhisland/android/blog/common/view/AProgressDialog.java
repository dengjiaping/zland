package com.zhisland.android.blog.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

public class AProgressDialog extends Dialog {

	public static enum OrientationEnum {
		vertical, horizontal;
	}

	private TextView tvMsg;
	private String curMsg = "正在请求服务器。。。";

	private OrientationEnum orientation = OrientationEnum.horizontal;

	public AProgressDialog(Context context) {
		super(context, R.style.PROGRESS_DIALOG);
	}

	public AProgressDialog(Context context,OrientationEnum orientation) {
		super(context, R.style.PROGRESS_DIALOG);
		this.orientation = orientation;
	}

	public AProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window dialogWindow = getWindow();
		dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = DensityUtil.getWidth() * 4 / 5; // 宽度
		lp.alpha = 0.95f; // 透明度
		dialogWindow.setAttributes(lp);

		if(orientation == OrientationEnum.horizontal){
			this.setContentView(R.layout.progress_dialog);
		}else{
			this.setContentView(R.layout.progress_dialog_v);
		}

		tvMsg = (TextView) findViewById(R.id.tv_progress_dlg);
		tvMsg.setText(curMsg);

	}

	public void setText(String msgTxt) {
		this.curMsg = msgTxt;
		if (tvMsg != null) {
			this.tvMsg.setText(msgTxt);
		}
	}

}
