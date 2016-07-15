package com.opswat.metscanClient;


import com.opswat.metadefender.core.client.FileScanOptions;
import com.opswat.metadefender.core.client.MetadefenderCoreClient;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;
import com.opswat.metadefender.core.client.responses.FileScanResult;
import org.junit.Test;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ScanFileTests extends BaseWireMockTest {


	@Test
	public void success() throws MetadefenderClientException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		createStub("/file", "POST", 200, getJsonFromFile("/apiResponses/scanFile/scanFile_success.json"));

		InputStream is = getClass().getResourceAsStream("/testScanFile.txt");

		String dataId = metadefenderCoreClient.scanFile(is, null);
		assertEquals("61dffeaa728844adbf49eb090e4ece0e", dataId);

		verify(postRequestedFor(urlMatching("/file"))
		);
	}

	@Test
	public void success_withDefaultUserAgent() throws MetadefenderClientException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());
		metadefenderCoreClient.setUserAgent("MyAgent");

		createStub("/file", "POST", 200, getJsonFromFile("/apiResponses/scanFile/scanFile_success.json"));

		InputStream is = getClass().getResourceAsStream("/testScanFile.txt");

		String dataId = metadefenderCoreClient.scanFile(is, null);
		assertEquals("61dffeaa728844adbf49eb090e4ece0e", dataId);

		verify(postRequestedFor(urlMatching("/file")).withHeader("user_agent", equalTo("MyAgent")));
	}

	@Test
	public void success_sync() throws MetadefenderClientException, InterruptedException, ExecutionException, TimeoutException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String existingDataId = "61dffeaa728844adbf49eb090e4ece0e";

		createStub("/file", "POST", 200, getJsonFromFile("/apiResponses/scanFile/scanFile_success.json"));
		createStub("/file/" + existingDataId, "GET", 200, getJsonFromFile("/apiResponses/fetchScanResult/fetchScanResult_success.json"));

		InputStream is = getClass().getResourceAsStream("/testScanFile.txt");
		FileScanResult result = metadefenderCoreClient.scanFileSync(is, new FileScanOptions().setFileName("fileName.txt"), 50, 4000);
		assertEquals("Allowed", result.process_info.result);
		assertEquals("Clean", result.scan_results.scan_all_result_a);
		assertNull(result.extracted_files);

		verify(postRequestedFor(urlMatching("/file")));
		verify(getRequestedFor(urlMatching("/file/" + existingDataId)));
	}

	@Test
	public void success_syncTimeout() throws MetadefenderClientException, InterruptedException, ExecutionException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		String existingDataId = "61dffeaa728844adbf49eb090e4ece0e";

		createStub("/file", "POST", 200, getJsonFromFile("/apiResponses/scanFile/scanFile_success.json"));
		createStub("/file/" + existingDataId, "GET", 200, getJsonFromFile("/apiResponses/fetchScanResult/fetchScanResult_inProgress.json"));

		boolean isException = false;
		InputStream is = getClass().getResourceAsStream("/testScanFile.txt");
		try {
			// it should be a timeout, because we return in progress response every time
			FileScanResult result = metadefenderCoreClient.scanFileSync(is, new FileScanOptions().setFileName("fileName.txt"), 50, 2000);
		} catch (TimeoutException e) {
			isException = true;
		}
		assertTrue(isException);

		verify(postRequestedFor(urlMatching("/file")));
		verify(getRequestedFor(urlMatching("/file/" + existingDataId)));
	}

	@Test
	public void successWithFileOptions() throws MetadefenderClientException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		createStub("/file", "POST", 200, getJsonFromFile("/apiResponses/scanFile/scanFile_success.json"));

		InputStream is = getClass().getResourceAsStream("/testScanFile.txt");

		String dataId = metadefenderCoreClient.scanFile(is,
				new FileScanOptions()
						.setUserAgent("Java client")
						.setFileName("file.txt")
						.setRule("Default Rule")
		);
		assertEquals("61dffeaa728844adbf49eb090e4ece0e", dataId);

		verify(postRequestedFor(urlMatching("/file")).withHeader("user_agent", equalTo("Java client")));
	}

	@Test
	public void withError() throws MetadefenderClientException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(getMockApiUrl());

		createStub("/file", "POST", 500, getJsonFromFile("/apiResponses/errorJson.json"));

		InputStream is = getClass().getResourceAsStream("/testScanFile.txt");

		boolean isException = false;
		try {
			metadefenderCoreClient.scanFile(is, null);
		} catch (MetadefenderClientException e) {
			isException = true;
		}
		assertTrue(isException);

		verify(postRequestedFor(urlMatching("/file"))
		);
	}


}
