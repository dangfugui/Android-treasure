package com.dang.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaPersonalStorage;
import com.baidu.frontia.api.FrontiaStatistics;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoResult;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileOperationListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileUploadListener;
import com.dang.service.Buckups;
import com.dang.tool.Data;
import com.dang.tool.Filetool;
import com.dang.treasure.R;
import com.uvchip.mediacenter.MainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class BackupsActivity extends Activity{
	protected static final String String = null;

	private FrontiaStatistics stat;
	
	private Button btstart;
	private Button btfinish;
	private Button btappdata;
	private EditText etsourcepath;
	private EditText ettargetpath;
	private TextView tvlog;
	private TextView tvloging;
	private ProgressBar progress;
	private boolean loginreply=false;
	private FrontiaAuthorization authorization;
	private FrontiaPersonalStorage mCloudStorage;
	private String TAG="BuckupsActivity";
	private String appdatapath=Data.myappdatapath;
	private String baidubuckupspath=Data.Baidubuckupspath;
	private String buckupspath=Data.buckupspath;
	private String buckupsname;//
	private String makepath;//要备份到的云路径	
	private String log;
	private String loging;
	private int progress_now;
	private int progress_max;
	private String sourcepath=Data.Myfilepath;
	private String targetpath=Data.Baidubuckupspath;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		
		myAuthorization();
		setContentView(R.layout.abb_activity_buckups);
		loadview();
		
	}
	 protected void onRestart() {
		 super.onRestart();
		 uplog("");
	 }
	//处理界面
	private void loadview() {
		stat = Frontia.getStatistics();
		tvlog=(TextView) findViewById(R.id.abb_tvlog);
		tvloging=(TextView) findViewById(R.id.abb_tvloging);
		
		etsourcepath=(EditText) findViewById(R.id.abbet_source);
		ettargetpath=(EditText) findViewById(R.id.abbet_target);
		progress=(ProgressBar) findViewById(R.id.abbpb_progress);
		Intent intent=this.getIntent();
		String intentpath=intent.getStringExtra("path");
		String intentbuckupspath=intent.getStringExtra("backupspath");
		String nowpath=intent.getStringExtra("nowpath");
		SimpleDateFormat dDateFormat=new SimpleDateFormat("yyy-MM-dd  HH-mm");
		String date=dDateFormat.format(new Date());
		targetpath=targetpath+date;
		if(intentpath!=null){
			targetpath=intentpath;
		}
		if(intentbuckupspath!=null){
			sourcepath=intentbuckupspath;
		}
		if(nowpath!=null){
			//String url=Data.kuyun.substring(0,Data.kuyun.length()-1);
			
			sourcepath=Data.kuyun;
			targetpath=nowpath;
			Data.kuyun=null;
			
		}
		File a=new File(sourcepath);
		if(a.isDirectory()){
			targetpath=targetpath+"/"+a.getName();
		}
		etsourcepath.setText(sourcepath);
		ettargetpath.setText(targetpath);
		/*Time time=new Time();
		time.setToNow();
		String nowtime=time.year+"-"+time.month+"-"+time.monthDay+" "+time.hour+"-"+time.minute;*/
		/*SimpleDateFormat dDateFormat=new SimpleDateFormat("yyy-MM-dd  HH-mm");
		String date=dDateFormat.format(new Date());
		etname.setText(date);*/
		btstart=(Button) findViewById(R.id.abb_btstart);
		btstart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				/*buckupspath=Data.buckupspath;
				buckupsname=etname.getText().toString();
				makepath=baidubuckupspath+buckupsname;
				makeDir(makepath);
				buckupsFolder(new File(buckupspath));*/
				Toast.makeText(getApplicationContext(), "开始上传",0).show();
				sourcepath=etsourcepath.getText().toString();
				targetpath=ettargetpath.getText().toString();
				makeDir(targetpath);
				buckupsFolder(new File(sourcepath));
			}
		});
		btappdata=(Button) findViewById(R.id.abb_btappdata);
		btappdata.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "开始上传",0).show();
				mydeleteFile(Data.Bappdatabuckupspath);
				Filetool filetool = new Filetool();
				String cachepath=Data.myappdatapath+"/cache";
				if(filetool.DeleteFolder(cachepath)){
					uplog("为了节约流量缓存清理完毕");
				}
				sourcepath=appdatapath;
				targetpath=Data.Bappdatabuckupspath;
				etsourcepath.setText(sourcepath);
				ettargetpath.setText(targetpath);
				makeDir(targetpath);
				buckupsFolder(new File(sourcepath));
			}
		});
		btfinish=(Button) findViewById(R.id.abb_btfinish);
		btfinish.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		String note= intent.getStringExtra("note");
		//Toast.makeText(getApplicationContext(), "ddddddddd"+note, 0).show();
		if(note!=null){
			Toast.makeText(getApplicationContext(), "开始上传",0).show();
			mydeleteFile(Data.Bappdatabuckupspath);
			Filetool filetool = new Filetool();
			String cachepath=Data.myappdatapath+"/cache";
			if(filetool.DeleteFolder(cachepath)){
				uplog("为了节约流量缓存清理完毕");
			}
			sourcepath=appdatapath;
			targetpath=Data.Bappdatabuckupspath;
			etsourcepath.setText(sourcepath);
			ettargetpath.setText(targetpath);
			makeDir(targetpath);
			buckupsFolder(new File(sourcepath));
		}
	}
	//自定义备份
	private void custombuckups() {
		final EditText editText=new EditText(this);
		new AlertDialog.Builder(this)
		.setTitle("自定义备份")
		.setMessage("请输入备份路径")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView(editText)
		.setPositiveButton("开始备份", new DialogInterface.OnClickListener(){
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				String custompath=editText.getText().toString();
			//	buckupsname=etname.getText().toString();
				buckupspath=custompath;
				makepath=baidubuckupspath+buckupsname;
				makeDir(makepath);
				File file=new File(custompath);
				if(file.exists()){
					Toast.makeText(getApplicationContext(), "备份开始", 1)	.show();
					buckupsFolder(file);
				}else{
					Toast.makeText(getApplicationContext(), "请输入合法路径", 1)	.show();
				}
			}
		})
		.setNegativeButton("取消", null).show();
		
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
						 loginreply=true;
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
	//递归上传文件
	private void buckupsFolder(File file) {
		// TODO 自动生成的方法存根
		if(file.isDirectory()){
			File []files=file.listFiles();
			if(files!=null){
				for(int j=0;j<files.length;j++){
					File afile=files[j];
					if(afile.isDirectory()){
						String sourcedir=afile.getPath().substring(sourcepath.length()+1, afile.getPath().length());
						Log.v(TAG,makepath+"dddd"+sourcedir);
						String targetdir=targetpath+"/"+sourcedir;
						makeDir(targetdir);
						buckupsFolder(afile);
					}else{
						String sourcepath1=afile.getPath().substring(sourcepath.length()+1, afile.getPath().length());
						String targetpath1=targetpath+"/"+sourcepath1;
						
						uploadFile(afile.getPath(), targetpath1);
						
					}
				}
			}
		}else{
			String targetpath1=targetpath+"/"+file.getName();
			uploadFile(file.getPath(), targetpath1);
		}
		
	}
	//创建文件夹
	private void makeDir(final String path) {
		mCloudStorage.makeDir(
				path,
				new FileInfoListener() {
					public void onFailure(int arg0, String arg1) {
						uplog(path+"!~?创建失败:"+arg1);
					}
					public void onSuccess(FileInfoResult arg0) {
						uplog(path+"+++创建成功");
					}
				});	
	}
	//上传文件
	private void uploadFile(String source,String target) {
		Log.v(TAG,source+"_--to--_"+target);
		progress_max++;
		progress.setMax(progress_max);
		mCloudStorage.uploadFile(source,target,
				new FileProgressListener() {

					@Override
					public void onProgress(String source, long bytes, long total) {
						tvloging.setText(source+"↗↗↗"+bytes/1024+"KB→"+total/1024+"KB");
					}

				}, new FileUploadListener() {
					public void onSuccess(String source,FileInfoResult result) {
						progress_now++;
						progress.setProgress(progress_now);
						uplog(source+"+++上传成功");
						tvloging.setText("");
						if(progress_max==progress_now){
							Toast.makeText(getApplicationContext(), "上传完成", 1).show();
							uplog("上传完成(*^__^*)\n 共处理文件:>>>"+progress_max);
							dNotification("上传完成(*^__^*)","上传完成(*^__^*)", "共上传【"+progress_now+"】个文件");
						}
					}
					public void onFailure(String source, int errCode,String errMsg) {		
						uplog(source+"!~?上传失败:"+errMsg);
						progress_now++;
						if(progress_max==progress_now){
							Toast.makeText(getApplicationContext(), "上传完成", 1).show();
							uplog("上传完成(*^__^*)\n 共处理文件:>>>"+progress_max);
						}
					}

				});
	}
	
	//更新log
	private void uplog(String newlog) {
		// TODO 自动生成的方法存根
		log=newlog+"/n"+log;
		tvlog.setText(log);
	}
	
	//删除
	private void mydeleteFile(String path){
		final String deletepath=path;
		 mCloudStorage.deleteFile(deletepath,new FileOperationListener() {
	            public void onSuccess(String s) {
	            	
	            	}

				@Override
				public void onFailure(String arg0, int arg1, String arg2) {
					// TODO 自动生成的方法存根
					 Toast.makeText(getApplicationContext(), arg1+"删除失败"+arg2, 0).show();
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
	//加入菜单
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
	//统计
	public void onResume() {
		super.onResume();
		stat.pageviewStart(this, "Buckups");
	}

	public void onPause() {
		super.onPause();
		stat.pageviewEnd(this, null);
	}


}
