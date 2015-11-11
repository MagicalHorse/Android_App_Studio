package com.shenma.yueba.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;

import com.shenma.yueba.application.MyApplication;
import com.shenma.yueba.config.PerferneceConfig;
import com.shenma.yueba.interfaces.LocationBackListner;

public class LocationUtil {

	public static void getLocation(Context ctx,LocationBackListner listner) {
		 double latitude = 0.0;
		 double longitude = 0.0;
		MyApplication.getInstance().showMessage(ctx,"定位中...");
		LocationManager locationManager = (LocationManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(!isOPen(ctx)){
			openGPS(ctx);
		}
			if (location != null && 0 != location.getLatitude()
					&& 0 != location.getLongitude()) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				PerferneceUtil.setString(PerferneceConfig.LATITUDE, latitude + "");
				PerferneceUtil.setString(PerferneceConfig.LONGITUDE, longitude + "");

				Log.i("location", latitude + "----" + longitude);
				listner.callBack(true);
			}else{
				Location locationNetWork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (locationNetWork != null && locationNetWork.getLatitude() != 0
						&& locationNetWork.getLongitude() != 0) {
					latitude = locationNetWork.getLatitude(); // 经度
					longitude = locationNetWork.getLongitude(); // 纬度
					PerferneceUtil.setString(PerferneceConfig.LATITUDE, latitude + "");
					PerferneceUtil.setString(PerferneceConfig.LONGITUDE, longitude + "");
					Log.i("location", latitude + "----" + longitude);
					listner.callBack(true);
				}else{
					listner.callBack(false);
				}
			}
		return;
		}



	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isOPen(final Context context) {
		LocationManager locationManager
				= (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}



	/**
	 * 强制帮用户打开GPS
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (PendingIntent.CanceledException e) {
			e.printStackTrace();
		}
	}

}
