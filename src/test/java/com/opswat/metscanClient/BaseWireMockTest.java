package com.opswat.metscanClient;


import com.github.tomakehurst.wiremock.client.RemoteMappingBuilder;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class BaseWireMockTest {

	public static final String TEST_SESSION_ID = "22c4b45a38a449628247c9d431b48fcd";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

	protected String getMockApiUrl() {
		return "http://localhost:" + wireMockRule.port();
	}


	protected void createStubForLogin() {
		createStub("/login", "POST", 200, "{\"session_id\":\""+TEST_SESSION_ID+"\"}");
	}

	protected void createStubForVersion() {
		createStub("/version", "GET", 200, getJsonFromFile("/apiResponses/version/getVersion_success.json"));
	}


	protected void createStub(String uri, String method, int status, String json) {
		RemoteMappingBuilder remoteMapping;
		if("GET".equals(method)) {
			remoteMapping = get(urlMatching(uri));
		} else if("POST".equals(method)) {
			remoteMapping = post(urlMatching(uri));
		} else {
			throw new RuntimeException("Unhandled method: " + method);
		}

		stubFor(remoteMapping
				.willReturn(aResponse()
						.withStatus(status)
						.withHeader("Content-Type", "application/json; charset=utf-8")
						.withBody(json)));
	}


	protected String getJsonFromFile(String fileName) {
		try {
			byte[] data = inputStreamToByteArray(getClass().getResourceAsStream(fileName));
			return new String(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] inputStreamToByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}


}
