package com.opswat.metadefender.core.client.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opswat.metadefender.core.client.MetadefenderCoreClient;

import java.util.Date;

public class EngineVersion {

	public Boolean active;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MetadefenderCoreClient.DATE_FORMAT_MILLIS_RESOLUTION)
	public Date def_time;
	public Integer download_progress;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MetadefenderCoreClient.DATE_FORMAT_MILLIS_RESOLUTION)
	public Date download_time;
	public String eng_id;
	public String eng_name;
	public String eng_type;
	public String eng_ver;
	public String engine_type;
	public String state;
	public String type;

}
