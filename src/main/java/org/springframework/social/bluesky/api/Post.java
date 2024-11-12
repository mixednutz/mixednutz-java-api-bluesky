package org.springframework.social.bluesky.api;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post {
	
	@JsonProperty("$type")
	private String type;
	private String text;
	private ZonedDateTime createdAt;
	private List<Facet> facets;
	private Reply reply;
	private Embed embed;
	
	public static class Facet {
		private Index index;
		private List<Feature> features;
		public Facet(Index index, List<Feature> features) {
			super();
			this.index = index;
			this.features = features;
		}
		public Index getIndex() {
			return index;
		}
		public void setIndex(Index index) {
			this.index = index;
		}
		public List<Feature> getFeatures() {
			return features;
		}
		public void setFeatures(List<Feature> features) {
			this.features = features;
		}
	}
	
	public static class Index {
		private int byteStart;
		private int byteEnd;
		public Index(int byteStart, int byteEnd) {
			super();
			this.byteStart = byteStart;
			this.byteEnd = byteEnd;
		}
		public int getByteStart() {
			return byteStart;
		}
		public void setByteStart(int byteStart) {
			this.byteStart = byteStart;
		}
		public int getByteEnd() {
			return byteEnd;
		}
		public void setByteEnd(int byteEnd) {
			this.byteEnd = byteEnd;
		}
	}
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Feature {
		@JsonProperty("$type")
		private String type;
		private String did; //app.bsky.richtext.facet#mention
		private String uri; //app.bsky.richtext.facet#link
		private String tag; //app.bsky.richtext.facet#tag
		
		public Feature(String type, String did, String uri, String tag) {
			super();
			this.type = type;
			this.did = did;
			this.uri = uri;
			this.tag = tag;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDid() {
			return did;
		}
		public void setDid(String did) {
			this.did = did;
		}
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		
	}
	
	public static class Reply {
		/**
		 * reference to the root of the entire thread.
		 */
		private MessageReference root;
		/**
		 * reference to the immediate message this is replying to
		 */
		private MessageReference parent;
		
		public Reply(MessageReference root, MessageReference parent) {
			super();
			this.root = root;
			this.parent = parent;
		}
		public MessageReference getRoot() {
			return root;
		}
		public void setRoot(MessageReference root) {
			this.root = root;
		}
		public MessageReference getParent() {
			return parent;
		}
		public void setParent(MessageReference parent) {
			this.parent = parent;
		}
		
	}
	
	public static class Embed {
		@JsonProperty("$type")
		private final String type;

		public Embed(String type) {
			super();
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}
	
	public static class RecordEmbed extends Embed {
		
		public static final String TYPE = "app.bsky.embed.record";

		private MessageReference record;
		
		public RecordEmbed() {
			super(TYPE);
		}

		public MessageReference getRecord() {
			return record;
		}

		public void setRecord(MessageReference record) {
			this.record = record;
		}
				
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ExternalEmbed extends Embed {
		
		public static final String TYPE = "app.bsky.embed.external";

		private External external;
		
		public ExternalEmbed() {
			super(TYPE);
		}

		public ExternalEmbed(External external) {
			this();
			this.external = external;
		}

		public External getExternal() {
			return external;
		}

		public void setExternal(External external) {
			this.external = external;
		}	
		
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class External {
		private String uri;
		private String title;
		private String description;
		private Thumb thumb;
		
		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Thumb getThumb() {
			return thumb;
		}

		public void setThumb(Thumb thumb) {
			this.thumb = thumb;
		}
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Thumb {
		@JsonProperty("$type")
		private String type;
		private Ref ref;
		private String mimeType;
		private long size;
		
		public Thumb() {
		}
		public Thumb(BlobResponse blob) {
			this.type = blob.getType();
			this.ref = new Ref(blob.getRef());
			this.mimeType = blob.getMimeType();
			this.size = blob.getSize();
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Ref getRef() {
			return ref;
		}
		public void setRef(Ref ref) {
			this.ref = ref;
		}
		public String getMimeType() {
			return mimeType;
		}
		public void setMimeType(String mimeType) {
			this.mimeType = mimeType;
		}
		public long getSize() {
			return size;
		}
		public void setSize(long size) {
			this.size = size;
		}
		
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ZonedDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(ZonedDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<Facet> getFacets() {
		return facets;
	}

	public void setFacets(List<Facet> facets) {
		this.facets = facets;
	}

	public Reply getReply() {
		return reply;
	}

	public void setReply(Reply reply) {
		this.reply = reply;
	}

	public Embed getEmbed() {
		return embed;
	}

	public void setEmbed(Embed embed) {
		this.embed = embed;
	}
	
	

}
