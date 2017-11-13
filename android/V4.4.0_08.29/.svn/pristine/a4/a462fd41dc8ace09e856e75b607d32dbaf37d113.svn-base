package com.zhisland.lib.async.http;

import java.util.UUID;

public class ResponseContext<S> {
	private Object context;
	private AsyncHttpResponseHandler<S> responseHandler;
	private UUID requestUid;
	private boolean shouldCanceledAfterDestroy;

	public long postTime = 0;

	public ResponseContext(Object context,
			AsyncHttpResponseHandler<S> responseHandler, UUID requestUid,
			boolean shouldCanceledAfterDestroy) {
		this.context = context;
		this.responseHandler = responseHandler;
		this.requestUid = requestUid;
		this.shouldCanceledAfterDestroy = shouldCanceledAfterDestroy;
	}

	public boolean shouldCanceledAfterDestroy() {
		return shouldCanceledAfterDestroy;
	}

	public Object getContext() {
		return context;
	}

	public AsyncHttpResponseHandler<S> getResponseHandler() {
		return responseHandler;
	}

	public UUID getRequestUid() {
		return requestUid;
	}
}
