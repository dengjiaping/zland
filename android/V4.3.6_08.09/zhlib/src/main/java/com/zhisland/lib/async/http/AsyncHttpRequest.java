/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.zhisland.lib.async.http;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import com.zhisland.lib.util.MLog;

class AsyncHttpRequest implements Runnable {

	private final AbstractHttpClient client;
	private final HttpContext context;
	private final HttpUriRequest request;
	private final AsyncHttpResponseHandler<?> responseHandler;
	private int executionCount;
	private long postStartTime;

	private boolean hasReplaceHost = false;

	public int priority = -1;

	public AsyncHttpRequest(AbstractHttpClient client, HttpContext context,
			HttpUriRequest request, AsyncHttpResponseHandler<?> responseHandler) {
		this.client = client;
		this.context = context;
		this.request = request;
		this.responseHandler = responseHandler;
		this.postStartTime = System.currentTimeMillis();
	}

	public void run() {
		try {
			if (responseHandler != null) {
				responseHandler.sendStartMessage();
			}

			makeRequestWithRetries();

			if (responseHandler != null) {
				responseHandler.sendFinishMessage();
			}
		} catch (IOException e) {
			if (responseHandler != null) {
				responseHandler.sendFinishMessage();
				responseHandler.sendFailureMessage(e, null);
			}
		}
	}

	private void makeRequestWithRetries() throws ConnectException {
		// This is an additional layer of retry logic lifted from droid-fu
		// See:
		// https://github.com/kaeppler/droid-fu/blob/master/src/main/java/com/github/droidfu/http/BetterHttpRequestBase.java
		boolean retry = true;
		IOException cause = null;
		HttpRequestRetryHandler retryHandler = client
				.getHttpRequestRetryHandler();
		while (retry) {
			try {
				makeRequest();
				return;
			} catch (Exception e) {
				cause = new IOException(e.getMessage());
				retry = retryHandler.retryRequest(cause, ++executionCount,
						context);
				MLog.e(AsyncHttpResponseHandler.TAG, e.getMessage(), e);
			}
		}

		// no retries left, crap out with exception
		ConnectException ex = new ConnectException();
		ex.initCause(cause);
		throw ex;
	}

	private void makeRequest() throws IOException {
		boolean isInterrupted = Thread.currentThread().isInterrupted();
		MLog.d(AsyncHttpResponseHandler.TAG, Thread.currentThread().getName()
				+ " " + isInterrupted);
		if (!Thread.currentThread().isInterrupted()) {

			long be = System.currentTimeMillis();
			long waitTime = be - postStartTime;
			HttpResponse response = client.execute(request, context);
			long diff = System.currentTimeMillis() - be;
			MLog.d(AsyncHttpResponseHandler.TAG, priority + "---" + diff + "  "
					+ waitTime + " " + request.getURI().toString());

			if (!Thread.currentThread().isInterrupted()) {
				if (responseHandler != null) {
					responseHandler.sendResponseMessage(response);
				}
			} else {
				// reached whenever the request is cancelled before its response
				// is received
			}
		}

	}
}