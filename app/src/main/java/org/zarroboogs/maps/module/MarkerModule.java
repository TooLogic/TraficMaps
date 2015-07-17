package org.zarroboogs.maps.module;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import org.zarroboogs.maps.R;
import org.zarroboogs.maps.beans.PaperCameraBean;
import org.zarroboogs.maps.utils.FileUtils;
import org.zarroboogs.maps.utils.JsonUtils;

import java.util.ArrayList;

/**
 * Created by wangdiyuan on 15-7-16.
 */
public class MarkerModule {
    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor mCameraBd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Context mContext;

    public MarkerModule(MapView mapView){
        this.mMapView = mapView;
        this.mContext = mMapView.getContext().getApplicationContext();
        this.mBaiduMap = mMapView.getMap();
    }


    public void showCamera() {
        // add marker overlay
        ArrayList<PaperCameraBean> cameras = JsonUtils.prasePaperCameras(FileUtils.readStringFromAsset(mContext, "db.json"));
        for (PaperCameraBean pc : cameras) {
            LatLng ll = new LatLng(pc.getLatitude(), pc.getLongtitude());
            OverlayOptions oo = new MarkerOptions().position(ll).icon(mCameraBd).zIndex(pc.getId()).draggable(true);
            Marker marker = (Marker) (mBaiduMap.addOverlay(oo));
        }
    }

    public void enableMakerDragListener(){
        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(mContext, "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                        + marker.getPosition().longitude, Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    public void setOnMarkerClickListener(BaiduMap.OnMarkerClickListener listener){
        mBaiduMap.setOnMarkerClickListener(listener);
    }

    public void clearAllMarkers(){
        mBaiduMap.clear();
    }

    public void onDestroy(){
        mCameraBd.recycle();
    }
}
