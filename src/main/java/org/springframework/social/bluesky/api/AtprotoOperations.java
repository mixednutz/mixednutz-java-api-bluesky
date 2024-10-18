package org.springframework.social.bluesky.api;

public interface AtprotoOperations {

	<R> RecordResponse<R> createRecord(String repo, String collection, R record);
	
	<R> RecordResponse<R> getRecord(String repo, String collection, String recordKey, Class<R> recordType);
	
}
