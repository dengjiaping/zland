/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zhisland.lib.bitmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.apache.http.HttpResponse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import com.zhisland.lib.R;
import com.zhisland.lib.async.MyHandler;
import com.zhisland.lib.async.MyHandler.HandlerListener;
import com.zhisland.lib.async.PriorityFutureTask;
import com.zhisland.lib.async.ThreadManager;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.ToastUtil;
import com.zhisland.lib.util.text.ZHLink;

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public abstract class ImageWorker {
	private static final String TAG = "freeimg";
	private static final String RES_PREFIX = "Resource:";
	private static final int FADE_IN_TIME = 200;
	
	public static final int IMG_SIZE_SMALL = 1;
	public static final int IMG_SIZE_MIDDLE = 2;
	public static final int IMG_SIZE_LARGE = 3;
	
    public enum ImgSizeEnum {
    	SMALL, MIDDLE, LARGE;
    }
	
	public ImgSizeEnum imgSizeDefault = ImgSizeEnum.MIDDLE;

	public static int SWITCH = 1;

	protected ImageCache mImageCache;
	// private boolean mFadeInBitmap = true;
	private boolean mExitTasksEarly = false;

	protected Context mContext;

	protected ImageWorker(Context context) {
		mContext = context;
	}

	public void setImageResource(ImageView iv, int resId) {
		Bitmap bm = getBitmapFromRes(resId);
		iv.setImageBitmap(bm);
	}

	/**
	 * Load an image specified by the data parameter into an ImageView (override
	 * {@link ImageWorker#processBitmap(Object)} to define the processing
	 * logic). A memory and disk cache will be used if an {@link ImageCache} has
	 * been set using {@link ImageWorker#setImageCache(ImageCache)}. If the
	 * image is found in the memory cache, it is set immediately, otherwise an
	 * {@link AsyncTask} will be created to asynchronously load the bitmap.
	 * 
	 * @param imageUrl
	 *            The URL of the image to download.
	 * @param imageView
	 *            The ImageView to bind the downloaded image to.
	 */
	public void loadImage(String imageUrl, ImageView imageView) {
		this.loadImage(imageUrl, imageView, R.id.invalidResId, null, true,
				false,imgSizeDefault);
	}

	public void loadImage(String imageUrl, ImageView imageView,
			int placeHolderId) {
		this.loadImage(imageUrl, imageView, placeHolderId, null, true, false,imgSizeDefault);
	}
	
	public void loadImage(String imageUrl, ImageView imageView,ImgSizeEnum imgSize) {
		this.loadImage(imageUrl, imageView, R.id.invalidResId, null, true,
				false,imgSize);
	}

	public void loadImage(String imageUrl, ImageView imageView,
			int placeHolderId,ImgSizeEnum imgSize) {
		this.loadImage(imageUrl, imageView, placeHolderId, null, true, false,imgSize);
	}

	public void loadImage(String imageUrl, ImageView imageView,
			int placeHolderId, boolean fillBackground) {
		this.loadImage(imageUrl, imageView, placeHolderId, null, true,
				fillBackground,imgSizeDefault);
	}

	public void loadImage(String imageUrl, ImageView imageView, boolean fadeIn) {
		this.loadImage(imageUrl, imageView, R.id.invalidResId, null, fadeIn,
				false,imgSizeDefault);
	}

	public void loadImage(String imageUrl, ImageView imageView,
			int placeHolderId, ImageLoadListener listener, boolean fadeIn) {
		this.loadImage(imageUrl, imageView, placeHolderId, listener, fadeIn,
				false,imgSizeDefault);
	}

	public void loadImage(String imageUrl, ImageView imageView,
			Bitmap placeHolder,ImgSizeEnum imgSize) {
		this.loadImage(imageUrl, imageView, placeHolder, null, false, false,imgSize);
	}

	public void loadImage(String imageUrl, ImageView imageView,
			int placeHolderId, ImageLoadListener listener, boolean fadeIn,
			boolean fillBackground,ImgSizeEnum imgSize) {

		Bitmap bm = getBitmapFromRes(placeHolderId);
		loadImage(imageUrl, imageView, bm, listener, fadeIn, fillBackground,imgSize);
	}

	/**
	 * get bitmap from resource.
	 */
	public Bitmap getBitmapFromRes(int placeHolderId) {
		Bitmap bm = null;
		if (placeHolderId != R.id.invalidResId) {
			String resUrl = RES_PREFIX + placeHolderId;
			bm = mImageCache.getBitmapFromMemCache(resUrl);
			if (bm == null) {
				try {
					bm = BitmapFactory.decodeResource(mContext.getResources(),
							placeHolderId);
					mImageCache.addBitmapToCache(resUrl, bm);
				} catch (Exception ex) {

				}
			}
		}
		return bm;
	}

	public void loadImage(String imageUrl, ImageView imageView,
			Bitmap placeHolder, ImageLoadListener listener, boolean fadeIn,
			boolean fillBackground,ImgSizeEnum imgSize) {
		
		imageUrl = fixUrl(imageUrl, imgSize);
		
		if (StringUtil.isNullOrEmpty(imageUrl)) {
			if (fillBackground) {
				imageView.setBackgroundDrawable(new BitmapDrawable(
						ZHApplication.APP_RESOURCE, placeHolder));
			} else {
				imageView.setImageBitmap(placeHolder);
			}
		}

		imageUrl = StringUtil.validUrl(imageUrl);

		Bitmap bitmap = null;

		if (mImageCache != null) {
			bitmap = mImageCache.getBitmapFromMemCache(imageUrl);
		}

		if (bitmap != null) {
			MLog.d(TAG, "cache hit: " + imageView.hashCode());
			// Bitmap found in memory cache
			if (fillBackground) {
				imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
			} else {
				imageView.setImageBitmap(bitmap);
			}
			if (listener != null) {
				listener.onLoadFinished(imageUrl,
						ImageLoadListener.STATUS_SUCCESS);
			}

			return;
		}
		if (!cancelPotentialWork(imageUrl, imageView)) {
			MLog.d(TAG, "has run: " + imageView.hashCode());
			return; // the imageview is deal with the same data
		}

		MLog.d(TAG, "will: " + imageUrl);
		MLog.d(TAG, "will load: " + imageView.hashCode());
		final AsyncBitmap task = new AsyncBitmap(imageUrl, imageView, listener,
				fadeIn, fillBackground);

		final AsyncDrawable asyncDrawable = new AsyncDrawable(
				mContext.getResources(), placeHolder, task);

		if (fillBackground) {
			imageView.setBackgroundDrawable(asyncDrawable);
		} else {
			imageView.setImageDrawable(asyncDrawable);
		}
		ThreadManager.instance().execute(buildFutureTask(task), null);
	}
	
	public String fixUrl(String url, ImgSizeEnum imgSize) {
		if (!StringUtil.isNullOrEmpty(url)) {
			if (ZHLink.isValidUrl(String.valueOf(url))) {
				int lastPointIndex = url.lastIndexOf(".");
				if (lastPointIndex > 1) {
					try {
						String[] strArr1 = url.substring(0, lastPointIndex)
								.split("/");
						String name = strArr1[strArr1.length - 1];
						int lastLineIndex = name.lastIndexOf("_");
						String startStr;
						//目前文件服务器返回的文件名为18位,不是18位的不做处理
						if (lastLineIndex < 0 || name.length() == 18) {
							startStr = name;
						} else {
							startStr = name.substring(0, lastLineIndex);
						}
						if (startStr.length() != 18) {
							return url;
						}
						int sideLength;
						switch (imgSize) {
						case SMALL:
							sideLength = DensityUtil.getWidth() / 4;
							url = url.replace(name, startStr + "_" + sideLength
									+ "x" + sideLength);
							break;
						case MIDDLE:
							sideLength = DensityUtil.getWidth() * 3 / 4;
							url = url.replace(name, startStr + "_" + sideLength
									+ "x" + sideLength);
							break;
						case LARGE:
							sideLength = DensityUtil.getHeight();
							url = url.replace(name, startStr + "_" + sideLength
									+ "x" + sideLength);
							break;
						}
					} catch (Exception e) {
					}
				}
			}
		}
		return url;
	}

	private PriorityFutureTask<Object> buildFutureTask(Runnable task) {
		return new PriorityFutureTask<Object>(task, null, 3);
	}

	public Bitmap getBitmap(String imageUrl) {
		imageUrl = StringUtil.validUrl(imageUrl);
		Bitmap bitmap = null;
		if (mImageCache != null) {
			bitmap = mImageCache.getBitmapFromMemCache(imageUrl);
			if(bitmap == null){
				bitmap = getBitmap(imageUrl, ImgSizeEnum.MIDDLE);
			}
			
			if(bitmap == null){
				bitmap = getBitmap(imageUrl, ImgSizeEnum.LARGE);
			}
			
			if(bitmap == null){
				bitmap = getBitmap(imageUrl, ImgSizeEnum.SMALL);
			}
		}
		return bitmap;
	}
	
	public Bitmap getBitmap(String imageUrl,ImgSizeEnum imgSize) {
		imageUrl = StringUtil.validUrl(imageUrl);
		imageUrl = fixUrl(imageUrl, imgSize);
		Bitmap bitmap = null;
		if (mImageCache != null) {
			bitmap = mImageCache.getBitmapFromMemCache(imageUrl);
		}
		return bitmap;
	}

	/**
	 * Set the {@link ImageCache} object to use with this ImageWorker.
	 * 
	 * @param cacheCallback
	 */
	public void setImageCache(ImageCache cacheCallback) {
		mImageCache = cacheCallback;
	}

	public ImageCache getImageCache() {
		return mImageCache;
	}

	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
	}

	/**
	 * Subclasses should override this to define any processing or work that
	 * must happen to produce the final bitmap. This will be executed in a
	 * background thread and be long running. For example, you could resize a
	 * large bitmap here, or pull down an image from the network.
	 * 
	 * @param data
	 *            The data to identify which image to process, as provided by
	 *            {@link ImageWorker#loadImage(Object, ImageView)}
	 * @return The processed bitmap
	 */
	protected abstract Bitmap processBitmap(Object data);

	protected Bitmap decodeSampledBitmapFromFile(String data) {
		Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(data,
				DensityUtil.getWidth() / 3);
		return bitmap;
	}

	/**
	 * Simple network connection check.
	 * 
	 * @param context
	 */
	protected void checkConnection(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
//			ToastUtil.showShort(context.getResources().getString(R.string.network_error));
			MLog.e(TAG, "checkConnection - no connection found");
		}
	}

	public static void cancelWork(ImageView imageView) {
		final AsyncBitmap bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {
			bitmapWorkerTask.cancel();
			final Object bitmapData = bitmapWorkerTask.fileUrl;
			MLog.d(TAG, "cancelWork - cancelled work for " + bitmapData);
		}
	}

	/**
	 * Returns true if the current work has been canceled or if there was no
	 * work in progress on this image view. Returns false if the work in
	 * progress deals with the same data. The work is not stopped in that case.
	 */
	public static boolean cancelPotentialWork(Object data, ImageView imageView) {
		final AsyncBitmap bitmapWorkerTask = getBitmapWorkerTask(imageView);

		if (bitmapWorkerTask != null) {
			final Object bitmapData = bitmapWorkerTask.fileUrl;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapWorkerTask.cancel(false);
				MLog.d(TAG, "cancelPotentialWork - cancelled work for " + data);
			} else {
				// The same work is already in progress.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active work task (if any) associated with
	 *         this imageView. null if there is no such task.
	 */
	private static AsyncBitmap getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * Called when the processing is complete and the final bitmap should be set
	 * on the ImageView.
	 * 
	 * @param imageView
	 * @param bitmap
	 * @param fadeIn
	 * @param fillBackground
	 */
	private void setImageBitmap(ImageView imageView, Bitmap bitmap,
			boolean fadeIn, boolean fillBackground) {
		if (fadeIn) {
			// Transition drawable with a transparent drwabale and the final
			// bitmap
			final TransitionDrawable td = new TransitionDrawable(
					new Drawable[] {
							new ColorDrawable(android.R.color.transparent),
							new BitmapDrawable(mContext.getResources(), bitmap) });
			if (fillBackground) {
				imageView.setBackgroundDrawable(td);
			} else {
				imageView.setImageDrawable(td);
			}
			td.startTransition(FADE_IN_TIME);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	protected File downloadBitmap(Context context, String urlString) {

		if (ImageWorker.SWITCH < 1) {
			return null;
		}

		final File cacheDir = context.getFilesDir();

		final File cacheFile = new File(DiskLruCache.createFilePath(cacheDir,
				UUID.randomUUID().toString() + urlString));

		MLog.d(TAG, "downloadBitmap - downloading - " + urlString);

		Utils.disableConnectionReuseIfNecessary();
		HttpURLConnection urlConnection = null;

		try {
			String desUrlString = StringUtil.validUrl(urlString);
			final URL url = new URL(desUrlString);
			urlConnection = (HttpURLConnection) url.openConnection();

			MLog.d(TAG, "start write stream to file " + urlString);
			workStreamToFile(urlConnection.getInputStream(), cacheFile);
			MLog.d(TAG, "end write stream to file " + urlString);

			return cacheFile;

		} catch (final IOException e) {
			MLog.e(TAG, "Error in downloadBitmap - " + e);
		} catch (Exception e) {
			MLog.e(TAG, "Error in downloadBitmap - " + e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}

		}

		return null;
	}

	protected void workStreamToFile(InputStream stream, File file)
			throws Exception {
		BufferedOutputStream out = null;
		InputStream in = null;
		try {
			in = new BufferedInputStream(stream, Utils.IO_BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(file),
					Utils.IO_BUFFER_SIZE);

			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
		} catch (Exception e) {
			MLog.e(TAG, "Error in write file - " + e);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
				MLog.e(TAG, "Error in close input stream - " + e);
			}
			try {
				out.close();
			} catch (Exception e) {
				MLog.e(TAG, "Error in close out stream - " + e);

			}
		}
	}

	protected static void recycle(Bitmap bm) {
		if (bm != null && !bm.isRecycled()) {
			bm.recycle();
		}
	}

	public class AsyncBitmap implements Runnable, HandlerListener {

		private static final int SUCCESS_MESSAGE = 0;
		private static final int START_MESSAGE = 1;

		private Handler handler;
		private final long postStartTime;

		public int priority = -1;
		private boolean isCancelled = false;

		private final String fileUrl;
		private final boolean fadeIn;
		private final boolean fillBackground;
		private final WeakReference<ImageView> imageViewReference;
		private final WeakReference<ImageLoadListener> listenerReference;

		public AsyncBitmap(String fileUrl, ImageView imageView,
				ImageLoadListener listener, boolean fadeIn) {
			this(fileUrl, imageView, listener, fadeIn, false);
		}

		public AsyncBitmap(String fileUrl, ImageView imageView,
				ImageLoadListener listener, boolean fadeIn,
				boolean fillBackGround) {

			this.fileUrl = fileUrl;
			imageViewReference = new WeakReference<ImageView>(imageView);
			listenerReference = new WeakReference<ImageLoadListener>(listener);
			this.fadeIn = fadeIn;
			this.fillBackground = fillBackGround;

			this.postStartTime = System.currentTimeMillis();

			if (Looper.myLooper() != null) {
				handler = new MyHandler(this);
			}

			AsyncBitmapManager.addAsyncBitmap(this, imageView.getContext());
		}

		@Override
		public void run() {
			long be = System.currentTimeMillis();
			try {
				Message msgStart = handler.obtainMessage(START_MESSAGE);
				msgStart.sendToTarget();

				Bitmap bm = doInBackGround();
				Message msgPost = handler.obtainMessage(SUCCESS_MESSAGE, bm);
				msgPost.sendToTarget();

				MLog.d(TAG, 3 + "---" + (System.currentTimeMillis() - be)
						+ "  " + (be - postStartTime) + "  " + fileUrl);
			} catch (Exception e) {
				MLog.d(TAG, fileUrl + "   " + e.getMessage());
			}
		}

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS_MESSAGE:
				onPostExecute((Bitmap) msg.obj);
				break;
			case START_MESSAGE:
				onPreExecute();
				break;

			default:
				return false;
			}
			return true;
		}

		protected void onPreExecute() {

		}

		protected Bitmap doInBackGround() {
			final String dataString = String.valueOf(fileUrl);
			Bitmap bitmap = null;
			ImageView attachedImageView = getAttachedImageView();

			// If the image cache is available and this task has not been
			// cancelled by another
			// thread and the ImageView that was originally bound to this task
			// is still bound back
			// to this task and our "exit early" flag is not set then try and
			// fetch the bitmap from
			// the cache
			if (mImageCache != null && attachedImageView != null
					&& !mExitTasksEarly) {
				bitmap = mImageCache.getBitmapFromDiskCache(dataString);
			}

			if (bitmap == null) {
				MLog.d(TAG, "cache not hit");
			}

			// If the bitmap was not found in the cache and this task has not
			// been cancelled by
			// another thread and the ImageView that was originally bound to
			// this task is still
			// bound back to this task and our "exit early" flag is not set,
			// then call the main
			// process method (as implemented by a subclass)
			if (bitmap == null && attachedImageView != null && !mExitTasksEarly) {
				MLog.d(TAG, "start download " + fileUrl);
				boolean isRemotePath = ZHLink.isValidUrl(String
						.valueOf(fileUrl));
				if (!isRemotePath) {
					bitmap = decodeSampledBitmapFromFile(fileUrl);
				} else {
					bitmap = processBitmap(fileUrl);
				}
			}

			if (bitmap == null) {
				MLog.e(TAG, "download failture");
			}

			// If the bitmap was processed and the image cache is available,
			// then add the processed
			// bitmap to the cache for future use. Note we don't check if the
			// task was cancelled
			// here, if it was, and the thread is still running, we may as well
			// add the processed
			// bitmap to our cache as it might be used again in the future
			if (bitmap != null && mImageCache != null) {
				mImageCache.addBitmapToCache(dataString, bitmap);
			}

			return bitmap;
		}

		protected Bitmap onProcessResponse(HttpResponse response) {
			return null;
		}

		protected void onPostExecute(Bitmap bitmap) {
			// if cancel was called on this task or the "exit early" flag is set
			// then we're done
			if (isCancelled || mExitTasksEarly) {
				MLog.d(TAG, "cancel or exist " + fileUrl);
				return;
			}

			final ImageView imageView = getAttachedImageView();
			if (bitmap != null && imageView != null) {
				ImageLoadListener listener = this.getAttachedListener();
				setImageBitmap(imageView, bitmap, this.fadeIn, fillBackground);
				if (listener != null) {
					MLog.d(TAG, "listener is invoked " + fileUrl);
					listener.onLoadFinished(fileUrl,
							ImageLoadListener.STATUS_SUCCESS);
				} else {
					MLog.d(TAG, "listener is null " + fileUrl);
				}
			} else {
				ImageLoadListener listener = this.getAttachedListener();
				if (listener != null) {
					MLog.d(TAG, "listener is invoked with null" + fileUrl);
					listener.onLoadFinished(fileUrl,
							ImageLoadListener.STATUS_FAIL);
				} else {
					MLog.d(TAG, "listener is null and bitmap is null" + fileUrl);
				}
			}
		}

		public void cancel() {
			this.cancel(false);
		}

		public void cancel(boolean existEarly) {
			this.isCancelled = true;
			mExitTasksEarly = existEarly;
		}

		/**
		 * Returns the ImageView associated with this task as long as the
		 * ImageView's task still points to this task as well. Returns null
		 * otherwise.
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			final AsyncBitmap bitmapWorkerTask = getBitmapWorkerTask(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}

		private ImageLoadListener getAttachedListener() {
			final ImageView imageView = imageViewReference.get();
			final ImageLoadListener listener = listenerReference.get();
			final AsyncBitmap bitmapWorkerTask = getBitmapWorkerTask(imageView);

			if (this == bitmapWorkerTask) {
				return listener;
			}

			return null;
		}

	}
}
