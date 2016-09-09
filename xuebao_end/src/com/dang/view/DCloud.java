package com.dang.view;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.uvchip.files.FileManager.FileFilter;
import com.uvchip.files.FileManager.FilesFor;
import com.uvchip.files.FileManager.ViewMode;
import com.uvchip.files.FileItemForOperation;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaPersonalStorage;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileInfoResult;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileListListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileOperationListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileProgressListener;
import com.baidu.frontia.api.FrontiaPersonalStorageListener.FileTransferListener;
import com.baidu.mobstat.d;
import com.dang.service.Buckups;
import com.dang.tool.Data;
import com.dang.treasure.R;
import com.uvchip.mediacenter.filebrowser.Browser;
import com.uvchip.utils.ViewEffect;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Images;
import android.sax.StartElementListener;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DCloud extends Browser implements OnItemLongClickListener{
	final boolean DEBUG = false;
	private Context dContext;
	private ProgressDialog progressDialog;
	private String dcloudpath=Data.BAIDUPATH;
	private boolean willExit=false;
	//百度认证
	private FrontiaAuthorization authorization;
	private FrontiaPersonalStorage mCloudStorage;
	
	private boolean loginreply=false;
	private TextView tvnowpath;
	static{
		TAG = DCloud.class.getCanonicalName();
		
	}
	private ListView mListView;
	private boolean onResume = false;
	private String backpath=Data.BAIDUPATH;

	
	
	public DCloud(Context context) {
		super(context);
		dContext=context;
		initView();
		mViewMode = ViewMode.LISTVIEW;
	}
	public void onResume(){
		if(!onResume){
			onResume=myAuthorization();
		}
			QueryMenu();
	}

	private void initView() {
		authorization=Frontia.getAuthorization();
		mCloudStorage=Frontia.getPersonalStorage();
		mView = mInflater.inflate(R.layout.aab_cloud_browser, null);
		tvnowpath=(TextView) mView.findViewById(R.id.aabtv_nowpath);
		mListView = (ListView)mView.findViewById(R.id.aablv_cloudfile);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
	}

	
	public void QueryMenu() {
		//super.QueryData(preFile,clear,filter);
		//myAuthorization();
			getcloud(dcloudpath);
	}
	
	//获得百度云列表
	private void getcloud(final String nowpath) {
		if(nowpath.length()>dcloudpath.length()){
			backpath=nowpath.substring(0,nowpath.lastIndexOf("/"));
		}
		tvnowpath.setText(nowpath);
		final List<HashMap<String, Object>>data=new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object>back=new HashMap<String, Object>();
		back.put("name","/");
		back.put("image",R.drawable.aai_file_back);
		back.put("property","返回到>>"+backpath);
		back.put("path", backpath);
		data.add(back);	
		mCloudStorage.list(nowpath, FrontiaPersonalStorage.BY_TIME,
				null, new FileListListener() {
					public void onSuccess(List<FileInfoResult> result) {
						for (FileInfoResult info : result) {
							HashMap<String, Object>item=new HashMap<String, Object>();
							String filename=info.getPath().substring(info.getPath().lastIndexOf("/")+1, info.getPath().length()).toLowerCase();
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);  
							String datetime = sdf.format(info.getModifyTime()*1000); 
							String property;
							if(info.isDir()){
								item.put("image",R.drawable.aai_flie_dir);
								property=datetime;
							}else{
								item.put("image",R.drawable.aai_file_download);
								float size=info.getSize()/1024;
								property=datetime+"  "+size+"KB";
							}
							item.put("name",filename);
							item.put("property",property);
							item.put("path", info.getPath());
							item.put("FileInfoResult", info);
							data.add(item);
						}
						SimpleAdapter adapter=new SimpleAdapter(dContext, data, R.layout.aai_item_cloud,
								new String[]{"image","name","property"}, 
								new int[]{R.id.aaiim_image,R.id.aaitv_name,R.id.aaitv_property});
						mListView.setAdapter(adapter);
					}
					public void onFailure(int errCode, String errMsg) {
						Toast.makeText(dContext, "获取失败:"+errMsg, 0).show();
						SimpleAdapter adapter=new SimpleAdapter(dContext, data, R.layout.aai_item_cloud,
								new String[]{"image","name","path"}, 
								new int[]{R.id.aaiim_image,R.id.aaitv_name,R.id.aaitv_property});
						mListView.setAdapter(adapter);
						
					}
				});

	}
	//点击item的事件监听
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ListView listview=(ListView) parent;
		HashMap<String,Object>item=(HashMap<String, Object>) listview.getItemAtPosition(position);
		FileInfoResult afile=(FileInfoResult) item.get("FileInfoResult");
		String path=(String) item.get("path");
		if(position==0||afile.isDir()){
			getcloud(path);
		}else{
			startdownDialog(path);
		}
	}
	//长按item的事件监听
	public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
		willExit=false;
		ListView listview=(ListView) parent;
		final HashMap<String,Object>item=(HashMap<String, Object>) listview.getItemAtPosition(position);
		//显示菜单
		String menuitems[]={"打开","删除","新建","酷云","下载","上传"};
		AlertDialog.Builder dialog=new AlertDialog.Builder(dContext);
		dialog.setTitle("菜单")
		.setItems(menuitems, new  DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Toast.makeText(getApplicationContext(), which+"",0).show();
				disposeitemmenu(item,which);
			}
		})
		//.setNegativeButton("negative", null)
		.show();
		return false;
	}
	//菜单的点击事件
	protected void disposeitemmenu(HashMap<String, Object> item, int which) {
		willExit=false;
		FileInfoResult afile=(FileInfoResult) item.get("FileInfoResult");
		final String path=(String) item.get("path");
		boolean isDir=true;
		if(afile!=null){
			if(!afile.isDir()){
				isDir=false;
			}
		}
		Log.d("tt", path);
		switch (which) {
		case 0://打开
			if(isDir){
				getcloud(path);
			}else{
				startdownDialog(path);
			}
			break;
		case 1://删除
			deleteDiaLog(path);
			 break;
		case 2://新建
			makeDirDiaLog(path);
			//getcloud(tvnowpath.getText().toString());
			break;
		case 3://酷云
			if(Data.kuyun==null){
				Data.kuyun=path;
				Toast.makeText(mContext, "请到【文件】里选择酷云进行下载", 1).show();
				}else{
					String kuyun=Data.kuyun.substring(0,4);
					if(kuyun.equals("/app")){
						Data.kuyun=path;
						Toast.makeText(mContext, "请到【文件】里选择酷云进行下载", 1).show();
						break;
					}
					if(isDir){
							Intent intent=new Intent(mContext,BackupsActivity.class);
							intent.putExtra("nowpath",path);
							mContext.startActivity(intent);
					}else{
						Data.kuyun=path;
						Toast.makeText(mContext, "请到【文件】里选择酷云进行下载", 1).show();
					}
				}
			break;
		case 4://下载
			if(!isDir){
				startdownDialog(path);
			}else{
				Intent intent=new Intent(dContext,RecoverActivity.class);
				intent.putExtra("path",path);
				dContext.startActivity(intent);
			}
			break;
		case 5://上传
			if(!isDir){
				Toast.makeText(dContext, "请选择文件夹进行上传", 0).show();
			}else{
				Intent intent=new Intent(dContext,BackupsActivity.class);
				intent.putExtra("path",path);
				dContext.startActivity(intent);
			}
			
		default:
			break;
		}
		
	}
	
	
	//百度授权
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
				authorization.bindBaiduOAuth((Activity) dContext, list, new AuthorizationListener(){
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
			authorization.authorize((Activity) dContext,MediaType.BAIDU.toString(),list,new AuthorizationListener() {
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
	
	//新建文件夹DiaLog
	private void makeDirDiaLog(final String path) {
		// TODO 自动生成的方法存根
		final EditText editText=new EditText(dContext);
		new AlertDialog.Builder(dContext)
		.setTitle("新建文件夹")
		.setView( editText)
		.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				//String dir=path.substring(0,path.lastIndexOf("/"))+"/"+editText.getText();
				makeDir(path+"/"+editText.getText());
			}
		})
		.setNegativeButton("取消", null).show();
	}
	//删除DiaLog
	private void deleteDiaLog(final String path) {
		new AlertDialog.Builder(dContext)
		.setTitle("确认删除文件吗?")	
		.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				final String deletepath=path;
				 mCloudStorage.deleteFile(deletepath,new FileOperationListener() {
			            public void onSuccess(String s) {
			            	backpath=deletepath.substring(0,deletepath.lastIndexOf("/"));
			            	if(backpath.length()>=dcloudpath.length()){
			            	getcloud(backpath);
			            	}else{
			            		makeDir(dcloudpath);
			            		getcloud(dcloudpath);
			            	}
			            }	     
			            public void onFailure(String s, int errCode, String errMsg) {
			               Toast.makeText(dContext, s+"删除失败"+errMsg, 0).show();
			            }
			        });
				
			}
		})
		.setNegativeButton("取消", null).show();
	}
	
	//显示备份名称DiaLog设置下载路径 
	private void startdownDialog(final String downpath){
		final EditText editText=new EditText(dContext);
		editText.setText(Data.downpath);
		new AlertDialog.Builder(dContext)
		.setTitle("下载文件")
		.setMessage("请输入本地路径    (默认为:sdcard/Treasure/Download)")
		.setView( editText)
		.setPositiveButton("开始下载", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				File file=new File(editText.getText()+"/");
				if(!file.exists()){
					file.mkdirs();
				}
				String filename=downpath.substring(downpath.lastIndexOf("/")+1, downpath.length());
				String target=editText.getText()+"/"+filename;
				downloadFile(downpath,target);
			}
		})
		.setNegativeButton("取消", null).show();
	}
	//下载
	protected void downloadFile(String source,String target) {
		Log.d("dd", source+"=========="+target);
		downDialog();
		mCloudStorage.downloadFile(source,target,
				new FileProgressListener() {
					public void onProgress(String source, long bytes, long total) {
						progressDialog.setProgress((int) (bytes/1024));
						progressDialog.setMax((int) (total/1024));
					}
				}, new FileTransferListener() {
					public void onSuccess(String source, String newTargetName) {
						Log.d("dd", "下载成功:"+ source+newTargetName);
						progressDialog.dismiss();
					}
					public void onFailure(String source, int errCode,
							String errMsg) {
						Log.d("dd", "下载失败:"+ errCode+errMsg);
						Toast.makeText(dContext, "下载失败:"+errMsg, 0).show();
					}
				});
	}
	
	//显示下载
	private void downDialog(){
		progressDialog = new ProgressDialog(dContext);
		// progressDialog.setIconAttribute(android.R.attr.alertDialogIcon);
		progressDialog.setTitle("正在下载");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		//progressDialog.setMax(100);
		progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "后台下载", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
			
			}
		});
		progressDialog.show();
	}
	//新建文件夹
	private void makeDir(final String dir) {
		mCloudStorage.makeDir(dir, new FileInfoListener() {
			public void onSuccess(FileInfoResult arg0) {
				String back=dir.substring(0,dir.lastIndexOf("/"));
				getcloud(back);
				}
			public void onFailure(int arg0, String arg1) {
				// TODO 自动生成的方法存根
				Toast.makeText(dContext,"新建失败"+arg1,1).show();
			}
		});
		
	}
	
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public boolean onLongClick(View v) {
		return false;
	}

	@Override
	public void whichOperation(FilesFor filesFor, int size) {
		
	}

	@Override
	public void queryFinished() {
		
	}

	@Override
	public void queryMatched() {
		refreshData();
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
	}
	@Override
	public void onContextMenuClosed(Menu menu) {
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return false;
	}
	
	@Override
	public boolean onBackPressed() {
		if(willExit&&tvnowpath.getText().toString().equals(dcloudpath)){
			return false;
		}
		if(tvnowpath.getText().toString().equals(dcloudpath)){
			Toast.makeText(dContext,"再按一次退出程序!", 0).show();
			willExit=true;
		}
		getcloud(backpath);
		return true;
	}
	
}
