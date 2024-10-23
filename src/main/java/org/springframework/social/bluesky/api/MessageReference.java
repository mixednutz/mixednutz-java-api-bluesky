package org.springframework.social.bluesky.api;

public class MessageReference {

	private String uri;
	private String cid;
	
	public MessageReference(String uri, String cid) {
		super();
		this.uri = uri;
		this.cid = cid;
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
	
}
