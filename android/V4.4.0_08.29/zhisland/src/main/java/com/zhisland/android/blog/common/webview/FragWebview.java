package com.zhisland.android.blog.common.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.android.blog.R;
import com.zhisland.android.blog.common.base.DefaultTitleBarClickListener;
import com.zhisland.android.blog.common.base.TitleBarProxy;
import com.zhisland.android.blog.common.view.LineProgressView;
import com.zhisland.android.blog.common.webview.WVWrapper.WebListener;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.view.web.WebShare;

/**
 * webview fragment，主要通过{@link WVWrapper}对webview进行控制和调用
 * 
 * @author arthur
 * 
 */
public class FragWebview extends FragBase {

	public static final String INK_URL = "frag_web_url";
	public static final String INK_CONTENT = "frag_web_content";
	public static final String INK_IS_SHOW_SHARE = "frag_is_show_share";
	public static final String INK_TITLE = "frag_web_title";
	public static final String INK_COOKIES_HOST = "frag_web_cookies_host";
	public static final String INK_COOKIES_VALUE = "frag_web_cookies_value";
	public static final String INK_DIALOG_SHOW = "frag_web_dialog_show";
	public static final String INK_CACHE = "frag_web_enable_cache";
	private static final String TAG = "fragweb";

	private WVWrapper wvWrapper;
	protected View tool;
	protected TextView[] fonts;

	protected String curUrl;
	protected String curTitle;
	protected String curContent;

	/**
	 * 清掉当前url对应的缓存并重新加载
	 */
	public void refresh() {
		if (wvWrapper == null)
			return;

		wvWrapper.refresh();
	}

	/**
	 * 获取share object
	 */
	public WebShare getShareObj() {
		if (wvWrapper != null)
			return wvWrapper.getShareObj();

		return null;
	}

	/**
	 * 设置初始化的URL
	 */
	public void setUrl(String url) {
		this.curUrl = url;
	}

	public void setCurContent(String curContent) {
		this.curContent = curContent;
	}

	// -------------free image-------------
	private LinearLayout root;
	public LineProgressView progressView;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = (LinearLayout) inflater.inflate(R.layout.frag_tab_item, null);
		progressView = (LineProgressView)root.findViewById(R.id.progress);
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		root.setLayoutParams(lp);
		WebView webview = new WebView(getActivity());
		boolean isDialogShow = getActivity().getIntent().getBooleanExtra(
				INK_DIALOG_SHOW, true);
		boolean enableCache = getActivity().getIntent().getBooleanExtra(
				INK_CACHE, false);
		if(isDialogShow){
			wvWrapper = new WVWrapper(webview, progressView, enableCache);
		}else{
			wvWrapper = new WVWrapper(webview, isDialogShow, enableCache);
		}
		
