package com.zhisland.lib.bitmap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLHelper {

	private static final Object lockObj = new Object();
	private static SSLContext sslContext = null;

	private static SSLContext getSSLContext() {
		if (sslContext == null) {
			synchronized (lockObj) {
				if (sslContext == null) {
					X509TrustManager easyTrustManager = new X509TrustManager() {

						public java.security.cert.X509Certificate[] getAcceptedIssuers() {
							return null;
						}

						@Override
						public void checkClientTrusted(
								java.security.cert.X509Certificate[] chain,
								String authType)
								throws java.security.cert.CertificateException {

						}

						@Override
						public void checkServerTrusted(
								java.security.cert.X509Certificate[] chain,
								String authType)
								throws java.security.cert.CertificateException {

						}

					};

					// Create a trust manager that does not validate certificate
					// chains
					TrustManager[] trustAllCerts = new TrustManager[] { easyTrustManager };

					// Install the all-trusting trust manager
					try {
						sslContext = SSLContext.getInstance("TLS");

						sslContext.init(null, trustAllCerts,
								new java.security.SecureRandom());

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return sslContext;
	}

	public static void trustAllHosts() {
		SSLContext sslContext = getSSLContext();
		if (sslContext != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());
		}
	}

}
