package net.mixednutz.api.bluesky.client;

import java.util.Collections;
import java.util.Map;

import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.bluesky.api.Post;
import org.springframework.social.bluesky.api.RecordResponse;
import org.springframework.social.connect.Connection;

import net.mixednutz.api.bluesky.model.BlueskyPostElement;
import net.mixednutz.api.bluesky.model.PostForm;
import net.mixednutz.api.client.PostClient;
import net.mixednutz.api.model.ITimelineElement;

public class PostAdapter implements PostClient<PostForm> {
	
	private final Connection<Bluesky> conn;
	
	public PostAdapter(Connection<Bluesky> conn) {
		super();
		this.conn = conn;
	}

	@Override
	public PostForm create() {
		return new PostForm();
	}

	@Override
	public ITimelineElement postToTimeline(PostForm post) {
		Post record = post.toPostRecord();
		RecordResponse<Post> postResponse = conn.getApi().createPost(record);
		return new BlueskyPostElement(postResponse);
	}

	@Override
	public Map<String, Object> referenceDataForPosting() {
		return Collections.emptyMap();
	}

}
