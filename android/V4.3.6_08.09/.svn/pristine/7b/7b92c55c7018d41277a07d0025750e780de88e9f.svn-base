package com.zhisland.android.blog.common.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.dto.User;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;

/**
 * 由上而下弹出消息框的通用util类，主要用于现场模式的消息弹出
 * 
 * @author zhangxiang
 * 
 */
public class OnlineNotify {
	public final static int TYPE_SEND = 1;
	public final static int TYPE_REFUSE = 2;
	public final static int TYPE_NORMAL = 3;
	public static int BASE_COLOR = 0xE5109C5E;
	final static Handler h = new Handler() {
		public void handleMessage(android.os.Message msg) {
			((PopupWindow) msg.obj).dismiss();
		}
	};

	/**
	 * 为popwindows添加点击事件
	 * 
	 * @author zhangxiang
	 * 
	 */
	public interface OnPopWindowsClickListener {
		public void onClick(View view);
	}

	/**
	 * 为popwindows添加主界面，这个一般用于通用的popwindows而不只是特定的通知
	 * 
	 * @author zhangxiang
	 * 
	 */
	public interface OnPopWindowsInitViewListener {
		public View onInit();
	}

	private static void popWindowsInTop(final Activity mContext,
			final View contentView,
			final OnPopWindowsClickListener clickListener, boolean isTransStatus) {
		final PopupWindow popupWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setTouchable(true);
		popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		ColorDrawable cd = new ColorDrawable(0x000000);
		popupWindow.setBackgroundDrawable(cd);
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		if (isTransStatus)
			popupWindow.showAtLocation(mContext.getWindow().getDecorView(),
					Gravity.TOP, 0, 0);
		else
			popupWindow.showAtLocation(mContext.getWindow().getDecorView(),
					Gravity.TOP, 0, DensityUtil.getStatusHeight());

		h.postDelayed(new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.obj = popupWindow;
				h.sendMessage(msg);
			}
		}, 3000);
		contentView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (clickListener != null) {
					clickListener.onClick(contentView);
					popupWindow.dismiss();
				}
			}
		});

	}

	private static void addHead(View v, Activity context, int color) {
		// View head = new View(context);
		// head.setLayoutParams(new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil
		// .getStatusHeight()));
		// head.setBackgroundColor(color << 16 >> 16 & 0xFF);
		// View orgView = ((LinearLayout) v).getChildAt(0);
		// ((LinearLayout) v).removeView(orgView);
		// ((LinearLayout) v).addView(head);
		// ((LinearLayout) v).addView(orgView);
		v.setPadding(0, DensityUtil.getStatusHeight(), 0, 0);
	}

	/**
	 * 从上而下弹出自定义的popwindows
	 * 
	 * @param context
	 * @param isTransStatus
	 * @param clickListener
	 * @param initViewListener
	 */
	public static void popWindowsInTopByCommon(final Activity context,
			boolean isTransStatus, OnPopWindowsClickListener clickListener,
			OnPopWindowsInitViewListener initViewListener) {
		if (initViewListener == null) {
			return;
		}
		final View contentView = initViewListener.onInit();
		if (contentView == null) {
			return;
		}
		if (isTransStatus) {
			addHead(contentView, context, BASE_COLOR);
		}
		popWindowsInTop(context, contentView, clickListener, isTransStatus);
	}

	/**
	 * 弹出 现场模式中 联系人的通知栏
	 * 
	 * @param context
	 * @param isTransStatus
	 * @param clickListener
	 * @param user
	 */
	public static void popWindowsInTopInclueUser(final Activity context,
			boolean isTransStatus, OnPopWindowsClickListener clickListener,
			User user) {
		final View contentView = LayoutInflater.from(context).inflate(
				R.layout.scene_notify_user, null);

		ImageWorkFactory.getCircleFetcher().loadImage(
				user.userAvatar,
				(ImageView) contentView
						.findViewById(R.id.scene_notify_user_icon),
				R.drawable.img_campaign_guest);
		TextView tv = (TextView) (contentView
				.findViewById(R.id.scene_notify_user_des));
		tv.setText(user.userPosition);
		TextView name = (TextView) (contentView
				.findViewById(R.id.scene_notify_user_name));
		name.setText(user.name);
		contentView.setBackgroundColor(BASE_COLOR);
		if (isTransStatus) {
			addHead(contentView, context, BASE_COLOR);
		}
		popWindowsInTop(context, contentView, clickListener, isTransStatus);

	}

	/**
	 * 弹出 再场模式中 普通消息的通知栏
	 * 
	 * @param context
	 * @param isTransStatus
	 * @param clickListener
	 * @param msg
	 * @param type
	 */
	public static void popWindowsInTopInclueMessage(final Activity context,
			boolean isTransStatus, OnPopWindowsClickListener clickListener,
			String msg, int type) {
		final View contentView = LayoutInflater.from(context).inflate(
				R.layout.scene_notify_msg_layout, null);
		int icon_res = R.drawable.state_feed;
		int bg_color = BASE_COLOR;
		switch (type) {
		case TYPE_NORMAL:
			bg_color = 0xE5DBB786;
			icon_res = R.drawable.state_feed;
			break;
		case TYPE_REFUSE:
			bg_color = 0xE5EA473A;
			icon_res = R.drawable.state_wrong;
			break;
		case TYPE_SEND:
			bg_color = 0xE5109C5E;
			icon_res = R.drawable.state_right;
			break;
		default:
			break;
		}
		ImageView iv = (ImageView) (contentView
				.findViewById(R.id.scene_notify_icon));
		TextView tv = (TextView) (contentView
				.findViewById(R.id.scene_notify_msg));
		tv.setText(StringUtil.isNullOrEmpty(msg)?"您有一条新公告":msg);
		iv.setImageResource(icon_res);
		// findViewById(R.id.scene_notify_msg_rl).
		contentView.setBackgroundColor(bg_color);
		if (isTransStatus) {
			addHead(contentView, context, bg_color);
		}
		popWindowsInTop(context, contentView, clickListener, isTransStatus);
	}
}