		webview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (tool != null) {
					tool.setVisibility(View.GONE);
				}
				return false;
			}
		});
		if (getActivity() instanceof WebListener) {
			WebListener webListener = (WebListener) getActivity();
			wvWrapper.setWebListener(webListener);
		}
		root.addView(webview, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		tool = inflater.inflate(R.layout.web_font, null);
		RelativeLayout.LayoutParams paramTool = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		paramTool.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		root.addView(tool, paramTool);
		tool.setVisibility(View.GONE);

		fonts = new TextView[4];
		OnClickListener fontListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (TextView tv : fonts) {
					boolean isClicked = tv.equals(v);
					tv.setSelected(isClicked);
					if (isClicked) {
						tv.setTextColor(Color.WHITE);
					} else {
						tv.setTextColor(getResources().getColor(
								R.color.txt_light_gray));
					}
				}
				Object obj = v.getTag();
				if (obj instanceof Integer) {
					wvWrapper.setFontSize((Integer) obj);
				}
			}
		};

		ViewGroup vg = (ViewGroup) tool.findViewById(R.id.web_font_content);
		for (int i = 0; i < 4; i++) {
			TextView tv = new TextView(getActivity());
			tv.setText("A");
			tv.setBackgroundResource(R.drawable.sel_bg_web_font);
			tv.setTextSize(16 + 3 * i);
			tv.setPadding(0, 0, 0, DensityUtil.dip2px(5 + (-i) * 1f));
			tv.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
			tv.setIncludeFontPadding(false);
			LinearLayout.LayoutParams paramTv = new LinearLayout.LayoutParams(
					DensityUtil.dip2px(35), LayoutParams.MATCH_PARENT);
			if (i > 0) {
				paramTv.leftMargin = DensityUtil.dip2px(1);
			}
			tv.setTag(i + 1);
			tv.setOnClickListener(fontListener);
			vg.addView(tv, paramTv);
			fonts[i] = tv;
		}
		fonts[1].setSelected(true);

		tool.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tool.setVisibility(View.GONE);
			}
		});
		tool.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				MLog.d(TAG, "");
				tool.setVisibility(View.GONE);
			}
		});
		initBar(root, inflater, container, savedInstanceState);
		return root;
	}

	public TitleBarProxy titleBar;

	TextView backBtn;
	ImageView vClose;
	public TextView titleText;
	String titleStr;

	private void initBar(ViewGroup root, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		titleBar = new TitleBarProxy();
		titleBar.configTitle(root, TitleType.TITLE_LAYOUT,
				new DefaultTitleBarClickListener(getActivity()) {

					@Override
					public void onTitleClicked(View view, int tagId) {
						switch (tagId) {
						case TitleBarProxy.TAG_BACK:
							getActivity().onBackPressed();
							break;
						}
					}
				});

		ProgressBar pb = new ProgressBar(getActivity(), null,
				android.R.attr.progressBarStyleSmallInverse);
		pb.setIndeterminateDrawable(getResources().getDrawable(
				R.drawable.progress_light));
		pb.setIndeterminate(true);

		titleBar.addBackButton();
		titleStr = getActivity().getIntent().getStringExtra(INK_TITLE);
		getActivity().setTitle(titleStr);
		if (root != null) {
			titleText = (TextView) root.findViewById(R.id.title_text);
			if (titleText != null) {
				if (StringUtil.isNullOrEmpty(titleStr) && titleBar != null) {
					titleBar.setImageTitle(R.drawable.img_zhisland_title);
				} else {
					titleText.setVisibility(View.VISIBLE);
					titleText.setText(titleStr);
					titleBar.getRootView().findViewById(R.id.ivTitle)
							.setVisibility(View.GONE);
				}
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String url = curUrl;
		String host = getActivity().getIntent()
				.getStringExtra(INK_COOKIES_HOST);
		String value = getActivity().getIntent().getStringExtra(
				INK_COOKIES_VALUE);
		if (url == null) {
			url = getActivity().getIntent().getStringExtra(INK_URL);
			// 防止url输入不完整不能打开页面的情况
			if (!StringUtil.isNullOrEmpty(url)) {
				boolean fullUrl = url.contains("http://")
						|| url.contains("https://");
				if (!fullUrl) {
					url = "http://" + url;
				}
			}
		}
		if (!StringUtil.isNullOrEmpty(url)) {
			if (StringUtil.isNullOrEmpty(host)
					&& StringUtil.isNullOrEmpty(value))
				wvWrapper.loadUrl(url);
			else
				wvWrapper.loadUrl(url, host, value);
		} else if (!StringUtil.isNullOrEmpty(curContent)) {
			wvWrapper.loadData(curContent);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		wvWrapper.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 加载url
	 */
	private void loadUrl(String url) {
		wvWrapper.loadUrl(url);
	}

	@Override
	public void onResume() {
		super.onResume();
		wvWrapper.onResume();
	}

	@Override
	public void onPause() {
		wvWrapper.onPause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		wvWrapper.onDestroy();
		super.onDestroy();
	}

	/**
	 * 显示字体调整试图
	 */
	public void showFontView() {
		tool.setVisibility(View.VISIBLE);
		tool.requestFocus();
	}

	/**
	 * 是否可以后退
	 */
	public boolean canGoBack() {
		return wvWrapper.canGoBack();
	}

	/**
	 * 后退
	 */
	public void goBack() {
		wvWrapper.goBack();
	}

	@Override
	public String getPageName() {
		return "fragwebview";
	}

}
