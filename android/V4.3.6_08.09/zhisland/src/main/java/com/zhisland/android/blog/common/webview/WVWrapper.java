package com.zhisland.android.blog.common.webview;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhisland.android.blog.common.app.AppUtil;
import com.zhisland.android.blog.common.app.ZhislandApplication;
import com.zhisland.android.blog.common.uri.AUriMgr;
import com.zhisland.android.blog.common.view.AProgressDialog;
import com.zhisland.android.blog.common.view.LineProgressView;
import com.zhisland.lib.bitmap.ImageResizer;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.image.FreeImages;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.web.JSBase;
import com.zhisland.lib.view.web.WebShare;
import com.zhisland.lib.view.web.ZHWebviewCache;

/**
 * webview的adapter类，内涵对webview的通用处理，包括点击查看图片大图，自动跳转正和岛自定义链接
 * 
 * @author arthur
 * 
 */
public class WVWrapper {

	/**
	 * 
	 * @author arthur
	 * 
	 */
	public static interface WebListener extends Serializable {

		void showShare();

		void onPageStart();

		void onPageFinish();

		void onReceivedTitle(String title);

		void onShowCustomView(View view, CustomViewCallback callback);

		void onHideCustomView();

		void loadError();
	}

	public static final String LOCAL_HOST = "zh_local_host";
	private static final String FILENAME_HTML = "zhisland.html";
	private static final String ENCODE = "UTF-8";
	private static final String TAG = "fragweb";

	// ========for upload image========
	private final static int REQ_FILE_CHOOSER = 1;
	private ValueCallback<Uri> mUploadMessage;

	// ==========web view field=============
	private WebView webview;
	private WebListener webListener;
	protected String curUrl;
	protected String curTitle;
	protected String curContent;
	public MyWebChromeClient myWebChromeClient;
	private HashMap<String, String> titles = new HashMap<String, String>();
	private Stack<String> backHis = new Stack<String>();

	// =====image browse start =====
	public static final int RES_VIEW_IMAGE = 101;
	protected int fromIndex = 0;
	protected FreeImages images = null;
	protected String imageUrl;// first image
	private WebShare share;
	// =====image browse finish =====
	private boolean enableCache;
	private Handler handler = new Handler();
	private Activity activity;
	private LineProgressView progressView;
	private final String errorUrl = "file:///android_asset/error.html";

	public WVWrapper(WebView webview) {
		this.webview = webview;
		this.activity = (Activity) webview.getContext();
		configWebview();
	}

	// 是否有loadding 框
	public WVWrapper(WebView webview, boolean isDialogShow, boolean enableCache) {
		this.webview = webview;
		this.activity = (Activity) webview.getContext();
		this.enableCache = enableCache;
		configWebview();
	}

	// 是否有loadding 框
	public WVWrapper(WebView webview, LineProgressView progressView,
			boolean enableCache) {
		this.webview = webview;
		this.activity = (Activity) webview.getContext();
		this.enableCache = enableCache;
		this.progressView = progressView;
		configWebview();
	}

	public void setWebView(WebView webview) {
		this.webview = webview;
		this.activity = (Activity) webview.getContext();
		configWebview();
	}

	/**
	 * 清掉当前url对应的缓存并重新加载
	 */
	public void refresh() {
		if (webview == null)
			return;

		if (!StringUtil.isNullOrEmpty(curUrl)) {
			webview.reload();
		} else if (!StringUtil.isNullOrEmpty(curContent)) {
			loadData(curContent);
		}

	}

	/**
	 * 获取share object
	 */
	public WebShare getShareObj() {
		if (share == null) {
			share = new WebShare();
			share.description = curUrl;
			share.imageurl = imageUrl == null ? "" : imageUrl;
			share.title = curTitle == null ? "" : curTitle;
			share.url = curUrl;
		}
		return share;
	}

	public void setWebListener(WebListener listener) {
		this.webListener = listener;
	}

	public void setFontSize(int size) {
		String jsString = String.format(
				"javascript:(function() {setFontSize(%d);})()", size,
				Locale.getDefault());
		loadUrl(jsString);
	}

	/**
	 * 加载字符串数据
	 */
	public void loadData(String content) {
		curContent = content;
		webview.loadData(curContent, "text/html; charset=UTF-8", null);
	}

