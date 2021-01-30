package com.fscloud.lib_base.utils.gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author MrChen
 * @ProjectName :
 * @date :2021/1/20 16:34
 * @description:
 */
public class GPS implements LifecycleObserver {

    private Context context;
    private LocationManager locationManager;
    private static final String TAG = "GPS-Info";

    public GPS(Context context) {
        this.context = context;
        initLocationManager();
    }

    /**
     * 获取权限，并检查有无开户GPS
     */
    private void initLocationManager() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 转到手机设置界面，用户设置GPS
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        getProviders();
    }

    /**
     * 获取可定位的方式
     */
    private MyLocationListener myLocationListener;
    private String bestProvider;

    private void getProviders() {
        //获取定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String s : providers) {
            Log.e(TAG, s);
        }

        Criteria criteria = new Criteria();
        // 查询精度：高，Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精确
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 是否查询海拨：否
        criteria.setAltitudeRequired(true);
        // 是否查询方位角 : 否
        criteria.setBearingRequired(true);
        // 设置是否要求速度
        criteria.setSpeedRequired(false);
        // 电量要求：低
        //     criteria.setPowerRequirement(Criteria.ACCURACY_COARSE);

        bestProvider = locationManager.getBestProvider(criteria, false);  //获取最佳定位


    }

    public void startLocation(LocationSuccessListener success) {

        myLocationListener = new MyLocationListener(success);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }

        Log.e(TAG, "startLocation: ");
        if (locationManager == null) {
            Log.e(TAG, "locationManager为空");
        }
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(context, "请打开GPS和使用网络定位以提高精度", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
        //    locationManager.requestLocationUpdates(bestProvider, 10, 10, myLocationListener);

        locationManager.requestLocationUpdates("network", 1000, 1, myLocationListener);
        //    locationManager.requestLocationUpdates("gps", 10000, 1, myLocationListener);
    }

    public void stopLocation() {
        Log.e(TAG, "stopLocation: ");
        locationManager.removeUpdates(myLocationListener);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        stopLocation();
        context =null;
    }


    private class MyLocationListener implements LocationListener {

        LocationSuccessListener successListener;

        public MyLocationListener(LocationSuccessListener successListener) {
            this.successListener = successListener;
        }

        @Override
        public void onLocationChanged(Location location) {
            //定位时调用
            try {
                Log.e(TAG, "onLocationChanged");

                Log.e(TAG, "onLocationChanged" + new Gson().toJson(location));

                List<Address> addresses = new ArrayList<>();
                //经纬度转城市

                Geocoder geocoder = new Geocoder(context);
                try {
                    Log.e(TAG, "onLocationChanged  getLatitude =" + location.getLatitude());
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);


                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onLocationChanged__IOException");
                }
                for (Address address : addresses) {
                    //国家  CN
                    //                Log.e(TAG, address.getCountryCode() + "");
                    //                //国家
                    //                Log.e(TAG, address.getCountryName() + "");
                    //                //省，市，地址
                    //                Log.e(TAG, address.getAdminArea() + "");
                    //                Log.e(TAG, address.getLocality() + "");
                    //                Log.e(TAG, address.getFeatureName() + "");
                    //
                    //                //经纬度
                    //                Log.e(TAG, String.valueOf(address.getLatitude()) + "");
                    //                Log.e(TAG, String.valueOf(address.getLongitude()) + "");

                    successListener.getLocationData(address, new LocationCityInfo(address.getAdminArea(), address.getLocality(), address.getFeatureName(), address.getLatitude(), address.getLongitude()));
                    stopLocation();

                }

                /**
                 * 针对8.0系统  要在子线程拿数据
                 */
                if (addresses.size() == 0) {
                    try {
                        new Thread() {
                            @Override
                            public void run() {
                                //需要在子线程中处理的逻辑
                                if (location != null) {
                                    Address tempAddress = getAddress(context, location.getLatitude(), location.getLongitude());
                                    if (tempAddress == null) {
                                        stopLocation();
                                        interrupt();
                                        return;
                                    }
                                    successListener.getLocationData(tempAddress, new LocationCityInfo(tempAddress.getAdminArea(), tempAddress.getLocality(), tempAddress.getFeatureName(), tempAddress.getLatitude(), tempAddress.getLongitude()));
                                    stopLocation();
                                    interrupt();
                                }
                            }
                        }.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "定位监听抛异常 " + e.getLocalizedMessage() + "___" + e.getMessage());
                stopLocation();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //定位状态改变

            Log.e(TAG, "onStatusChanged_" + provider);

        }

        @Override
        public void onProviderEnabled(String provider) {
            //定位开启
            Log.e(TAG, "onProviderEnabled");

        }

        @Override
        public void onProviderDisabled(String provider) {
            //定位关闭
            Log.e(TAG, "onProviderDisabled");
            successListener.getLocationErr("定位关闭");
        }
    }

    /**
     * 根据经纬度获取地理位置
     * * @param context 上下文
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return {@link Address}
     */
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0)
                return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface LocationSuccessListener {

        void getLocationData(Address address, LocationCityInfo data);

        void getLocationErr(String err);

    }

    public class LocationCityInfo {


        public LocationCityInfo(String adminArea, String locality, String featureName, double lat, double lon) {
            this.adminArea = adminArea;
            this.locality = locality;
            this.featureName = featureName;
            this.lat = lat;
            this.lon = lon;
        }

        private String adminArea;//省
        private String locality;//市
        private String featureName;//地址
        private double lat;
        private double lon;

        public String getAdminArea() {
            return adminArea;
        }

        public void setAdminArea(String adminArea) {
            this.adminArea = adminArea;
        }

        public String getLocality() {
            return locality;
        }

        public void setLocality(String locality) {
            this.locality = locality;
        }

        public String getFeatureName() {
            return featureName == null ? "" : featureName;
        }

        public void setFeatureName(String featureName) {
            this.featureName = featureName;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        @Override
        public String toString() {
            return "LocationCityInfo{" +
                    "adminArea='" + adminArea + '\'' +
                    ", locality='" + locality + '\'' +
                    ", featureName='" + featureName + '\'' +
                    ", lat=" + lat +
                    ", lon=" + lon +
                    '}';
        }
    }
}
