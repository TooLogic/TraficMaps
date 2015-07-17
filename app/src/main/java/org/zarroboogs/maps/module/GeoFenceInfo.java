package org.zarroboogs.maps.module;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by wangdiyuan on 15-7-17.
 */
public class GeoFenceInfo {
    private LatLng latLng;
    private int mId = 0;

    private String GEOFENCE_BROADCAST_ACTION = "org.zarroboogs.maps.geofence_broadcast";

    private IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    private PendingIntent mPendingIntent;

    public GeoFenceInfo(Context context, int id) {
        this.mId = id;
        filter.addAction(GEOFENCE_BROADCAST_ACTION + mId);

        Intent intent = new Intent(GEOFENCE_BROADCAST_ACTION + mId);
        mPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    public LatLng getLatLng(){
        return latLng;
    }
    public PendingIntent getPendingIntent() {
        return mPendingIntent;
    }

    public IntentFilter getFilter() {
        return filter;
    }
}
