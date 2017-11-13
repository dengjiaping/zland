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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.zhisland.lib.async.MyHandler;
import com.zhisland.lib.async.MyHandler.HandlerListener;
import com.zhisland.lib.util.MLog;

/**
 * Used to intercept and handle the responses from requests made using
 * {@link AsyncHttpClient}. The {@link #onSuccess(String)} method is designed to
 * be anonymously overridden with your own response handling code.
 * {@link AsyncHttpResponseHandler#handleSuccessStringMessage(String)} and
 * {@link #handleFailureMessage(Throwable, String)} will run in background
 * thread, recommend you'd better parse your response in these methods.
 * <p>
 * Additionally, you can override the {@link #onFailure(Throwable, String)},
 * {@link #onStart()}, and {@link #onFinish()} methods as required.
 * <p>
 * 
 */
public abstract class AsyncHttpResponseHandler<S> implements HandlerListener {
	public static final String TAG = "zhttp";
	private static final int SUCCESS_MESSAGE = 0;
	private static final int FAILURE_MESSAGE = 1;
	private static final int START_MESSAGE = 2;
	private static final int FINISH_MESSAGE = 3;
	private static final int PROGRESS_MESSAGE = 4;
	private Handler handler;
	private boolean isPostBack = true;
	private boolean useSyncMode = false;
	public String url;
	private static final Object lock = new Object();

	public void hidePostBack() {
		synchronized (lock) {
			isPostBack = false;
		}
	}

	public void doPostBack() {
		synchronized (lock) {
			isPostBack = true;
		}
	}

	public AsyncHttpResponseHandler() {
		if (Looper.myLooper() != null) {
			handler = new MyHandler(this);
		}

	}

	//
	// Callbacks to be overridden, typically anonymously
	//

	/**
	 * Fired when the request is started, override to handle in your own code
	 */
	public void onStart() {
	}

	/**
	 * Fired in all cases when the request is finished, after both success and
	 * failure, override to handle in your own code
	 */
	public void onFinish() {
	}

	/**
	 * Fired when a request returns successfully, override to handle in your own
	 * code
	 * 
	 * @param content
	 *            the body of the HTTP response from the server
	 */
	public void onSuccess(S content) {

	}

	/**
	 * Fired when a request fails to complete, override to handle in your own
	 * code
	 * 
	 * @param error
	 *            the underlying cause of the failure
	 * @param content
	 *            the response body, if any
	 */
	public void onFailure(Throwable failture) {

	}

	/**
	 * when exception happened, allow replace host
	 */
	public boolean replaceURIHost(HttpUriRequest req) {
		return false;
	}

	//
	// Pre-processing of messages (executes in background threadpool thread)
	//

	protected void sendSuccessMessage(S taskResponse) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, taskResponse));
	}

	protected void sendFailureMessage(Throwable e, String responseBody) {
		Throwable failture = handleFailureMessage(e, responseBody);
		sendMessage(obtainMessage(FAILURE_MESSAGE, failture));
	}

	protected void sendStartMessage() {
		sendMessage(obtainMessage(START_MESSAGE, null));
	}

	protected void sendFinishMessage() {
		sendMessage(obtainMessage(FINISH_MESSAGE, null));
	}

	/**
	 * pre-processing of messages, in background thread
	 * 
	 * @throws Exception
	 */
	protected abstract S handleSuccessStringMessage(HttpResponse response)
			throws Exception;

	/**
	 * pre-processing of messages, in background thread
	 */
	protected abstract Throwable handleFailureMessage(Throwable e,
			String responseBody);

	// Methods which emulate android's Handler and Message methods
	@SuppressWarnings("unchecked")
	public boolean handleMessage(Message msg) {

		switch (msg.what) {
		case SUCCESS_MESSAGE:
			onSuccess((S) msg.obj);
			break;
		case FAILURE_MESSAGE:
			Throwable repsonse = (Throwable) msg.obj;
			onFailure(repsonse);
			break;
		case START_MESSAGE:
			onStart();
			break;
		case FINISH_MESSAGE:
			onFinish();
			break;
		default:
			return false;
		}
		return true;
	}

	protected void sendMessage(Message msg) {
		if (!isPostBack) {
			return;
		}

		MLog.d(TAG, "send msg " + msg.what + "  " + url);
		if (!useSyncMode && handler != null) {
			handler.sendMessage(msg);
		} else {
			handleMessage(msg);
		}
	}

	/**
	 * if set to true, the request will execute and callback in current
	 * thread,or in a worker thread
	 * 
	 * @param useSyncMode
	 */
	public void setSyncMode(boolean useSyncMode) {
		this.useSyncMode = useSyncMode;
	}

	/**
	 * get sync mode
	 * 
	 * @return
	 */
	public boolean isSyncMode() {
		return this.useSyncMode;
	}

	protected Message obtainMessage(int responseMessage, Object response) {
		Message msg = null;
		if (handler != null) {
			msg = this.handler.obtainMessage(responseMessage, response);
		} else {
			msg = new Message();
			msg.what = responseMessage;
			msg.obj = response;
		}
		return msg;
	}

	// Interface to AsyncHttpRequest
	void sendResponseMessage(HttpResponse response) {
		MLog.e(TAG, "start sendResponseMessage " + url);
		StatusLine status = response.getStatusLine();
		if (status.getStatusCode() >= 300) {
			MLog.e(TAG,
					"sendResponseMessage error with code "
							+ status.getStatusCode() + "   " + url);
			String responseBody = null;
			try {
				HttpEntity entity = null;
				HttpEntity temp = response.getEntity();
				if (temp != null) {
					entity = new BufferedHttpEntity(temp);
				}
				responseBody = EntityUtils.toString(entity);
			} catch (IOException e) {
				MLog.e(TAG, e.getMessage(), e);
				sendFailureMessage(e, null);
			}
			String desc = "";
			Header header = response.getFirstHeader("msg");
			if (header != null) {
				String value = header.getValue();
				try {
					desc = URLDecoder.decode(value, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			sendFailureMessage(new HttpResponseException(
					status.getStatusCode(), desc),
					responseBody);
			return;
		}

		S taskResponse = null;
		try {
			MLog.e(TAG, "start handle response " + url);
			taskResponse = handleSuccessStringMessage(response);
			MLog.e(TAG, "finish handle response " + url);
			sendSuccessMessage(taskResponse);
		} catch (IOException e) {
			MLog.e(TAG, e.getMessage(), e);
			sendFailureMessage(e, null);
		} catch (Exception e) {
			MLog.e(TAG, e.getMessage(), e);
			sendFailureMessage(e, null);
		}

	}

}