package com.opswat.metadefender.core.client.exceptions;


public class MetadefenderClientException extends Exception {

	public Integer responseCode = null;


	public MetadefenderClientException(String s, int responseCode) {
		super(s);
		this.responseCode = responseCode;
	}

	public MetadefenderClientException(String s) {
		super(s);
	}

	public String getDetailedMessage() {
		String mess = this.getMessage();

		if(responseCode != null) {
			mess += " [code: " + this.responseCode + "]";
		}

		return mess;
	}
}
