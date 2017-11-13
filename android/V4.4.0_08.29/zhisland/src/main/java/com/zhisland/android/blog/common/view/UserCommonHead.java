package com.zhisland.android.blog.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.zhisland.android.blog.R;

/**
 * <b>profile中用到的headview，支持单个button的使用
 * <p>
 * </b>
 * 使用方法,在布中加入下面这个代码</br>&lt;com.zhisland.android.blog.common.view.UserCommonHead</br>
 * android:id="@+id/vHead"</br> android:layout_width="fill_parent"</br>
 * android:layout_height="wrap_content" /></br>
 * <p>
 * 或者直接用new UserCommonHead(Context context)来手动添加
 * <p>
 * 
 * @author zhangxiang
 * 
 */
public class UserCommonHead extends RelativeLayout {

	public static final int NO_BTN_ICON = -1;

	@InjectView(R.id.ivUCHHead)
	ImageView ivUCHHead;

	@InjectView(R.id.tvUCHDesc)
	TextView tvUCHDesc;

	@InjectView(R.id.tvUCHBtnText)
	TextView tvUCHBtnText;

	@InjectView(R.id.btnUCH)
	View btnUCH;

	@InjectView(R.id.ivUCHBtnIcon)
	ImageView ivUCHBtnIcon;

	private View rootView;

	public UserCommonHead(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public UserCommonHead(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		rootView = LayoutInflater.from(context).inflate(
				R.layout.user_common_head, null);
		ButterKnife.inject(this, rootView);
		addView(rootView);
	}

	/**
	 * 当作纯button来用
	 */
	public void btnOnly() {
		ivUCHHead.setVisibility(View.GONE);
		tvUCHDesc.setVisibility(View.GONE);
	}

	/**
	 * 设置头图的资源
	 * 
	 * @param id
	 */
	public void setHeadImage(int id) {
		ivUCHHead.setImageResource(id);
	}

	/**
	 * 设置描述
	 * 
	 * @param desc
	 */
	public void setDesc(String desc) {
		tvUCHDesc.setText(desc);
	}

	/**
	 * 设置button左边的图片，参数为res，如果不要图标使用 {@link UserCommonHead.NO_BTN_ICON}
	 * 
	 * @param id
	 */
	public void setBtnIcon(int id) {
		if (id == NO_BTN_ICON) {
			ivUCHBtnIcon.setVisibility(View.GONE);
			return;
		}
		ivUCHBtnIcon.setImageResource(id);
	}

	/**
	 * 设置button的文字
	 * 
	 * @param text
	 */
	public void setBtnText(String text) {
		tvUCHBtnText.setText(text);
	}

	/**
	 * 设置按钮 Visiable
	 */
	public void setBtnVisiable(int visiableState) {
		btnUCH.setVisibility(visiableState);
	}

	/**
	 * 设置button的点击事件
	 * 
	 * @param listener
	 */
	public void setBtnClickListener(OnClickListener listener) {
		btnUCH.setOnClickListener(listener);
	}
}
