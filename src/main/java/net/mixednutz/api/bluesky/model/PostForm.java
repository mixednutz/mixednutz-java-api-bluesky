package net.mixednutz.api.bluesky.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.social.bluesky.api.Post;
import org.springframework.social.bluesky.api.Post.ExternalEmbed;
import org.springframework.social.bluesky.api.Post.Facet;
import org.springframework.social.bluesky.api.Post.Feature;
import org.springframework.social.bluesky.api.Post.Index;
import org.springframework.social.bluesky.api.Post.Reply;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mixednutz.api.bluesky.client.PostAdapter;
import net.mixednutz.api.model.IPost;

public class PostForm implements IPost {
	
	private static final int MAX_TEXT_SIZE = 300;

	String text;
	
	Reply inReplyTo;
		
	//Post Builder
	String textPart;
	String urlPart;
	String[] tagsPart;
	
	PostAdapter postAdapter;
	
	public PostForm() {
		super();
	}
	
	public PostForm(PostAdapter postAdapter) {
		this.postAdapter = postAdapter;
	}

	public PostForm(String text) {
		super();
		this.text = text;
	}
	
	private int countUrlAndTagsLength() {
		StringBuffer urlandtags = new StringBuffer()
				.append(" ").append(urlPart);
		if (tagsPart!=null) {
			for (String tag: tagsPart) {
				if (tag.startsWith("#")) {
					urlandtags.append(" ").append(tag);
				} else {
					urlandtags.append(" #").append(tag);
				}
			}
		}
		return MAX_TEXT_SIZE - urlandtags.length();
	}
	
	public Post toPostRecord() {
		Post post = new Post();
		post.setType("app.bsky.feed.post");
		if (text!=null) {
			post.setText(text);
		} else if (textPart!=null && urlPart==null) {
			post.setText(textPart);
		} else {
			List<Facet> facets = new ArrayList<>();
			
			//Shorten text
			final int NEW_MAX_SIZE = countUrlAndTagsLength();
			
			StringBuffer buffer = new StringBuffer();
			if (textPart.length()>NEW_MAX_SIZE) {
				buffer.append(textPart.substring(0, NEW_MAX_SIZE-3))
					.append("...");
			} else {
				buffer.append(textPart);
			}
			
			facets.add(new Facet(
					new Index(buffer.length()+1, buffer.length()+urlPart.length()+2), 
					List.of(new Feature("app.bsky.richtext.facet#link",null,urlPart,null))));
			buffer.append(" ").append(urlPart);
			
			try {
				ExternalEmbed embed = postAdapter.createExternalEmbed(urlPart);
				post.setEmbed(embed);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (tagsPart!=null) {
				for (String tag: tagsPart) {
					if (tag.startsWith("#")) {
						facets.add(new Facet(
								new Index(buffer.length()+1, buffer.length()+tag.length()+2), 
								List.of(new Feature("app.bsky.richtext.facet#tag",null,null,tag))));
						buffer.append(" ").append(tag);
					} else {
						facets.add(new Facet(
								new Index(buffer.length()+1, buffer.length()+tag.length()+3), 
								List.of(new Feature("app.bsky.richtext.facet#tag",null,null,tag))));
						buffer.append(" #").append(tag);
					}
				}
			}
			post.setText(buffer.toString());
			post.setFacets(facets);
		}
		if (inReplyTo!=null) {
			post.setReply(inReplyTo);
		}
		post.setCreatedAt(ZonedDateTime.now());
		return post;
	}

	@Override
	public void setInReplyTo(Serializable inReplyToId) {
		if (inReplyToId instanceof String) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				inReplyTo = mapper.readValue((String)inReplyToId, Reply.class);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		if (inReplyToId instanceof Reply) {
			inReplyTo = (Reply)inReplyToId;
		}
	}

	@Override
	public void setTags(String[] tags) {
		this.tagsPart = tags;
	}
	
	public void setComposeBody(String text) {
		setText(text);
	}

	@Override
	public void setText(String text) {
		this.textPart = text;
		
	}

	@Override
	public void setUrl(String url) {
		this.urlPart = url;
	}
	
}
