package com.opswat.metadefender.core.clientexample;

import com.opswat.metadefender.core.client.FileScanOptions;
import com.opswat.metadefender.core.client.MetadefenderCoreClient;
import com.opswat.metadefender.core.client.exceptions.MetadefenderClientException;
import com.opswat.metadefender.core.client.responses.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class Example {

	/**
	 * @param args command line args
	 */
	public static void main(String[] args) {

		Map<String, String> arguments = processArguments(args);

		String apiUrl =         arguments.get("-h");
		String apiUser =        arguments.get("-u");
		String apiUserPass =    arguments.get("-p");
		String action =         arguments.get("-a");
		String file =           arguments.get("-f");
		String hash =           arguments.get("-m");


		if(file != null && !file.isEmpty()) {

			if("scan".equals(action)) {
				scanFile(apiUrl, file);

			} else if("scan_sync".equals(action)) {
				try {
					scanFileSync(apiUrl, file);
				} catch (InterruptedException e) {
					System.out.println("Interrupted");
				} catch (ExecutionException e) {
					System.out.println("Exception during the operation " + e.getMessage());
				} catch (TimeoutException e) {
					System.out.println("Process timeout");
				}
			}
		}

		if(hash != null && !hash.isEmpty()) {
			fetchScanResultByHash(apiUrl, hash);
		}

		if("info".equals(action)) {
			showApiInfo(apiUrl, apiUser, apiUserPass);
		}


	}


	private static void scanFileSync(String apiUrl, String file) throws InterruptedException, ExecutionException, TimeoutException {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(apiUrl);

		try {
			InputStream inputStream = new FileInputStream(file);
			FileScanResult result = metadefenderCoreClient.scanFileSync(inputStream, new FileScanOptions().setFileName(getFileNameFromPath(file)), 200, 5000);
			System.out.println("File scan finished with result: " + result.process_info.result);

		} catch (MetadefenderClientException e) {
			System.out.println("Error during file scan: " + e.getDetailedMessage());
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file + " Exception: " + e.getMessage());
		}
	}

	private static void scanFile(String apiUrl, String file) {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(apiUrl);

		// This is optional: using custom HttpConnector
		metadefenderCoreClient.setHttpConnector(new CustomHttpConnector());

		try {
			InputStream inputStream = new FileInputStream(file);
			String dataId = metadefenderCoreClient.scanFile(inputStream, new FileScanOptions().setFileName(getFileNameFromPath(file)));
			System.out.println("File scan started. The data id is: " + dataId);

		} catch (MetadefenderClientException e) {
			System.out.println("Error during file scan: " + e.getDetailedMessage());
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + file + " Exception: " + e.getMessage());
		}
	}

	private static void fetchScanResultByHash(String apiUrl, String hash)  {
		MetadefenderCoreClient metadefenderCoreClient = new MetadefenderCoreClient(apiUrl);

		try {
			FileScanResult result = metadefenderCoreClient.fetchScanResultByHash(hash);
			System.out.println("Fetch result by file hash: " + result.process_info.result);

		} catch (MetadefenderClientException e) {
			System.out.println("Error during fetch scan by hash: " + e.getDetailedMessage());
		}

	}

	private static void showApiInfo(String apiUrl, String apiUser, String apiUserPass) {

		MetadefenderCoreClient metadefenderCoreClient;

		try {
			metadefenderCoreClient = new MetadefenderCoreClient(apiUrl, apiUser, apiUserPass);
			metadefenderCoreClient.setHttpConnector(new CustomHttpConnector());

			System.out.println("Metadefender client created. Session id is: " + metadefenderCoreClient.getSessionId());

		} catch (MetadefenderClientException e) {
			System.out.println("Cannot login to this API. Error message: " + e.getDetailedMessage());
			return;
		}

		try {
			License license = metadefenderCoreClient.getCurrentLicenseInformation();
			System.out.println("Licensed to: " + license.licensed_to);

		} catch (MetadefenderClientException e) {
			System.out.println("Cannot get license details: " + e.getDetailedMessage());
		}

		try {
			List<EngineVersion> result = metadefenderCoreClient.getEngineVersions();
			System.out.println("Fetched engine/database versions: " + result.size());

		} catch (MetadefenderClientException e) {
			System.out.println("Cannot get engine/database   versions: " + e.getDetailedMessage());
		}

		try {
			ApiVersion apiVersion = metadefenderCoreClient.getVersion();
			System.out.println("Api endpoint apiVersion: " + apiVersion.version);

		} catch (MetadefenderClientException e) {
			System.out.println("Cannot get api endpoint version: " + e.getDetailedMessage());
		}

		try {
			List<ScanRule> scanRules = metadefenderCoreClient.getAvailableScanRules();
			System.out.println("Available scan rules: " + scanRules.size());

		} catch (MetadefenderClientException e) {
			System.out.println("Cannot get available scan rules: " + e.getDetailedMessage());
		}

		try {
			metadefenderCoreClient.logout();
			System.out.println("Client successfully logged out.");

		} catch (MetadefenderClientException e) {
			System.out.println("Cannot log out: " + e.getDetailedMessage());
		}

	}







	////// Util methods


	public static void printUsage(String message) {
		if(message != null) {
			System.out.println(message);
		}

		System.out.println("\n\n\nExample usages: \n\n" +
				"  Example -h http://localhost:8008 -a scan -f fileToScan\n" +
				"  Example -h http://localhost:8008 -u yourUser -p yourPass -a info\n\n\n" +
				"\t  -h   host                    Required\n" +
				"\t  -u   username                Required if action is 'info'\n" +
				"\t  -p   password                Required if action is 'info'\n" +
				"\t  -a   action to do            Required (scan|scan_sync|info|hash)\n" +
				"\t  -f   path to file to scan    Required if action is (scan|scan_sync)" +
				"\t  -m   hash (md5|sha1|sha256)  Required if action is (hash)" +
				"\n\n\n");
	}

	/**
	 * Processing, and validating command line arguments
	 * @param args command line arguments
	 * @return processed parameters
	 */
	private static Map<String, String> processArguments(String[] args) {
		Map<String, String> parameters = new HashMap<>();

		List<String> switches = Arrays.asList("-h", "-u", "-p", "-a", "-f", "-m");

		for(String switchStr : switches) {
			int index = getSwitchIndex(args, switchStr);

			if(index >= 0) {
				if(args.length >= index + 1) {
					parameters.put(switchStr, args[index + 1]);
				} else {
					printUsage("Missing value for switch: " + switchStr);
					System.exit(1);
				}
			}
		}

		if(!parameters.containsKey("-h")) {
			printUsage("-h is required");
			System.exit(1);
		}
		if(!parameters.containsKey("-a")) {
			printUsage("-a is required");
			System.exit(1);
		}

		String action = parameters.get("-a");

		List<String> allowedActions = Arrays.asList("scan", "scan_sync", "info", "hash");

		if(!allowedActions.contains(action)) {
			printUsage("Invalid action: " + action);
			System.exit(1);
		}

		if("info".equals(action) ) {
			if(!parameters.containsKey("-u")) {
				printUsage("-u is required");
				System.exit(1);
			}
			if(!parameters.containsKey("-p")) {
				printUsage("-p is required");
				System.exit(1);
			}
		}
		if("scan".equals(action) || "scan_sync".equals(action)) {
			if(!parameters.containsKey("-f")) {
				printUsage("-f is required");
				System.exit(1);
			}
		}

		if("hash".equals(action)) {
			if(!parameters.containsKey("-m")) {
				printUsage("-m is required");
				System.exit(1);
			}
		}

		return parameters;
	}

	private static int getSwitchIndex(String[] args, String switchStr) {
		for(int i = 0; i < args.length; i++) {
			if(switchStr.equals(args[i])) {
				return i;
			}
		}
		return -1;
	}

	private static String getFileNameFromPath(String file) {
		String parts[];
		if(file.contains("/")) {
			parts = file.split("/");
		} else if(file.contains("\\")) {
			parts = file.split("\\\\");
		} else {
			return file;
		}

		if(parts.length > 1) {
			return parts[parts.length - 1];
		}

		return file;
	}

}
