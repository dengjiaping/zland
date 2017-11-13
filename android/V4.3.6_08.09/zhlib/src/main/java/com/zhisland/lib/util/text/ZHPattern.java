package com.zhisland.lib.util.text;

import java.util.regex.Pattern;

public class ZHPattern {

	public ZHPattern(Pattern pat, int flag, SpanCreator creator) {
		this.realPat = pat;
		this.flag = flag;
		this.creator = creator;
	}

	public Pattern realPat;
	public int flag;
	public SpanCreator creator;
}
