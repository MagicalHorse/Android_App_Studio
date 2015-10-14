package com.shenma.yueba.util;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.shenma.yueba.constants.Constants;
import com.shenma.yueba.inter.LocationBackListner;

public class LocationUtil {

	private double latitude = 0.0;
	private double longitude = 0.0;
	LocationManager locationManager;
	private Context ctx;
	public LocationUtil(Context ctx) {
		this.ctx = ctx;
		locationManager = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public void getLocation(LocationBackListner listner) {
		Toast.makeText(ctx,"定位中...",Toast.LENGTH_SHORT).show();
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(!ToolsUtil.isOPen(ctx)){
			ToolsUtil.openGPS(ctx);
		}
			if (location != null && 0 != location.getLatitude()
					&& 0 != location.getLongitude()) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				SharedUtil.setStringPerfernece(ctx, Constants.LATITUDE, latitude + "");
				SharedUtil.setStringPerfernece(ctx, Constants.LONGITUDE, longitude + "");
				Log.i("location", latitude + "----" + longitude);
				listner.callBack(true);
			}else{
				Location locationNetWork = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (locationNetWork != null && locationNetWork.getLatitude() != 0
						&& locationNetWork.getLongitude() != 0) {
					latitude = locationNetWork.getLatitude(); // 经度
					longitude = locationNetWork.getLongitude(); // 纬度
					SharedUtil.setStringPerfernece(ctx, Constants.LATITUDE, latitude + "");
					SharedUtil.setStringPerfernece(ctx, Constants.LONGITUDE, longitude + "");
					Log.i("location", latitude + "----" + longitude);
					listner.callBack(true);
				}else{
					listner.callBack(false);
				}
			}
		return;
		}

}
