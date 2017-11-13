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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;

/**
 * A simple disk LRU bitmap cache to illustrate how a disk cache would be used
 * for bitmap caching. A much more robust and efficient disk LRU cache solution
 * can be found in the ICS source code
 * (libcore/luni/src/main/java/libcore/io/DiskLruCache.java) and is preferable
 * to this simple implementation.
 */
public class DiskLruCache {

	private static final String TAG = "DiskLruCache";
	private static final String CACHE_FILENAME_PREFIX = "cache_";
	private static final int MAX_REMOVALS = 4;
	private static final int INITIAL_CAPACITY = 32;
	private static final float LOAD_FACTOR = 0.75f;

	private final File mCacheDir;
	private int cacheSize = 0;
	private int cacheByteSize = 0;
	private final int maxCacheItemSize = 512; // 512 item default
	private long maxCacheByteSize = 1024 * 1024 * 50; // 50MB default
	private CompressFormat mCompressFormat = CompressFormat.JPEG;
	private int mCompressQuality = 70;

	private final Map<String, String> mLinkedHashMap = Collections
			.synchronizedMap(new LinkedHashMap<String, String>(
					INITIAL_CAPACITY, LOAD_FACTOR, true));

	/**
	 * A filename filter to use to identify the cache filenames which have
	 * CACHE_FILENAME_PREFIX prepended.
	 */
	private static final FilenameFilter cacheFileFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String filename) {
			return filename.startsWith(CACHE_FILENAME_PREFIX);
		}
	};

	/**
	 * Used to fetch an instance of DiskLruCache.
	 * 
	 * @param context
	 * @param cacheDir
	 * @param maxByteSize
	 * @return
	 */
	public static DiskLruCache openCache(Context context, File cacheDir,
			long maxByteSize) {
		if (!cacheDir.exists()) {
			boolean result = cacheDir.mkdirs();
			MLog.d(TAG, "create cache directory:" + result);
		}

		if (cacheDir.isDirectory() && cacheDir.canWrite()
				&& Utils.getUsableSpace(cacheDir) > maxByteSize) {
			return new DiskLruCache(cacheDir, maxByteSize);
		}

		return null;
	}

	/**
	 * Constructor that should not be called directly, instead use
	 * {@link DiskLruCache#openCache(Context, File, long)} which runs some extra
	 * checks before creating a DiskLruCache instance.
	 * 
	 * @param cacheDir
	 * @param maxByteSize
	 */
	private DiskLruCache(File cacheDir, long maxByteSize) {
		mCacheDir = cacheDir;
		maxCacheByteSize = maxByteSize;
		final File[] files = mCacheDir.listFiles(cacheFileFilter);
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i] != null) {
					mLinkedHashMap.put(files[i].getName(),
							files[i].getAbsolutePath());
					cacheSize = mLinkedHashMap.size();
					cacheByteSize += files[i].length();
				}
			}
		}
	}

	/**
	 * Add a bitmap to the disk cache.
	 * 
	 * @param key
	 *            A unique identifier for the bitmap.
	 * @param data
	 *            The bitmap to store.
	 */
	public void put(String key, Bitmap data) {

		String mappedKey = mappingKey(key);
		synchronized (mLinkedHashMap) {
			if (mLinkedHashMap.get(mappedKey) == null) {
				try {
					final String file = createMappedFilePath(mCacheDir,
							mappedKey);
					if (writeBitmapToFile(data, file)) {
						put(mappedKey, file);
						flushCache();
					}
				} catch (final FileNotFoundException e) {
					MLog.e(TAG, "Error in put: " + e.getMessage());
				} catch (final IOException e) {
					MLog.e(TAG, "Error in put: " + e.getMessage());
				}
			}
		}
	}

	private void put(String key, String file) {
		mLinkedHashMap.put(key, file);
		cacheSize = mLinkedHashMap.size();
		cacheByteSize += new File(file).length();
	}

	public void remove(String key) {
		String path = getPath(key);
		if (StringUtil.isNullOrEmpty(key)) {
			return;
		}
		try {
			File file = new File(path);
			if (file.exists()) {
				cacheByteSize -= file.length();
				file.delete();
			}
		} catch (Exception ex) {
		} finally {
		}

	}

	/**
	 * Flush the cache, removing oldest entries if the total size is over the
	 * specified cache size. Note that this isn't keeping track of stale files
	 * in the cache directory that aren't in the HashMap. If the images and keys
	 * in the disk cache change often then they probably won't ever be removed.
	 */
	private void flushCache() {
		Entry<String, String> eldestEntry;
		File eldestFile;
		long eldestFileSize;
		int count = 0;

		while (count < MAX_REMOVALS
				&& (cacheSize > maxCacheItemSize || cacheByteSize > maxCacheByteSize)) {
			eldestEntry = mLinkedHashMap.entrySet().iterator().next();
			eldestFile = new File(eldestEntry.getValue());
			eldestFileSize = eldestFile.length();
			mLinkedHashMap.remove(eldestEntry.getKey());
			eldestFile.delete();
			cacheSize = mLinkedHashMap.size();
			cacheByteSize -= eldestFileSize;
			count++;
			MLog.d(TAG, "flushCache - Removed cache file, " + eldestFile + ", "
					+ eldestFileSize);
		}
	}

	/**
	 * Get an image from the disk cache.
	 * 
	 * @param key
	 *            The unique identifier for the bitmap
	 * @return The bitmap or null if not found
	 */
	public Bitmap get(String key) {

		String mappedKey = mappingKey(key);

		synchronized (mLinkedHashMap) {
			final String file = mLinkedHashMap.get(mappedKey);
			if (file != null) {
				MLog.d(TAG, "Disk cache hit");
				return BitmapFactory.decodeFile(file);
			} else {
				final String existingFile = createMappedFilePath(mCacheDir,
						mappedKey);
				if (new File(existingFile).exists()) {
					put(mappedKey, existingFile);
					MLog.d(TAG, "Disk cache hit (existing file)");
					return BitmapFactory.decodeFile(existingFile);
				}
			}
			return null;
		}
	}

	/**
	 * get file path
	 * 
	 * @param key
	 * @return
	 */
	public String getPath(String key) {

		String mappedKey = mappingKey(key);

		synchronized (mLinkedHashMap) {
			final String file = mLinkedHashMap.get(mappedKey);
			if (file != null) {
				MLog.d(TAG, "Disk cache hit");
				return file;
			} else {
				final String existingFile = createMappedFilePath(mCacheDir,
						mappedKey);
				if (new File(existingFile).exists()) {
					put(mappedKey, existingFile);
					MLog.d(TAG, "Disk cache hit (existing file)");
					return existingFile;
				}
			}
			return null;
		}
	}

	/**
	 * Checks if a specific key exist in the cache.
	 * 
	 * @param key
	 *            The unique identifier for the bitmap
	 * @return true if found, false otherwise
	 */
	public boolean containsKey(String key) {

		String mappedKey = mappingKey(key);
		// See if the key is in our HashMap
		if (mLinkedHashMap.containsKey(mappedKey)) {
			return true;
		}

		// Now check if there's an actual file that exists based on the key
		final String existingFile = createMappedFilePath(mCacheDir, mappedKey);
		if (new File(existingFile).exists()) {
			// File found, add it to the HashMap for future use
			put(mappedKey, existingFile);
			return true;
		}
		return false;
	}

	/**
	 * Removes all disk cache entries from this instance cache dir
	 */
	public void clearCache() {
		DiskLruCache.clearCache(mCacheDir);
	}

	public void asyncClearCache() {
		ClearTask task = new ClearTask(mCacheDir);
		task.execute();
	}

	public static void asyncClearCache(File cacheDir) {
		ClearTask task = new ClearTask(cacheDir);
		task.execute();
	}

	public static void asyncClearCache(Context context, String uniqueName) {
		File cacheDir = getDiskCacheDir(context, uniqueName);
		ClearTask task = new ClearTask(cacheDir);
		task.execute();
	}

	/**
	 * Removes all disk cache entries from the application cache directory in
	 * the uniqueName sub-directory.
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            A unique cache directory name to append to the app cache
	 *            directory
	 */
	public static void clearCache(Context context, String uniqueName) {
		File cacheDir = getDiskCacheDir(context, uniqueName);
		clearCache(cacheDir);
	}

	/**
	 * Removes all disk cache entries from the given directory. This should not
	 * be called directly, call {@link DiskLruCache#clearCache(Context, String)}
	 * or {@link DiskLruCache#clearCache()} instead.
	 * 
	 * @param cacheDir
	 *            The directory to remove the cache files from
	 */
	private static void clearCache(File cacheDir) {
		final File[] files = cacheDir.listFiles(cacheFileFilter);
		if(files == null){
			return;
		}
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/**
	 * Get a usable cache directory (external if available, internal otherwise).
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            A unique directory name to append to the cache dir
	 * @return The cache dir
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {

		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir
		final String cachePath;
		if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
				|| !Utils.isExternalStorageRemovable()) {
			File f = Utils.getExternalCacheDir(context);
			cachePath = f.getPath();
		} else {
			File f = context.getFilesDir();
			cachePath = f.getPath();
		}

		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * Creates a constant cache file path given a target cache directory and an
	 * image key.
	 * 
	 * @param cacheDir
	 * @param key
	 * @return
	 */
	public static String createFilePath(File cacheDir, String key) {

		String mappedKey = mappingKey(key);
		if (mappedKey != null) {
			return cacheDir.getAbsolutePath() + File.separator + mappedKey;
		}

		return null;
	}

	public static String createMappedFilePath(File cacheDir, String key) {

		return cacheDir.getAbsolutePath() + File.separator + key;
	}

	public static String mappingKey(String key) {
		try {
			return CACHE_FILENAME_PREFIX
					+ URLEncoder.encode(key.replace("*", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create a constant cache file path using the current cache directory and
	 * an image key.
	 * 
	 * @param key
	 * @return
	 */
	public String createFilePath(String key) {
		return createFilePath(mCacheDir, key);
	}

	/**
	 * Sets the target compression format and quality for images written to the
	 * disk cache.
	 * 
	 * @param compressFormat
	 * @param quality
	 */
	public void setCompressParams(CompressFormat compressFormat, int quality) {
		mCompressFormat = compressFormat;
		mCompressQuality = quality;
	}

	/**
	 * Writes a bitmap to a file. Call
	 * {@link DiskLruCache#setCompressParams(CompressFormat, int)} first to set
	 * the target bitmap compression and format.
	 * 
	 * @param bitmap
	 * @param file
	 * @return
	 */
	private boolean writeBitmapToFile(Bitmap bitmap, String file)
			throws IOException, FileNotFoundException {

		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(file),
					Utils.IO_BUFFER_SIZE);
			return bitmap.compress(mCompressFormat, mCompressQuality, out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	private static final class ClearTask extends AsyncTask<Void, Void, Void> {

		private final File cacheDir;

		public ClearTask(File mCacheDir) {
			this.cacheDir = mCacheDir;
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (this.cacheDir != null && cacheDir.isDirectory()) {
				DiskLruCache.clearCache(cacheDir);
			}
			return null;
		}

	}

	public long getCachSize() {
		return DiskLruCache.getCacheSizes(mCacheDir);
	}

	private static long getCacheSizes(File cacheDir) {
		final File[] files = cacheDir.listFiles(cacheFileFilter);
		if (files == null) {
			return 0;
		}
		long size = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				try {
					size = size + getFileSize(files[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				size = size + files[i].length();
			}
		}
		return size;
	}

	private static long getFileSize(File f) throws Exception {
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
		}
		return s;
	}
}
