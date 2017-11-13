package com.zhisland.lib.async.http.task;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import com.google.gson.reflect.TypeToken;
import com.zhisland.lib.ZHServiceCode;
import com.zhisland.lib.async.http.AsyncHttpClient;
import com.zhisland.lib.async.http.AsyncHttpResponseHandler;
import com.zhisland.lib.async.http.RequestContext;
import com.zhisland.lib.async.http.RequestParams;
import com.zhisland.lib.async.http.ResponseContext;
import com.zhisland.lib.async.http.task.TaskHelper.EnumSwitch;
import com.zhisland.lib.util.MLog;

public abstract class HttpTask<S> {

	public static boolean HAS_NAV_TO_LOGOUT = false;
	private AsyncHttpClient httpClient;
	protected Object context;
	private AsyncHttpResponseHandler<S> responseHandler;
	private boolean isCanceled = false;
	private final UUID taskId = UUID.randomUUID();
	private boolean shouldCancel = true;
	private final TaskCallback<S> responseCallback;
	protected int priority = 1;
	private String url;

	private static TaskHelper helper = new TaskHelper();

	public HttpTask(Object context, final TaskCallback<S> responseCallback) {
		this(context, responseCallback, true, false);
	}

	public HttpTask(Object context, final TaskCallback<S> responseCallback,
			boolean shouldCancel) {
		this(context, responseCallback, shouldCancel, false);
	}

	private AsyncHttpResponseHandler<S> createAsyncHttpResponseHandler() {
		return new AsyncHttpResponseHandler<S>() {

			@Override
			public void onStart() {
				HttpTask.this.onStart();
			}

			@Override
			public void onSuccess(S content) {
				HttpTask.this.onSuccess(content);
			}

			@Override
			public void onFailure(Throwable failture) {

				HttpTask.this.onFailure(failture);
			}

			@Override
			public void onFinish() {
				HttpTask.this.onFinish();
			}

			@Override
			protected S handleSuccessStringMessage(HttpResponse response)
					throws Exception {
				MLog.d(AsyncHttpResponseHandler.TAG,
						"handleSuccessStringMessage " + url);
				return HttpTask.this.handleSuccessStringMessage(response);
			}

			@Override
			protected Throwable handleFailureMessage(Throwable e,
					String responseBody) {
				return HttpTask.this.handleFailureMessage(e, responseBody);
			}

			@Override
			public boolean replaceURIHost(HttpUriRequest req) {
				return super.replaceURIHost(req);
			}

		};
	}

	public HttpTask(Object context, final TaskCallback<S> responseCallback,
			boolean shouldCancel, boolean isSync) {

		this.shouldCancel = shouldCancel;
		this.httpClient = AsyncHttpClient.Factory.getSingletonInstance();
		this.context = context;
		this.responseCallback = responseCallback;

		responseHandler = createAsyncHttpResponseHandler();
		responseHandler.setSyncMode(isSync);

	}

	/**
	 * 设置同步或者异步
	 * 
	 * @param isSync
	 */
	public void setSyncMode(boolean isSync) {
		if (responseHandler != null) {
			responseHandler.setSyncMode(isSync);
		}
	}

	protected void get(RequestParams params, HashMap<String, String> headers) {

		params = this.applyCommonParams(params);

		headers = this.applyHeader(headers);
		params.put("taskName", this.getClass().getSimpleName());
		this.url = getUrl();
		ResponseContext<S> responseContext = new ResponseContext<S>(context,
				responseHandler, taskId, shouldCancel);
		RequestContext requestContext = new RequestContext(url, params, headers);

		httpClient.get(requestContext, responseContext);
	}

	protected void post(RequestParams params, HashMap<String, String> headers) {
		// this.timeCost = System.currentTimeMillis();
		params = this.applyCommonParams(params);

		headers = this.applyHeader(headers);

		params.put("taskName", this.getClass().getSimpleName());

		ResponseContext<S> responseContext = new ResponseContext<S>(context,
				responseHandler, taskId, shouldCancel);
		this.url = getUrl();
		RequestContext requestContext = new RequestContext(url, params, headers);

		requestContext.priority = 1;
		httpClient.post(requestContext, responseContext);
	}

