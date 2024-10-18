package net.mixednutz.api.bluesky;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.mixednutz.api.core.model.NetworkInfoSmall;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BlueskyFeedType extends NetworkInfoSmall  {
	
	private static final String DISPLAY_NAME = "Bluesky";
	private static final String HOST_NAME = "bsky.com";
	private static final String ID = "bluesky";
	private static final String ICON_NAME = "bluesky";
	
	private static BlueskyFeedType instance;

	public BlueskyFeedType() {
		super();
		this.setDisplayName(DISPLAY_NAME);
		this.setHostName(HOST_NAME);
		this.setId(ID);
		this.setFontAwesomeIconName(ICON_NAME);
	}

	public static BlueskyFeedType getInstance() {
		if (instance==null) {
			instance = new BlueskyFeedType();
		}
		return instance;
	}

	@Override
	public String[] compatibleMimeTypes() {
		return new String[] {
				"text/plain", //text and links
				"image/*" //images
				};
	}

}
