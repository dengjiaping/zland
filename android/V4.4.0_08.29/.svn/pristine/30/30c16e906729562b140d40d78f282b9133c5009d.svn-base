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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

import com.zhisland.lib.R;
import com.zhisland.lib.bitmap.ImageWorker.ImgSizeEnum;
import com.zhisland.lib.util.DensityUtil;
import com.zhisland.lib.util.MLog;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images
 * fetched from a URL.
 */
public class ImageCircleFetcher extends ImageWorker {
	private static final String TAG = "ImageFetcher";
	private static final String CIRCLE_RES_PREFIX = "CIRCLERES:";

	/**
	 * Initialize providing a target image width and height for the processing
	 * images.
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
	public ImageCircleFetcher(Context context) {
		super(context);
		imgSizeDefault = ImageWorker.ImgSizeEnum.SMALL;
		init(context);
	}

	private void init(Context context) {
		checkConnection(context);
	}

	@Override
	protected Bitmap processBitmap(Object data) {
		if (data == null) {
			return null;
		}
		MLog.d(TAG, "processBitmap - " + data);

		// Download a bitmap, write it to a file
		final File f = downloadBitmap(mContext, String.valueOf(data));

		if (f != null) {
			// Return a sampled down version
			try {
				Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
				f.delete();
				return bm;
			} catch (final OutOfMemoryError ex) {
				MLog.e(TAG, "Decode Bitmap:" + f.getAbsolutePath(), ex);
			}
		}

		return null;
	}

	@Override
	protected void workStreamToFile(InputStream stream, File file)
			throws Exception {

		super.workStreamToFile(stream, file);
		Bitmap bitmap = ImageResizer.decodeMinSampledBitmapFromFile(
				file.getAbsolutePath(), DensityUtil.dip2px(80));
		// Bitmap bitmap = (Bitmap) BitmapFactory.decodeStream(stream);
		if (bitmap == null) {
			throw new Exception();
		}
		Bitmap roundedBitmap = getCircleBitmap(bitmap);

		recycle(bitmap);

		// Bitmap strokeBitmap = rotateAndFrame(roundedBitmap, 0);
		// recycle(roundedBitmap);

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			roundedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			recycle(roundedBitmap);
		} catch (FileNotFoundException e) {
			MLog.d(TAG, e.getMessage(), e);
		} finally {
			try {
				fos.close();
			} catch (Exception ex) {

			}
		}
	}

	// private static final float PHOTO_BORDER_WIDTH = 1.0f;
	private static final int interval = 0;

	@Override
	public Bitmap getBitmapFromRes(int placeHolderId) {
		Bitmap bm = null;
		if (placeHolderId != R.id.invalidResId) {
			String resUrl = CIRCLE_RES_PREFIX + placeHolderId;
			bm = mImageCache.getBitmapFromMemCache(resUrl);
			if (bm == null) {
				try {
					Bitmap bmRes = BitmapFactory.decodeResource(
							mContext.getResources(), placeHolderId);
					bm = getCircleBitmap(bmRes);
					mImageCache.addBitmapToCache(resUrl, bm);
					bmRes.recycle();
				} catch (Exception ex) {

				}
			}
		}
		return bm;
	}

	public static Bitmap getCircleBitmap(Bitmap bitmap) {

		final int bitmapWidth = bitmap.getWidth();
		final int bitmapHeight = bitmap.getHeight();
		final int bitmapLength = bitmapHeight > bitmapWidth ? bitmapWidth
				: bitmapHeight;
		int borderWidth = 0;// bitmapWidth / 40;
		final int strokedLength = bitmapLength + 2 * borderWidth + 2 * interval;

		Bitmap output = Bitmap.createBitmap(strokedLength, strokedLength,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.WHITE);
		canvas.drawCircle(strokedLength / 2, strokedLength / 2,
				bitmapLength / 2, paint);

		// 按照bitmap宽高中最小的那个值做居中正方形切割图片
		Bitmap tmpBitmap = null;
		if (bitmapWidth > bitmapHeight) {
			tmpBitmap = Bitmap.createBitmap(bitmap,
					(bitmapWidth - bitmapLength) / 2, 0, bitmapLength,
					bitmapLength);
		} else {
			tmpBitmap = Bitmap.createBitmap(bitmap, 0,
					(bitmapHeight - bitmapLength) / 2, bitmapLength,
					bitmapLength);
		}

		BitmapShader shader = new BitmapShader(tmpBitmap,
				Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawCircle(strokedLength / 2, strokedLength / 2,
				(bitmapLength + 2) / 2, paint);

		tmpBitmap.recycle();

		return output;
	}

	protected Bitmap decodeSampledBitmapFromFile(String data){
		Bitmap result = null;
		Bitmap bitmap = ImageResizer.decodeSampledBitmapFromFile(data,
				DensityUtil.getWidth() / 3);
		if (bitmap != null) {
			result = getCircleBitmap(bitmap);
			bitmap.recycle();
		}
		return result;
	}
}
