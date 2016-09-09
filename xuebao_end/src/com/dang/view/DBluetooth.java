package com.dang.view;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.micode.notes.data.Notes;
import net.micode.notes.ui.NoteEditActivity;
import net.micode.notes.ui.NotesListActivity;

import org.zirco.ui.activities.BrowserMainActivity;

import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.dang.service.AppData;
import com.dang.tool.DataBluetooth;
import com.dang.treasure.R;
import com.diary.DiaryMain;
import com.diary.activity.DiaryNew;
import com.diary.provider.DiaryAdapter;
import com.uvchip.files.FileManager.FilesFor;
import com.uvchip.files.FileManager.ViewMode;
import com.uvchip.mediacenter.filebrowser.Browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DatabaseErrorHandler;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DBluetooth extends Browser{
	final boolean DEBUG = false;
	private Context dContext;
	private AppData appdata;
	//分享
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private String mybluetooth;
	private boolean onResume = false;
	private String note="";
	private ScrollView scrollview;
	private Button btsend;
	private Button btrefresh;
	private Button btbluetooth;
	private Button btnote;
	private Button btdiary;
	private Button btsign;
	private EditText etsend;
	private EditText tvnote;
	private BluetoothAdapter adapter;
	private int counter=0;
	private String name="null";
	private String id="0";
	
	private Handler handler = new Handler();    
	 private Runnable task =new Runnable() {    
		       public void run() {    
		    	   if(adapter.isEnabled()){
	            		 adapter.startDiscovery();
	            		 appdata.saveString("note",tvnote.getText().toString());
	            		 counter=counter+1;
	            			if((counter%2)==0){
	            				btrefresh.setBackgroundResource(R.drawable.ada_top_refresh);
	            			}else{
	            				btrefresh.setBackgroundResource(R.drawable.ada_top_refresh1);
	            			}
	            		//btrefresh.setText(""+DataBluetooth.getsize());	
	            		btbluetooth.setText(""+DataBluetooth.getsize());
		            	 handler.postDelayed(task,5*1000);//设置延迟时间，此处是5秒  
	            	}
		      }     
	    };  

	public DBluetooth(Context context) {
		super(context);
		dContext=context;
		initView();
		mViewMode = ViewMode.LISTVIEW;
	}
	public void onResume(){
		appdata.saveString("note",tvnote.getText().toString());
		if (!onResume) {//如果菜单为空 载入菜单
			QueryMenu();
			onResume = true;
		}
	}

	private void initView() {
		adapter = BluetoothAdapter.getDefaultAdapter();
		mView = mInflater.inflate(R.layout.ada_bluetooth, null);
		appdata=new AppData(dContext);
		scrollview=(ScrollView) mView.findViewById(R.id.adasc_scroll);
		tvnote=(EditText) mView.findViewById(R.id.adaet_note);
		etsend=(EditText) mView.findViewById(R.id.adaet_send);
		btsend=(Button) mView.findViewById(R.id.adabt_send);
		btsend.setOnClickListener(this);
		btrefresh=(Button) mView.findViewById(R.id.adabt_refresh);
		btrefresh.setOnClickListener(this);
		btbluetooth=(Button) mView.findViewById(R.id.adabt_bluetooth);
		btbluetooth.setOnClickListener(this);
		btnote=(Button) mView.findViewById(R.id.adabt_note);
		btnote.setOnClickListener(this);
		btdiary=(Button) mView.findViewById(R.id.adabt_diary);
		btdiary.setOnClickListener(this);
		btsign=(Button) mView.findViewById(R.id.adabt_sign);
		btsign.setOnClickListener(this);
		Map<String, String> adata= appdata.getsign();
		id=adata.get("id");
		name=adata.get("name");
		tvnote.setText(appdata.getString("note"));
		if(adapter.isEnabled()){
			findBluetooth();
			//handler.postDelayed(task,5000);//延迟调用  
		    handler.post(task);//立即调用  
		    btbluetooth.setBackgroundResource(R.drawable.ada_top_openbluetooth);
		    btbluetooth.setText(""+DataBluetooth.getsize());	
		    //btbluetooth.setTextColor(0xffFD172E);
		    mybluetooth=adapter.getName();
		    appdata.savesign(name, id);
		}
	}
	
	
	
	public void QueryMenu() {
		//super.QueryData(preFile,clear,filter);
	}
	//点击刷新
	private void refresh() {
		
		if(adapter.isEnabled()){		
			 adapter.startDiscovery();
    		 appdata.saveString("note",tvnote.getText().toString());
    		 counter=counter+1;
    			if((counter%2)==0){
    				btrefresh.setBackgroundResource(R.drawable.ada_top_refresh);
    			}else{
    				btrefresh.setBackgroundResource(R.drawable.ada_top_refresh1);
    			}
    		btbluetooth.setText(""+DataBluetooth.getsize());	
			
		}else{
			Toast.makeText(dContext, "正在打开蓝牙", 1).show();
		/*//直接打开系统的蓝牙设置面板
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		((Activity) mContext).startActivityForResult(intent, 0x1);*/
		//直接打开蓝牙
		adapter.enable();
		findBluetooth();
		adapter.startDiscovery();
		btbluetooth.setBackgroundResource(R.drawable.ada_top_openbluetooth);
		 //btbluetooth.setTextColor(0xffFD172E);
		 //btbluetooth.setText("关闭\n蓝牙");
		btbluetooth.setText(""+DataBluetooth.getsize());
		 mybluetooth=adapter.getName();
		 appdata.savesign(name, id);
		 handler.postDelayed(task,5000);//延迟调用  
		//handler.post(task);//立即调用  
		}
		
	}
	private void findBluetooth() {
		 //upnote("findBluetooth()");
		// 创建一个接收ACTION_FOUND广播的BroadcastReceiver
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // 发现设备
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // 从Intent中获取设备对象
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            String newtext=DataBluetooth.send(device.getName(),device.getAddress());
		            //tring newtext=DataBluetooth.send("dang"+DataBluetooth.key+"123456"+DataBluetooth.key+"201311020103",device.getAddress());
		           // upnote(device.getName()+"@@"+device.getAddress());
		           if(newtext!=null){
		        	   upnote(newtext);
		           }
		        }
		        adapter.startDiscovery();
		    }
		};
		// 注册BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		mContext.registerReceiver(mReceiver, filter); // 不要忘了之后解除绑定
		
	}
	//发送
	private void send(){
		/*if(!adapter.isEnabled()){
			Toast.makeText(dContext, "正在打开蓝牙", 1).show();
			adapter.enable();
			findBluetooth();
			adapter.startDiscovery();
			btbluetooth.setBackgroundResource(R.drawable.ada_top_openbluetooth);
			mybluetooth=adapter.getName();
			 btbluetooth.setText("关闭\n蓝牙");
			 btbluetooth.setTextColor(0xffFD172E);
			 handler.postDelayed(task,5000);//延迟调用  
			 //handler.post(task);//立即调用  
		}*/
		if(adapter.isEnabled()){
			String send=etsend.getText().toString();
			upnote(send);
			adapter.setName(name+DataBluetooth.key+send+DataBluetooth.key+id);
			etsend.setText("");
			Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);//设置持续时间（最多300秒）
			mContext.startActivity(discoveryIntent);
			adapter.startDiscovery();
			//handler.post(task);//立即调用
		}else{
			if(tvnote.getText().toString().length()<10){
				Toast.makeText(dContext, "蓝牙没有打开\n仅能自己使用", 1).show();
			}
			String send=etsend.getText().toString();
			upnote(send);
			etsend.setText("");
		}
		
	}
	private void upnote(String newtext) {
		note=note+newtext+"\n";
		String newcontent=tvnote.getText()+newtext+"\n";
		note=tvnote.getText()+newtext+"\n";
		int a =tvnote.getSelectionStart();
		tvnote.setText(newcontent);
		appdata.saveString("note", newcontent);
		tvnote.setSelection(a);
		//scrollview.scrollTo(0, 10000000);  
	}
	@Override
	public void onClick(View v) {
		appdata.saveString("note",tvnote.getText().toString());
		switch (v.getId()) {
		case R.id.adabt_refresh:
			refresh();
			break;
		case R.id.adabt_send:
			send();
			break;
		case R.id.adabt_bluetooth:
			bluetooth();
			break;
		case R.id.adabt_diary:
			SimpleDateFormat dDateFormat=new SimpleDateFormat("yyy-MM-dd");
			String date=dDateFormat.format(new Date());
			Intent i = new Intent(dContext, DiaryNew.class);
			i.putExtra(DiaryAdapter.KEY_DATE, date);
			i.putExtra("diary",tvnote.getText().toString());
			tvnote.setText("");
			dContext.startActivity(i);
		
			break;
		case R.id.adabt_note:
			 Intent intent = new Intent(dContext, NoteEditActivity.class);
			 intent.putExtra("note", tvnote.getText().toString());
		     intent.setAction(Intent.ACTION_INSERT_OR_EDIT);
		     intent.putExtra(Notes.INTENT_EXTRA_FOLDER_ID, Notes.ID_ROOT_FOLDER);
		     ((Activity) dContext).startActivityForResult(intent, 103);
		     tvnote.setText("");
			break;
		case R.id.adabt_sign:
			sign();
			break;
		default:
			break;
		}
	}
	//签到
	private void sign() {
		LayoutInflater inflater=((Activity) dContext).getLayoutInflater();
		final View layout=inflater.inflate(R.layout.adl_diallog, null);//(ViewGroup) findViewById(R.layout.mydialog));
		final EditText edmyid = (EditText)layout.findViewById(R.id.adlet_id);
		final EditText edmyname = (EditText)layout.findViewById(R.id.adlet_name);
		final EditText edmytext=(EditText)layout.findViewById(R.id.adlet_text);
		edmyid.setText(id);
		edmyname.setText(name);
		//final EditText myEditText = (EditText)layout.findViewById(R.id.mdetname);
		AlertDialog.Builder dialog=new AlertDialog.Builder(dContext);
		dialog.setTitle("签到")
		.setView(layout);
		dialog.setNegativeButton("统计", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {				
				upnote(DataBluetooth.getuser("统计"));
			}
			
		});
		dialog.setNeutralButton("列表", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//测试				
				/*for(int a=11101;a<11111;a++){
					Log.d("tt", a+"send");
					String newte=DataBluetooth.send("aname"+a+
						DataBluetooth.key+"content"+a
						+DataBluetooth.key+a,a+"");
				}
				for(int a=11115;a<11120;a++){
					Log.d("tt", a+"send");
					String newte=DataBluetooth.send("aname"+a+
						DataBluetooth.key+"content"+a
						+DataBluetooth.key+a,a+"");
				}*/
				String test=DataBluetooth.getlist("列表");
				upnote(test);
			}
		})
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				name=edmyname.getText().toString();
				id=edmyid.getText().toString();
				String text=edmytext.getText().toString();
				appdata.savesign(name, id);
				Toast.makeText(dContext,"签到期间请勿更新笔记",1).show();
				if(adapter.isEnabled()){
					adapter.setName(name+DataBluetooth.key+DataBluetooth.keytext+text+DataBluetooth.key+id);
					Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
					discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);//设置持续时间（最多300秒）
					mContext.startActivity(discoveryIntent);
					adapter.startDiscovery();
					//handler.post(task);//立即调用
				}else{
					Toast.makeText(dContext, "正在打开蓝牙", 1).show();
					adapter.enable();
					findBluetooth();
					adapter.startDiscovery();
					btbluetooth.setBackgroundResource(R.drawable.ada_top_openbluetooth);
					 //btbluetooth.setTextColor(0xffFD172E);
					 //btbluetooth.setText("关闭\n蓝牙");
					 btbluetooth.setText(""+DataBluetooth.getsize());
					 mybluetooth=adapter.getName();
					 handler.postDelayed(task,5000);//延迟调用  
				}
				DataBluetooth.send(name+DataBluetooth.key+DataBluetooth.keytext+text+DataBluetooth.key+id,adapter.getAddress());
			}
		});
		dialog.show();
		
		
		
		
		
	}
	private void bluetooth() {
		if(!adapter.isEnabled()){
			adapter.enable();
		    btbluetooth.setBackgroundResource(R.drawable.ada_top_openbluetooth);
		    mybluetooth=adapter.getName();
		   // btbluetooth.setText("关闭\n蓝牙");
		   // btbluetooth.setTextColor(0xffFD172E);
		    appdata.savesign(name, id);
		 
			Toast.makeText(dContext, "正在打开蓝牙", 1).show();
			adapter.startDiscovery();
			findBluetooth();
			appdata.savesign(name, id);
			btbluetooth.setText(""+DataBluetooth.getsize());
		    handler.postDelayed(task, 5000);//立即调用  
		}else{
			adapter.setName(mybluetooth);
			Toast.makeText(dContext, "正在关闭蓝牙", 1).show();
			btbluetooth.setBackgroundResource(R.drawable.ada_top_closebluetooth);
			btbluetooth.setText("蓝牙已\n关闭");
			//btbluetooth.setTextColor(android.graphics.Color.BLACK);
			adapter. disable();
		}
		
	}
	//点击item的事件监听
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}
	
	public boolean onPrepareOptionsMenu(Menu menu) {
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	
	
	@Override
	public boolean onLongClick(View v) {
		return false;
	}

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
		return false;
	}
	@Override
	public void whichOperation(FilesFor filesFor, int size) {
		// TODO 自动生成的方法存根
		
	}
}
