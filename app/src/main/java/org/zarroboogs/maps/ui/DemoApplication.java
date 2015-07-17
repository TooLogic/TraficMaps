package org.zarroboogs.maps.ui;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

public class DemoApplication extends Application {

	private static Context sCntext;

	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		sCntext = this;
	}

	public static Context getAppContext(){
		return sCntext;
	}

}