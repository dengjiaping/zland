package com.zhisland.lib.util.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.zhisland.lib.component.application.ZHApplication;

public class ZHLinkBuilder {

	public static final String REGEX_AT = "@\\w{2,31}";
	public static final String REGEX_TOPIC = "[#＃][^#＃\n]+[#＃]";
	private static final String HTTP_URL = "(([a-zA-Z0-9+-.]+://)*(([a-zA-Z0-9\\.\\-]+\\.(bb|so|com|cn|net|pro|org|int|info|xxx|biz|coop))|(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}))(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?(?=\\b|[^a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]))";
	private static final String WWW_URL = "(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
	public static final String REGEX_URL = HTTP_URL + "|" + WWW_URL;
	public static final String REGEX_URL_NO_HEAD = "([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";public static final String REGEX_NUMBER = "(^[0-9]{3,4}\\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\\([0-9]{3,4}\\)[0-9]{3,8}$)|(^0{0,1}1[0-9]{10}$)|([0-9]{5,14})";
	public static final String REGEX_APP_SCHEMA_URL = ZHApplication.APP_CONFIG
			.getSchema()
			+ "://[a-zA-Z0-9\\.\\-]+(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?";

	private static HashMap<String, Pattern> regexMap = new HashMap<String, Pattern>();
	protected ArrayList<ZHPattern> includeRegexes = new ArrayList<ZHPattern>();
	protected ArrayList<ZHPattern> excludeRegexes = new ArrayList<ZHPattern>();

	public ZHLinkBuilder registerPattern(String regex, int flag,
			SpanCreator creator) {

		Pattern pat = null;
		if (regexMap.keySet().contains(regex)) {
			pat = regexMap.get(regex);
		} else {
			pat = Pattern.compile(regex);
			regexMap.put(regex, pat);
		}

		ZHPattern zpat = new ZHPattern(pat, flag, creator);
		if (flag == ZHLink.FLAG_EXCLUDE_OTHER) {
			excludeRegexes.add(zpat);
		} else {
			includeRegexes.add(zpat);
		}

		return this;
	}

	public ZHLink create() {
		ZHLink link = new ZHLink(excludeRegexes, includeRegexes);
		return link;
	}

	public ZHLink create(boolean clearSpanBeforeFormat) {
		ZHLink link = new ZHLink(excludeRegexes, includeRegexes,
				clearSpanBeforeFormat);
		return link;
	}
}