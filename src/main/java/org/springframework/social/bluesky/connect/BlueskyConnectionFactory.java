package org.springframework.social.bluesky.connect;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.AbstractConnection;

public class BlueskyConnectionFactory extends ConnectionFactory<Bluesky> {

	public BlueskyConnectionFactory(RestTemplateBuilder restTemplateBuilder, String handle, String password, String pdsHost) {
		super("bluesky", new BlueskyServiceProvider(restTemplateBuilder, handle, password, pdsHost), new BlueskyApiAdapter());
	}
	
	public Connection<Bluesky> createConnection() {
		return new BlueskyConnection(getProviderId(), null,
				(BlueskyServiceProvider)getServiceProvider(), 
				this.getApiAdapter());
	}

	@Override
	public Connection<Bluesky> createConnection(ConnectionData data) {
		return new BlueskyConnection(data, 
				(BlueskyServiceProvider)getServiceProvider(), 
				this.getApiAdapter());
	}
	
	
	static class BlueskyConnection extends AbstractConnection<Bluesky>{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private transient final BlueskyServiceProvider serviceProvider;
		
		private transient Bluesky api;

		public BlueskyConnection(String providerId, String providerUserId,
				BlueskyServiceProvider serviceProvider, ApiAdapter<Bluesky> apiAdapter) {
			super(apiAdapter);
			this.serviceProvider = serviceProvider;
			initApi();
			initKey(providerId, providerUserId);
		}

		public BlueskyConnection(ConnectionData data, 
				BlueskyServiceProvider serviceProvider, ApiAdapter<Bluesky> apiAdapter) {
			super(data, apiAdapter);
			this.serviceProvider = serviceProvider;
			initApi();
		}

		@Override
		public Bluesky getApi() {
			return api;
		}

		@Override
		public ConnectionData createData() {
			synchronized (getMonitor()) {
				return new ConnectionData(getKey().getProviderId(), getKey().getProviderUserId(), getDisplayName(), getProfileUrl(), getImageUrl(), null, null, null, null);
			}
		}
		
		private void initApi() {
			api = serviceProvider.getApi();
		}
		
	}

}
