package net.mixednutz.api.bluesky.client;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.social.bluesky.api.BlobResponse;
import org.springframework.social.bluesky.api.Bluesky;
import org.springframework.social.bluesky.api.Post;
import org.springframework.social.bluesky.api.RecordResponse;
import org.springframework.social.bluesky.api.Post.External;
import org.springframework.social.bluesky.api.Post.ExternalEmbed;
import org.springframework.social.connect.Connection;
import org.springframework.web.client.RestTemplate;

import net.mixednutz.api.bluesky.client.OembedClient.Oembed;
import net.mixednutz.api.bluesky.model.BlueskyPostElement;
import net.mixednutz.api.bluesky.model.PostForm;
import net.mixednutz.api.client.PostClient;
import net.mixednutz.api.model.ITimelineElement;

public class PostAdapter implements PostClient<PostForm> {
	
	private final Connection<Bluesky> conn;
	OembedClient oembedClient = new OembedClient(new RestTemplate());
	
	public PostAdapter(Connection<Bluesky> conn) {
		super();
		this.conn = conn;
	}

	@Override
	public PostForm create() {
		return new PostForm(this);
	}
	
	public ExternalEmbed createExternalEmbed(String url) {
		//TODO get oembed from meta tags instead
		Oembed oembed = oembedClient.lookupOembed(url);
		
		External external = new External();
		external.setUri(url);
		external.setTitle(oembed.getTitle());
		external.setThumb(createThumb(oembed.getThumbnailUrl()));
		external.setDescription("N/A");
		
		return new ExternalEmbed(external);
	}
	
	private Post.Thumb createThumb(String imageUrl) {
		//download image url into bytes
		ResponseEntity<byte[]> response = oembedClient.downloadFile(imageUrl);
		
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
