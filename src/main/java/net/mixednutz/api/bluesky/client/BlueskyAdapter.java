package net.mixednutz.api.bluesky.client;

import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.connect.Connection;

import net.mixednutz.api.bluesky.model.PostForm;
import net.mixednutz.api.client.GroupClient;
import net.mixednutz.api.client.MixednutzClient;
import net.mixednutz.api.client.PostClient;
import net.mixednutz.api.client.TimelineClient;
import net.mixednutz.api.client.UserClient;

public class BlueskyAdapter implements MixednutzClient {
	
	private final Connection<Bluesky> conn;
	PostAdapter postAdapter;

	public BlueskyAdapter(Connection<Bluesky> conn) {
		super();
		this.conn = conn;
		this.initSubApis();
	}

	@Override
	public GroupClient getGroupClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostClient<PostForm> getPostClient() {
		return postAdapter;
	}

	@Override
	public TimelineClient<?> getTimelineClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserClient<?> getUserClient() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void initSubApis() {
		postAdapter = new PostAdapter(conn);
	}

}
