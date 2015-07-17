package org.zarroboogs.maps.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.MapView;

/**
 * Created by wangdiyuan on 15-7-15.
 */
public class BaiduMapUiUtils {

    public static void removeUnusedViews(MapView mapView) {
        // 隐藏多余的控件和LOGO
        for (int i = 0, j = mapView.getChildCount(); i < j; i++) {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.GONE);
            }
            if (child instanceof RelativeLayout) {
                child.setVisibility(View.GONE);
            }
            if (child instanceof ImageView) {
                child.setVisibility(View.GONE);
            }
        }
    }

}
