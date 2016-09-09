package com.dang.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaPersonalStorage;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoResult;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileTransferListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileUploadListener;
import com.dang.tool.Data;
import com.dang.treasure.R;
import com.uvchip.mediacenter.MainActivity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Buckups extends IntentService{
	private String TAG="Buckups";
	private FrontiaPersonalStorage mCloudStorage;
	private String baidubuckupspath=Data.Baidubuckupspath;
	private String buckupspath=Data.buckupspath;
	private String buckupsname;//
	private String makepath;//要备份到的云路径
	
	
	public Buckups() {
		super("Buckups");
		// TODO 自动生成的构造函数存根13963245305
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mCloudStorage = Frontia.getPersonalStorage();
		buckupsname=intent.getStringExtra("name");
		dNotification("开始备份","正在备份",buckupsname);

		
		makepath=baidubuckupspath+buckupsname;
		makeDir(makepath);
		buckupsFolder(new File(buckupspath));
	}
	
	
	

	private void buckupsFolder(File file) {
		// TODO 自动生成的方法存根
		File []files=file.listFiles();
		if(files!=null){
			for(int j=0;j<files.length;j++){
				File afile=files[j];
				if(afile.isDirectory()){
					String sourcedir=afile.getPath().substring(buckupspath.length()+1, afile.getPath().length());
					Log.v(TAG,makepath+"dddd"+sourcedir);
					String targetdir=makepath+"/"+sourcedir;
					makeDir(targetdir);
					buckupsFolder(afile);
				}else{
					String sourcepath=afile.getPath().substring(buckupspath.length()+1, afile.getPath().length());
					String targetpath=makepath+"/"+sourcepath;
					
					uploadFile(afile.getPath(), targetpath);
					
				}
			}
		}
		
	}
	//创建文件夹
	private void makeDir(final String path) {
		mCloudStorage.makeDir(
				path,
				new FileInfoListener() {
					public void onFailure(int arg0, String arg1) {
						Log.v(TAG,"makeDir failure:"+arg0+ arg1);
					}
					public void onSuccess(FileInfoResult arg0) {
						Log.v(TAG, "makeDir:-"+path);
					}
				});	
	}
	
	private void uploadFile(String source,String target) {
		Log.v(TAG,source+"_--to--_"+target);
		mCloudStorage = Frontia.getPersonalStorage();
		mCloudStorage.uploadFile(source,
				target,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						Log.v(TAG, "makeDidddddddddddd");
					}

				}, new FileUploadListener() {

					@Override
					public void onSuccess(String source,
							FileInfoResult result) {
						Log.v(TAG, "makeDidddddddddddd");
					}

					@Override
					public void onFailure(String source, int errCode,
							String errMsg) {
						Log.v(TAG, "makeDidddddddddddd");

					}

				});
			
	}
	
	//任务栏显示
	private void dNotification(String toast,String title,String message) {
		Notification notification = null;
		NotificationManager manage=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent intent = new Intent(this,MainActivity.class);
		PendingIntent contentIntent=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		notification=new Notification(R.drawable.abi_menu_buckups,toast,System.currentTimeMillis());
		notification.setLatestEventInfo(this, title, message, contentIntent);
			
		manage.notify(0,notification);
	}

	
}
