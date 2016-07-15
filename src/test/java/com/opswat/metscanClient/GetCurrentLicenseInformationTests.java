package com.opswat.metscanClient;


import com.opswat.metadefender.core.client.MetadefenderCoreClient;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;
import com.opswat.metadefender.core.client.responses.License;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class GetCurrentLicenseInformationTests extends BaseWireMockTest {


	@Test
	public void success() throws MetadefenderClientException, IOException {
		createStubForLogin();

		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl(), "admin", "admin");

		createStub("/admin/license", "GET", 200, getJsonFromFile("/apiResponses/getCurrentLicenseInformation/getCurrentLicenseInformation_success.json"));

		License result = metadefenderCoreClient.getCurrentLicenseInformation();
		assertEquals(3740L, result.days_left.longValue());
		assertEquals("OPSWAT, Inc.", result.licensed_to);
		assertEquals(10L, result.max_agent_count.longValue());


		verify(getRequestedFor(urlMatching("/admin/license"))
		);
	}


	@Test
	public void success_withNewUnknownFields() throws MetadefenderClientException {
		createStubForLogin();

		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl(), "admin", "admin");

		createStub("/admin/license", "GET", 200, getJsonFromFile("/apiResponses/getCurrentLicenseInformation/getCurrentLicenseInformation_withNewUnknownFields.json"));

		License result = metadefenderCoreClient.getCurrentLicenseInformation();
		assertEquals(3740L, result.days_left.longValue());
		assertEquals("OPSWAT, Inc.", result.licensed_to);
		assertEquals(10L, result.max_agent_count.longValue());


		verify(getRequestedFor(urlMatching("/admin/license"))
		);
	}

	@Test
	public void serverError() throws MetadefenderClientException, IOException {
		createStubForLogin();

		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl(), "admin", "admin");

		createStub("/admin/license", "GET", 500, getJsonFromFile("/apiResponses/errorJson.json"));

		boolean isException = false;
		try {
			metadefenderCoreClient.getCurrentLicenseInformation();
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);


		verify(getRequestedFor(urlMatching("/admin/license"))
		);
	}


	@Test
	public void apiRedirectTest() throws MetadefenderClientException {createStubForLogin();
		createStubForLogin();

		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl(), "admin", "admin");


		stubFor(get(urlMatching("/admin/license"))
				.willReturn(aResponse()
						.withStatus(302)
						.withHeader("Content-Type", "application/json; charset=utf-8")
						.withHeader("Location", "/admin/licenseRedirected"))); // simulating redirect response

		// the redirected resource
		stubFor(get(urlMatching("/admin/licenseRedirected"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json; charset=utf-8")
						.withBody(getJsonFromFile("/apiResponses/getCurrentLicenseInformation/getCurrentLicenseInformation_success.json"))));


		License result = metadefenderCoreClient.getCurrentLicenseInformation();
		assertEquals(3740L, result.days_left.longValue());


		verify(getRequestedFor(urlMatching("/admin/license")));
		verify(getRequestedFor(urlMatching("/admin/licenseRedirected")));
	}
}
