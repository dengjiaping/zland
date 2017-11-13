package com.zhisland.android.blog.im.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.app.PrefUtil;
import com.zhisland.android.blog.im.view.adapter.ImSessAdapter.OnAdapterListener;
import com.zhisland.im.data.IMMessage;
import com.zhisland.lib.image.MultiImgPickerActivity;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.view.ResizeView;
import com.zhisland.lib.view.ResizeView.ResizeListener;

/**
 * 用来控制聊天页面的逻辑交互
 * 
 * @author arthur
 * 
 */
public class FragChatViewController implements OnAdapterListener {

	public static interface OnFragChatListener {

		void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView);

		void sendImageMessage(String path);

		void sendVideoMessage(String path, int videoDuration);
	}

	public static final int REQ_VIDEO_CAPTURE = 102;
	public static final int REQ_IMG_SELECT = 103;
	public static final int REQ_VIDEO_SELECT = 104;
	public static final int REQ_WEB_SEARCH = 107;
	public static final int REQ_SELECT_VCARD = 106;
	public static final int REQ_GET_LOC = 108;
	public static final int REQ_GET_LOC_INFO = 109;
	public static final int REQ_SELECT_FORWARD_DEST = 301;
	public static final String TMP_VIDEO_NAME = "chat_tmpvideo.mp4";
	public static final int POP_SHOW = 10001;
	private static final int COLLAPSE_PANEL_MESSAGE = 10000;
	private static final int LIST_SCROLL_BOTTOM_MAGIC_NUM = -100000;
	static final int FULLSCREEN_INPUT_HEIGHT = DensityUtil.dip2px(100);
	private static final String TAG = "chatcontroller";

	@InjectView(R.id.root_view)
	public ResizeView rootView;
	@InjectView(R.id.pullRefreshAbsListView)
	public PullToRefreshListView pullView;
	public View mFragmentView;
    Activity activity;
	private OnFragChatListener onFragChatListener;
	private boolean mShowChatPanel = true;
	private SessBottomController viewController;
	private ChatResizeListener listener;
	private boolean isKeyboardShown = false;

	private static class ChatHandler extends Handler{
		WeakReference<Activity> activity;
		WeakReference<FragChatViewController> fragController;
		public ChatHandler(FragChatViewController fragController) {
			this.activity = new WeakReference<Activity>(fragController.activity);
			this.fragController = new WeakReference<FragChatViewController>(fragController);
		}

		@Override
		public void handleMessage(Message msg) {
			{
				switch (msg.what) {
				case COLLAPSE_PANEL_MESSAGE: {
					fragController.get().collapsePanel();
				}
					break;
				default:
					break;
				}
			}
		}
	}
	private ChatHandler chatHandler;
	
	public FragChatViewController(Activity activity, View view,
			ISessController controller) {
		this.activity = activity;
		
		this.mFragmentView = view;
		
		chatHandler = new ChatHandler(this);
		
		ButterKnife.inject(this, view);

		initListView();

		initBottom(activity, controller);
	}

	public void setOnFragChatListener(OnFragChatListener fragChatListener) {
		this.onFragChatListener = fragChatListener;
	}

	private void initBottom(Activity activity, ISessController controller) {
		if (mShowChatPanel) {
			viewController = new SessBottomController(activity, rootView,
					chatHandler, controller, false);

		} else {
			View bottomView = mFragmentView.findViewById(R.id.rl_chat_bottom);
			bottomView.setVisibility(View.GONE);
		}

		listener = new ChatResizeListener();
		rootView = (ResizeView) mFragmentView.findViewById(R.id.root_view);
		rootView.setListener(listener);
		rootView.getViewTreeObserver().addOnGlobalLayoutListener(listener);
	}

	private void initListView() {
		pullView.setBackgroundResource(R.color.white);
		pullView.getRefreshableView().setBackgroundResource(R.color.white);
		pullView.setMode(Mode.PULL_FROM_START);

		pullView.getRefreshableView().setDividerHeight(0);
		pullView.getRefreshableView().setDivider(null);
		pullView.getRefreshableView().setSelector(R.color.transparent);
		pullView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				if (onFragChatListener != null) {
					onFragChatListener.onPullDownToRefresh(refreshView);
				}
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
			}
		});

		pullView.getRefreshableView().setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (!chatHandler.hasMessages(COLLAPSE_PANEL_MESSAGE)) {
						android.os.Message delayMsg = new android.os.Message();
						delayMsg.what = COLLAPSE_PANEL_MESSAGE;
						chatHandler.sendMessageDelayed(delayMsg, 2000);
					}
				} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					chatHandler.removeMessages(COLLAPSE_PANEL_MESSAGE);
					collapsePanel();
				} else {
					chatHandler.removeMessages(COLLAPSE_PANEL_MESSAGE);
				}
				return false;
			}
		});

		pullView.getRefreshableView().setOnScrollListener(
				new OnScrollListener() {

					@Override
					public void onScroll(AbsListView arg0, int arg1, int arg2,
							int arg3) {
					}

					@Override
					public void onScrollStateChanged(AbsListView arg0, int arg1) {
						if (arg1 == SCROLL_STATE_TOUCH_SCROLL) {
							collapsePanel();
						}
					}

				});
	}

	public boolean collapsePanel() {
		if (viewController != null) {
			return viewController.collapseAll();
		}
		return false;
	}

	public void smoothToPreItem(final int oldPos) {
		ValueAnimator va = ObjectAnimator.ofInt(0, DensityUtil.dip2px(150));
		va.setDuration(250);
		va.setStartDelay(250);
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int currentDiff = (Integer) animation.getAnimatedValue();
				pullView.getRefreshableView().setSelectionFromTop(oldPos,
						currentDiff);
			}
		});
		va.start();
	}

	public void showLastItem() {
		if (pullView.getRefreshableView().getAdapter() != null)
			pullView.getRefreshableView().setSelectionFromTop(
					pullView.getRefreshableView().getAdapter().getCount(),
					LIST_SCROLL_BOTTOM_MAGIC_NUM);
	}

	private class ChatResizeListener implements OnGlobalLayoutListener,
			ResizeListener {

		int previousHeightDiffrence = 0;

		@Override
		public void onGlobalLayout() {
			
		}

		@Override
		public void onSizeChanged(int w, int h, int oldw, int oldh) {

			Rect r = new Rect();
			rootView.getWindowVisibleDisplayFrame(r);

			int screenHeight = rootView.getRootView().getHeight();
			int heightDifference = screenHeight - (r.bottom);

			if (previousHeightDiffrence - heightDifference > 50) {
				viewController.collapseAll();
			}

			previousHeightDiffrence = heightDifference;
			if (heightDifference > 100) {
				isKeyboardShown = true;
				viewController.refreshAttachHeight(heightDifference);
				PrefUtil.Instance().setInputHeight(heightDifference);
				chatHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						showLastItem();
					}
				}, 100);
			} else {
				isKeyboardShown = false;
			}
			viewController.isKeyBoardVisible = isKeyboardShown;

			// if (viewController == null)
			// return;
			// if (oldh <= 0) {
			// viewHeight = h;
			// return;
			// }
			// int diff = viewHeight - h;
			// if (diff < 0)
			// return;
			// if (Math.abs(diff) > DensityUtil.getHeight() * 2 / 5) {
			// // 正常输入法
			// // activity.getWindow().setSoftInputMode(
			// // WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			// // activity.getWindow().setSoftInputMode(
			// // WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			// viewController.attachController.show();
			// viewController.refreshAttachHeight(diff);
			// viewController.fullScreenInput = false;
			// PrefUtil.Instance().setInputHeight(diff);
			// isKeyboardShown = true;
			// } else if (Math.abs(diff) > FULLSCREEN_INPUT_HEIGHT) {
			// // 全屏手写
			// viewController.fullScreenInput = true;
			// isKeyboardShown = false;
			// }

		}
	}

	public boolean isKeyboardShown() {
		return isKeyboardShown;
	}

	private boolean isBottom() {
		if (pullView.getRefreshableView() == null
				|| pullView.getRefreshableView().getAdapter() == null) {
			return true;
		}
		int lastPos = pullView.getRefreshableView().getLastVisiblePosition();
		int lastItem = pullView.getRefreshableView().getCount() - 1;
		int lastBottom = pullView.getRefreshableView()
				.getChildAt(pullView.getRefreshableView().getChildCount() - 1)
				.getBottom();
		int viewHeight = pullView.getRefreshableView().getHeight();

		if (lastPos == lastItem && lastBottom <= viewHeight) {
			return true;
		}

		return false;
	}

	// ========onadapter listener==========
	@Override
	public void onUserAt(String username) {
		viewController.appendAt(username);
	}

	@Override
	public void onReplyClicked(IMMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLongTextCollapsed(IMMessage msg) {
		// int position = mChatAdapter.getItemPosition(msg);
		// if (position >= 0) {
		// pullView.getRefreshableView().setSelection(position);
		// }
	}

	// ===========on activity result============
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQ_SELECT_VCARD: {
				// long vcardId = data.getLongExtra(UserListFragment.FRIEND_UID,
				// -1);
				// UserDao dao = DatabaseHelper.getHelper().getUserDao();
				// vcardUser = dao.getUserById(vcardId);
				// if (vcardUser != null) {
				// String message = "确定发送" + vcardUser.nickname + "的名片到当前聊天？";
				// showResultSendConfirm(message, REQ_SELECT_VCARD);
				// }
				break;
			}
			case REQ_IMG_SELECT: {
				if (data == null) {
					return;
				}
				try {
					ArrayList<String> pathes = (ArrayList<String>) data
							.getSerializableExtra(MultiImgPickerActivity.RLT_PATHES);
					for (String path : pathes) {
						onFragChatListener.sendImageMessage(path);
					}

				} catch (Exception e) {
					MLog.e(TAG, e.getMessage(), e);
				}
			}
				break;
			case REQ_VIDEO_SELECT: {
				// if (resultCode == Activity.RESULT_OK) {
				// Uri u = data.getData();
				// String path = "";
				// int videoDuration = 0;
				// String[] cols = new String[] {
				// MediaStore.Video.VideoColumns.DATA,
				// MediaStore.Video.VideoColumns.DURATION };
				// try {
				//
				// Cursor cursor = activity.getContentResolver().queryAllContacts(u,
				// cols, null, null, null);
				// if (cursor.moveToFirst()) {
				// path = cursor.getString(0);
				// videoDuration = (int) (cursor.getLong(1) / 1000);
				// }
				// cursor.close();
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				// if (StringUtil.isNullOrEmpty(path)
				// && android.os.Build.VERSION.SDK_INT >=
				// Build.VERSION_CODES.KITKAT
				// && DocumentsContract.isDocumentUri(activity, u)) {
				// String wholeID = DocumentsContract.getDocumentId(u);
				// String id = wholeID.split(":")[1];
				// String[] column = cols;
				// String sel = MediaStore.Images.Media._ID + "=?";
				// Cursor cursor = activity.getContentResolver().queryAllContacts(
				// MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				// column, sel, new String[] { id }, null);
				// if (cursor.moveToFirst()) {
				// path = cursor.getString(0);
				// videoDuration = (int) (cursor.getLong(1) / 1000);
				// }
				// cursor.close();
				// }
				// if (!StringUtil.isNullOrEmpty(path)) {
				// onFragChatListener
				// .sendVideoMessage(path, videoDuration);
				// }
				// }
				//
			}
				break;
			case REQ_VIDEO_CAPTURE: {
				if (resultCode == Activity.RESULT_OK) {
					try {
						onFragChatListener
								.sendImageMessage(viewController.attachController.tmpPath);
					} catch (Exception e) {

					}
				}
			}
				break;
			case REQ_GET_LOC: {
				// if (resultCode == Activity.RESULT_OK && data != null) {
				// int lat = data.getIntExtra(
				// BDMapActivity.MAP_CENTER_KEY_LAT, 0);
				// int lon = data.getIntExtra(
				// BDMapActivity.MAP_CENTER_KEY_LON, 0);
				// Intent intent = new Intent(getActivity(),
				// BDMapActivity.class);
				// intent.putExtra(BDMapActivity.MAP_CENTER_KEY_LAT, lat);
				// intent.putExtra(BDMapActivity.MAP_CENTER_KEY_LON, lon);
				// intent.putExtra(BDMapActivity.MAP_IS_SEND, true);
				// getActivity().startActivityForResult(intent,
				// REQ_GET_LOC_INFO);
				//
				// }
				break;
			}
			case REQ_GET_LOC_INFO: {
				// if (resultCode == Activity.RESULT_OK && data != null) {
				// int lat = data.getIntExtra(
				// BDMapActivity.MAP_CENTER_KEY_LAT, 0);
				// int lon = data.getIntExtra(
				// BDMapActivity.MAP_CENTER_KEY_LON, 0);
				// String locInfo = data
				// .getStringExtra(BDMapActivity.LOC_INFO);
				// sendLocationMessage(lat, lon, locInfo);
				// }
			}
				break;
			default:
				break;
			}
		}
	}

}
