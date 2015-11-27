package com.shenma.yueba.baijia.modle;

public class StoreItem {

	private String StoreId;
	private String StoreName;
	private String Logo;
	private String Location;
	private String CityId;
	private String CityName;
	private double Lon;
	private double Lat;
	private String StoreLogo;
	private String StoreLocation;

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public String getCityId() {
		return CityId;
	}

	public void setCityId(String cityId) {
		CityId = cityId;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}


	public double getLon() {
		return Lon;
	}

	public void setLon(double lon) {
		Lon = lon;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public String getStoreId() {
		return StoreId;
	}

	public void setStoreId(String storeId) {
		StoreId = storeId;
	}

	public String getStoreName() {
		return StoreName;
	}

	public void setStoreName(String storeName) {
		StoreName = storeName;
	}

	public String getStoreLogo() {
		return StoreLogo;
	}

	public void setStoreLogo(String storeLogo) {
		StoreLogo = storeLogo;
	}

	public String getStoreLocation() {
		return StoreLocation;
	}

	public void setStoreLocation(String storeLocation) {
		StoreLocation = storeLocation;
	}
}
