package com.opswat.metscanClient;

import com.opswat.metadefender.core.client.MetadefenderCoreClient;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;
import com.opswat.metadefender.core.client.responses.FileScanResult;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class FetchScanResultByHashTests extends BaseWireMockTest {

	@Test
	public void success() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String existingHash = "e981b537cff14c3fbbba923d7a71ff2e";

		createStub("/hash/"+existingHash, "GET", 200, getJsonFromFile("/apiResponses/fetchScanResultByHash/fetchScanResultByHash_success.json"));

		FileScanResult result = metadefenderCoreClient.fetchScanResultByHash(existingHash);
		assertEquals(existingHash, result.data_id);
		assertEquals("Allowed", result.process_info.result);
		assertEquals("Clean", result.scan_results.scan_all_result_a);


		verify(getRequestedFor(urlMatching("/hash/"+existingHash))
		);
	}

	@Test
	public void withNotFound() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String nonExistingHash = "61dffeaa728844adbf49eb090e4ece0e";

		createStub("/hash/"+nonExistingHash, "GET", 200, getJsonFromFile("/apiResponses/fetchScanResultByHash/fetchScanResult_notFound.json"));


		boolean isException = false;
		try {
			metadefenderCoreClient.fetchScanResultByHash(nonExistingHash);
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);

		verify(getRequestedFor(urlMatching("/hash/"+nonExistingHash))
		);
	}

	@Test
	public void withError() {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String existingDataId = "61dffeaa728844adbf49eb090e4ece0e";

		createStub("/hash/"+existingDataId, "GET", 500, getJsonFromFile("/apiResponses/errorJson.json"));


		boolean isException = false;
		try {
			metadefenderCoreClient.fetchScanResultByHash(existingDataId);
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);

		verify(getRequestedFor(urlMatching("/hash/"+existingDataId))
		);
	}

	@Test
	public void withoutDataId() {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		boolean isException = false;
		try {
			metadefenderCoreClient.fetchScanResultByHash(null);
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);
	}

}
