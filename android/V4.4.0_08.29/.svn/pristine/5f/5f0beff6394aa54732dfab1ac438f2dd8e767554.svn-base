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

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.support.v4.util.LruCache;

import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
/**
 * This class holds our bitmap caches (memory and disk).
 */
public class ImageCache {

	private static final String TAG = "ImageCache";

	// Default memory cache size
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB

	// Default disk cache size
	private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 32; // 64MB

	// Compression settings when writing images to disk cache
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.PNG;
	private static final int DEFAULT_COMPRESS_QUALITY = 70;

	// Constants to easily toggle various caches
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

	private static final Object lockObj = new Object();
	private static ImageCache instance = null;

	private static final Object lockHeaderObj = new Object();
	private static ImageCache header_instance = null;

	public static ImageCache getInstance() {
		if (instance == null) {
			synchronized (lockObj) {
				if (instance == null) {
					ImageCacheParams cacheParams = new ImageCacheParams(
							"remote");
					cacheParams.diskCacheSize = 32 * 1024 * 1024;
					cacheParams.memCacheSize = 1024 * 1024 * Utils
							.getMemoryClass(ZHApplication.APP_CONTEXT) / 8;
					ImageCache tmp = new ImageCache(ZHApplication.APP_CONTEXT,
							cacheParams);
					instance = tmp;
				}
			}
		}
		return instance;
	}

	public static ImageCache getHeaderCacheInstance() {
		if (header_instance == null) {
			synchronized (lockHeaderObj) {
				if (header_instance == null) {
					ImageCacheParams cacheParams = new ImageCacheParams(
							"headers");
					cacheParams.diskCacheSize = 10 * 1024 * 1024;
					cacheParams.memCacheSize = (int) (Runtime.getRuntime()
							.maxMemory() / 8);
					ImageCache tmp = new ImageCache(ZHApplication.APP_CONTEXT,
							cacheParams);
					header_instance = tmp;
				}
			}
		}
		return header_instance;
	}

	private DiskLruCache mDiskCache;
	private LruCache<String, Bitmap> mMemoryCache;

	/**
	 * Creating a new ImageCache object using the specified parameters.
	 * 
	 * @param context
	 *            The context to use
	 * @param cacheParams
	 *            The cache parameters to use to initialize the cache
	 */
	private ImageCache(Context context, ImageCacheParams cacheParams) {
		init(context, cacheParams);
	}

	/**
	 * Creating a new ImageCache object using the default parameters.
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            A unique name that will be appended to the cache directory
	 */
	private ImageCache(Context context, String uniqueName) {
		init(context, new ImageCacheParams(uniqueName));
	}

	/**
	 * Initialize the cache, providing all parameters.
	 * 
	 * @param context
	 *            The context to use
	 * @param cacheParams
	 *            The cache parameters to initialize the cache
	 */
	private void init(Context context, ImageCacheParams cacheParams) {
		final File diskCacheDir = DiskLruCache.getDiskCacheDir(context,
				cacheParams.uniqueName);

		// Set up disk cache
		if (cacheParams.diskCacheEnabled) {
			mDiskCache = DiskLruCache.openCache(context, diskCacheDir,
					cacheParams.diskCacheSize);
			if (mDiskCache != null) {
				mDiskCache.setCompressParams(cacheParams.compressFormat,
						cacheParams.compressQuality);
				if (cacheParams.clearDiskCacheOnStart) {
					mDiskCache.asyncClearCache();
				}
			}
		}

		// Set up memory cache
		if (cacheParams.memoryCacheEnabled) {
			mMemoryCache = new LruCache<String, Bitmap>(
					cacheParams.memCacheSize) {

				/**
				 * Measure item size in bytes rather than units which is more
				 * practical for a bitmap cache
				 */
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					int size = Utils.getBitmapSize(bitmap);
					return size;
				}
			};
		}
	}

	public void addBitmapToCache(String data, Bitmap bitmap) {
		if (data == null || bitmap == null) {
			return;
		}

		// Add to memory cache
		if (mMemoryCache != null && mMemoryCache.get(data) == null) {
			mMemoryCache.put(data, bitmap);
		}

		// Add to disk cache
		if (mDiskCache != null && !mDiskCache.containsKey(data)) {
			mDiskCache.put(data, bitmap);
		}
		MLog.d(TAG, "add bitmap, size:" + mMemoryCache.evictionCount()
				+ " bytes" + mMemoryCache.size());
	}

	/**
	 * Get from memory cache.
	 * 
	 * @param data
	 *            Unique identifier for which item to get
	 * @return The bitmap if found in cache, null otherwise
	 */
	public Bitmap getBitmapFromMemCache(String data) {
		if (StringUtil.isNullOrEmpty(data)) {
			return null;
		}
		if (mMemoryCache != null) {
			final Bitmap memBitmap = mMemoryCache.get(data);

			if (memBitmap != null) {
				MLog.d(TAG, "Memory cache hit:" + data + ", memory cache bytes"
						+ mMemoryCache.size());
				return memBitmap;
			}
		}
		return null;
	}

	public boolean containBitmap(String data) {

		data = StringUtil.validUrl(data);

		if (StringUtil.isNullOrEmpty(data)) {
			return false;
		}

		if (mMemoryCache != null) {
			final Bitmap memBitmap = mMemoryCache.get(data);

			if (memBitmap != null) {
				MLog.d(TAG, "Memory cache  contain:" + data
						+ ", memory cache bytes" + mMemoryCache.size());
				return true;
			}
		}
		return false;
	}

	/**
	 * Get from disk cache.
	 * 
	 * @param data
	 *            Unique identifier for which item to get
	 * @return The bitmap if found in cache, null otherwise
	 */
	public Bitmap getBitmapFromDiskCache(String data) {
		data = StringUtil.validUrl(data);
		if (mDiskCache != null) {
			return mDiskCache.get(data);
		}
		return null;
	}

	/**
	 * get file path from disk cache, if not exist, return null
	 * 
	 * @param data
	 * @return
	 */
	public String getFileFromDiskCache(String data) {
		data = StringUtil.validUrl(data);
		if (mDiskCache != null) {
			return mDiskCache.getPath(data);
		}
		return null;
	}

	public void clearCaches() {
		if (mDiskCache != null) {
			mDiskCache.clearCache();
		}
		mMemoryCache.evictAll();
	}

	public void invalidCache(String data) {
		data = StringUtil.validUrl(data);

		if (StringUtil.isNullOrEmpty(data)) {
			return;
		}

		if (mMemoryCache != null) {
			mMemoryCache.remove(data);
		}

		if (mDiskCache != null) {
			mDiskCache.remove(data);

		}
	}

	/**
	 * A holder class that contains cache parameters.
	 */
	public static class ImageCacheParams {
		public String uniqueName;
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
		public int compressQuality = DEFAULT_COMPRESS_QUALITY;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;

		public ImageCacheParams(String uniqueName) {
			this.uniqueName = uniqueName;
		}
	}
	
	public long getCachSize(){
		if (mDiskCache != null) {
			return mDiskCache.getCachSize();
		}
		return 0;
	}
}
