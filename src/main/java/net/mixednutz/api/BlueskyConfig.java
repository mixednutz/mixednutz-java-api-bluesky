package net.mixednutz.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.bluesky.connect.BlueskyConnectionFactory;
import org.springframework.social.connect.web.CredentialsCallback;
import org.springframework.social.connect.web.CredentialsInterceptor;

import net.mixednutz.api.bluesky.provider.BlueskyProvider;
import net.mixednutz.api.provider.IOauth1Credentials;

@Profile("bluesky")
@Configuration
@ConfigurationProperties(prefix="mixednutz.social")
@ComponentScan("net.mixednutz.bluesky")
public class BlueskyConfig {
	
	private BlueskyConnectionProperties bluesky= new BlueskyConnectionProperties();
	
	@Bean
	public BlueskyConnectionFactory blueskyConnectionFactory(RestTemplateBuilder restTemplateBuilder) {
		return new BlueskyConnectionFactory(restTemplateBuilder, 
				bluesky.handle, bluesky.password, bluesky.pdsHost);
	}
	@Bean
	public BlueskyProvider blueskyService(RestTemplateBuilder restTemplateBuilder) {
		return new BlueskyProvider(blueskyConnectionFactory(restTemplateBuilder), getBluesky());
	}
	
	@Bean
	public CredentialsInterceptor<Bluesky, IOauth1Credentials> blueskyCredentialsInterceptor(CredentialsCallback callback) {
		return new BlueskyCredentialsInterceptor(callback);
	}
	
	public BlueskyConnectionProperties getBluesky() {
		return bluesky;
	}
	public void setBluesky(BlueskyConnectionProperties bluesky) {
		this.bluesky = bluesky;
	}
	
	public static class BlueskyCredentialsInterceptor extends CredentialsInterceptor<Bluesky, IOauth1Credentials> {

		public BlueskyCredentialsInterceptor(CredentialsCallback callback) {
			super(Bluesky.class, IOauth1Credentials.class, callback);
		}
		
	}

	public static class BlueskyConnectionProperties {
		
		private String handle;
		private String password;
		private String pdsHost;
		private String embedMetadataServiceUrl;
		
		public String getHandle() {
			return handle;
		}
		public void setHandle(String handle) {
			this.handle = handle;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPdsHost() {
			return pdsHost;
		}
		public void setPdsHost(String pdsHost) {
			this.pdsHost = pdsHost;
		}
		public String getEmbedMetadataServiceUrl() {
			return embedMetadataServiceUrl;
		}
		public void setEmbedMetadataServiceUrl(String embedMetadataServiceUrl) {
			this.embedMetadataServiceUrl = embedMetadataServiceUrl;
		}
				
	}

}
