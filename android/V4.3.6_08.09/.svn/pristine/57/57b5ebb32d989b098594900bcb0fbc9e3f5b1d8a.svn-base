package com.zhisland.lib.image.viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片浏览器默认的字符串适配器
 * 
 * @author 向飞
 *
 */
public class DefaultStringAdapter implements ImageDataAdapter {

	private List<String> urls = new ArrayList<String>();

	/**
	 * 增加url
	 * 
	 * @param url
	 */
	public void add(String url) {
		this.urls.add(url);
	}

	/**
	 * 增加一个url
	 * 
	 * @param location
	 * @param url
	 */
	public void add(int location, String url) {
		this.urls.add(location, url);
	}

	/**
	 * 增加一组url
	 * 
	 * @param location
	 * @param urls
	 */
	public void add(int location, List<String> urls) {
		this.urls.addAll(location, urls);
	}

	@Override
	public int count() {
		return urls.size();
	}

	@Override
	public String getItem(int postion) {
		return urls.get(postion);
	}

}
