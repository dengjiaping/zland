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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;

/**
 * A collection of string request parameters or files to send along with
 * requests made from an {@link AsyncHttpClient} instance.
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 * RequestParams params = new RequestParams();
 * params.put(&quot;username&quot;, &quot;james&quot;);
 * params.put(&quot;password&quot;, &quot;123456&quot;);
 * params.put(&quot;email&quot;, &quot;my@email.com&quot;);
 * params.put(&quot;profile_picture&quot;, new File(&quot;pic.jpg&quot;)); // Upload a File
 * params.put(&quot;profile_picture2&quot;, someInputStream); // Upload an InputStream
 * params.put(&quot;profile_picture3&quot;, new ByteArrayInputStream(someBytes)); // Upload
 * // some
 * // bytes
 * 
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.post(&quot;http://myendpoint.com&quot;, params, responseHandler);
 * </pre>
 */
public class RequestParams {
	private static String ENCODING = "UTF-8";

	protected ConcurrentHashMap<String, String> urlParams;
	protected ConcurrentHashMap<String, FileBody> fileParams;

	/**
	 * Constructs a new empty <code>RequestParams</code> instance.
	 */
	public RequestParams() {
		init();
	}

	/**
	 * Constructs a new RequestParams instance containing the key/value string
	 * params from the specified map.
	 * 
	 * @param source
	 *            the source key/value string map to add.
	 */
	public RequestParams(Map<String, String> source) {
		init();

		for (Map.Entry<String, String> entry : source.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Constructs a new RequestParams instance and populate it with a single
	 * initial key/value string param.
	 * 
	 * @param key
	 *            the key name for the intial param.
	 * @param value
	 *            the value string for the initial param.
	 */
	public RequestParams(String key, String value) {
		init();

		put(key, value);
	}

	/**
	 * Adds a key/value string pair to the request.
	 * 
	 * @param key
	 *            the key name for the new param.
	 * @param value
	 *            the value string for the new param.
	 */
	public void put(String key, String value) {
		if (key != null && value != null) {
			urlParams.put(key, value);
		}
	}

	/**
	 * Adds a file to the request.
	 * 
	 * @param key
	 *            the key name for the new param.
	 * @param file
	 *            the file to add.
	 */
	public void put(String key, File file) throws FileNotFoundException {
		if (key != null && file != null) {
			fileParams.put(key, new FileBody(file));
		}
	}

	public void put(String key, File file, long start, int count)
			throws FileNotFoundException {
		if (key != null && file != null) {
			fileParams.put(key, new RangeFileBody(file, start, count));
		}
	}

	/**
	 * Removes a parameter from the request.
	 * 
	 * @param key
	 *            the key name for the parameter to remove.
	 */
	public void remove(String key) {
		urlParams.remove(key);
		fileParams.remove(key);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (ConcurrentHashMap.Entry<String, String> entry : urlParams
				.entrySet()) {
			if (result.length() > 0)
				result.append("&");

			result.append(entry.getKey());
			result.append("=");
			result.append(entry.getValue());
		}

		for (ConcurrentHashMap.Entry<String, FileBody> entry : fileParams
				.entrySet()) {
			if (result.length() > 0)
				result.append("&");

			result.append(entry.getKey());
			result.append("=");
			result.append("FILE");
		}

		return result.toString();
	}

	HttpEntity getEntity() {
		HttpEntity entity = null;
		if (!fileParams.isEmpty()) {

			MultipartEntity multipartEntity = new MultipartEntity();

			// Add string params
			for (ConcurrentHashMap.Entry<String, String> entry : urlParams
					.entrySet()) {
				try {
					multipartEntity.addPart(entry.getKey(), new StringBody(
							entry.getValue()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			// Add file params
			for (ConcurrentHashMap.Entry<String, FileBody> entry : fileParams
					.entrySet()) {
				FileBody file = entry.getValue();
				try {
					if (file.getInputStream() != null) {
						multipartEntity.addPart(entry.getKey(), file);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			entity = multipartEntity;
		} else {
			try {
				entity = new UrlEncodedFormEntity(getParamsList(), ENCODING);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return entity;
	}

	private void init() {
		urlParams = new ConcurrentHashMap<String, String>();
		fileParams = new ConcurrentHashMap<String, FileBody>();
	}

	public List<BasicNameValuePair> getParamsList() {
		List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

		for (ConcurrentHashMap.Entry<String, String> entry : urlParams
				.entrySet()) {
			lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return lparams;
	}

	protected String getParamString() {
		if (urlParams.size() < 1) {
			return "";
		}
		return URLEncodedUtils.format(getParamsList(), ENCODING);
	}

}