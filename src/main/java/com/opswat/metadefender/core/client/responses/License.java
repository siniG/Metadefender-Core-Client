package com.opswat.metadefender.core.client.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.opswat.metadefender.core.client.MetadefenderCoreClient;

import java.util.Date;

public class License {

	public String deployment;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MetadefenderCoreClient.DATE_FORMAT_DAY_RESOLUTION)
	public Date expiration;
	public Integer days_left;
	public String licensed_engines;
	public String licensed_to;
	public Integer max_agent_count;
	public Boolean online_activated;
	public String product_id;
	public String product_name;
}
