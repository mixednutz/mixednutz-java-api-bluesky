package net.mixednutz.api.bluesky.client;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.social.bluesky.api.BlobResponse;
import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.bluesky.api.Post;
import org.springframework.social.bluesky.api.Post.External;
import org.springframework.social.bluesky.api.Post.ExternalEmbed;
import org.springframework.social.bluesky.api.RecordResponse;
import org.springframework.social.connect.Connection;
import org.springframework.web.client.RestTemplate;

import net.mixednutz.api.BlueskyConfig.BlueskyConnectionProperties;
import net.mixednutz.api.bluesky.client.EmbedClient.ExtractedMetadata;
import net.mixednutz.api.bluesky.model.BlueskyPostElement;
import net.mixednutz.api.bluesky.model.PostForm;
import net.mixednutz.api.client.PostClient;
import net.mixednutz.api.model.ITimelineElement;

public class PostAdapter implements PostClient<PostForm> {
	
	private final Connection<Bluesky> conn;
	private EmbedClient embedClient;
	
	public PostAdapter(Connection<Bluesky> conn, BlueskyConnectionProperties blueskyProps) {
		super();
		this.conn = conn;
		this.embedClient = new EmbedClient(new RestTemplate(), blueskyProps.getEmbedMetadataServiceUrl());
	}

	@Override
	public PostForm create() {
		return new PostForm(this);
	}
	
	public ExternalEmbed createExternalEmbed(String url) {
		ExtractedMetadata embed = embedClient.lookupEmbedMetadata(url);
		
		External external = new External();
		external.setUri(url);
		external.setTitle(embed.getTitle());
		external.setThumb(createThumb(embed.getImageUrl()));
		external.setDescription(embed.getDescription());
		
		return new ExternalEmbed(external);
	}
	
	private Post.Thumb createThumb(String imageUrl) {
		//download image url into bytes
		ResponseEntity<byte[]> response = embedClient.downloadFile(imageUrl);
		
		//reupoad
		BlobResponse blob = conn.getApi().uploadBlob(
				response.getBody(), 
				response.getHeaders().getContentType().toString());
		
		return new Post.Thumb(blob);
	}
	
	@Override
	public ITimelineElement postToTimeline(PostForm post) {
		Post record = post.toPostRecord();
		RecordResponse<Post> postResponse = conn.getApi().createPost(record);
		return new BlueskyPostElement(postResponse);
	}

	@Override
	public Map<String, Object> referenceDataForPosting() {
		return Collections.emptyMap();
	}
	

}
