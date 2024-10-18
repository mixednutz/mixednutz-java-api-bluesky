package org.springframework.social.bluesky.connect;

import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.bluesky.api.Profile;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

public class BlueskyApiAdapter implements ApiAdapter<Bluesky>{

	@Override
	public boolean test(Bluesky api) {
		try {
			api.getProfile(api.getHandle());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}

	@Override
	public void setConnectionValues(Bluesky api, ConnectionValues values) {
		try {
			Profile profile = api.getProfile(api.getHandle());
			values.setProviderUserId(api.getHandle());
			values.setDisplayName(profile.getDisplayName());
			values.setImageUrl(profile.getAvatar());
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public UserProfile fetchUserProfile(Bluesky api) {
		try {
			Profile profile = api.getProfile(api.getHandle());
			return new UserProfileBuilder()
					.setName(profile.getDisplayName())
					.setUsername(profile.getHandle())
					.build();
		} catch (Exception e) {
			return null;
		}	
	}

	@Override
	public void updateStatus(Bluesky api, String message) {
		try {
			api.createPost(message);
		} catch (Exception e) {
		}	
	}

}
