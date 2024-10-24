package net.mixednutz.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.social.bluesky.connect.BlueskyConnectionFactory;

import net.mixednutz.api.bluesky.provider.BlueskyProvider;

@Profile("bluesky")
@Configuration
@ConfigurationProperties(prefix="mixednutz.social")
public class BlueskyConfig {
	
	private BlueskyConnectionProperties bluesky= new BlueskyConnectionProperties();
	
	@Bean
	public BlueskyConnectionFactory blueskyConnectionFactory(RestTemplateBuilder restTemplateBuilder) {
		return new BlueskyConnectionFactory(restTemplateBuilder, 
				bluesky.handle, bluesky.password, bluesky.pdsHost);
	}
	@Bean
	public BlueskyProvider blueskyService(RestTemplateBuilder restTemplateBuilder) {
		return new BlueskyProvider(blueskyConnectionFactory(restTemplateBuilder));
	}
	
	public BlueskyConnectionProperties getBluesky() {
		return bluesky;
	}
	public void setBluesky(BlueskyConnectionProperties bluesky) {
		this.bluesky = bluesky;
	}

	public static class BlueskyConnectionProperties {
		
		private String handle;
		private String password;
		private String pdsHost;
		
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
		
	}

}
