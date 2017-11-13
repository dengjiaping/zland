package com.zhisland.lib.image;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhisland.lib.R;
import com.zhisland.lib.bitmap.ImageLoadListener;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.component.frag.FragBase;
import com.zhisland.lib.view.ImageViewEx;

/**
 * @See MultiImgPickerActivity 图片选择预览功能
 */
public class FragImgPreview extends FragBase {

	public static final String IMAGES = "freeimages";
	public static final String TO_INDEX = "to_index";
	public static final String MAX_INDEX = "max_index";
	public static final String CUR_INDEX = "cur_index";
	public static final String PLACE_HOLDER = "place_holder";

	public static final String STYLE_KEY = "browse_style";
	public static final String IS_FROM_PROIFLE_EDITOR = "IS_FROM_PROIFLE_EDITOR";

	public static final int BUTTON_SAVE = 100;
	public static final int BUTTON_DELETE = 101;
	public static final int POST_IMAGE_DELETE = 1009;

	public static int screenWidth;
	public static int screenHeight;
	private NewsGallery gallery;
	private ImageView ivImgCheck;
	private GalleryAdapter adapter;
	private boolean isFullScreen = false;
	private RelativeLayout navigation;

	private TextView title;
	private TextView back;

	private int curIndex = 0;
	private int maxIndex = 0;
	private int curPostion;

	/**
	 * 反选的图片
	 */
	public ArrayList<String> unSelImgs;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = (View) inflater
				.inflate(R.layout.frag_img_preview, null);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		initView(rootView);

		return rootView;
	}

	public void setImages(ArrayList<String> images) {
		if (unSelImgs == null) {
			unSelImgs = new ArrayList<String>();
		} else {
			unSelImgs.clear();
		}
		this.curPostion = 0;
		curIndex = 0;
		maxIndex = images.size();
		adapter = new GalleryAdapter(getActivity(), images);

		gallery.setAdapter(adapter);
	}

	private void initView(View rootView) {
		gallery = (NewsGallery) rootView.findViewById(R.id.gallery);

		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框

		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				setTitleText(position);
				FragImgPreview.this.curPostion = position;
				String path = (String) gallery.getItemAtPosition(position);
				if (unSelImgs.contains(path)) {
					ivImgCheck.setBackgroundResource(R.drawable.chb_unselect);
				} else {
					ivImgCheck.setBackgroundResource(R.drawable.chb_select);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		ivImgCheck = (ImageView) rootView.findViewById(R.id.ivImgCheck);
		ivImgCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = (String) gallery
						.getItemAtPosition(FragImgPreview.this.curPostion);
				if (unSelImgs.contains(path)) {
					ivImgCheck.setBackgroundResource(R.drawable.chb_select);
					unSelImgs.remove(path);
				} else {
					ivImgCheck.setBackgroundResource(R.drawable.chb_unselect);
					unSelImgs.add(path);
				}
				((MultiImgPickerActivity) getActivity())
						.refreshTvFinish(unSelImgs.size());
			}
		});

		navigation = (RelativeLayout) rootView.findViewById(R.id.navigation);

		setFullScreen(isFullScreen);

		title = (TextView) rootView.findViewById(R.id.titledes);
		back = (TextView) rootView.findViewById(R.id.gallery_back);
		back.setTextColor(Color.LTGRAY);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MultiImgPickerActivity) getActivity()).previewBack();
			}
		});
		setTitleStyle();
		setTitleText(curIndex);
		screenWidth = getActivity().getWindow().getWindowManager()
				.getDefaultDisplay().getWidth();
		screenHeight = getActivity().getWindow().getWindowManager()
				.getDefaultDisplay().getHeight();

	}

	private void setTitleStyle() {
		title.setTextColor(this.getResources().getColor(R.color.white));
		back.setTextColor(this.getResources().getColor(R.color.white));
	}

	public void setTitleText(int cur) {
		if (cur >= 0 && maxIndex > 0 && cur < maxIndex) {
			title.setText(cur + 1 + " / " + maxIndex);
			curIndex = cur;
		}
	}

	private void setFullScreen(boolean isFull) {
		isFullScreen = isFull;
		if (isFull) {
			navigation.setVisibility(View.INVISIBLE);
		} else {

			navigation.setVisibility(View.VISIBLE);
		}
	}

	private class GalleryAdapter extends BaseAdapter {

		private final List<String> paths;
		protected LayoutInflater inflater = null;

		ArrayList<View> mScrapHeap = new ArrayList<View>();

		public GalleryAdapter(Context context, List<String> galleryList) {

			this.paths = galleryList;

			this.inflater = (LayoutInflater) ZHApplication.APP_CONTEXT
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			if (paths != null) {
				return paths.size();
			}
			return 0;
		}

		@Override
		public String getItem(int position) {
			if (paths != null) {
				return paths.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private View getNoUsedView() {
			if (mScrapHeap.size() < 4) {
				return null;
			}
			for (int i = 0; i < mScrapHeap.size(); i++) {
				if (mScrapHeap.get(i).getParent() == null) {
					return mScrapHeap.get(i);
				}
			}
			return null;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			String image = this.getItem(position);

			convertView = getNoUsedView();
			final Holder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.picnews_item, null);
				holder = new Holder();
				holder.simage = (ImageViewEx) convertView
						.findViewById(R.id.gallerysimage);
				holder.image = (ImageViewEx) convertView
						.findViewById(R.id.galleryimage);
				holder.pro = (ProgressBar) convertView
						.findViewById(R.id.galleryprogress);

				convertView.setTag(holder);
				mScrapHeap.add(convertView);
			} else {
				holder = (Holder) convertView.getTag();

			}
			holder.image.setImageBitmap(null);
			holder.simage.setImageBitmap(null);
			holder.pro.setVisibility(View.VISIBLE);

			if (image != null) {
				ImageWorkFactory.getFetcher().loadImage(image, holder.image,
						R.drawable.loading, holder, false);
			} else {
				holder.onLoadFinished(image, ImageLoadListener.STATUS_FAIL);
			}

			return convertView;
		}

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

	@Override
	protected String getPageName() {
		return "frag_img_preview";
	}

}
