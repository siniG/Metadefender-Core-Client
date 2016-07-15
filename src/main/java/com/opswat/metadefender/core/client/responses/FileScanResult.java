package com.opswat.metadefender.core.client.responses;


public class FileScanResult {

	public String data_id;
	public FileInfo file_info;
	public ProcessInfo process_info;
	public ScanResults scan_results;

	/**
	 * Can be null if the scanned file is not an archive file.
	 */
	public ExtractedFiles extracted_files;

	public boolean isScanFinished() {
		return process_info.progress_percentage == 100;
	}
}
