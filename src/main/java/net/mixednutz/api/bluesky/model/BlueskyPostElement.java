package net.mixednutz.api.bluesky.model;

import org.springframework.social.bluesky.api.MessageReference;
import org.springframework.social.bluesky.api.Post;
import org.springframework.social.bluesky.api.RecordResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		this.setReference(reference(response));
		this.setUri(response.getUri());
	}
	
	private String reference(RecordResponse<Post> response)  {
		ObjectMapper mapper = new ObjectMapper();
		MessageReference ref = new MessageReference(response.getUri(), response.getCid());
		try {
			return mapper.writeValueAsString(ref);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
		
		
}
