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

import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images
 * fetched from a URL.
 */
public class ImageLocalFetcher extends ImageWorker {
	private static final String TAG = "ImageFetcher";

	// 64MB

	/**
	 * Initialize providing a target image width and height for the processing
	 * images.
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public ImageLocalFetcher(Context context) {
		super(context);
	}

	@Override
	protected Bitmap processBitmap(Object data) {

		if (data == null)
			return null;

		MLog.d(TAG, "processBitmap - " + data);
		File file = new File(data.toString());
		if (!file.exists())
			return null;

		// Return a sampled down version
		try {
			int screenHeight = DensityUtil.getHeight();
			Bitmap bm = ImageResizer.decodeSampledBitmapFromFile(
					file.getAbsolutePath(), screenHeight);
			// BitmapFactory.decodeFile(file.getAbsolutePath());
			return bm;
		} catch (final OutOfMemoryError ex) {
		}

		return null;
	}

}
