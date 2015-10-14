package com.shenma.yueba.baijia.modle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gyj
 * @version 创建时间：2015-5-10 下午5:55:12 程序的简单说明
 */

public class CityInfoBackBean extends BaseRequest implements Serializable{
	private CityListItembean data;

	public CityListItembean getData() {
		return data;
	}

	public void setData(CityListItembean data) {
		this.data = data;
	}
}