	protected void put(RequestParams params, HashMap<String, String> headers) {

		headers = this.applyHeader(headers);

		ResponseContext<S> responseContext = new ResponseContext<S>(context,
				responseHandler, taskId, shouldCancel);
		this.url = getUrl();
		RequestContext requestContext = new RequestContext(url, params, headers);

		requestContext.priority = 1;
		httpClient.put(requestContext, responseContext);
	}

	protected void delete(RequestParams params, HashMap<String, String> headers) {

		headers = this.applyHeader(headers);
		this.url = getUrl();
		ResponseContext<S> responseContext = new ResponseContext<S>(context,
				responseHandler, taskId, shouldCancel);

		RequestContext requestContext = new RequestContext(url, params, headers);

		requestContext.priority = 1;
		httpClient.delete(requestContext, responseContext);
	}

	protected RequestParams applyCommonParams(RequestParams params) {
		if (params == null) {
			params = new RequestParams();
		}
		return params;
	}

	protected HashMap<String, String> applyHeader(
			HashMap<String, String> headers) {
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		return headers;
	}

	/**
	 * if cancel the task, UI will not receive anything
	 */
	public void cancel() {
		MLog.d(AsyncHttpResponseHandler.TAG, "cancel task " + url);
		isCanceled = true;
	}

	public UUID getTaskId() {
		return taskId;
	}

	@SuppressWarnings("unchecked")
	protected Throwable handleFailureMessageInBackGround(Throwable e,
			String responseBody) {

		return e;
	}

	public abstract void execute();

	/**
	 * when override this method, {@link getPatureUrl} will take no effect
	 * 
	 * @return
	 */
	protected String getUrl() {
		return getBaseUrl() + getPartureUrl();
	}

	protected String getBaseUrl() {
		return null;
	}

	protected String getPartureUrl() {
		return null;
	}

	protected Type getDeserializeType() {
		return new TypeToken<ZHResponse<Object>>() {
		}.getType();
	}

	/**
	 * when exception happened, will invoke this method to replace host and
	 * retry request
	 * 
	 * @param req
	 * @return
	 */
	protected boolean replaceURIHost(HttpUriRequest req) {
		return false;
	}

	protected RequestParams put(RequestParams params, String key, String value) {
		return helper.put(params, key, value, null, false);
	}

	protected RequestParams put(RequestParams params, String key, int value) {
		return helper.put(params, key, value, -1, EnumSwitch.LargeThan);
	}

	protected RequestParams forcePut(RequestParams params, String key, int value) {
		return helper.put(params, key, value);
	}

	protected RequestParams put(RequestParams params, String key, long value) {
		return helper.put(params, key, value, -1, EnumSwitch.LargeThan);
	}

	protected RequestParams put(RequestParams params, String key, File file) {
		return helper.put(params, key, file);
	}

	protected RequestParams put(RequestParams params, String key, File file,
			long start, int count) {
		return helper.put(params, key, file, start, count);
	}

	// ==============call back method======================

	protected void onStart() {
		if (!isCanceled && responseCallback != null) {
			responseCallback.onStart();
		}
	}

	protected void onSuccess(S content) {

		TaskManager.removeTask(HttpTask.this.context, HttpTask.this);

		if (!isCanceled && responseCallback != null) {
			responseCallback.onSuccess(content);
		}
	}

	protected void onFailure(Throwable failture) {

		TaskManager.removeTask(HttpTask.this.context, HttpTask.this);
		if (failture != null) {
			// GAProxy.trackEvent("网络请求失败", getUrl(), failture.toString(), 0);
		}

		if (!isCanceled && responseCallback != null) {
			responseCallback.onFailure(failture);
		}
	}

