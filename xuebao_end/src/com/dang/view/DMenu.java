package com.dang.view;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.micode.notes.ui.NotesListActivity;

import org.zirco.ui.activities.BrowserMainActivity;

import com.uvchip.files.FileManager.FileFilter;
import com.uvchip.files.FileManager.FilesFor;
import com.uvchip.files.FileManager.ViewMode;
import com.uvchip.files.FileItemForOperation;
import com.baidu.feedback.FeedbackActivity;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaPersonalStorage;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.mobstat.d;
import com.dang.service.Buckups;
import com.dang.tool.Data;
import com.dang.treasure.R;
import com.diary.DiaryMain;
import com.uvchip.mediacenter.filebrowser.Browser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.sax.StartElementListener;
import android.text.format.Time;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DMenu extends Browser{
	final boolean DEBUG = false;
	private Context dContext;
	private String[] DMenuName= new String[]{"0-登陆","1-备份","2-还原","3-网络","4-笔记","5-时光","6-分享","7-反馈","8-关于","9-退出"};
	private int[]DmenuImageId=new int[]{R.drawable.abi_menu_login,R.drawable.abi_menu_buckups,
			R.drawable.abi_menu_recover,R.drawable.abi_menu_browser,R.drawable.abi_menu_note,R.drawable.abi_menu_diary,R.drawable.abi_menu_social,
			R.drawable.abi_menu_feedback,R.drawable.abi_menu_about,R.drawable.abi_menulog_out};
	//百度认证
	private FrontiaAuthorization authorization;
	private FrontiaPersonalStorage mCloudStorage;
	//分享
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	
	
	private boolean loginreply=false;
	static{
		TAG = DMenu.class.getCanonicalName();
		
	}
	private ListView mListView;
	private boolean onResume = false;
	
	
	public DMenu(Context context) {
		super(context);
		dContext=context;
		initView();
		mViewMode = ViewMode.LISTVIEW;
	}
	public void onResume(){
		if (!onResume) {//如果菜单为空 载入菜单
			QueryMenu();
			onResume = true;
		}
	}

	private void initView() {
		mView = mInflater.inflate(R.layout.music_browser, null);
		mListView = (ListView)mView.findViewById(R.id.lvMusicList);
		mListView.setOnItemClickListener(this);
	}

	
	public void QueryMenu() {
		//super.QueryData(preFile,clear,filter);

		List <HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();

		for(int i=0;i<DMenuName.length;i++){
			HashMap<String,Object>item=new HashMap<String, Object>();
			item.put("name",DMenuName[i]);
			item.put("image", DmenuImageId[i]);
			item.put("menuto",R.drawable.abi_menu_to);
			data.add(item);
		}

		SimpleAdapter adapter=new SimpleAdapter(dContext, data, R.layout.abi_item_menu,
				new String[]{"image","name","menuto"}, 
				new int[]{R.id.abiim_image,R.id.abitv_nenuname,R.id.abiim_image2});
		mListView.setAdapter(adapter);
	}
	
	//点击item的事件监听
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//FileItemForOperation fileItem = mData.getFileItems().get(position);
		//clickFileItem(fileItem);
		//Toast.makeText(mContext, "position:"+position+"id:"+id, 0).show();
		switch (position) {
		case 0:
			Intent intent0=new Intent(dContext,LogInActivity.class);
			dContext.startActivity(intent0);
			break;
		case 1:
			Intent intent1=new Intent(dContext,BackupsActivity.class);
			dContext.startActivity(intent1);
			break;
		case 2:
			Intent intent2=new Intent(dContext,RecoverActivity.class);
			dContext.startActivity(intent2);
			break;
		case 3://网络
			Intent intent3=new Intent(dContext,BrowserMainActivity.class);
			//intent3.putExtra("URL","www.baidu.com");
			dContext.startActivity(intent3);
			break;
		case 4://笔记
			Intent intent4=new Intent(dContext,NotesListActivity.class);
			dContext.startActivity(intent4);
			break;
		case 5://时光
			Intent intent5=new Intent(dContext,DiaryMain.class);
			dContext.startActivity(intent5);
			break;
		case 6://分享
			showsocial();
			break;
		case 7://反馈
			Intent intent6=new Intent(dContext,FeedbackActivity.class);
			dContext.startActivity(intent6);
			break;
		case 8://关于
			aboutDialog();
			break;
		case 9://退出
			((Activity) dContext).finish();
		default:
			break;
		}
	}
	//分享
	private void showsocial() {
		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(dContext);
		mSocialShare.setClientId(MediaType.SINAWEIBO.toString(), Data.SINA_APP_KEY);
		mSocialShare.setClientId(MediaType.QZONE.toString(), "100358052");
		mSocialShare.setClientId(MediaType.QQFRIEND.toString(), "100358052");
		mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "百度");
		mSocialShare.setClientId(MediaType.WEIXIN.toString(), "wx329c742cb69b41b8");
		mImageContent.setTitle("学宝");
		mImageContent.setContent("最好用的手机学习资源软件");
		mImageContent.setLinkUrl("http://www.sdnu.edu.cn/");
		mImageContent.setImageUri(Uri.parse("http://demo.sc.chinaz.com/Files/pic/icons/xt_0606_6/SkyLine%20025.png"));
		mSocialShare.show(((Activity) dContext).getWindow().getDecorView(), mImageContent, FrontiaTheme.DARK,  new FrontiaSocialShareListener() {
			public void onSuccess() {
				Toast.makeText(dContext, "分享成功",0).show();
			}
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(dContext, "分享失败:"+arg1, 1).show();
			}
			public void onCancel() {
			}
		});
	}
	//关于Dialog
	private void aboutDialog() {
		new AlertDialog.Builder(dContext)
		.setTitle("关于")
		.setMessage("第十届齐鲁软件设计大赛参赛作品\n百度Frontia API应用\nE-mail:dangqihe@163.com")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setNegativeButton("返回", null).show();
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
	//显示备份名称DiaLog
	private void buckupsDialog(){
		final EditText editText=new EditText(dContext);
		//获取当前时间
		SimpleDateFormat dDateFormat=new SimpleDateFormat("yyy-MM-dd  HH-mm");
		String date=dDateFormat.format(new Date());
		/*Time time=new Time();
		time.setToNow();
		String nowtime=time.year+"-"+time.month+"-"+time.monthDay+" "+time.hour+"-"+time.minute;*/
		editText.setText(date);
		new AlertDialog.Builder(dContext)
		.setTitle("备份")
		.setMessage("请输入备份名称    (默认为当前时间)")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setView( editText)
		.setPositiveButton("开始备份", new DialogInterface.OnClickListener(){
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				String edstr=editText.getText().toString();
				Intent intent=new Intent(dContext,Buckups.class);
				intent.putExtra("name",edstr);
				dContext.startService(intent);
			}
		})
		.setNegativeButton("取消", null).show();
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
		return false;
	}
}
