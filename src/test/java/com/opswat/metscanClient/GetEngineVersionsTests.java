package com.opswat.metscanClient;


import com.opswat.metadefender.core.client.MetadefenderCoreClient;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;
import com.opswat.metadefender.core.client.responses.EngineVersion;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetEngineVersionsTests extends BaseWireMockTest {


	@Test
	public void success() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		createStub("/stat/engines", "GET", 200, getJsonFromFile("/apiResponses/getEngineVersions/getEngineVersions_success.json"));

		List<EngineVersion> result = metadefenderCoreClient.getEngineVersions();

		assertEquals(47, result.size());


		verify(getRequestedFor(urlMatching("/stat/engines"))
		);
	}

	@Test
	public void serverError() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		createStub("/stat/engines", "GET", 200, getJsonFromFile("/apiResponses/errorJson.json"));

		boolean isException = false;

		try {
			metadefenderCoreClient.getEngineVersions();
		} catch (MetadefenderClientException e) {
			isException = true;
		}

		assertTrue(isException);


		verify(getRequestedFor(urlMatching("/stat/engines"))
		);
	}

}
