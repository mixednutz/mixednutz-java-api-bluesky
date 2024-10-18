package org.springframework.social.bluesky.api.impl;

import java.time.Instant;

public class SessionObject {
	
	private final String accessJwt;
	private final String refreshJwt;
	private final Instant accessExpiresAt;
	private final Instant refreshExpiresAt;
	
	public SessionObject(String accessJwt, String refreshJwt,
			Instant accessExpiresAt, Instant refreshExpiresAt) {
		super();
		this.accessJwt = accessJwt;
		this.refreshJwt = refreshJwt;
		this.accessExpiresAt = accessExpiresAt;
		this.refreshExpiresAt = refreshExpiresAt;
	}
	
	public String getAccessJwt() {
		return accessJwt;
	}
	public String getRefreshJwt() {
		return refreshJwt;
	}
	
	public boolean isAccessExpired() {
		return Instant.now().isAfter(accessExpiresAt);
	}
	
	public boolean isRefreshExpired() {
		return Instant.now().isAfter(refreshExpiresAt);
	}
	
}
