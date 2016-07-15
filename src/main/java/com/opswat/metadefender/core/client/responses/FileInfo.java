package com.opswat.metadefender.core.client.responses;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.opswat.metadefender.core.client.MetadefenderCoreClient;

import java.util.Date;

public class FileInfo {

	public String display_name;
	public Long file_size;
	public String file_type;
	public String file_type_description;
	public String md5;
	public String sha1;
	public String sha256;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = MetadefenderCoreClient.DATE_FORMAT_MILLIS_RESOLUTION)
	public Date upload_timestamp;
}
