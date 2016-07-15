package com.opswat.metscanClient;


import com.opswat.metadefender.core.client.MetadefenderCoreClient;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;
import com.opswat.metadefender.core.client.responses.FileScanResult;
import org.junit.Test;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.*;

public class FetchScanResultTests extends BaseWireMockTest {

	@Test
	public void success() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String existingDataId = "59f92cb3e3194c6381d3f8819a0d47ed";

		createStub("/file/"+existingDataId, "GET", 200, getJsonFromFile("/apiResponses/fetchScanResult/fetchScanResult_success.json"));


		FileScanResult result = metadefenderCoreClient.fetchScanResult(existingDataId);
		assertEquals(existingDataId, result.data_id);
		assertEquals("Allowed", result.process_info.result);
		assertEquals("Clean", result.scan_results.scan_all_result_a);
		assertNull(result.extracted_files);


		verify(getRequestedFor(urlMatching("/file/"+existingDataId))
		);
	}

	@Test
	public void success_withArchive() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String existingDataId = "fafb3a12b0d141909b3a3ba6b26e42c9";

		createStub("/file/"+existingDataId, "GET", 200, getJsonFromFile("/apiResponses/fetchScanResult/fetchScanResult_success_withArchive.json"));


		FileScanResult result = metadefenderCoreClient.fetchScanResult(existingDataId);
		assertEquals(existingDataId, result.data_id);
		assertEquals("Allowed", result.process_info.result);
		assertEquals("Clean", result.scan_results.scan_all_result_a);
		assertNotNull(result.extracted_files);
		assertEquals(2L, result.extracted_files.files_in_archive.size());


		verify(getRequestedFor(urlMatching("/file/"+existingDataId))
		);
	}

	@Test
	public void withNotFound() throws MetadefenderClientException, IOException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String nonExistingId = "61dffeaa728844adbf49eb090e4ece0e";

		createStub("/file/"+nonExistingId, "GET", 200, getJsonFromFile("/apiResponses/fetchScanResult/fetchScanResult_notFound.json"));


		boolean isException = false;
		try {
			metadefenderCoreClient.fetchScanResult(nonExistingId);
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);

		verify(getRequestedFor(urlMatching("/file/"+nonExistingId))
		);
	}

	@Test
	public void withError() {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String existingDataId = "61dffeaa728844adbf49eb090e4ece0e";

		createStub("/file/"+existingDataId, "GET", 500, getJsonFromFile("/apiResponses/errorJson.json"));


		boolean isException = false;
		try {
			metadefenderCoreClient.fetchScanResult(existingDataId);
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);

		verify(getRequestedFor(urlMatching("/file/"+existingDataId))
		);
	}

	@Test
	public void withoutDataId() {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		boolean isException = false;
		try {
			metadefenderCoreClient.fetchScanResult(null);
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);
	}

}
