package org.zarroboogs.maps.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

/**
 * Created by wangdiyuan on 15-7-14.
 */
public class LatLngUtils {

    private static CoordinateConverter converter = new CoordinateConverter();

    public static LatLng G2B(LatLng gaode) {
        converter.from(CoordinateConverter.CoordType.COMMON);
        converter.coord(gaode);
        LatLng result = converter.convert();
        return result;
    }
}
