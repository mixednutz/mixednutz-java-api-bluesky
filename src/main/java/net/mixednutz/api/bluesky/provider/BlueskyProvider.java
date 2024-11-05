package net.mixednutz.api.bluesky.provider;

import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;

import net.mixednutz.api.BlueskyConfig.BlueskyConnectionProperties;
import net.mixednutz.api.bluesky.BlueskyFeedType;
import net.mixednutz.api.bluesky.client.BlueskyAdapter;
import net.mixednutz.api.core.provider.AbstractApiProvider;
import net.mixednutz.api.model.INetworkInfoSmall;
import net.mixednutz.api.provider.IOauth1Credentials;

public class BlueskyProvider extends AbstractApiProvider<BlueskyAdapter, IOauth1Credentials> {

	private final ConnectionFactory<Bluesky> connectionFactory;
	private final BlueskyConnectionProperties blueskyProps;
	
	public BlueskyProvider(ConnectionFactory<Bluesky> connectionFactory, 
			BlueskyConnectionProperties blueskyProps) {
		super(BlueskyAdapter.class, IOauth1Credentials.class);
		this.connectionFactory = connectionFactory;
		this.blueskyProps = blueskyProps;
	}

	@Override
	public BlueskyAdapter getApi(IOauth1Credentials creds) {
		return new BlueskyAdapter(
				createConnection(
						createConnectionData(creds)), blueskyProps);
	}
	
	protected ConnectionData createConnectionData(IOauth1Credentials creds) {
		/*
		 * For now we're just using username/password so we have no auth yet
		 */
		return new ConnectionData(creds.getProviderId(), null, null, null, null, 
				null, null, null, null);
	}
	
	protected Connection<Bluesky> createConnection(ConnectionData cd) {
		return connectionFactory.createConnection(cd);
	}

	@Override
	public INetworkInfoSmall getNetworkInfo() {
		return new BlueskyFeedType();
	}

	@Override
	public String getProviderId() {
		return connectionFactory.getProviderId();
	}

}
