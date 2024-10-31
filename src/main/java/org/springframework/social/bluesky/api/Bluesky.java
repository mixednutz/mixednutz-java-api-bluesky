package org.springframework.social.bluesky.api;

public interface Bluesky {
	
	String getHandle();
	
	Profile getProfile(String actor);
	
	RecordResponse<Post> createPost(String text);
	
	RecordResponse<Post> createPost(Post post);
	
	BlobResponse uploadBlob(byte[] data, String mimeType);
	
	RecordResponse<Post> getPost(String uri);
	
	AtprotoOperations getAtprotoOperations();
	
}
