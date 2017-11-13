package com.zhisland.android.blog.common.view.express;

import com.zhisland.lib.util.text.LinkSpanCreator;
import com.zhisland.lib.util.text.SpanCreator;

public class CreatorFactory {

	private static LinkSpanCreator linkCreator = null;
	private static ImageSpanCreator imageCreator = null;

	public static SpanCreator getLinkSpanCreator() {
		if (linkCreator == null) {
			linkCreator = new LinkSpanCreator();
		}
		return linkCreator;
	}

	public static SpanCreator getImageSpanCreator() {
		if (imageCreator == null) {
			imageCreator = new ImageSpanCreator();
		}
		return imageCreator;
	}
}
