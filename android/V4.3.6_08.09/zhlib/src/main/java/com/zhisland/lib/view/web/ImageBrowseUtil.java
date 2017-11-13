package com.zhisland.lib.view.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.webkit.WebView;

import com.zhisland.lib.async.http.task.GsonHelper;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.image.FreeImage;
import com.zhisland.lib.image.FreeImages;
import com.zhisland.lib.image.viewer.FreeImageViewer;
import com.zhisland.lib.util.DensityUtil;

public class ImageBrowseUtil {

	private static final int RES_VIEW_IMAGE = 11;
	public static final String TO_INDEX = "toIndex";
	private static String jsString = null;

	private int fromIndex = 0;
	private FreeImages images = null;
	protected WebView webView;
	private final Handler handler;
	private final Context context;

	public static String getJSString() {
		if (jsString == null) {
			jsString = initJS();
		}
		return jsString;
	}

	public ImageBrowseUtil(Context context, WebView webview, Handler handler) {
		this.context = context;
		this.webView = webview;
		this.handler = handler;
	}

	@SuppressLint("JavascriptInterface")
	void setupJsIntrface() {
		webView.addJavascriptInterface(new JavaScriptInterface(context),
				"Android");
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		case RES_VIEW_IMAGE:
			int toIndex = data.getIntExtra(TO_INDEX, fromIndex);
			if (toIndex != fromIndex) {
				FreeImage fromImage = images.images.get(fromIndex);
				FreeImage toImage = images.images.get(toIndex);
				int scrollByDp = toImage.top - fromImage.top;
				int scrollBy = DensityUtil.dip2px(ZHApplication.APP_CONTEXT,
						scrollByDp);
				int contentHeight = DensityUtil.dip2px(
						ZHApplication.APP_CONTEXT, webView.getContentHeight());
				int webViewHeight = webView.getHeight();

				int height = contentHeight - webViewHeight;

				int sy = webView.getScrollY();
				int toY = sy + scrollBy;
				if (toY < 0) {
					toY = 0;
				} else if (height < toY) {
					toY = height;
				}

				SmoothScrollRunnable sr = new SmoothScrollRunnable(handler,
						webView, sy, toY);
				handler.post(sr);
			}
			break;
		}
	}

	private static String initJS() {

		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					ZHApplication.APP_CONTEXT.getAssets().open("js")));
			String word;
			while ((word = br.readLine()) != null)
				sb.append(word);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close(); // stop reading
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();

	}

	protected class JavaScriptInterface {
		Context mContext;

		/**
		 * Instantiate the interface and set the context
		 */
		public JavaScriptInterface(Context c) {
			mContext = c;
		}

		/**
		 * Show a toast from the web page
		 */
		public void showToast(final String toast) {
			handler.post(new Runnable() {

				@Override
				public void run() {

					webView.getScrollX();
					FreeImages images = GsonHelper.GetCommonGson().fromJson(
							toast, FreeImages.class);
					ImageBrowseUtil.this.images = images;
					fromIndex = images.select;
					Intent intent = new Intent(context, FreeImageViewer.class);
					intent.putExtra(FreeImageViewer.IMAGES, images.images);
					intent.putExtra(FreeImageViewer.CUR_INDEX, images.select);
					intent.putExtra(FreeImageViewer.MAX_INDEX,
							images.images.size());
					intent.putExtra(FreeImageViewer.RIGHT_INDEX,
							FreeImageViewer.BUTTON_SAVE);
					((Activity) context).startActivityForResult(intent,
							RES_VIEW_IMAGE);
				}
			});
		}
	}

	private static final class SmoothScrollRunnable implements Runnable {

		static final int ANIMATION_DURATION_MS = 750;
		static final int ANIMATION_FPS = 1000 / 60;

		private final Interpolator interpolator;
		private final int scrollToY;
		private final int scrollFromY;
		private final Handler handler;

		private final WebView webView;
		private final boolean continueRunning = true;
		private long startTime = -1;
		private int currentY = -1;

		SmoothScrollRunnable(Handler handler, WebView webView, int fromY,
				int toY) {
			this.handler = handler;
			this.webView = webView;
			this.scrollFromY = fromY;
			this.scrollToY = toY;
			this.interpolator = new AccelerateDecelerateInterpolator();
		}

		@Override
		public void run() {

			/**
			 * Only set startTime if this is the first time we're starting, else
			 * actually calculate the Y delta
			 */
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = (1000 * (System.currentTimeMillis() - startTime))
						/ ANIMATION_DURATION_MS;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math
						.round((scrollFromY - scrollToY)
								* interpolator
										.getInterpolation(normalizedTime / 1000f));
				this.currentY = scrollFromY - deltaY;
				webView.scrollTo(0, currentY);
			}

			// If we're not at the target Y, keep going...
			if (continueRunning && scrollToY != currentY) {
				handler.postDelayed(this, ANIMATION_FPS);
			}
		}

	};
}
