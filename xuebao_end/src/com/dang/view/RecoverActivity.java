package com.dang.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaPersonalStorage;
import com.baidu.frontia.api.FrontiaStatistics;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoResult;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileListListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileTransferListener;
import com.dang.tool.Data;
import com.dang.tool.Filetool;
import com.dang.treasure.R;
import com.uvchip.mediacenter.MainActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RecoverActivity extends Activity{
	private FrontiaStatistics stat;
	private TextView tvlog;
	private TextView tvloging;
	private EditText etsourcepath;
	private EditText ettargetpath;
	private Button btstart;
	private Button btappdata;
	private Button btfinish;
	private FrontiaAuthorization authorization;
	private FrontiaPersonalStorage mCloudStorage;
	private ProgressBar progress;
	private int progress_now;
	private int progress_max;
	private boolean loginreply;
	private String log;
	private String loging;
	private String sourcepath;
	private String targetpath=Data.downpath;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.abz_activity_blank);
		myAuthorization();
		setContentView(R.layout.abc_activity_recovery);
		loadview();
	}
	 protected void onRestart() {
		 super.onRestart();
		 uplog("");
	 }

	private void loadview() {
		stat = Frontia.getStatistics();
		tvlog=(TextView) findViewById(R.id.abctv_log);
		tvloging=(TextView) findViewById(R.id.abctv_loging);
		progress=(ProgressBar) findViewById(R.id.abcpb_progress);
		etsourcepath=(EditText) findViewById(R.id.abcet_sourcepath);
		ettargetpath=(EditText) findViewById(R.id.abcet_targetpath);
		Intent intent=this.getIntent();
		String nowpath=intent.getStringExtra("nowpath");
		if(nowpath!=null){
			sourcepath=Data.kuyun;			
			targetpath=nowpath;
			Data.kuyun=null;
		}
		String path=intent.getStringExtra("path");
		if(path!=null){
			sourcepath=path;
		}
		etsourcepath.setText(sourcepath);
		ettargetpath.setText(targetpath);
		btappdata=(Button) findViewById(R.id.abcbt_appdata);
		btappdata.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "开始还原",0).show();
				Filetool filetool = new Filetool();
				etsourcepath.setText(Data.Bappdatabuckupspath);
				ettargetpath.setText(Data.appdataparentpath);
				String deletepath=Data.myappdatapath;
				if(filetool.deleteSubdirectory(deletepath)){
					uplog("本地整理完毕(*^__^*)");
				}
				sourcepath=etsourcepath.getText().toString();
				targetpath=ettargetpath.getText().toString();
				String filename=sourcepath.substring(sourcepath.lastIndexOf("/")+1,sourcepath.length());
				targetpath=targetpath+"/"+filename;
				makeDir(targetpath);
				mydownload(sourcepath);
			}
		});
		btfinish=(Button) findViewById(R.id.abcbt_finish);
		btfinish.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		btstart=(Button) findViewById(R.id.abcbt_start);
		btstart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sourcepath=etsourcepath.getText().toString();
				targetpath=ettargetpath.getText().toString();
				if((sourcepath.length()-sourcepath.lastIndexOf("."))<10){//是文件
					//Toast.makeText(getApplicationContext(), "是文件", 1).show();
					String name=sourcepath.substring(sourcepath.lastIndexOf("/"), sourcepath.length());
					String targeturl=targetpath+name;				
					downloadFile(sourcepath, targeturl);
				}else{
				String filename=sourcepath.substring(sourcepath.lastIndexOf("/")+1,sourcepath.length());
				targetpath=targetpath+"/"+filename;
				makeDir(targetpath);
				mydownload(sourcepath);
				}
			}
		});
		String note= intent.getStringExtra("note");
		//Toast.makeText(getApplicationContext(), "ddddddddd"+note, 0).show();
		if(note!=null){
			Toast.makeText(getApplicationContext(), "开始还原",0).show();
			Filetool filetool = new Filetool();
			etsourcepath.setText(Data.Bappdatabuckupspath);
			ettargetpath.setText(Data.appdataparentpath);
			String deletepath=Data.myappdatapath;
			if(filetool.deleteSubdirectory(deletepath)){
				uplog("本地整理完毕(*^__^*)");
			}
			sourcepath=etsourcepath.getText().toString();
			targetpath=ettargetpath.getText().toString();
			String filename=sourcepath.substring(sourcepath.lastIndexOf("/")+1,sourcepath.length());
			targetpath=targetpath+"/"+filename;
			makeDir(targetpath);
			mydownload(sourcepath);
			
		}
	}
	//递归文件
	private void mydownload(final String sourcedir) {
	
		mCloudStorage.list(sourcedir,FrontiaPersonalStorage.BY_TIME,null,new FileListListener() {
			public void onSuccess(List<FileInfoResult> result) {
				for(FileInfoResult afile : result){
					if(afile.isDir()){
						String sourcedir=afile.getPath().substring(sourcepath.length()+1, afile.getPath().length());
						String targetdir=targetpath+"/"+sourcedir;
						makeDir(targetdir);
						mydownload(afile.getPath());
					}else{
						String sourceurl=afile.getPath().substring(sourcepath.length()+1, afile.getPath().length());
						String targeturl=targetpath+"/"+sourceurl;
						downloadFile(afile.getPath(), targeturl);
					}
				}
			}	
			public void onFailure(int arg0, String arg1) {
				uplog(sourcedir+"展开失败:"+arg1);
			}
		});
	}
	//新建文件夹
	private void makeDir(String Dir){
		File file=new File(Dir);
		if(!file.exists()){
			file.mkdirs();
			uplog(file.getPath()+"+++创建成功");
		}
	}

	//下载
	protected void downloadFile(String source,String target) {
		progress_max++;
		progress.setMax(progress_max);
			mCloudStorage.downloadFile(source,target,
					new FileProgressListener() {
						public void onProgress(String source, long bytes, long total) {
							tvloging.setText(source+"↗↗↗"+bytes/1024+"KB→"+total/1024+"KB");
							
						}
					}, new FileTransferListener() {
						public void onSuccess(String source, String newTargetName) {
							uplog(newTargetName+">>>下载成功");
							progress_now++;
							progress.setProgress(progress_now);
							tvloging.setText("");
							if(progress_max==progress_now){
								Toast.makeText(getApplicationContext(), "下载完成", 1).show();
								uplog("下载完成(*^__^*)\n 共处理文件:>>>"+progress_max);
								dNotification("下载完成(*^__^*)","下载完成(*^__^*)","共下载【"+progress_max+"】个文件");
							}
						}
						public void onFailure(String source, int errCode,
								String errMsg) {
							uplog(source+"!?下载失败:"+errMsg);
							progress_now++;
							if(progress_max==progress_now){
								Toast.makeText(getApplicationContext(), "下载完成", 1).show();
								uplog("下载完成(*^__^*)\n 共处理文件:>>>"+progress_max);
							}
						}
					});
			if(progress_max==progress_now){
				Toast.makeText(getApplicationContext(), "下载完成", 1).show();
				uplog("下载完成(*^__^*)\n 共处理文件:>>>"+progress_max);
			}
		}
	
	//更新log
	private void uplog(String newlog) {
		// TODO 自动生成的方法存根
		log=newlog+"\n"+log;
		tvlog.setText(log);
	}

	//百度认证
	private boolean myAuthorization() {
		authorization=Frontia.getAuthorization();
		mCloudStorage=Frontia.getPersonalStorage();
		FrontiaAccount currentAccount=Frontia.getCurrentAccount();
		if(null!=currentAccount&&FrontiaAccount.Type.USER==currentAccount.getType()){
			FrontiaUser user=(FrontiaUser) currentAccount;
			if(!MediaType.BAIDU.toString().equals(user.getPlatform())){
				ArrayList<String>list=new ArrayList<String>();
				list.add("basic");
				list.add("netdisk");
				authorization.bindBaiduOAuth(this, list, new AuthorizationListener(){
					public void onCancel() {
						
					}
					public void onFailure(int arg0, String errMsg) {
						
					}
					public void onSuccess(FrontiaUser arg0) {
						loginreply=true;;
					}	
				});
			}else{
				loginreply=true;;
			}
		}else{
			//登陆
			ArrayList<String>list=new ArrayList<String>();
			list.add("basic");
			list.add("netdisk");
			authorization.authorize(this,MediaType.BAIDU.toString(),list,new AuthorizationListener() {
		        public void onSuccess(FrontiaUser arg0) {
		            Frontia.setCurrentAccount(arg0);
		            loginreply=true;;
		        }
		        public void onFailure(int arg0, String arg1) {
		        }
		        public void onCancel() {
		        }
		    });
		}
		
		return loginreply;
	}
	
	//任务栏显示
			private void dNotification(String toast,String title,String message) {
				Notification notification = null;
				NotificationManager manage=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Intent intent = new Intent(this,MainActivity.class);
				PendingIntent contentIntent=PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				
				notification=new Notification(R.drawable.abi_menu_recover,toast,System.currentTimeMillis());
				notification.setLatestEventInfo(this, title, message, contentIntent);
					
				manage.notify(0,notification);
			}	
	//统计
	public void onResume() {
		super.onResume();
		stat.pageviewStart(this, "Recover");
	}

	public void onPause() {
		super.onPause();
		stat.pageviewEnd(this, null);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * 
		 * add()方法的四个参数，依次是：
		 * 
		 * 1、组别，如果不分组的话就写Menu.NONE,
		 * 
		 * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
		 * 
		 * 3、顺序，那个菜单现在在前面由这个参数的大小决定
		 * 
		 * 4、文本，菜单的显示文本
		 */

		menu.add(Menu.NONE, 1, 1, "清空日志");
		return true;
	}
	//菜单的点击事件
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			log="log已清空\n";
			return true;
		}
		return false;
	}

	
}


