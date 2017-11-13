package com.zhisland.android.blog.common.base;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.lib.util.DensityUtil;

/**
 * 标题上文字或者图片快速创建
 */
public class TitleCreator {

	private static TitleCreator instance;

	public static TitleCreator Instance() {
		if (instance == null) {
			synchronized (TitleCreator.class) {
				if (instance == null)
					instance = new TitleCreator();
			}
		}

		return instance;
	}

	private TitleCreator() {
	}

	/**
	 * 创建一个标题上的默认颜色的TextView
	 * 
	 * @param text
	 *            textContent
	 * @param textColor
	 *            textColor
	 */
	public TextView createTextButton(Context context, String text) {
		return createTextButton(context, text, -1);
	}

	/**
	 * 创建一个标题上的带颜色的TextView
	 * 
	 * @param text
	 *            textContent
	 * @param color
	 *            textColor
	 */
	public TextView createTextButton(Context context, String text, int color) {
		TextView textView = new TextView(context);
		textView.setGravity(Gravity.CENTER);
		textView.setText(text);
		if (color != -1) {
			textView.setTextColor(context.getResources().getColor(color));
		}else {
			textView.setTextColor(context.getResources().getColor(R.color.color_f2));
		}
		DensityUtil.setTextSize(textView, R.dimen.app_title_btn_text_size);
		return textView;
	}

	/**
	 * 创建一个标题上的ImageView
	 * 
	 * @param resId
	 *            图片资源id
	 */
	public ImageView createImageButton(Context context, int resId) {
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(resId);
		imageView.setScaleType(ScaleType.CENTER);
		return imageView;
	}
}
