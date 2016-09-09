package com.dang.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.impl.client.TunnelRefusedException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

public class WebData {
	public static String webkey="<we>";
	private Context mcontext;
	public WebData(Context context) {
		this.mcontext = context;
	}
	
	
	public Set<String> getwebs(){
		 SharedPreferences preferences=mcontext.getSharedPreferences("webdata", Context.MODE_PRIVATE);
		 Map<String, ?>all= preferences.getAll();
		 Set<String> values=new HashSet<String>();
		 for (Map.Entry<String, ?> entry : all.entrySet()) {
			  // System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
			   values.add(entry.getKey()+webkey+entry.getValue());
			  }
		return values;
		
	}
	/**
	 * 添加收藏
	 * @param name 网站名
	 * @param url   url
	 */
	public  boolean addweb(String name,String url) {
		 SharedPreferences preferences=mcontext.getSharedPreferences("webdata", Context.MODE_PRIVATE);
		 Editor editor = preferences.edit();
		 if(preferences.getString(name, "").length()<1){
			 editor.putString(name, url);
			 editor.commit();
			 return true;
		 }else{
			 editor.remove(name);
			 editor.commit();
			 return false;
		 }
	}
	
}
