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
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.StaticLayout;

import com.zhisland.lib.async.PriorityFutureTask;
import com.zhisland.lib.async.ThreadManager;
import com.zhisland.lib.component.application.ZHApplication;
import com.zhisland.lib.util.MLog;

/**
 * The AsyncHttpClient can be used to make asynchronous GET, POST, PUT and
 * DELETE HTTP requests in your Android applications. Requests can be made with
 * additional parameters by passing a {@link RequestParams} instance, and
 * responses can be handled by passing an anonymously overridden
 * {@link AsyncHttpResponseHandler} instance.
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get(&quot;http://www.google.com&quot;, new AsyncHttpResponseHandler() {
 * 	&#064;Override
 * 	public void onSuccess(String response) {
 * 		System.out.println(response);
 * 	}
 * });
 * </pre>
 */
public class AsyncHttpClient {
	private static final String TAG = "httpclient";
	public static final String TIME_TAG = "rtime";

	private static final int DEFAULT_MAX_CONNECTIONS = 4;
	private static final int DEFAULT_SOCKET_TIMEOUT = 20 * 1000;
	private static final int DEFAULT_MAX_RETRIES = 2;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";

	private static int maxConnections = DEFAULT_MAX_CONNECTIONS;
	private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

	private final DefaultHttpClient httpClient;
	private final HttpContext httpContext;
	// private final Map<Context, List<WeakReference<Future<?>>>>
	// requestListMap;
	// private final Map<UUID, WeakReference<Future<?>>> requestMap;
	private final Map<String, String> clientHeaderMap;

	/**
	 * Creates a new AsyncHttpClient.
	 */
	private AsyncHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();

