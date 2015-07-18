package org.zarroboogs.maps.module;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import org.zarroboogs.maps.debug.Debuger;
import org.zarroboogs.maps.utils.CommUtils;
import org.zarroboogs.maps.utils.FileUtils;

/**
 * Created by wangdiyuan on 15-7-16.
 */
public class LocationModule {

    private static final String TAG = "LocationModule";
    private Context mContext;
    private LocationClient mLocationClient;
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
    public MyLocationListenner myListener = new MyLocationListenner();

    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;
    private MapView mMapView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private MySensorEventListener mEventListener = new MySensorEventListener();
    private BDLocation mBDLocation;
    private float mDevicesDirection = 0f;

    private static final String LOCATION_MODE = "location_mode";

    public LocationModule(Context context) {
        this.mContext = context;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        mLocationClient = new LocationClient(mContext);
    }

    public void startLocation(MapView mapView) {
        this.mMapView = mapView;
        this.mBaiduMap = mMapView.getMap();

        mBaiduMap.setMyLocationEnabled(true);
        String mode = FileUtils.readSharedPreference(LOCATION_MODE);
        MyLocationConfiguration.LocationMode saveMode = TextUtils.isEmpty(mode) ? MyLocationConfiguration.LocationMode.FOLLOWING
                : Enum.valueOf(MyLocationConfiguration.LocationMode.class, mode);
        mBaiduMap.setMyLocationConfigeration(configuration(saveMode));

        mLocationClient.registerLocationListener(myListener);
        mLocationClient.setLocOption(clientOption());
        mLocationClient.start();

        changeLocationModeIfDrag();


    }

    private void changeLocationModeIfDrag(){
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Log.d(TAG, "changeLocationModeIfDrag");
                    changeLocationMode(MyLocationConfiguration.LocationMode.NORMAL);
                }
            }
        });
    }
    public void onResume() {
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorManager.registerListener(mEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);
    }

    class MySensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float x = sensorEvent.values[SensorManager.DATA_X];
                mDevicesDirection = x;

                MyLocationData old = mBaiduMap.getLocationData();
                if (old != null && Math.abs(mDevicesDirection - old.direction) > 5){
                    MyLocationData locData = new MyLocationData.Builder()
                            .accuracy(old.accuracy)
                                    // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(mDevicesDirection).latitude(old.latitude)
                            .longitude(old.longitude).build();
                    mBaiduMap.setMyLocationData(locData);
                    log("devices_x: " + x);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    public void onPause() {
        mSensorManager.unregisterListener(mEventListener);
    }

    public void onDestroy() {
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
    }

    private MyLocationConfiguration configuration(MyLocationConfiguration.LocationMode mode) {
        FileUtils.writeSharedPreference(LOCATION_MODE, mode.toString());
        return new MyLocationConfiguration(mode, true, null);
    }

    public void changeLocationMode(MyLocationConfiguration.LocationMode mode) {
        mBaiduMap.setMyLocationConfigeration(configuration(mode));
        mCurrentMode = mode;
    }

    public void changToNextLocationMode() {
        MyLocationConfiguration.LocationMode mode = nextLocationMode(mCurrentMode);
        Log.d(TAG, mode.toString());

        if (mode != MyLocationConfiguration.LocationMode.COMPASS){
            Log.d(TAG, "restore to NORMAL");
            MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(0).rotate(0).build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
            mBaiduMap.animateMapStatus(u);
        }

        changeLocationMode(mode);
    }

    private MyLocationConfiguration.LocationMode nextLocationMode(MyLocationConfiguration.LocationMode currentMode) {
        switch (currentMode) {
            case FOLLOWING: {
                return MyLocationConfiguration.LocationMode.COMPASS;
            }
            case COMPASS: {
                return MyLocationConfiguration.LocationMode.FOLLOWING;
            }
            case NORMAL: {
                return MyLocationConfiguration.LocationMode.FOLLOWING;
            }

        }
        return null;
    }

    public LocationClientOption clientOption() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(true);
        return option;
    }


    private void log(String msg) {
        Log.d(TAG, msg);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            if (Debuger.DEBUG){
                location = Debuger.DEBUG_LOCATION;
            }

            MyLocationData old = mBaiduMap.getLocationData();
            if (old != null){
                log("old->lat: " + old.latitude + "-" + old.longitude + "   new->lat: " + location.getLatitude() + "-" +  location.getLongitude());
            }

            if (mBDLocation == null || location.getLatitude() != mBDLocation.getLatitude() || location.getLongitude() != mBDLocation.getLongitude()){

                log("update Location");

                double lat = location.getLatitude();
                double lon = location.getLongitude();

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mDevicesDirection).latitude(lat)
                        .longitude(lon).build();
                log("device direction: " + location.getDirection());
                mBaiduMap.setMyLocationData(locData);

                // update
                mBDLocation = location;
            }


            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
