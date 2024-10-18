package org.springframework.social.bluesky.api;

public interface Bluesky {
	
	String getHandle();
	
	Profile getProfile(String actor);
	
	RecordResponse<Post> createPost(String text);
	
	RecordResponse<Post> createPost(Post post);
	
	RecordResponse<Post> getPost(String uri);
	
	AtprotoOperations getAtprotoOperations();
	
}