	/**
	 * 加载url
	 */
	public void loadUrl(String url) {
		webview.loadUrl(url);
	}

	private String host = null;

	public void loadUrl(String url, String host, String value) {
		synCookies(activity, host, value);
		this.host = host;
		webview.loadUrl(url);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQ_FILE_CHOOSER: {
			if (null == mUploadMessage)
				return;
			Uri result = data == null || resultCode != Activity.RESULT_OK ? null
					: data.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
			break;
		}
		// case RES_VIEW_IMAGE: {
		// int toIndex = data.getIntExtra(FreeImageViewer.TO_INDEX, fromIndex);
		// if (toIndex != fromIndex) {
		// FreeImage fromImage = images.images.get(fromIndex);
		// FreeImage toImage = images.images.get(toIndex);
		// int scrollByDp = toImage.top - fromImage.top;
		// int scrollBy = DensityUtil.dip2px(scrollByDp);
		// int contentHeight = DensityUtil.dip2px(webview
		// .getContentHeight());
		// int webViewHeight = webview.getHeight();
		//
		// int height = contentHeight - webViewHeight;
		//
		// int sy = webview.getScrollY();
		// int toY = sy + scrollBy;
		// if (toY < 0) {
		// toY = 0;
		// } else if (height < toY) {
		// toY = height;
		// }
		//
		// SmoothScrollRunnable sr = new SmoothScrollRunnable(webview,
		// handler, sy, toY);
		// handler.post(sr);
		// }
		// break;
		// }
		}
	}

	public void onResume() {
		webview.resumeTimers();
		callHiddenWebViewMethod("onResume");
		String jsString = String.format("javascript:(function() {%s();})()",
				"onResume");
		loadUrl(jsString);
	}

	public void onPause() {
		webview.pauseTimers();
		callHiddenWebViewMethod("onPause");
	}

	public void onDestroy() {
		ViewGroup vg = (ViewGroup) webview.getParent();
		if (vg != null) {
			vg.removeView(webview);
		}
		webview.destroy();
	}

	/**
	 * 是否可以后退
	 */
	public boolean canGoBack() {
		return webview.canGoBack();
		// return backHis.size() > 1;
	}

	/**
	 * 后退
	 */
	public void goBack() {
		// WebBackForwardList history = webview.copyBackForwardList();
		webview.goBack();
		// try {
		// if (backHis == null || backHis.size() == 0) {
		// activity.finish();
		// }
		// lastPopUrl = backHis.pop();
		// String nextLoadUrl = backHis.lastElement();
		// if (nextLoadUrl.equals(errorUrl)) {
		// if (backHis.indexOf(nextLoadUrl) != 0) {
		// backHis.pop();
		// nextLoadUrl = backHis.lastElement();
		// }
		// }
		// loadUrl(nextLoadUrl);
		// } catch (Exception e) {
		// e.printStackTrace();
		// activity.finish();
		// }
	}

	// ==========internal methods
	/**
	 * 配置webview
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void configWebview() {
		webview.setHorizontalScrollBarEnabled(false);
		webview.setVerticalScrollBarEnabled(true);

		WebSettings setting = webview.getSettings();
		setting.setDefaultTextEncodingName("UTF-8");
		setting.setSupportZoom(true);
		setting.setBuiltInZoomControls(false);
		
		setting.setJavaScriptEnabled(true);
		setting.setDomStorageEnabled(true);
		setting.setDatabaseEnabled(true);
		String dir = ZhislandApplication.APP_CONTEXT.getDir("zhdatabase",
				Context.MODE_PRIVATE).getPath();
		setting.setDatabasePath(dir);
		String originalUA = setting.getUserAgentString();
		String curUA = originalUA + " " + AUriMgr.SCHEMA_BLOG() + "/"
				+ AppUtil.Instance().getVersionCode();
		setting.setUserAgentString(curUA);
		setting.setAppCacheEnabled(true);
		setting.setAppCacheMaxSize(1024 * 1024 * 5);
		
		//扩大比例的缩放
		// setting.setUseWideViewPort(true);
		//自适应屏幕
		setting.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		setting.setLoadWithOverviewMode(true);
		
		if (enableCache)
			setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		else
			setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// setPageCacheCapacity(setting);
		webview.setWebViewClient(new MyWebViewClient());
		myWebChromeClient = new MyWebChromeClient();
		webview.setWebChromeClient(myWebChromeClient);
		webview.addJavascriptInterface(new JavaScriptInterface(activity),
				"Android");

		// CookieManager.getInstance().setAcceptCookie(true);
		//
		// //ArrayList<Pair<String, String>> cookies = new
		// ArrayList<Pair<String, String>>();
		// CookieManager.getInstance().setCookie(".zhisland.com/",
		// "uid=" + PrefUtil.Instance().getUserId());
		// synCookies(activity);
	}

	protected void setPageCacheCapacity(WebSettings webSettings) {
		try {
			Class<?> c = Class.forName("android.webkit.WebSettingsClassic");

			Method tt = c.getMethod("setPageCacheCapacity",
					new Class[] { int.class });

			tt.invoke(webSettings, 5);

		} catch (ClassNotFoundException e) {
			System.out.println("No such class: " + e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void synCookies(Context context, String host, String value) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		cookieManager.removeSessionCookie();// 移除
		// ".zhisland.com/"
		// "uid="+ PrefUtil.Instance().getUserId()
		cookieManager.setCookie(host, value);// cookies是在HttpClient中获得的cookie
		CookieSyncManager.getInstance().sync();
	}

	private void callHiddenWebViewMethod(String name) {
		if (webview != null) {
			try {
				Method method = WebView.class.getMethod(name);
				method.invoke(webview);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	protected class JavaScriptInterface extends JSBase {
		Context mContext;

		/** Instantiate the interface and set the context */
		public JavaScriptInterface(Context c) {
			mContext = c;
		}

		/** Show a toast from the web page */
		@JavascriptInterface
		public void showToast(final String toast) {
			handler.post(new Runnable() {

				@Override
				public void run() {

					webview.getScrollX();
					// FreeImages images = GsonHelper.GetCommonGson().fromJson(
					// toast, FreeImages.class);
					// WVWrapper.this.images = images;
					// fromIndex = images.select;
					//
					// DefaultStringAdapter adapter = new
					// DefaultStringAdapter();
					// for (FreeImage fi : images.images) {
					// adapter.add(fi.url());
					// }
					// FreeImageViewer.invoke(activity, adapter, fromIndex,
					// adapter.count(), FreeImageViewer.BUTTON_SAVE,
					// RES_VIEW_IMAGE);
				}
			});
		}

		@JavascriptInterface
		public void setImageUrl(final String imageUrl) {
			WVWrapper.this.imageUrl = imageUrl;
		}

	}

	private final class MyWebViewClient extends WebViewClient {

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			MLog.d("zxdxp8l", "shouldInterceptRequest===" + url);

			WebResourceResponse response;
			if (isInfo(url)) {
				response = loadInfo(url);
				if (response != null) {
					return response;
				}
			}

			response = loadForLocalRes(url);
			if (response != null) {
				return response;
			}

			response = loadAssetRes(url);
			if (response != null) {
				return response;
			}

			response = loadFromImageCache(url);
			if (response != null) {
				return response;
			}

			response = super.shouldInterceptRequest(view, url);
			return response;
		}

		/**
		 * 加载本地的资源
		 */
		private WebResourceResponse loadForLocalRes(String url) {
			MLog.d("zxdxp8l", "loadForLocalRes : " + url);
			Uri uri = Uri.parse(url);
			String host = uri.getHost();
			if (host == null || !host.equals(LOCAL_HOST))
				return null;

			String path = uri.getLastPathSegment();

			Bitmap bitmap = ImageResizer.decodeFileByMaxWidth(path,
					DensityUtil.getWidth());
			if (bitmap != null) {
				try {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG,
							50 /* ignored for PNG */, bos);
					byte[] bitmapdata = bos.toByteArray();
					ByteArrayInputStream bs = new ByteArrayInputStream(
							bitmapdata);
					return new WebResourceResponse("image/*", "base64", bs);
				} catch (Exception ex) {
					MLog.e(TAG, ex.getMessage(), ex);
				}
			}
			return null;
		}

		/**
		 * 从本地缓存加载资源,如果是资讯，且本地不存在，则从网络加载并缓存
		 */
		private WebResourceResponse loadInfo(String url) {
			MLog.d("zxdxp8l", "loadInfo : " + url);
			if (!isInfo(url))
				return null;

			String cacheKey = GetCacheKey(url);
			ZHWebviewCache htmlCache;
			htmlCache = DefaultDataListener.instance().getCache(cacheKey);

			if (htmlCache == null) {
				return null;
			}

			File file = new File(htmlCache.filepath);
			if (file.exists()) {
				FileInputStream is;
				try {
					is = new FileInputStream(file);

					return new WebResourceResponse("text/html", ENCODE, is);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		/**
		 * 从头像库加载资源文件
		 */
		private WebResourceResponse loadFromImageCache(String url) {
			MLog.d("zxdxp8l", "loadFromImageCache : " + url);
			if (url.endsWith("jpg") || url.endsWith("png")) {
				// 头像处理
				Bitmap bitmap = ImageWorkFactory.getCircleFetcher().getBitmap(
						url);
				if (bitmap != null) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.PNG,
							100 /* ignored for PNG */, bos);
					byte[] bitmapdata = bos.toByteArray();
					ByteArrayInputStream bs = new ByteArrayInputStream(
							bitmapdata);
					return new WebResourceResponse("image/*", "base64", bs);
				}
			}
			return null;
		}

		/**
		 * 尝试从asset资源文件夹加载资源
		 */
		private WebResourceResponse loadAssetRes(String url) {
			MLog.d("zxdxp8l", "loadAssetRes : " + url);
			if (ZhislandApplication.LAZY_WEB_FILES != null) {
				Uri uri = Uri.parse(url);
				for (String fileName : ZhislandApplication.LAZY_WEB_FILES) {
					if (uri.getScheme().startsWith("http")
							&& url.contains(fileName)) {
						InputStream is;
						try {
							is = activity.getAssets().open(
									ZhislandApplication.LAZY_DIR + "/"
											+ fileName);
							String encode = fileName.contains("jpg")
									|| fileName.contains("png") ? "base64"
									: "UTF-8";
							String format = fileName.contains("jpg")
									|| fileName.contains("png") ? "image/*"
									: "text/css";
							return new WebResourceResponse(format, encode, is);
						} catch (IOException e) {
							MLog.e(TAG, e.getMessage(), e);
							break;
						}
					}
				}
			}

			return null;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (StringUtil.isNullOrEmpty(url)) {
				return false;
			}
			Uri uri = Uri.parse(url);
			String schema = uri.getScheme();
			if (schema == null || !schema.equals(AUriMgr.SCHEMA_BLOG())) {
				if (host != null && host.equals(".zhisland.com/")) {
					view.loadUrl(url);
				}
				return false;
			}
			AUriMgr.instance().viewRes(activity, url);
			return true;

		}

		private String curErrorUrl = null;

		@Override
		public void onReceivedError(WebView view, int errCode,
				String description, String failingUrl) {

			MLog.d("zxdxp8l", "onReceivedError===" + failingUrl);
			this.curErrorUrl = failingUrl;
			try {
				view.stopLoading();
				view.clearView();
			} catch (Exception e) {
				e.printStackTrace();
			}

			backHis.remove(failingUrl);
			view.loadUrl(errorUrl);
			if (webListener != null) {
				webListener.loadError();
				webListener.onPageFinish();
			}
		}

		AProgressDialog dialog;

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			this.curErrorUrl = null;
			curUrl = url;
			if (webListener != null) {
				webListener.onReceivedTitle("");
				MLog.d("zxdxp8l", "onPageStarted===" + url);
				webListener.onPageStart();
				if (progressView != null) {
					progressView.setVisibility(View.VISIBLE);
				}
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			boolean isCurErrorUrl = this.curErrorUrl != null
					&& this.curErrorUrl.equals(url);
			boolean isDuplicateUrl = !backHis.isEmpty()
					&& backHis.lastElement().equals(url);

			if (!isCurErrorUrl && !isDuplicateUrl) {
				// cleanOverride(url);
				backHis.push(url);
			}
			if (webListener != null) {
				webListener.onReceivedTitle(view.getTitle());
				webListener.onPageFinish();
			}

		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url,
				boolean isReload) {
			// super.doUpdateVisitedHistory(view, url, isReload);
			MLog.d("zxdxp8l", "doUpdateVisitedHistory===" + url + ", isReload="
					+ isReload);
		}

		@Override
		public void onLoadResource(WebView view, String url) {

			// MLog.d("zxdxp8l", "onLoadResource===" + url);

		}
	}

	private final class MyWebChromeClient extends WebChromeClient {
		int lastProgress = 0;

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			WVWrapper.this.curTitle = title;
			if (webListener != null) {
				webListener.onReceivedTitle(title);
			}
			titles.put(view.getUrl(), title);
		}

		// The undocumented magic method override
		// Eclipse will swear at you if you try to put @Override here
		// For Android 3.0+
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			activity.startActivityForResult(
					Intent.createChooser(i, "File Chooser"), REQ_FILE_CHOOSER);

		}

		// For Android 3.0+
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			activity.startActivityForResult(
					Intent.createChooser(i, "File Browser"), REQ_FILE_CHOOSER);
		}

		// For Android 4.1
		@SuppressWarnings("unused")
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			activity.startActivityForResult(
					Intent.createChooser(i, "File Chooser"), REQ_FILE_CHOOSER);

		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			webListener.onShowCustomView(view, callback);
			super.onShowCustomView(view, callback);
		}

		@Override
		public void onHideCustomView() {
			webListener.onHideCustomView();
			super.onHideCustomView();
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (progressView != null) {
				if (lastProgress != newProgress) {
					lastProgress = newProgress;
					progressView.update(lastProgress);
					if (lastProgress == 100) {
						progressView.setBackgroundColor(Color.WHITE);
					}
				}

			}

		}
	}

	private static final class SmoothScrollRunnable implements Runnable {

		static final int ANIMATION_DURATION_MS = 750;
		static final int ANIMATION_FPS = 1000 / 60;

		private final Interpolator interpolator;
		private final int scrollToY;
		private final int scrollFromY;
		private final Handler handler;
		private WebView webview;

		private long startTime = -1;
		private int currentY = -1;

		public SmoothScrollRunnable(WebView webview, Handler handler,
				int fromY, int toY) {
			this.webview = webview;
			this.handler = handler;
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
				webview.scrollTo(0, currentY);
			}

			// If we're not at the target Y, keep going...
			if (scrollToY != currentY) {
				handler.postDelayed(this, ANIMATION_FPS);
			}
		}

	}

	/**
	 * 根据URL生成缓存KEY
	 */
	private static String GetCacheKey(String url) {
		return com.zhisland.lib.util.MD5.getMD5(url) + FILENAME_HTML;
	}

	/**
	 * 判断一个URL是否是资讯
	 */
	private static boolean isInfo(String url) {
		return url.contains("zhisland.com")
				&& url.toLowerCase(Locale.getDefault()).contains(
						"mod=News&act=index".toLowerCase(Locale.getDefault()));
	}

	/**
	 * 获取特定的本地资源路径
	 */
	public static final String LocalPathUrl(String path) {
		if (StringUtil.isNullOrEmpty(path))
			return null;
		String s1 = URLEncoder.encode(path);
		String s2 = URLEncoder.encode(s1);
		return String.format("http://%s/%s", LOCAL_HOST, s2);
	}

	public void onHideCustomView() {
		myWebChromeClient.onHideCustomView();
	}

	/**
	 * 清理因为 pc url转手机url时产生的重定向
	 * 
	 * @param url
	 */
	private void cleanOverride(String url) {
		if (backHis == null || backHis.size() == 0) {
			return;
		}
		if (backHis.contains(url) && !backHis.lastElement().equals(url)) {
			while (backHis.size() != 0) {
				backHis.pop();
				if (backHis.lastElement().equals(url)) {
					backHis.pop();
					break;
				}
			}
		}

		try {
			String currentHost = new URL(url).getHost();
			String lastHost = new URL(backHis.lastElement()).getHost();
			if (currentHost.startsWith("m.")
					&& !lastHost.startsWith("m.")
					&& currentHost.substring(currentHost.indexOf(".")).equals(
							lastHost.substring(lastHost.indexOf(".")))) {
				backHis.remove(backHis.lastElement());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
