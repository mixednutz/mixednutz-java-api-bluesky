package org.springframework.social.bluesky.connect;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.social.ServiceProvider;
import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.bluesky.api.impl.BlueskyTemplate;
import org.springframework.social.bluesky.api.impl.SessionObject;
import org.springframework.web.client.RestTemplate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class BlueskyServiceProvider implements ServiceProvider<Bluesky> {
	
	private static final Logger LOG = LoggerFactory.getLogger(BlueskyServiceProvider.class);

	private final RestTemplateBuilder restTemplateBuilder;
	private final RestTemplate restTemplate;
	private final String handle;
	private final String password;
	private final String pdsHost;
	
	public BlueskyServiceProvider(RestTemplateBuilder restTemplateBuilder, 
			String handle, String password, String pdsHost) {
		super();
		this.restTemplateBuilder = restTemplateBuilder;
		this.handle = handle;
		this.password = password;
		this.pdsHost = pdsHost;
		this.restTemplate = this.restTemplateBuilder.build();
	}
	
	public Bluesky getApi() {
		return new BlueskyTemplate(this.restTemplateBuilder.build(), this, handle, pdsHost);
	}
	
	public SessionObject createSessionObject() {
		CreateSessionResponseData response = createSession();
		
		DecodedJWT decodedRefreshJWT = JWT.decode(response.refreshJwt);
		DecodedJWT decodedAccessJWT = JWT.decode(response.accessJwt);
		
		Date refreshExpiresAt = decodedRefreshJWT.getExpiresAt();
		Date accessExpiresAt = decodedAccessJWT.getExpiresAt();
		
		LOG.info("Session created to {}. AccessJwt expires at {}, RefreshJwt expires at {}",
				pdsHost, accessExpiresAt, refreshExpiresAt);
		
		return new SessionObject(
				response.getAccessJwt(), response.getRefreshJwt(), 
				accessExpiresAt.toInstant(), refreshExpiresAt.toInstant());
	}
	
	public SessionObject refreshSessionObject(SessionObject session) {
		CreateSessionResponseData response = refreshSession(session);
		
		DecodedJWT decodedRefreshJWT = JWT.decode(response.refreshJwt);
		DecodedJWT decodedAccessJWT = JWT.decode(response.accessJwt);
		
		Date refreshExpiresAt = decodedRefreshJWT.getExpiresAt();
		Date accessExpiresAt = decodedAccessJWT.getExpiresAt();
		
		LOG.info("Session to {} refreshed. AccessJwt expires at {}, RefreshJwt expires at {}",
				pdsHost, accessExpiresAt, refreshExpiresAt);
		
		return new SessionObject(
				response.getAccessJwt(), response.getRefreshJwt(), 
				accessExpiresAt.toInstant(), refreshExpiresAt.toInstant());
	}
	
	CreateSessionResponseData createSession() {
		ResponseEntity<CreateSessionResponseData> response = restTemplate.postForEntity(
				pdsHost+"/xrpc/com.atproto.server.createSession", 
				new LoginRequestData(handle, password), CreateSessionResponseData.class);
		return response.getBody();
	}
	
	CreateSessionResponseData refreshSession(SessionObject session) {
		RequestEntity<Void> requestEntity = RequestEntity
				.post(pdsHost+"/xrpc/com.atproto.server.refreshSession")
				.header("Authorization", "Bearer "+session.getRefreshJwt())
				.build();
		ResponseEntity<CreateSessionResponseData> response = restTemplate.exchange(
				requestEntity, CreateSessionResponseData.class);
		return response.getBody();
	}
	
	static class LoginRequestData {
		private final String identifier;
		private final String password;
		
		public LoginRequestData(String identifier, String password) {
			super();
			this.identifier = identifier;
			this.password = password;
		}
		
		public String getIdentifier() {
			return identifier;
		}
		
		public String getPassword() {
			return password;
		}
		
	}
	
	static class CreateSessionResponseData {
		
		private String accessJwt;
		private String refreshJwt;
		private String handle;
		private String did;
		
		public String getAccessJwt() {
			return accessJwt;
		}
		public void setAccessJwt(String accessJwt) {
			this.accessJwt = accessJwt;
		}
		public String getRefreshJwt() {
			return refreshJwt;
		}
		public void setRefreshJwt(String refreshJwt) {
			this.refreshJwt = refreshJwt;
		}
		public String getHandle() {
			return handle;
		}
		public void setHandle(String handle) {
			this.handle = handle;
		}
		public String getDid() {
			return did;
		}
		public void setDid(String did) {
			this.did = did;
		}
		
				
	}
	

}
