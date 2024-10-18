package org.springframework.social.bluesky.api;

public class RecordResponse<R> {

	private String uri;
	private String cid;
	private R value;
	
	public RecordResponse(String uri, String cid, R value) {
		super();
		this.uri = uri;
		this.cid = cid;
		this.value = value;
	}

	public RecordResponse() {
		super();
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public R getValue() {
		return value;
	}

	public void setValue(R value) {
		this.value = value;
	}
	
}
