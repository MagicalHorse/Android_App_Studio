package com.shenma.yueba.refreshservice;

import com.shenma.yueba.baijia.modle.CityListItembean;

import java.util.ArrayList;
import java.util.List;

import interfaces.CityChangeRefreshInter;

public class CityChangeRefreshService {

	private List<CityChangeRefreshInter> mList = new ArrayList<CityChangeRefreshInter>();

	public void addToList(CityChangeRefreshInter inter) {
		if (!mList.contains(inter)) {
			mList.add(inter);
		}
	}

	public void removeFromList(CityChangeRefreshInter inter) {
		if (mList.contains(inter)) {
			mList.remove(mList.indexOf(inter));
		}
	}

	public void refreshList(CityListItembean bean) {
		for (int i = 0; i < mList.size(); i++) {
			mList.get(i).refresh(bean);
		}
	}
}
