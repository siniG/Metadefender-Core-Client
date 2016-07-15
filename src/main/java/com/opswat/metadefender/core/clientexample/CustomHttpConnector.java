package com.opswat.metadefender.core.clientexample;

import com.opswat.metadefender.core.client.HttpConnector;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class CustomHttpConnector extends HttpConnector {

	@Override
	public HttpResponse sendRequest(String url, String method, InputStream inputStream, Map<String, String> headers) throws MetadefenderClientException {

		// custom code for sending requests
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();

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

			if (200 <= conn.getResponseCode() && conn.getResponseCode() <= 299) {
				br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			} else {
				// if the server responds with 4xx or 5xx status code, we have to use conn.getErrorStream()
				br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
			}


			String response = "";
			String output;

			while ((output = br.readLine()) != null) {
				response += output;
			}

			conn.disconnect();

			return new HttpResponse(response, conn.getResponseCode());

		} catch (IOException e) {
			throw new MetadefenderClientException("Cannot connect to: " + url + " " + e.getMessage());
		}
	}
}
