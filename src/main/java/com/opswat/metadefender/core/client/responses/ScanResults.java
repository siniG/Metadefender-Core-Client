package com.opswat.metadefender.core.client.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.opswat.metadefender.core.client.MetadefenderCoreClient;

import java.util.Date;
import java.util.Map;

public class ScanResults {

	public String data_id;
	public Integer progress_percentage;
	public String scan_all_result_a;
	public Integer scan_all_result_i;

	public Map<String, EngineScanDetail> scan_details;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MetadefenderCoreClient.DATE_FORMAT_MILLIS_RESOLUTION)
	public Date start_time;
	public Integer total_avs;
	public Long total_time;
}
