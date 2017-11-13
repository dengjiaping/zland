package com.zhisland.lib.image.viewer;

import java.io.File;
import java.io.Serializable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.lib.R;
import com.zhisland.lib.bitmap.ImageCache;
import com.zhisland.lib.bitmap.ImageLoadListener;
import com.zhisland.lib.image.GalleryListener;
import com.zhisland.lib.image.NewsGallery;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.file.FileMgr;
import com.zhisland.lib.util.file.FileUtil;
import com.zhisland.lib.view.ImageViewEx;

/**
 * 浏览图片集的大图，支持图片随手势放大缩小，也可支持删除
 * 
 * 使用方法： FreeImageViewer.invoke(Context, ArrayList<ZHPicture>, curIndex,
 * maxIndex, rightButtonId, reqCode);
 * 参数含义：Context/图片集合/图片当前位置/图片总数/titlebar右侧按钮（现在支持保存、删除），可为空/请求码
 * 
 * 删除图集中图片返回删除位置,RESULT_CODE = FreeImageViewer.POST_IMAGE_DELETE int deleteIndex
 * = data.getIntExtra(FreeImageViewer.TO_INDEX, -1)；
 * 
 */
public class FreeImageViewer extends Activity implements GalleryListener {

	public static final String IMAGES = "freeimages";
	public static final String CUR_INDEX = "cur_index";
	public static final String MAX_INDEX = "max_index";
	public static final String RIGHT_INDEX = "btn_index";
	public static final String TO_INDEX = "to_index";

	/**
	 * 右侧按钮，支持保存，删除功能
	 */
	public static final int BUTTON_SAVE = 100;
	public static final int BUTTON_DELETE = 101;

	/**
	 * 用户删除RESULT_CODE
	 */
	public static final int POST_IMAGE_DELETE = 1009;

	public static int screenWidth;
	public static int screenHeight;
	private NewsGallery gallery;
	private GalleryAdapter adapter;
	private boolean isFullScreen = false;
	private RelativeLayout navigation;

	private TextView title;
	private TextView back;
	private TextView rightBtn;

	private int rightBtnIndex = 0;
	private int curIndex = 0;
	private int maxIndex = 0;

	/**
	 * 浏览指定的图片
	 * 
	 * @param context
	 * @param url
	 */
	public static void invoke(Activity context, String url) {
		DefaultStringAdapter adapter = new DefaultStringAdapter();
		adapter.add(0, url);
		FreeImageViewer.invoke(context, adapter, 0, 0,
				FreeImageViewer.BUTTON_SAVE, 0);
	}

	/**
	 * 浏览指定的图片集合
	 * 
	 * @param context
	 * @param urls
	 * @param curIndex
	 * @param maxIndex
	 * @param rightButtonId
	 * @param reqCode
	 */
	public static void invoke(Activity context, ImageDataAdapter adapter,
			int curIndex, int maxIndex, int rightButtonId, int reqCode) {
		Intent intent = new Intent(context, FreeImageViewer.class);
		intent.putExtra(IMAGES, (Serializable) adapter);
		intent.putExtra(CUR_INDEX, curIndex);
		intent.putExtra(MAX_INDEX, maxIndex);
		intent.putExtra(RIGHT_INDEX, rightButtonId);
		context.startActivityForResult(intent, reqCode);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ImageDataAdapter images = (ImageDataAdapter) this.getIntent()
				.getSerializableExtra(IMAGES);
		if (images == null || images.count() < 1) {
			this.finish();
			return;
		}

		curIndex = getIntent().getIntExtra(CUR_INDEX, 0);
		maxIndex = getIntent().getIntExtra(MAX_INDEX, 0);
		rightBtnIndex = getIntent().getIntExtra(RIGHT_INDEX, 0);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.free_image_viewer);

		gallery = (NewsGallery) findViewById(R.id.gallery);

		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
		gallery.setGestureListener(this);

		adapter = new GalleryAdapter(this, images);

		gallery.setAdapter(adapter);
		gallery.setSelection(curIndex, true);

		navigation = (RelativeLayout) findViewById(R.id.navigation);

