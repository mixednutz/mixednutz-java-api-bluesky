package net.mixednutz.api.bluesky.client;

import java.util.Collections;
import java.util.List;

import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.connect.Connection;

import net.mixednutz.api.BlueskyConfig.BlueskyConnectionProperties;
import net.mixednutz.api.client.GroupClient;
import net.mixednutz.api.client.MixednutzClient;
import net.mixednutz.api.client.RoleClient;
import net.mixednutz.api.client.TimelineClient;
import net.mixednutz.api.client.UserClient;
import net.mixednutz.api.model.IExternalRole;

public class BlueskyAdapter implements MixednutzClient {
	
	private final Connection<Bluesky> conn;
	private final BlueskyConnectionProperties blueskyProps;
	private PostAdapter postAdapter;
	private RoleClient roleClient = new RoleClient() {

		@Override
		public boolean hasRoles() {
			return false;
		}

		@Override
		public List<? extends IExternalRole> getAvailableRoles() {
			return null;
		}

		@Override
		public List<? extends IExternalRole> getRolesAssigned() {
			return Collections.emptyList();
		}};
	
	public BlueskyAdapter(Connection<Bluesky> conn, BlueskyConnectionProperties blueskyProps) {
		super();
		this.conn = conn;
		this.blueskyProps = blueskyProps;
		this.initSubApis();
	}

	@Override
	public GroupClient getGroupClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostAdapter getPostClient() {
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
	
	@Override
	public RoleClient getRoleClient() {
		return roleClient;
	}
	
	private void initSubApis() {
		postAdapter = new PostAdapter(conn, blueskyProps);
	}

}
