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

import com.zhisland.lib.util.MLog;
import com.zhisland.lib.util.StringUtil;
import com.zhisland.lib.util.text.ZHLink;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images
 * fetched from a URL.
 */
public class ImageFetcher extends ImageWorker {
	private static final String TAG = "freeimg";

	// 64MB

	/**
	 * Initialize providing a target image width and height for the processing
	 * images.
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public ImageFetcher(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		checkConnection(context);
	}

	@Override
	protected Bitmap processBitmap(Object data) {

		MLog.d(TAG, "processBitmap - " + data);

		if (data == null)
			return null;

		final File f = downloadBitmap(mContext, String.valueOf(data));

		if (f != null) {
			// Return a sampled down version
			try {
				Bitmap bm = ImageResizer.decodeMinSampledBitmapFromFile(
						f.getAbsolutePath(), 800);
				f.delete();
				MLog.d(TAG,
						(bm != null) + " Decode Bitmap:" + f.getAbsolutePath());
				return bm;
			} catch (final OutOfMemoryError ex) {
				MLog.e(TAG, "Decode Bitmap:" + f.getAbsolutePath(), ex);
			}
		}

		return null;
	}

	public static boolean isRemotePath(String path) {
		if (StringUtil.isNullOrEmpty(path))
			return false;

		return ZHLink.isValidUrl(path);
	}

}
