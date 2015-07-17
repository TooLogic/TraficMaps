package org.zarroboogs.maps.module;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;

import org.zarroboogs.maps.R;
import org.zarroboogs.maps.utils.LatLngUtils;

/**
 * Created by wangdiyuan on 15-7-17.
 */
public class GeoFenceModule {

    public static final String TAG = "GeoFenceModule";
    private Context mContext;
    private MapView mapView;
    private BaiduMap mBaiduMap;

    private LocationManagerProxy mLocationManagerProxy;//定位实例

    private PendingIntent mPendingIntent;

    public GeoFenceModule(MapView mapView){
        this.mContext = mapView.getContext().getApplicationContext();
        this.mapView = mapView;

        this.mBaiduMap = mapView.getMap();

        mLocationManagerProxy = LocationManagerProxy.getInstance(mContext);

        registerListener();
    }

    public void registerListener(){
        TTSController ttsController = TTSController.getInstance(mContext);
        ttsController.init();

//        ttsController.startSpeaking();


        IntentFilter fliter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
//        fliter.addAction(GEOFENCE_BROADCAST_ACTION);
//        mContext.registerReceiver(mGeoFenceReceiver, fliter);
//
//        Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
//        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);

    }


    public void onDestory() {
        // 销毁定位
        mLocationManagerProxy.removeGeoFenceAlert(mPendingIntent);
//        mLocationManagerProxy.removeUpdates(this);
        mLocationManagerProxy.destroy();
//        mContext.unregisterReceiver(mGeoFenceReceiver);

        TTSController ttsController = TTSController.getInstance(mContext);
        ttsController.destroy();
    }


    public void addGeoFence(com.baidu.mapapi.model.LatLng baiduLatLong) {
        com.baidu.mapapi.model.LatLng tmp = LatLngUtils.Baidu2Gaode(baiduLatLong);

        LatLng latLng = new LatLng(tmp.latitude, tmp.longitude);

        mLocationManagerProxy.removeGeoFenceAlert(mPendingIntent);

        //地理围栏使用时需要与定位请求方法配合使用
        // 设置地理围栏，位置、半径、超时时间、处理事件
        mLocationManagerProxy.addGeoFenceAlert(latLng.latitude,
                latLng.longitude, 1000, 1000 * 60 * 30, mPendingIntent);

        BitmapDescriptor mCameraBd = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
        OverlayOptions oo = new MarkerOptions().position(baiduLatLong).icon(mCameraBd).draggable(true);
        Marker marker = (Marker) (mBaiduMap.addOverlay(oo));
    }
}
