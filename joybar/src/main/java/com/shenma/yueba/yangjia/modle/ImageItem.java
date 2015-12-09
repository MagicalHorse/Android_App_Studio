package com.shenma.yueba.yangjia.modle;

import java.util.List;

public class ImageItem {

	private String id;
	private String sortorder;
	private String url;
	private String ImageUrl;
	private List<TagsBean> Tags;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSortorder() {
		return sortorder;
	}

	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	public List<TagsBean> getTags() {
		return Tags;
	}

	public void setTags(List<TagsBean> tags) {
		Tags = tags;
	}
}
