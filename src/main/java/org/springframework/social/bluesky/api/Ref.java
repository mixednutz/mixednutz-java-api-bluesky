package org.springframework.social.bluesky.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ref {
	@JsonProperty("$link")
	private String link;

	public Ref() {
	}
	
	public Ref(Ref copy) {
		this.link = copy.link;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
