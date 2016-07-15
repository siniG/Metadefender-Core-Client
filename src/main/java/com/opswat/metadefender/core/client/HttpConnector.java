package com.opswat.metadefender.core.client;

import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class HttpConnector {

	private static final int BUFFER_SIZE = 0x1000; // 4K


	public HttpResponse sendRequest(String url, String method, InputStream inputStream, Map<String, String> headers) throws MetadefenderClientException {

		HttpURLConnection conn = null;

		try {
			URL u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();

			conn.setRequestMethod(method);
			conn.setRequestProperty("Accept", "application/json");

			if(headers != null && !headers.isEmpty()) {
				for(String key :headers.keySet()) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}

			if(inputStream != null) {
				conn.setDoOutput(true);

				OutputStream os = conn.getOutputStream();
				copyStream(inputStream, os);
				os.flush();
			}

			BufferedReader br;

			int responseCode = conn.getResponseCode();

			if (200 <= responseCode && responseCode <= 299) {
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			} else {
				// If the response code isn't 200 or 2xx, we have to use getErrorStream() instead of getInputStream().
				br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
			}


			String response = "";
			String output;

			while ((output = br.readLine()) != null) {
				response += output;
			}

			conn.disconnect();

			return new HttpResponse(response, responseCode);

		} catch (IOException e) {
			throw new MetadefenderClientException("Cannot connect to: " + url + " " + e.getMessage());
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}

	}


	public HttpConnector.HttpResponse sendRequest(String url, String method) throws MetadefenderClientException {
		return sendRequest(url, method, null, new HashMap<String, String>());
	}

	public HttpConnector.HttpResponse sendRequest(String url, String method, byte[] body) throws MetadefenderClientException {
		return sendRequest(url, method, new ByteArrayInputStream(body), new HashMap<String, String>());
	}




	protected static void copyStream(InputStream from, OutputStream to) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];

		int bytesRead;
		do {
			bytesRead = from.read(buf);
			if (bytesRead > -1) {
				to.write(buf, 0, bytesRead);
			}
		} while (bytesRead > -1);
	}




	public static class HttpResponse {
		String response;
		int responseCode;

		public HttpResponse(String response, int responseCode) {
			this.response = response;
			this.responseCode = responseCode;
		}
	}
}
