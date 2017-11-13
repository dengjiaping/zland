package com.zhisland.android.blog.common.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zhisland.lib.util.StringUtil;

public class MobileUtil {

	public static String getFormatMobileNumber(String mobile) {
		mobile = getRelleayMobileNumber(mobile);
		StringBuffer buffer = new StringBuffer(mobile);
		if (buffer.length() > 3) {
			buffer.insert(3, " ");
		}
		if (buffer.length() > 8) {
			buffer.insert(8, " ");
		}
		return buffer.toString();
	}

	public static String getRelleayMobileNumber(String mobile) {
		return mobile.replace(" ", "");
	}
	
	/**
	 * 获取字符串中所有的数字
	 * */
	public static String getNumStr(String mobile){
		if (StringUtil.isNullOrEmpty(mobile)) {
			return "";
		}
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(mobile);
		String ret = m.replaceAll("").trim();
		return ret;
	}

	/**
	 * 用分隔符格式化电话号码（带国家码的电话无效）
	 * 
	 * @param mobile 电话号码。
	 * @param separator 分隔符
	 * 
	 * */
	public static String getFormatChineseMobile(String mobile, String separator) {
		if (StringUtil.isNullOrEmpty(mobile)) {
			return "";
		}
		if (StringUtil.isNullOrEmpty(separator)) {
			return mobile;
		}
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(mobile);
		String ret = m.replaceAll("").trim();
		if (ret.length() != 11 || !ret.startsWith("1")) {
			//如果不是1开头，11位，则直接返回mobile。不进行格式化。
			return mobile;
		}
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		for (int i = ret.length() - 1; i >= 0; i--) {
			sb.insert(0, ret.charAt(i));
			counter++;
			if (counter % 4 == 0) {
				sb.insert(0, separator);
			}
		}
		return sb.toString();
	}

	/**
	 * 校验密码
	 */
	public static boolean isValidPwd2(String pwd) {
		if (Pattern.matches("^[0-9a-zA-Z]{6,16}", pwd)) {
			return true;
		}
		return false;
	}

	/**
	 * 校验密码
	 */
	public static boolean isValidPwd(String pwd) {
		if (!StringUtil.isNullOrEmpty(pwd) && pwd.length() >= 6
				&& pwd.length() <= 16) {
			return true;
		}
		return false;
	}

	public static String replacePhoneNumberToAvaliable(String phone,
			boolean isReturnNull) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(phone);
		String ret = m.replaceAll("").trim();
		if (ret.startsWith("86")) {
			ret = ret.substring(2, ret.length());
		}
		if (StringUtil.phoneFormatCheck(ret)) {
			return ret;
		}
		return isReturnNull ? "" : phone;
	}

	/**
	 * List<String>逗号分隔 -->String
	 */
	public static String listToString(List<String> strs) {
		if (strs == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < strs.size(); i++) {
			buffer.append(strs.get(i));
			if (i != strs.size() - 1) {
				buffer.append(",");
			}
		}
		return buffer.toString();
	}

}
