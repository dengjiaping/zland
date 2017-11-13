package com.zhisland.lib.image;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.UUID;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.lib.R;
import com.zhisland.lib.component.act.BaseFragmentActivity;
import com.zhisland.lib.component.act.TitleType;
import com.zhisland.lib.image.FragImgBucket.OnBucketListener;
import com.zhisland.lib.image.FragImgPicker.OnPickerListener;
import com.zhisland.lib.image.ImgPicData.ImageBucket;
import com.zhisland.lib.image.ImgPicData.ImageItem;
import com.zhisland.lib.util.IntentUtil;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.file.FileMgr;
import com.zhisland.lib.util.file.FileMgr.DirType;

/**
 * 图片选择Activity，包括预览已选择图片功能
 *
 * 使用方法：MultiImgPickerActivity.invoke(context, maxCount, REQ_CODE);
 * 参数含义：Context/选择图片最大值/请求码REQ_CODE
 *
 * 返回用户选择的图片url集合，ArrayList<String> pathes =
 * data.getStringArrayListExtra(MultiImgPickerActivity.RLT_PATHES);
 */
public class MultiImgPickerActivity extends BaseFragmentActivity implements
		OnBucketListener, OnPickerListener, OnClickListener {

	/**
	 * 选择照片的上限
	 */
	public static final String INK_MAX_COUNT = "max_count";
	public static final String INK_CROP_WIDTH = "crop_width";
	public static final String INK_CROP_HEIGHT = "crop_height";

	public static final String BUNDLE_CACHE_KEY_TMP_PATH = "bundle_cache_key_tmp_path";

	/**
	 * 选中的图片列表
	 */
	public static final String RLT_PATHES = "result_image_pathes";

	private static final int REQ_CAPTUE = 1234;

	private static final int REQ_CROP = 1235;

	private static final int DEFAULT_MAX = 8;
	private static final String TITLE = "图片选择";

	private FragImgBucket fragBucket;
	private FragImgPicker fragPicker;
	private FragImgPreview fragPreview;

	private Fragment preFrag;
	private Fragment curFrag;
	private ImageBucket preBucket;

	private int maxCount;
	private int curCount;

	private RelativeLayout rlTop;
	private TextView tvBack;
	private TextView tvTitle;
	private TextView tvCancel;
	private TextView tvPreview;
	private FinishBtn tvFinish;

	private String tmpPath;

	private int cropWidth;
	private int cropHeight;
	private String outImagePath;

	private LinkedHashMap<ImageBucket, ArrayList<String>> selected = new LinkedHashMap();

	public static void invoke(Activity context, int max, int reqCode) {
		Intent intent = new Intent(context, MultiImgPickerActivity.class);
		intent.putExtra(MultiImgPickerActivity.INK_MAX_COUNT, max);
		context.startActivityForResult(intent, reqCode);
	}

	public static void invoke(Activity context, int cropWidth,int cropHeight, int reqCode) {
		Intent intent = new Intent(context, MultiImgPickerActivity.class);
		intent.putExtra(MultiImgPickerActivity.INK_CROP_WIDTH, cropWidth);
		intent.putExtra(MultiImgPickerActivity.INK_CROP_HEIGHT, cropHeight);
		context.startActivityForResult(intent, reqCode);
	}

	@Override
	public void onContinueCreate(Bundle savedInstanceState) {
		super.onContinueCreate(savedInstanceState);

		setSwipeBackEnable(false);
        setSaveInstanceEnable(true);

		setContentView(R.layout.multi_img_picker);

		rlTop = (RelativeLayout) findViewById(R.id.rlTop);

		tvBack = (TextView) findViewById(R.id.tvBack);
		tvBack.setTextColor(Color.LTGRAY);
		tvBack.setOnClickListener(this);
		tvBack.setVisibility(View.INVISIBLE);

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(TITLE);

		tvCancel = (TextView) findViewById(R.id.tvCancel);
		tvCancel.setTextColor(Color.LTGRAY);
		tvCancel.setOnClickListener(this);

		tvPreview = (TextView) findViewById(R.id.tvPreview);
		tvPreview.setTextColor(Color.LTGRAY);
		tvPreview.setOnClickListener(this);

		tvFinish = (FinishBtn) findViewById(R.id.tvFinish);
		tvFinish.update(maxCount, 0);
		tvFinish.setOnClickListener(this);

		Intent intent = getIntent();
		maxCount = intent.getIntExtra(INK_MAX_COUNT, DEFAULT_MAX);
		cropWidth = intent.getIntExtra(INK_CROP_WIDTH, 0);
		cropHeight = intent.getIntExtra(INK_CROP_HEIGHT, 0);

		if(cropWidth > 0 && cropHeight > 0){
			maxCount = 1;
			outImagePath = FileMgr.Instance().getFilepath(DirType.IMAGE, System.currentTimeMillis() + ".jpg");
		}

		fragBucket = new FragImgBucket();
		fragBucket.setBucketListener(this);
		fragPicker = new FragImgPicker();
		fragPicker.setOnPickListener(this);
		fragPreview = new FragImgPreview();

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.frag_container, fragBucket);
		ft.add(R.id.frag_container, fragPicker);
		ft.add(R.id.frag_container, fragPreview);
		ft.hide(fragPicker);
		ft.hide(fragPreview);
		ft.commit();
		this.curFrag = fragBucket;
	}

	@Override
	public void onBucketClicked(ImageBucket bucket) {
		if (FragImgBucket.PIC_CAP.equals(bucket.bucketName)) {
			tmpPath = FileMgr.Instance().getFilepath(DirType.TMP,
					UUID.randomUUID().toString() + ".jpg");
			Intent intent = IntentUtil.intentToCaptureImage(tmpPath);
			startActivityForResult(intent, REQ_CAPTUE);
			overridePendingTransition(R.anim.act_bottom_in, R.anim.act_hold);
			return;
		}
		showPicker(bucket);
		this.preBucket = bucket;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(tmpPath != null){
			outState.putString(BUNDLE_CACHE_KEY_TMP_PATH, tmpPath);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Object obj = savedInstanceState.getString(BUNDLE_CACHE_KEY_TMP_PATH);
		if(obj != null && obj instanceof String){
			tmpPath = (String) obj;
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_CAPTUE) {
			if (resultCode == RESULT_OK) {
				if(cropWidth > 0 && cropHeight > 0){
					Uri imageUri = Uri.fromFile(new File(tmpPath));
					Uri outUri = Uri.fromFile(new File(outImagePath));
					startActivityForResult(IntentUtil.cropIntent(imageUri, cropWidth,cropHeight,outUri),REQ_CROP);
				}else{
					Intent intent = new Intent();
					ArrayList<String> result = new ArrayList<String>();
					result.add(tmpPath);
					intent.putExtra(RLT_PATHES, result);
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		} else if(requestCode == REQ_CROP){
			if (resultCode == RESULT_OK) {
				if(!StringUtil.isNullOrEmpty(outImagePath)){
					File file = new File(outImagePath);
					if(file.exists()){
						Intent intent = new Intent();
						ArrayList<String> result = new ArrayList<String>();
						result.add(outImagePath);
						intent.putExtra(RLT_PATHES, result);
						setResult(RESULT_OK, intent);
						finish();
						return;
					}
				}
				setResult(RESULT_CANCELED, null);
				finish();
			}
		}else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public int onImgClicked(ImageBucket bucket, String imgpath) {
		ArrayList<String> pathes;
		if (!selected.containsKey(bucket)) {
			pathes = new ArrayList<String>();
			selected.put(bucket, pathes);
		} else {
			pathes = selected.get(bucket);
		}
		if (pathes.contains(imgpath)) {
			pathes.remove(imgpath);
			curCount--;
			bucket.selectCount--;

			tvFinish.update(maxCount, curCount);
			if (curCount == 0) {
				tvPreview.setTextColor(Color.LTGRAY);
			} else {
				tvPreview.setTextColor(Color.DKGRAY);
			}
			return OnPickerListener.STU_UNSELECT;
		} else {
			if (curCount >= maxCount) {
				ToastUtil.showShort("您最多只能选择" + maxCount + "张图片");
				return OnPickerListener.STU_REACH_MAX;
			} else {
				pathes.add(imgpath);
				curCount++;
				bucket.selectCount++;
				tvFinish.update(maxCount, curCount);
				if (curCount == 0) {
					tvPreview.setTextColor(Color.LTGRAY);
				} else {
					tvPreview.setTextColor(Color.DKGRAY);
				}
				return OnPickerListener.STU_SELECT;
			}
		}
	}

	private void showBucket() {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.show(fragBucket);
		ft.hide(fragPicker);
		ft.hide(fragPreview);
		ft.commit();
		this.curFrag = fragBucket;
		fragBucket.getPullProxy().getAdapter().notifyDataSetChanged();
		rlTop.setVisibility(View.VISIBLE);
		tvBack.setVisibility(View.INVISIBLE);
		tvPreview.setVisibility(View.VISIBLE);
	}

	private void showPicker(ImageBucket bucket) {
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.show(fragPicker);
		fragPicker.setData(bucket);
		ft.hide(fragBucket);
		ft.hide(fragPreview);
		ft.commit();
		this.curFrag = fragPicker;
		rlTop.setVisibility(View.VISIBLE);
		tvBack.setVisibility(View.VISIBLE);
		tvPreview.setVisibility(View.VISIBLE);
	}

	private void showPreview(ArrayList<String> paths) {
		fragPreview.setImages(paths);
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.show(fragPreview);
		ft.hide(fragBucket);
		ft.hide(fragPicker);
		ft.commit();
		this.curFrag = fragPreview;
		rlTop.setVisibility(View.GONE);
		tvBack.setVisibility(View.VISIBLE);
		tvPreview.setVisibility(View.INVISIBLE);
	}

	@Override
	protected int titleType() {
		return TitleType.TITLE_NONE;
	}

	@Override
	public void onBackPressed() {
		if (curFrag == fragBucket) {
			super.onBackPressed();
		} else if (curFrag == fragPicker) {
			showBucket();
		} else if (curFrag == fragPreview) {
			previewBack();
		}
	}

	public void previewBack() {
		processPreviewImgs(fragPreview.unSelImgs);
		if (preFrag == fragPicker) {
			showPicker(preBucket);
		} else {
			showBucket();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == tvBack) {
			showBucket();
		} else if (v == tvCancel) {
			finish();
		} else if (v == tvPreview) {
			if (curCount != 0) {
				preFrag = curFrag;
				showPreview(getSelectedPhotoPaths());
			}
		} else if (v == tvFinish) {
			if(cropWidth > 0 && cropHeight > 0){
				if (curCount == 1) {
					ArrayList<String> paths = getSelectedPhotoPaths();
					String localUrl = paths.get(0);
					Uri imageUri = Uri.fromFile(new File(localUrl));
					Uri outUri = Uri.fromFile(new File(outImagePath));
					startActivityForResult(IntentUtil.cropIntent(imageUri, cropWidth,cropHeight,outUri),REQ_CROP);
				}
			}else{
				if (curCount > 0) {
					Intent intent = new Intent();
					if (curFrag == fragPreview) {
						processPreviewImgs(fragPreview.unSelImgs);
                        ArrayList<String> selectedPhotoPaths = getSelectedPhotoPaths();
                        intent.putExtra(RLT_PATHES, selectedPhotoPaths);
					} else {
                        ArrayList<String> selectedPhotoPaths = getSelectedPhotoPaths();
                        intent.putExtra(RLT_PATHES, selectedPhotoPaths);
					}
					setResult(RESULT_OK, intent);
				} else {
					setResult(RESULT_CANCELED, null);
				}
				finish();
			}
		}

	}

	private ArrayList<String> getSelectedPhotoPaths() {
		ArrayList<String> result = null;
		for (ArrayList<String> pathes : selected.values()) {
            if (result == null){
                result = new ArrayList<>();
            }
			if (pathes.size() > 0) {
				result.addAll(pathes);
			}
		}
		return result;
	}

	/**
	 * 处理反选图片后的gallery
	 */
	private void processPreviewImgs(ArrayList<String> unSelImgs) {
		if (unSelImgs != null && unSelImgs.size() > 0) {
			for (String unSelImg : unSelImgs) {
				for (ArrayList<String> paths : selected.values()) {
					if (paths.contains(unSelImg)) {
						ImageBucket key = getKey(unSelImg);
						onImgClicked(key, unSelImg);
						Iterator<ImageItem> iterator = key.imageList.iterator();
						while (iterator.hasNext()) {
							ImageItem next = iterator.next();
							if (StringUtil.isEquals(next.imagePath, unSelImg)) {
								next.isSelected = false;
								break;
							}
						}
					}
				}

			}
		}
	}

	public void refreshTvFinish(int unSelCount) {
		tvFinish.update(maxCount, curCount - unSelCount);
	}

	private ImageBucket getKey(String value) {
		for (ArrayList<String> paths : selected.values()) {
			if (paths.contains(value)) {
				Iterator<ImageBucket> it = selected.keySet().iterator();
				while (it.hasNext()) {
					ImageBucket keyString = it.next();
					if (selected.get(keyString).equals(paths))
						return keyString;
				}
			}
		}
		return null;
	}

	@Override
	public String getPageName() {
		return null;
	}
}
