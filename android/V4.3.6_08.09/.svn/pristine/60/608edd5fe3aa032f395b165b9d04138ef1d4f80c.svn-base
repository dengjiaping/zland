package com.zhisland.lib.async.http;

import java.util.Map;

public class RequestContext {
	private Map<String, String> headers;
	private String url;
	private RequestParams params;
	public int priority = 0;

	public RequestContext(String url, RequestParams params,
			Map<String, String> headers) {
		this.url = url;
		this.params = params;
		this.headers = headers;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public String getUrl() {
		return url;
	}

	public RequestParams getParams() {
		return params;
	}

}
