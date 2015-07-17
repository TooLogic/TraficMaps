package org.zarroboogs.maps.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import org.zarroboogs.maps.R;
import org.zarroboogs.maps.module.GeoFenceModule;
import org.zarroboogs.maps.module.LocationModule;
import org.zarroboogs.maps.module.MarkerModule;

public class MapsActivity extends AppCompatActivity {

    public static final String TAG = "MapsActivity";
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    // 定位
    private LocationModule mLocationModule;
    private MarkerModule mMarkerModule;
    private GeoFenceModule mGeoFenceModule;

    private Button mLocationBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.maps_activity_layout);

        mLocationBtn = (Button) findViewById(R.id.my_location_btn);

        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationModule.changToNextLocationMode();
            }
        });

        mMapView = (MapView) findViewById(R.id.bmapView);
        BaiduMapUiUtils.removeUnusedViews(mMapView);

        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);

        // Marker
        mMarkerModule = new MarkerModule(mMapView);
        mMarkerModule.showCamera();
        mMarkerModule.enableMakerDragListener();

        mLocationModule = new LocationModule(getApplicationContext());
        mLocationModule.startLocation(mMapView);

        mGeoFenceModule = new GeoFenceModule(mMapView);
        mGeoFenceModule.addGeoFence(new LatLng(40.012984f,116.489999f));

    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        mLocationModule.onPause();

        mGeoFenceModule.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        mLocationModule.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocationModule.onDestroy();
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        super.onDestroy();
        // 回收 bitmap 资源
        mMarkerModule.onDestroy();
    }

}
