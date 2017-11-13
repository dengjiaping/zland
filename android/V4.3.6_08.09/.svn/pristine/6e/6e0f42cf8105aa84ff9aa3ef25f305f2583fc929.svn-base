package com.zhisland.lib.image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Stack;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Environment;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;

import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.image.ImgPicData.ImageBucket;
import com.zhisland.lib.image.ImgPicData.ImageItem;
import com.zhisland.lib.util.MLog;

/**
 * 专辑帮助类
 * 
 * @author Administrator
 * 
 */
public class AlbumHelper {
	private final String TAG = getClass().getSimpleName();
	private static AlbumHelper instance;

	private Context context;
	private ContentResolver cr;

	// 缩略图列表
	private HashMap<String, String> thumbnailList = new HashMap<String, String>();
	private HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();
	private Stack<WeakReference<ImageItem>> cachedItems = new Stack<WeakReference<ImageItem>>();

	private boolean hasChanged = false;

	private AlbumHelper() {
		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		FileObserver fileObserver = new FileObserver(path) {

			@Override
			public synchronized void onEvent(int event, String path) {
				hasChanged = true;
			}
		};
		fileObserver.startWatching();

		MediaReceiver receiver = new MediaReceiver();
		IntentFilter filter = new IntentFilter(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		ZHApplication.APP_CONTEXT.registerReceiver(receiver, filter);
	}

	class MediaReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			hasChanged = true;
		}

	}

	public static AlbumHelper instance() {
		if (instance == null) {
			instance = new AlbumHelper();
			instance.init(ZHApplication.APP_CONTEXT);
		}
		return instance;
	}

	/**
	 * 初始化
	 */
	public void init(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
		}
	}

	/**
	 * 得到缩略图
	 */
	private void getThumbnail() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
				Thumbnails.DATA };
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
				null, null, null);
		getThumbnailColumnData(cursor);
	}

	/**
	 * 从数据库中得到缩略图
	 */
	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int image_id;
			String image_path;
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				thumbnailList.put("" + image_id, image_path);
			} while (cur.moveToNext());
		}
	}

	/**
	 * 得到图片集
	 */
	private synchronized void buildImagesBucketList() {

		for (Entry<String, ImageBucket> entry : bucketList.entrySet()) {
			ImageBucket bucket = entry.getValue();
			bucket.selectCount = 0;
			bucket.count = 0;
			recycle(bucket.imageList);
			bucket.imageList.clear();
		}

		long startTime = System.currentTimeMillis();

		// 构造缩略图索引
		getThumbnail();

		// 构造相册索引
		String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
				Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
				Media.SIZE, Media.BUCKET_DISPLAY_NAME };
		// 得到一个游标
		Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				columns, null, null, null);
		if (cur != null && cur.moveToFirst()) {
			// 获取指定列的索引
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
			int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
			int bucketDisplayNameIndex = cur
					.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);

			do {
				String _id = cur.getString(photoIDIndex);
				String name = cur.getString(photoNameIndex);
				String path = cur.getString(photoPathIndex);
				String title = cur.getString(photoTitleIndex);
				String size = cur.getString(photoSizeIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
				String picasaId = cur.getString(picasaIdIndex);

				MLog.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: "
						+ picasaId + " name:" + name + " path:" + path
						+ " title: " + title + " size: " + size + " bucket: "
						+ bucketName + "---");

				ImageBucket bucket = bucketList.get(bucketId);
				if (bucket == null) {
					bucket = new ImageBucket();
					bucketList.put(bucketId, bucket);
					bucket.imageList = new ArrayList<ImageItem>();
					bucket.bucketName = bucketName;
				}
				bucket.count++;
				ImageItem imageItem = getImageItem();
				imageItem.imageId = _id;
				imageItem.imagePath = path;
				imageItem.thumbnailPath = thumbnailList.get(_id);
				bucket.imageList.add(0, imageItem);

			} while (cur.moveToNext());
		}
		if (cur != null) {
			cur.close();
		}
		hasChanged = false;
		long lastBuild = System.currentTimeMillis();
		MLog.d(TAG, "use time: " + (lastBuild - startTime) + " ms");
	}

	/**
	 * 得到图片集
	 */
	public List<ImageBucket> getImagesBucketList(boolean refresh) {
		if (refresh || hasChanged) {
			buildImagesBucketList();
		}
		List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
		for (Entry<String, ImageBucket> entry : bucketList.entrySet()) {
			tmpList.add(entry.getValue());
		}
		return tmpList;
	}

	private void recycle(List<ImageItem> items) {
		if (items == null || items.size() < 0)
			return;

		for (ImageItem item : items) {
			item.imageId = null;
			item.imagePath = null;
			item.isSelected = false;
			item.thumbnailPath = null;
			cachedItems.push(new WeakReference<ImgPicData.ImageItem>(item));
		}
	}

	private ImageItem getImageItem() {
		ImageItem item = null;
		while (!cachedItems.empty()) {
			item = cachedItems.pop().get();
			if (item != null) {

				break;
			}
		}
		if (item == null) {
			item = new ImageItem();
		}
		return item;
	}

}