		ConnManagerParams.setTimeout(httpParams, socketTimeout);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(maxConnections));
		ConnManagerParams.setMaxTotalConnections(httpParams,
				DEFAULT_MAX_CONNECTIONS);

		HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams,
				DEFAULT_SOCKET_BUFFER_SIZE);

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		KeyStore trustStore = null;
		SSLSocketFactory sf = null;
		try {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			sf = new MySSLSocketFactory(trustStore);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}
		if (sf != null) {
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			schemeRegistry.register(new Scheme("https", sf, 443));
		}
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				httpParams, schemeRegistry);

		httpContext = new SyncBasicHttpContext(null);

		// HttpHost proxy = new HttpHost("192.168.9.110", 80);
		// httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		httpClient = new DefaultHttpClient(cm, httpParams);
		String versionName = "";
		try {
			versionName = ZHApplication.APP_CONTEXT.getPackageManager()
					.getPackageInfo(ZHApplication.APP_CONTEXT.getPackageName(),
							0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		httpClient.getParams().setParameter(
				CoreProtocolPNames.USER_AGENT,
				String.format("正和岛 %s Android %s", versionName,
						Build.VERSION.RELEASE));
		httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context) {
				if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
					request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
				}
				for (String header : clientHeaderMap.keySet()) {
					request.addHeader(header, clientHeaderMap.get(header));
				}
			}
		});

		httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse response, HttpContext context) {
				final HttpEntity entity = response.getEntity();
				final Header encoding = entity.getContentEncoding();
				if (encoding != null) {
					for (HeaderElement element : encoding.getElements()) {
						if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
							response.setEntity(new InflatingEntity(response
									.getEntity()));
							break;
						}
					}
				}
			}
		});

		httpClient.setHttpRequestRetryHandler(new RetryHandler(
				DEFAULT_MAX_RETRIES));
		httpClient
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {

					@Override
					public long getKeepAliveDuration(HttpResponse response,
							HttpContext context) {
						long keepAlive = super.getKeepAliveDuration(response,
								context);
						// MLog.i(TIME_TAG, keepAlive + " keep alive");
						if (keepAlive == -1) {
							keepAlive = 500000;
						}
						return keepAlive;
					}
				});

		clientHeaderMap = new HashMap<String, String>();
	}

	/**
	 * Get the underlying HttpClient instance. This is useful for setting
	 * additional fine-grained settings for requests by accessing the client's
	 * ConnectionManager, HttpParams and SchemeRegistry.
	 */
	public HttpClient getHttpClient() {
		return this.httpClient;
	}

	/**
	 * Sets an optional CookieStore to use when making requests
	 * 
	 * @param cookieStore
	 *            The CookieStore implementation to use, usually an instance of
	 *            {@link PersistentCookieStore}
	 */
	public void setCookieStore(CookieStore cookieStore) {
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	public CookieStore getCookieStore() {
		return (CookieStore) httpContext
				.getAttribute(ClientContext.COOKIE_STORE);
	}

	/**
	 * Sets the User-Agent header to be sent with each request. By default,
	 * "Android Asynchronous Http Client/VERSION (http://loopj.com/android-async-http/)"
	 * is used.
	 * 
	 * @param userAgent
	 *            the string to use in the User-Agent header.
	 */
	public void setUserAgent(String userAgent) {
		HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
	}

	/**
	 * Sets the SSLSocketFactory to user when making requests. By default, a
	 * new, default SSLSocketFactory is used.
	 * 
	 * @param sslSocketFactory
	 *            the socket factory to use for https requests.
	 */
	public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.httpClient.getConnectionManager().getSchemeRegistry()
				.register(new Scheme("https", sslSocketFactory, 443));
	}

	/**
	 * Sets headers that will be added to all requests this client makes (before
	 * sending).
	 * 
	 * @param header
	 *            the name of the header
	 * @param value
	 *            the contents of the header
	 */
	public void addHeader(String header, String value) {
		clientHeaderMap.put(header, value);
	}

	/**
	 * remove headers by header's key
	 * 
	 * @param key
	 */
	public void removeHeader(String key) {
		if (clientHeaderMap.containsKey(key)) {
			clientHeaderMap.remove(key);
		}
	}

	/**
	 * clear all headers
	 */
	public void clearHeaders() {
		clientHeaderMap.clear();
	}

	// ==============================
	// =====all http methods=========

	/**
	 * Perform a HTTP GET request and track the Android Context which initiated
	 * the request.
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param params
	 *            additional GET parameters to send with the request.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void get(RequestContext requestContext,
			ResponseContext<?> responseContext) {
		sendRequest(
				httpClient,
				httpContext,
				new HttpGet(getUrlWithQueryString(requestContext.getUrl(),
						requestContext.getParams())),
				requestContext.getHeaders(), responseContext, 0);
	}

	/**
	 * Perform a HTTP POST request and track the Android Context which initiated
	 * the request.
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param entity
	 *            a raw {@link HttpEntity} to send with the request, for
	 *            example, use this to send string/json/xml payloads to a server
	 *            by passing a {@link org.apache.http.entity.StringEntity}.
	 * @param contentType
	 *            the content type of the payload you are sending, for example
	 *            application/json if sending a json payload.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void post(RequestContext requestContext,
			ResponseContext<?> responseContext) {
		sendRequest(
				httpClient,
				httpContext,
				addEntityToRequestBase(new HttpPost(requestContext.getUrl()),
						paramsToEntity(requestContext.getParams())),
				requestContext.getHeaders(), responseContext,
				requestContext.priority);
	}

	public void post(Object context, String url, byte[] bytes, int priority,
			AsyncHttpResponseHandler<?> responseHandler) {
		HttpPost post = new HttpPost(url);

		ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes);
		byteArrayEntity.setContentType("application/octet-stream");

		HttpEntityEnclosingRequestBase entotyBase = addEntityToRequestBase(
				post, byteArrayEntity);
		entotyBase.addHeader("cookie", "Z_AUTH=access_token");

		AsyncHttpRequest req = new AsyncHttpRequest(httpClient, httpContext,
				entotyBase, responseHandler);
		asyncExecuteRequest(req, priority, context);
	}

	/**
	 * Perform a HTTP PUT request and track the Android Context which initiated
	 * the request.
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param entity
	 *            a raw {@link HttpEntity} to send with the request, for
	 *            example, use this to send string/json/xml payloads to a server
	 *            by passing a {@link org.apache.http.entity.StringEntity}.
	 * @param contentType
	 *            the content type of the payload you are sending, for example
	 *            application/json if sending a json payload.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void put(RequestContext requestContext,
			ResponseContext<?> responseContext) {
		sendRequest(
				httpClient,
				httpContext,
				addEntityToRequestBase(new HttpPut(requestContext.getUrl()),
						paramsToEntity(requestContext.getParams())),
				requestContext.getHeaders(), responseContext, 2);
	}

	/**
	 * Perform a HTTP DELETE request.
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void delete(RequestContext requestContext,
			ResponseContext<?> responseContext) {

		final HttpDelete delete = new HttpDelete(requestContext.getUrl());
		sendRequest(httpClient, httpContext, delete,
				requestContext.getHeaders(), responseContext, 2);
	}

	public void setWapProxy(String proxy, int port) {
		HttpHost httpHost = new HttpHost(proxy, port);
		httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				httpHost);
	}

	public void removeWapProxy() {
		httpClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
	}

	// Private stuff

	private void sendRequest(DefaultHttpClient client, HttpContext httpContext,
			HttpUriRequest uriRequest, Map<String, String> headers,
			ResponseContext<?> responseContext, int priority) {

		if (headers != null) {
			Iterator<Entry<String, String>> it = headers.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				uriRequest.addHeader(entry.getKey(), entry.getValue());
			}
		}

		AsyncHttpRequest asyncRequest = new AsyncHttpRequest(client,
				httpContext, uriRequest, responseContext.getResponseHandler());
		asyncRequest.priority = priority;
		if (responseContext.getResponseHandler().isSyncMode()) {
			syncExecuteRequest(asyncRequest, priority,
					responseContext.getContext());
		} else {
			asyncExecuteRequest(asyncRequest, priority,
					responseContext.getContext());
		}

	}

	private void asyncExecuteRequest(AsyncHttpRequest req, int priority,
			Object context) {
		PriorityFutureTask<Object> request = new PriorityFutureTask<Object>(
				req, null, priority);
		ThreadManager.instance().execute(request, context);
	}

	private void syncExecuteRequest(AsyncHttpRequest req, int priority,
			Object context) {
		req.run();
	}

	private String getUrlWithQueryString(String url, RequestParams params) {
		if (params != null && params.getParamsList().size() > 0) {
			String paramString = params.getParamString();

			if (url.contains("?")) {
				url += "&" + paramString;
			} else {
				url += "?" + paramString;
			}
		}

		return url;
	}

	private HttpEntity paramsToEntity(RequestParams params) {
		HttpEntity entity = null;

		if (params != null) {
			entity = params.getEntity();
		}

		return entity;
	}

	private HttpEntityEnclosingRequestBase addEntityToRequestBase(
			HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
		if (entity != null) {
			requestBase.setEntity(entity);
		}

		return requestBase;
	}

	/**
	 * A utility function to close an input stream without raising an exception.
	 * 
	 * @param is
	 *            input stream to close safely
	 */
	public static void silentCloseInputStream(InputStream is) {
		try {
			if (is != null) {
				is.close();
			}
		} catch (IOException e) {
			MLog.w(TAG, "Cannot close input stream", e);
		}
	}

	/**
	 * A utility function to close an output stream without raising an
	 * exception.
	 * 
	 * @param os
	 *            output stream to close safely
	 */
	public static void silentCloseOutputStream(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			MLog.w(TAG, "Cannot close output stream", e);
		}
	}

	private static class InflatingEntity extends HttpEntityWrapper {
		public InflatingEntity(HttpEntity wrapped) {
			super(wrapped);
		}

		@Override
		public InputStream getContent() throws IOException {
			return new GZIPInputStream(wrappedEntity.getContent());
		}

		@Override
		public long getContentLength() {
			return -1;
		}
	}

	public static class Factory {
		private static Object lock = new Object();
		private static AsyncHttpClient instance = null;

		public static AsyncHttpClient getSingletonInstance() {
			if (instance == null) {
				synchronized (lock) {
					if (instance == null) {
						AsyncHttpClient temp = new AsyncHttpClient();
						instance = temp;
					}
				}
			}

			return instance;
		}
	}

}