		setFullScreen(isFullScreen);

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				setTitleText(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		title = (TextView) findViewById(R.id.titledes);
		back = (TextView) findViewById(R.id.gallery_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});
		rightBtn = (TextView) findViewById(R.id.gallery_action);
		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (rightBtnIndex) {
				case BUTTON_SAVE: {
					String item = adapter.getItem(gallery
							.getSelectedItemPosition());
					if (ImageCache.getInstance().containBitmap(
							StringUtil.validUrl(item))) {
						saveImage();
					}
				}
					break;
				case BUTTON_DELETE:
					showQuitDlg("确认删除图片吗？");
					break;
				}

			}
		});
		setTitleStyle();
		setRightBtn();
		setTitleText(curIndex);
		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();

	}

	private void setTitleStyle() {
		title.setTextColor(this.getResources().getColor(R.color.white));
		back.setTextColor(this.getResources().getColor(R.color.white));
		rightBtn.setTextColor(this.getResources().getColor(R.color.white));
		rightBtn.setPadding(DensityUtil.dip2px(10), 0, DensityUtil.dip2px(10),
				0);
	}

	public void setRightBtn() {
		switch (rightBtnIndex) {
		case BUTTON_SAVE:
			if (rightBtn != null) {
				rightBtn.setText("保存");
			}
			break;
		case BUTTON_DELETE:
			if (rightBtn != null) {
				rightBtn.setText("删除");
			}
			break;
		default:
			rightBtn.setVisibility(View.INVISIBLE);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		back();
	}

	private void back() {
		Intent intent = new Intent();
		intent.putExtra(TO_INDEX, gallery.getSelectedItemPosition());
		this.setResult(RESULT_OK, intent);
		FreeImageViewer.this.finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	public void setTitleText(int cur) {
		if (cur >= 0 && maxIndex > 0 && cur < maxIndex) {
			title.setText(cur + 1 + " / " + maxIndex);
			curIndex = cur;
		}
	}

	private void saveImage() {
		if (FileUtil.isExternalMediaMounted()) {

			if (adapter == null)
				return;

			String url = adapter.getItem(gallery.getSelectedItemPosition());
			String originalFilePath = ImageCache.getInstance()
					.getFileFromDiskCache(url);
			if (StringUtil.isNullOrEmpty(originalFilePath))
				return;

			String imageName = FileUtil.convertFileNameFromUrl(url);

			File sdcardFileDir = FileMgr.Instance().getDir(
					FileMgr.DirType.IMAGE);

			File sdcardFile = new File(sdcardFileDir, imageName + ".jpg");
			FileUtil.copyFile(new File(originalFilePath), sdcardFile);

			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.fromFile(sdcardFile)));

			ToastUtil.showLong("已成功保存至  " + sdcardFile.getAbsolutePath());

		} else {
			ToastUtil.showLong("请插入SD卡");
		}
	}

	@Override
	public void onSingleTabUp() {
		setFullScreen(!isFullScreen);
	}

	private void setFullScreen(boolean isFull) {
		isFullScreen = isFull;
		if (isFull) {
			navigation.setVisibility(View.INVISIBLE);
		} else {

			navigation.setVisibility(View.VISIBLE);
		}
	}

	private void showQuitDlg(String content) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				FreeImageViewer.this);
		builder.setTitle("提示")
				.setMessage(content)
				.setCancelable(false)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent();
						intent.putExtra(TO_INDEX,
								gallery.getSelectedItemPosition());
						setResult(POST_IMAGE_DELETE, intent);

						FreeImageViewer.this.finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static final class Holder implements ImageLoadListener {

		public ImageViewEx image;
		public ImageViewEx simage;
		public ProgressBar pro;

		@Override
		public void onLoadFinished(String url, int status) {
			pro.setVisibility(View.GONE);
			if (status == ImageLoadListener.STATUS_SUCCESS) {
				simage.setVisibility(View.GONE);
			} else {
				image.setImageDrawable(simage.getDrawable());
				simage.setVisibility(View.GONE);
			}

		}
	}

}