package com.opswat.metscanClient;

import com.opswat.metadefender.core.client.MetadefenderCoreClient;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;
import com.opswat.metadefender.core.client.responses.ScanRule;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GetAvailableScanRulesTests extends BaseWireMockTest {


	@Test
	public void success() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		createStub("/file/rules", "GET", 200, getJsonFromFile("/apiResponses/getAvailableScanRules/getAvailableScanRules_success.json"));

		List<ScanRule> result = metadefenderCoreClient.getAvailableScanRules();

		assertEquals(6, result.size());

		verify(getRequestedFor(urlMatching("/file/rules"))
		);
	}

	@Test
	public void success_withNewUnknownFields() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		createStub("/file/rules", "GET", 200, getJsonFromFile("/apiResponses/getAvailableScanRules/getAvailableScanRules_withNewUnknownFieldsJson.json"));

		List<ScanRule> result = metadefenderCoreClient.getAvailableScanRules();

		assertEquals(2, result.size());

		verify(getRequestedFor(urlMatching("/file/rules"))
		);
	}

	@Test
	public void serverError() {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		createStub("/file/rules", "GET", 500, getJsonFromFile("/apiResponses/errorJson.json"));

		boolean isException = false;
		try {
			metadefenderCoreClient.getAvailableScanRules();
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);


		verify(getRequestedFor(urlMatching("/file/rules"))
		);
	}


}
