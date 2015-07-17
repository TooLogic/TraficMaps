
package org.zarroboogs.maps.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zarroboogs.maps.beans.PaperCameraBean;

public class JsonUtils {

    // {"code":0,"data":{"Notes_1434352586132.png":1},"errInfo":[]}
    @SuppressWarnings("unchecked")
    public static ArrayList<String> filterNeedUploadFiles(String jsonStr) {
        ArrayList<String> needUpload = new ArrayList<String>();

        JSONObject responseJsonObject;
        try {
            responseJsonObject = new JSONObject(jsonStr);
            JSONObject data = responseJsonObject.getJSONObject("data");
            Iterator<String> keys = data.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                int value = data.getInt(key);
                if (value == 0) {
                }
            }

            return needUpload;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<PaperCameraBean> prasePaperCameras(String json){
        ArrayList<PaperCameraBean> cameraBeans = new ArrayList<>();

        JSONObject responseJsonObject;
        try {
            responseJsonObject = new JSONObject(json);
            JSONArray data = responseJsonObject.getJSONArray("data");
            for (int i = 0, j = data.length(); i < j; i++){
                JSONObject jsonObject = data.getJSONObject(i);
                double lat = jsonObject.getDouble("latitude");
                double lon = jsonObject.getDouble("longtitude");
                String name = jsonObject.getString("name");
                int id = jsonObject.getInt("id");
                String direction = jsonObject.getString("direction");
                Log.d("JsonUtils", "lat:" + lat + "lon:" + lat);

                PaperCameraBean paperCameraBean = new PaperCameraBean();
                paperCameraBean.setId(id);
                paperCameraBean.setDirection(direction);
                paperCameraBean.setLatitude(lat);
                paperCameraBean.setLongtitude(lon);
                paperCameraBean.setName(name);
                cameraBeans.add(paperCameraBean);
            }
            return cameraBeans;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