	protected void onFinish() {
		if (!isCanceled && responseCallback != null) {
			responseCallback.onFinish();
		}
	}

	/**
	 * handle response, 默认执行字符串的解析
	 * {@code HttpTask#handleStringProxy(HttpResponse)}
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected S handleSuccessStringMessage(HttpResponse response)
			throws Exception {
		MLog.d(AsyncHttpResponseHandler.TAG, "handleSuccessStringMessage "
				+ url);
		return HttpTask.this.handleStringProxy(response);
	}

	protected Throwable handleFailureMessage(Throwable e, String responseBody) {
		return HttpTask.this.handleFailureMessageInBackGround(e, responseBody);
	}

	// ---expose asynccallbackhandler's methods to override--

	protected boolean isPureRest = false;

	protected S handleStringProxy(HttpResponse response) throws Exception {

		if (isPureRest) {
			String responseBody = convertToString(response);
			Type listType = this.getDeserializeType();
			S res = null;
			MLog.e("zhapp", "deserialize start");
			MLog.d("zhapp", "responseBody ： " + responseBody);
			res = GsonHelper.GetCommonGson().fromJson(responseBody, listType);
			return res;
		} else {
			String responseBody = convertToString(response);
			Type listType = this.getDeserializeType();
			ZHResponse<S> res = null;
			MLog.e("zhapp", "deserialize start");
			res = GsonHelper.GetCommonGson().fromJson(responseBody, listType);
			if (res == null) {
				MLog.e("zhapp",
						"server response the request, but return nothing.");
				throw new Exception(
						"server response the request, but return nothing.");

			}

			if (res.code != ZHServiceCode.COMM_OK) {
				MLog.e("zhapp", res.msg == null ? "" : res.msg);
				ZHException ex = new ZHException(res.code, res.msg);
				throw ex;
			} else {
				MLog.e("zhapp", "return data");
				return res.data;
			}

		}

	}

	/**
	 * 将返回的请求转换成字符串
	 * 
	 * @param response
	 * @return
	 */
	protected String convertToString(HttpResponse response) throws IOException {
		return convertResponseToString(response);
	}

	protected static final int BUFFER_SIZE = 4096;

	/**
	 * Returns byte array of response HttpEntity contents
	 * 
	 * @param entity
	 *            can be null
	 * @return response entity body or null
	 * @throws java.io.IOException
	 *             if reading entity or creating byte array failed
	 */
	protected byte[] getResponseData(HttpEntity entity) throws IOException {
		byte[] responseBody = null;
		if (entity != null) {
			InputStream instream = entity.getContent();
			if (instream != null) {
				long contentLength = entity.getContentLength();
				if (contentLength > Integer.MAX_VALUE) {
					throw new IllegalArgumentException(
							"HTTP entity too large to be buffered in memory");
				}
				int buffersize = (contentLength < 0) ? BUFFER_SIZE
						: (int) contentLength;
				try {
					ByteArrayBuffer buffer = new ByteArrayBuffer(buffersize);
					try {
						byte[] tmp = new byte[BUFFER_SIZE];
						int l, count = 0;
						// do not send messages if request has been cancelled
						while ((l = instream.read(tmp)) != -1
								&& !Thread.currentThread().isInterrupted()) {
							count += l;
							buffer.append(tmp, 0, l);
						}
					} finally {
						AsyncHttpClient.silentCloseInputStream(instream);
					}
					responseBody = buffer.toByteArray();
				} catch (OutOfMemoryError e) {
					System.gc();
					throw new IOException(
							"File too large to fit into available memory");
				}
			}
		}
		return responseBody;
	}

	public static String convertResponseToString(HttpResponse response)
			throws IOException {
		HttpEntity entity = null;
		HttpEntity temp = response.getEntity();
		if (temp != null) {
			entity = new BufferedHttpEntity(temp);
		}
		String responseBody = EntityUtils.toString(entity);
		return responseBody;
	}

}
