package com.zhisland.lib.image.viewer;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.zhisland.lib.R;
import com.zhisland.lib.bitmap.ImageLoadListener;
import com.zhisland.lib.bitmap.ImageWorkFactory;
import com.zhisland.lib.bitmap.ImageWorker;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.image.viewer.FreeImageViewer.Holder;
import com.zhisland.lib.view.ImageViewEx;

/**
 * 图片浏览的ganllery adapter
 * 
 * @author 向飞
 *
 */
class GalleryAdapter extends BaseAdapter {

	// private final List<ImgBrowsable> gallery;
	protected LayoutInflater inflater = null;
	private final ImageDataAdapter dataAdapter;

	ArrayList<View> mScrapHeap = new ArrayList<View>();

	public GalleryAdapter(Context context, ImageDataAdapter dataAdapter) {

		this.dataAdapter = dataAdapter;

		this.inflater = (LayoutInflater) ZHApplication.APP_CONTEXT
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (dataAdapter != null) {
			return dataAdapter.count();
		}
		return 0;
	}

	@Override
	public String getItem(int position) {
		if (dataAdapter != null) {
			return dataAdapter.getItem(position);
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

		String url = this.getItem(position);

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

		if (url != null) {
			ImageWorkFactory.getFetcher().loadImage(url, holder.image,
					R.id.invalidResId, holder, false, false,ImageWorker.ImgSizeEnum.LARGE);
		} else {
			holder.onLoadFinished(url, ImageLoadListener.STATUS_FAIL);
		}

		return convertView;
	}

}