package org.springframework.social.bluesky.api.impl;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.social.bluesky.api.AtprotoOperations;
import org.springframework.social.bluesky.api.BlobResponse;
import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.bluesky.api.Post;
import org.springframework.social.bluesky.api.Profile;
import org.springframework.social.bluesky.api.RecordResponse;
import org.springframework.social.bluesky.connect.BlueskyServiceProvider;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BlueskyTemplate implements Bluesky {
	
	private final RestOperations rest;
	private final BlueskyServiceProvider serviceProvider;
	private final String handle;
	private final String pdsHost;
	
	private SessionObject session;
	
	private final AtprotoOperations atproto;
	
	public BlueskyTemplate(RestOperations restTemplate, BlueskyServiceProvider serviceProvider, String handle, String pdsHost) {
		super();
		this.rest = restTemplate;
		this.serviceProvider = serviceProvider;
		this.handle = handle;
		this.pdsHost = pdsHost;
		this.atproto = new AtprotoTemplate(this, restTemplate, pdsHost);
	}

	@Override
	public AtprotoOperations getAtprotoOperations() {
		return atproto;
	}

	@Override
	public String getHandle() {
		return handle;
	}
	
	public SessionObject session() {
		return session;
	}

	@Override
	public Profile getProfile(String actor) {
		URI uri = UriComponentsBuilder.fromHttpUrl(getBaseUrl()+"/xrpc/app.bsky.actor.getProfile")
			.queryParam("actor", actor)
			.build().toUri();
		
		authenticate();
		
		RequestEntity<Void> requestEntity = RequestEntity.get(uri)
			.header("Authorization", "Bearer "+session.getAccessJwt())
			.build();
		return rest.exchange(requestEntity, Profile.class).getBody();
	}
	
	@Override
	public RecordResponse<Post> createPost(String text) {
		Post post = new Post();
		post.setType("app.bsky.feed.post");
		post.setText(text);
		post.setCreatedAt(ZonedDateTime.now());
		
		authenticate();
		
		return atproto.createRecord(handle, "app.bsky.feed.post", post);
	}
		
	@Override
	public RecordResponse<Post> createPost(Post post) {
		authenticate();
		
		return atproto.createRecord(handle, "app.bsky.feed.post", post);
	}
	
	public BlobResponse uploadBlob(byte[] data, String mimeType) {
		authenticate();
		
		return atproto.uploadBlob(data, mimeType);
	}

	@Override
	public RecordResponse<Post> getPost(String uri) {
		return atproto.getRecord(handle, "app.bsky.feed.post", 
				getRecordKeyFromRecordUri(uri), Post.class);
	}
	
	private String getRecordKeyFromRecordUri(String uri) {
		return uri.substring(uri.lastIndexOf("/")+1);
	}

	/**
	 * this is only here for testing purposes
	 * @return
	 */
	String getTimeline() {
		RequestEntity<Void> requestEntity = RequestEntity
				.get(getBaseUrl()+"/xrpc/app.bsky.feed.getTimeline")
				.header("Authorization", "Bearer "+session.getAccessJwt())
				.build();
		
		@SuppressWarnings("rawtypes")
		ResponseEntity<LinkedHashMap> response = rest.exchange(requestEntity, LinkedHashMap.class);
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response.getBody());
			System.out.println(json);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	protected void authenticate() {
		if (session==null || session.isRefreshExpired()) {
			session = serviceProvider.createSessionObject();
		} else if (session.isAccessExpired()) {
			session = serviceProvider.refreshSessionObject(session);
		} 
	}
	
	protected String getBaseUrl() {
		if (session!=null) {
			//authenticated
			return pdsHost;
		} else {
			return "https://public.api.bsky.app";
		}
	}
	
	static class CreateRecordRequest {
		String repo;
		String collection;
		Object record;
		
		public CreateRecordRequest(String repo, String collection, Object record) {
			super();
			this.repo = repo;
			this.collection = collection;
			this.record = record;
		}

		public String getRepo() {
			return repo;
		}

		public void setRepo(String repo) {
			this.repo = repo;
		}

		public String getCollection() {
			return collection;
		}

		public void setCollection(String collection) {
			this.collection = collection;
		}

		public Object getRecord() {
			return record;
		}

		public void setRecord(Post record) {
			this.record = record;
		}
		
	}
	
	static class CreateRecordResponse {
		String uri;
		String cid;
		
		public CreateRecordResponse(String uri, String cid) {
			super();
			this.uri = uri;
			this.cid = cid;
		}
		public CreateRecordResponse() {
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
		
	}
	
	
}
