package com.dang.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.http.impl.client.TunnelRefusedException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class AppData {
	private Context mcontext;
	public AppData(Context context) {
		this.mcontext = context;
	}
	

	/**
	 * 保存参数
	 * @param name 姓名
	 * @param id id
	 */
	public void savesign(String name, String id) {
		 SharedPreferences preferences=mcontext.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("name", name);
		editor.putString("id", id);
		editor.commit();
	}
	public void saveString(String key, String value) {
		 SharedPreferences preferences=mcontext.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		if(value.length()>9999){
			value="数据过长";
		}
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public String getString(String key){
		 SharedPreferences preferences=mcontext.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		return preferences.getString(key, "");
	}
	/**
	 * 获取各项配置参数
	 * @return
	 */
	public Map<String, String> getsign(){
		 SharedPreferences preferences=mcontext.getSharedPreferences("appdata", Context.MODE_PRIVATE);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", preferences.getString("name", ""));
		params.put("id", preferences.getString("id", ""));
		return params;
	}
}
