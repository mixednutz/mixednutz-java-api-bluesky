package org.springframework.social.bluesky.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlobResponse {

	@JsonProperty("$type")
	private String type;
	private Ref ref;
	private String mimeType;
	private long size;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Ref getRef() {
		return ref;
	}
	public void setRef(Ref ref) {
		this.ref = ref;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
}
