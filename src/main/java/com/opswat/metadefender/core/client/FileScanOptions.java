package com.opswat.metadefender.core.client;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FileScanOptions {


	private Map<String, String> options = new HashMap<>();

	public Map<String, String> getOptions() {
		return options;
	}


	public FileScanOptions setFileName(String fileName) {
		this.options.put("filename", urlEncodeStr(fileName));
		return this;
	}

	public FileScanOptions setFilePath(String filePath) {
		this.options.put("filepath", filePath);
		return this;
	}

	public FileScanOptions setUserAgent(String userAgent) {
		this.options.put("user_agent", userAgent);
		return this;
	}

	public String getUserAgent() {
		return this.options.get("user_agent");
	}

	public FileScanOptions setRule(String rule) {
		this.options.put("rule", urlEncodeStr(rule));
		return this;
	}

	public FileScanOptions setArchivepwd(String archivepwd) {
		this.options.put("archivepwd", archivepwd);
		return this;
	}



	private String urlEncodeStr(String str) {
		try {
			String s = URLEncoder.encode(str, "UTF-8");
			// we have to use %20 for space encoding, instead of + sign
			return s.replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 not supported");
		}
	}

}
