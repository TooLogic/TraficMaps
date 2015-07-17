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
import org.zarroboogs.maps.beans.PaperCameraBean;
import org.zarroboogs.maps.utils.FileUtils;
import org.zarroboogs.maps.utils.JsonUtils;
import org.zarroboogs.maps.utils.LatLngUtils;

import java.util.ArrayList;

/**
 * Created by wangdiyuan on 15-7-17.
 */
public class GeoFenceModule {

    public static final String TAG = "GeoFenceModule";
    private Context mContext;
    private MapView mapView;
    private BaiduMap mBaiduMap;

    private GeoFenceManager mGeoFenceManager;


    public GeoFenceModule(MapView mapView) {
        this.mContext = mapView.getContext().getApplicationContext();
        this.mapView = mapView;

        this.mBaiduMap = mapView.getMap();

        mGeoFenceManager = new GeoFenceManager(mContext);


    }


    public void onCreate() {
        ArrayList<PaperCameraBean> cameras = JsonUtils.prasePaperCameras(FileUtils.readStringFromAsset(mContext, "db.json"));
        ArrayList<GeoFenceInfo> infs = new ArrayList<>();

        for (PaperCameraBean paperCameraBean : cameras) {
            GeoFenceInfo geoFenceInfo = new GeoFenceInfo(mContext, new com.baidu.mapapi.model.LatLng(paperCameraBean.getLatitude(), paperCameraBean.getLongtitude()), paperCameraBean.getId());
            infs.add(geoFenceInfo);
        }
        GeoFenceInfo geoFenceInfo = new GeoFenceInfo(mContext, new com.baidu.mapapi.model.LatLng(40.09705f, 116.426019f), 100);
        infs.add(geoFenceInfo);
        //40.09705-116.426019

        mGeoFenceManager.addAllGeoFenceAler(infs);
    }

    public void onDestory() {
        mGeoFenceManager.removeAllGeoFenceAlert();
    }


}
