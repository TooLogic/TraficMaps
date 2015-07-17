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
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";
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
        fliter.addAction(GEOFENCE_BROADCAST_ACTION);
        mContext.registerReceiver(mGeoFenceReceiver, fliter);

        Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION);
        mPendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);


        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次
        //在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 2000, 15, new AMapLocationListener() {
                    @Override
                    public void onLocationChanged(AMapLocation aMapLocation) {
                        Log.d(TAG    ,"onLocationChanged");
                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d(TAG    ,"onLocationChanged");
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                        Log.d(TAG    ,"onStatusChanged");
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                        Log.d(TAG    ,"onProviderEnabled");
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        Log.d(TAG    ,"onProviderDisabled");
                    }
                });
    }


    public void onDestory() {
        // 销毁定位
        mLocationManagerProxy.removeGeoFenceAlert(mPendingIntent);
//        mLocationManagerProxy.removeUpdates(this);
        mLocationManagerProxy.destroy();
        mContext.unregisterReceiver(mGeoFenceReceiver);

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

    private BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG    ,"onReceive");
            TTSController ttsController = TTSController.getInstance(mContext);
            // 接受广播
            if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                Bundle bundle = intent.getExtras();
                // 根据广播的status来确定是在区域内还是在区域外
                int status = bundle.getInt("status");
                if (status == 0) {
                    Toast.makeText(mContext, "不在区域", Toast.LENGTH_SHORT).show();
                    ttsController.playText("不在区域");
                } else {
                    Toast.makeText(mContext, "在区域内", Toast.LENGTH_SHORT).show();
                    ttsController.playText("在区域内");
                }
            }

        }
    };
}
