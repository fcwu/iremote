package com.doro.iremote;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

/**
 * Performs a blocking HTTP get request, without much flexibility.
 */
public class HttpRequestBlocking {

	private static String TAG = "HttpRequestBlocking";
	private String mUrl;
	private boolean mSuccess;
	private String mResult;
	private byte[] mBytes;

	// We set these for all requests.
	private static int mTimeout = 60000;

	static public void setTimeout(int timeout_ms) {
		mTimeout = timeout_ms;
	}

	/**
	 * Constructor.
	 */
	public HttpRequestBlocking(String url) {
			Log.d("HttpReq", "url = " + url);
			mUrl = url;
	}

	/**
	 * Returns whether the fetch resulted in a 200.
	 */
	public boolean success() {
		return mSuccess;
	}

	/**
	 * Returns the fetched content, or null if the fetch failed.
	 */
	public String response() {
		return mResult;
	}


	public byte[] responseBytes() {
		return mBytes;
	}

	public boolean doPost(String data, String cookie, String referer) {

		try {

			URL url = new URL(mUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setConnectTimeout(mTimeout);
			conn.setReadTimeout(mTimeout);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			((HttpURLConnection) conn).setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(true);
			HttpURLConnection.setFollowRedirects(true);
			conn.setInstanceFollowRedirects(true);

			conn.setRequestProperty(
					"User-agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) "
							+ "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
			conn.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			conn.setRequestProperty("Accept-Language",
					"zh-tw,en-us;q=0.7,en;q=0.3");
			conn.setRequestProperty("Accept-Charse",
					"Big5,utf-8;q=0.7,*;q=0.7");
			if (cookie != null)
				conn.setRequestProperty("Cookie", cookie);
			if (referer != null)
				conn.setRequestProperty("Referer", referer);

			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			if (data != null && data.length() > 0) {
				conn.setRequestProperty("Content-Length",
						String.valueOf(data.getBytes().length));
				java.io.DataOutputStream dos = new java.io.DataOutputStream(
						conn.getOutputStream());
				dos.writeBytes(data);
			}


			InputStream is = conn.getInputStream();
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int bytesRead;
			byte[] buffer = new byte[1024];

			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.flush();
			os.close();
			is.close();
			
			Log.d(TAG,
					String.format("finished request(size=%d, remote=%s)",
							os.size(), mUrl.toString()));
			mResult = os.toString();
			mBytes = os.toByteArray();

			mSuccess = conn.getResponseCode() == 200;
			
//			java.io.DataOutputStream dos = new java.io.DataOutputStream(
//					conn.getOutputStream());
//			dos.writeBytes(data);
//
//			java.io.BufferedReader rd = new java.io.BufferedReader(
//					new java.io.InputStreamReader(conn.getInputStream(),
//							charset));
//			String line;
//			while ((line = rd.readLine()) != null) {
//				System.out.println(line);
//			}
//
//			rd.close();
		} catch (java.io.IOException e) {
			mSuccess = false;
			Log.i(TAG, e.toString());
		} finally {
		}

		return mSuccess;
	}
	
	public boolean doGet(String cookie, String referer) {

		try {

			URL url = new URL(mUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setConnectTimeout(mTimeout);
			conn.setReadTimeout(mTimeout);
			conn.setDoOutput(false);
			conn.setDoInput(true);
			((HttpURLConnection) conn).setRequestMethod("GET");
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(true);
			HttpURLConnection.setFollowRedirects(true);
			conn.setInstanceFollowRedirects(true);

			conn.setRequestProperty(
					"User-agent",
					"Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) "
							+ "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
			if (cookie != null)
				conn.setRequestProperty("Cookie", cookie);
			if (referer != null)
				conn.setRequestProperty("Referer", referer);

			conn.setRequestProperty("Content-Type",
					"application/json");

			InputStream is = conn.getInputStream();
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int bytesRead;
			byte[] buffer = new byte[1024];

			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.flush();
			os.close();
			is.close();
			
			Log.d(TAG,
					String.format("finished request(size=%d, remote=%s)",
							os.size(), mUrl.toString()));
			mResult = os.toString();
			mBytes = os.toByteArray();

			mSuccess = conn.getResponseCode() == 200;
	
		} catch (java.io.IOException e) {
			mSuccess = false;
			Log.i(TAG, e.toString());
		} finally {
		}

		return mSuccess;
	}
}