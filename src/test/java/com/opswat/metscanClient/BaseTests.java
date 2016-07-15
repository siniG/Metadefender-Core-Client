package com.opswat.metscanClient;


import com.opswat.metadefender.core.client.MetadefenderCoreClient;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;
import com.opswat.metadefender.core.client.responses.ApiVersion;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class BaseTests extends BaseWireMockTest {


	@Test
	public void loginTest() throws MetadefenderClientException {
		createStubForLogin();

		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl(), "admin", "admin");
		assertEquals("Expected login session", TEST_SESSION_ID, metadefenderCoreClient.getSessionId());


		verify(postRequestedFor(urlMatching("/login"))
				.withRequestBody( equalToJson("{\"user\":\"admin\",\"password\":\"admin\"}")));
	}


	@Test
	public void validateCurrentSessionTest_loggedIn() throws MetadefenderClientException {
		createStubForLogin();

		createStubForVersion();

		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());
		metadefenderCoreClient.login("admin", "admin");

		boolean isLoggedIn = metadefenderCoreClient.validateCurrentSession();

		assertTrue(isLoggedIn);

	}

	@Test
	public void validateCurrentSessionTest_notLoggedIn_1() throws MetadefenderClientException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		boolean isLoggedIn = metadefenderCoreClient.validateCurrentSession();

		assertFalse(isLoggedIn);
	}

	@Test
	public void validateCurrentSessionTest_notLoggedIn_sessionExpired() throws MetadefenderClientException {
		createStubForLogin();

		createStub("/version", "GET", 403,
				getJsonFromFile("/apiResponses/version/getVersion_accessDenied.json"));

		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());
		metadefenderCoreClient.login("admin", "admin");

		boolean isLoggedIn = metadefenderCoreClient.validateCurrentSession();

		assertFalse(isLoggedIn);
	}




	@Test
	public void getVersionTest() throws MetadefenderClientException {
		createStubForLogin();

		createStubForVersion();


		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl(), "admin", "admin");

		ApiVersion apiVersion = metadefenderCoreClient.getVersion();

		assertEquals("Expected api version equals", "4.3.0.256", apiVersion.version);
		assertEquals("Expected product version equals", "MSCL", apiVersion.product_id);



		verify(getRequestedFor(urlMatching("/version"))
				.withHeader("apikey", equalTo(TEST_SESSION_ID)));
	}

	@Test
	public void logoutTest() throws MetadefenderClientException {
		createStubForLogin();

		createStub("/logout", "POST", 200,
				getJsonFromFile("/apiResponses/logout/logout_success.json"));


		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl(), "admin", "admin");

		assertNotNull(metadefenderCoreClient.getSessionId());

		metadefenderCoreClient.logout();

		assertNull(metadefenderCoreClient.getSessionId());


		verify(postRequestedFor(urlMatching("/logout"))
				.withHeader("apikey", equalTo(TEST_SESSION_ID)));
	}


	@Test
	public void malformedUrlTest() throws MetadefenderClientException {
		boolean isException = false;
		try {
			new MetadefenderCoreClient("htt://mal formed url:" + wireMockRule.port(), "admin", "admin");
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);
	}



}
