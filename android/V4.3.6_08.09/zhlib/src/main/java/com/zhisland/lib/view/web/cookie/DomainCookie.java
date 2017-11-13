package com.zhisland.lib.view.web.cookie;

import java.util.Date;

import org.apache.http.cookie.Cookie;

public class DomainCookie implements Cookie {

	private String name;
	private String value;
	private String domain;

	public DomainCookie(String name, String value, String domain) {
		this.name = name;
		this.value = value;
		this.domain = domain;
	}

	@Override
	public String getComment() {
		return null;
	}

	@Override
	public String getCommentURL() {
		return null;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	@Override
	public Date getExpiryDate() {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public int[] getPorts() {
		return null;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public boolean isExpired(Date date) {
		return false;
	}

	@Override
	public boolean isPersistent() {
		return false;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

}
