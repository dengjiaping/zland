/*
 * Copyright (C) 2010 Jason Fry
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

/**
 * @author Jason Fry - jasonfry.co.uk
 * @version 1.0.1
 * 
 */

package com.zhisland.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

public class PopPageControl extends PageControl {

	private static final int DURATION = 200;
	private float scaleToLarge = 1.0f;
	private float scaleToSmall = 1.0f;
	private ScaleAnimation saToLarge = null;
	private ScaleAnimation saToSmall = null;

	public PopPageControl(Context context) {
		super(context);
	}

	public PopPageControl(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void init() {
		super.init();
		scaleToLarge = 2.0f;
		scaleToSmall = 0.6f;

	}

	@Override
	protected void pageChanged(View toActive, View toInActive) {
		if (toInActive != null) {
			toInActive.setBackgroundDrawable(inactiveDrawable);
			TextView tv = (TextView) toInActive;
			tv.setText("");

			saToSmall = new ScaleAnimation(1.0f, scaleToSmall, 1.0f,
					scaleToSmall, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			saToSmall.setFillAfter(true);
			saToSmall.setDuration(DURATION);
			tv.startAnimation(saToSmall);
		}
		if (toActive != null) {
			toActive.setBackgroundDrawable(activeDrawable);
			TextView tv = (TextView) toActive;
			tv.setText("" + (mCurrentPage + 1));
			saToLarge = new ScaleAnimation(1.0f, scaleToLarge, 1.0f,
					scaleToLarge, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			saToLarge.setDuration(DURATION);
			saToLarge.setFillAfter(true);
			tv.startAnimation(saToLarge);
		}
		this.invalidate();
	}

}
