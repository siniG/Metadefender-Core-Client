package com.opswat.metadefender.core.client.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.opswat.metadefender.core.client.MetadefenderCoreClient;

import java.util.Date;

public class EngineScanDetail {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MetadefenderCoreClient.DATE_FORMAT_MILLIS_RESOLUTION)
	public Date def_time;
	public String location;
	public Integer scan_result_i;
	public Long scan_time;
	public String threat_found;
}
