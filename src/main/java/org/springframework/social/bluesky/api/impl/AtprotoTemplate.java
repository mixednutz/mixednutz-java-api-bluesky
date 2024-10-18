package org.springframework.social.bluesky.api.impl;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.social.bluesky.api.AtprotoOperations;
import org.springframework.social.bluesky.api.Post;
import org.springframework.social.bluesky.api.RecordResponse;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

public class AtprotoTemplate implements AtprotoOperations {
	
	private final RestOperations rest;
	private final String pdsHost;
	private final BlueskyTemplate bluesky;
		
	public AtprotoTemplate(BlueskyTemplate bluesky, RestOperations restTemplate, String pdsHost) {
		super();
		this.bluesky = bluesky;
		this.rest = restTemplate;
		this.pdsHost = pdsHost;
	}
	
	protected String getBaseUrl() {
		if (bluesky.session()!=null) {
			//authenticated
			return pdsHost;
		} else {
			return "https://public.api.bsky.app";
		}
	}

	@Override
	public <R> RecordResponse<R> createRecord(String repo, String collection, R record) {
		CreateRecordResponse response = createRecordInternal(repo, collection, record);
		return new RecordResponse<>(response.uri, response.cid, record);
	}

	CreateRecordResponse createRecordInternal(String repo, String collection, Object value) {
		RequestEntity<CreateRecordRequest> requestEntity = RequestEntity
				.post(getBaseUrl()+"/xrpc/com.atproto.repo.createRecord")
				.header("Authorization", "Bearer "+bluesky.session().getAccessJwt())
				.body(new CreateRecordRequest(repo, collection, value));
			
		return rest.exchange(requestEntity, CreateRecordResponse.class).getBody();
	}
	
	@Override
	public <R> RecordResponse<R> getRecord(String repo, String collection, String recordKey, Class<R> recordType) {
		URI uri = UriComponentsBuilder.fromHttpUrl(getBaseUrl()+"/xrpc/com.atproto.repo.getRecord")
				.queryParam("repo", repo)
				.queryParam("collection", collection)
				.queryParam("rkey", recordKey)
				.build().toUri();
		
		RequestEntity<Void> requestEntity = null;
		if (bluesky.session()!=null) {
			requestEntity = RequestEntity
					.get(uri)
					.header("Authorization", "Bearer "+bluesky.session().getAccessJwt())
					.build();
		} else {
			requestEntity = RequestEntity
					.get(uri).build();
		}

		return rest.exchange(requestEntity, new ParameterizedTypeReference<RecordResponse<R>>() {}).getBody();
	}
		
	static class CreateRecordRequest {
		private String repo;
		private String collection;
		private Object record;
		
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
		private String uri;
		private String cid;
		
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
