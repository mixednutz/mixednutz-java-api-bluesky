package org.springframework.social.bluesky.api;

import net.mixednutz.api.bluesky.BlueskyFeedType;
import net.mixednutz.api.core.model.TimelineElement;
import net.mixednutz.api.model.ITimelineElement;

public class BlueskyPostElement extends TimelineElement implements ITimelineElement {
	
	private static TimelineElement.Type TYPE = new TimelineElement.Type(){
		BlueskyFeedType feedType = BlueskyFeedType.getInstance();
		@Override
		public String getName() {return "post";}
		@Override
		public String getNamespace() {return feedType.getHostName();}
		@Override
		public String getId() {return feedType.getId()+"_"+getName();}
		};

	public BlueskyPostElement() {
		super();
	}

	public BlueskyPostElement(RecordResponse<Post> response) {
		super();
		this.setType(TYPE);
		//this.setPostedByUser(new TwitterUser(status.getUser()));
		this.setPostedOnDate(response.getValue().getCreatedAt());
		this.setDescription(response.getValue().getText());
		this.setUri(response.getUri());
	}
		
		
}
