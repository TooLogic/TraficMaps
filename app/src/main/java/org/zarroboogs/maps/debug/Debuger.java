package org.zarroboogs.maps.debug;

import com.baidu.location.BDLocation;

/**
 * Created by wangdiyuan on 15-7-17.
 */
public class Debuger {
    public static final boolean DEBUG = false;
    public static BDLocation DEBUG_LOCATION =  new BDLocation();
    static {
//        {
//            "id": 10,
//                "name": "鼓楼西大街西口",
//                "latitude": 39.95427322387695,
//                "longtitude": 116.3864974975586,
//                "direction": "E->W"
//        },
        DEBUG_LOCATION.setLatitude(39.95427322387695f);
        DEBUG_LOCATION.setLongitude(116.3864974975586f);
    }

}
