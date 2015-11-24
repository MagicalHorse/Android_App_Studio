package com.shenma.yueba.baijia.modle.newmodel;

import com.shenma.yueba.baijia.modle.BaseRequest;
import com.shenma.yueba.baijia.modle.IndexItemData;

public class IndexBackBean extends BaseRequest{
	public IndexItemData getData() {
		return data;
	}

	public void setData(IndexItemData data) {
		this.data = data;
	}

	IndexItemData data=new IndexItemData();
}
