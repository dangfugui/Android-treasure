package com.uvchip.mediacenter;

import com.baidu.frontia.FrontiaApplication;

import android.app.Application;
import android.os.Environment;

public class MApplication extends FrontiaApplication{
	private static MApplication instance = null;
	public static String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mediacenter/imgcache/";
	public static MApplication getInstance(){
		return instance;
	}
	private PreparedResource mResource;
	public PreparedResource getPreparedResource(){
		return mResource;
	}
	@Override
    public void onCreate() {
		super.onCreate();
		instance = this;
		mResource = new PreparedResource(this);
		
	}
}
