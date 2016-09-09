package com.dang.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import name.bagi.levente.pedometer.Pedometer;
import net.micode.notes.ui.NotesListActivity;
import net.micode.soundrecorder.SoundRecorder;

import org.zirco.ui.activities.BrowserMainActivity;

import com.baidu.feedback.FeedbackActivity;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.dang.tool.Data;
import com.dang.treasure.R;
import com.dang.view.BackupsActivity;
import com.dang.view.LogInActivity;
import com.dang.view.RecoverActivity;
import com.diary.DiaryMain;
import com.uvchip.files.FileManager.ViewMode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author yangyu
 *	功能描述：列表Fragment，用来显示滑动菜单打开后的内容
 */

@SuppressLint("ValidFragment")
public class MenuAdapt3 extends ListFragment {
	private static final int GET_IMAGE_VIA_CAMERA = 0;
	private static final int RESULT_OK = 0;
	List <HashMap<String, Object>> data;
	private Context dContext;
	private GridView gridview;
	private ImageButton btin;
	private Button btout;
	private TextView tvin;
	String localTempImgDir="Treasure/我的照片";
	String localTempImgFileName="a.jpg";
	//分享
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	
	private String[] DMenuName= new String[]{"备份","还原","笔记","时光","录音","拍照","运动","网络","分享","山师圈","反馈","关于"};
	private int[]DmenuImageId=new int[]{R.drawable.abi_menu_buckups,
			R.drawable.abi_menu_recover,R.drawable.abi_menu_note,R.drawable.abi_menu_diary,
			R.drawable.abi_menu_liying,R.drawable.abi_menu_camera,R.drawable.abi_menu_run,R.drawable.abi_menu_browser,R.drawable.abi_menu_social,
			R.drawable.abi_menu_sdnu,R.drawable.abi_menu_feedback,R.drawable.abi_menu_about};

	public void onCreate(Bundle savedInstanceState) {
	    // TODO Auto-generated method stub
	    super.onCreate(savedInstanceState);
	    data=new ArrayList<HashMap<String,Object>>();

	  }

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.aea_grid_menu, null);
		gridview=(GridView) view.findViewById(R.id.aeagr_gridview);
		btin=(ImageButton) view.findViewById(R.id.aeabt_in);
		btin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent0=new Intent(dContext,com.example.sdnusdk.sdun_WelcomeActivity.class);
				dContext.startActivity(intent0);
			}
		});
		tvin= (TextView) view.findViewById(R.id.aeatv_in);
		tvin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent0=new Intent(dContext,LogInActivity.class);
				dContext.startActivity(intent0);				
			}
		});
		btout=(Button) view.findViewById(R.id.aeabt_out);
		btout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((Activity) dContext).finish();
			}
		});
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Toast.makeText(dContext, ""+position, 0).show();
				switch (position) {
				case 0:
					Intent intent0=new Intent(dContext,BackupsActivity.class);
					dContext.startActivity(intent0);
					break;
				case 1:
					Intent intent1=new Intent(dContext,RecoverActivity.class);
					dContext.startActivity(intent1);
					break;
				case 7://网络
					Intent intent4=new Intent(dContext,BrowserMainActivity.class);
					//intent3.putExtra("URL","www.baidu.com");
					dContext.startActivity(intent4);
					break;
				case 6://run
					Intent intent5=new Intent(dContext,Pedometer.class);
					dContext.startActivity(intent5);
					break;
				case 4://录音
					Intent intent51=new Intent(dContext,SoundRecorder.class);
					dContext.startActivity(intent51);
					break;
				case 5:
					Camera();
					break;
				case 2://笔记
					Intent intent2=new Intent(dContext,NotesListActivity.class);
					dContext.startActivity(intent2);
					break;
				case 3://时光
					Intent intent3=new Intent(dContext,DiaryMain.class);
					dContext.startActivity(intent3);
					break;
				case 8://分享
					showsocial();
					break;
				case 9://sdnu
					Intent intent19=new Intent(dContext,com.dream.myqiyi.MainActivity.class);
					dContext.startActivity(intent19);
					break;
				case 10://反馈
					Intent intent6=new Intent(dContext,FeedbackActivity.class);
					dContext.startActivity(intent6);
					break;
				case 11://关于
					aboutDialog();
					break;
				default:
					break;
				}
				
			}
		});
		return view;
	}
	private void Camera() {
		File file = new File(Data.Mycamerapath); 
        if(!file.exists()){
			file.mkdirs();
		}
    	SimpleDateFormat dDateFormat=new SimpleDateFormat("yyy-MM-dd HH-mm");
		String date=dDateFormat.format(new Date());
		Time time=new Time();
		time.setToNow();
		String nowtime=time.year+"-"+time.month+"-"+time.monthDay+" "+time.hour+":"+time.minute;
        localTempImgFileName=nowtime+".jpg";
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
		String status=Environment.getExternalStorageState(); 
		if(status.equals(Environment.MEDIA_MOUNTED)) 
		{ 
		try { 
		File dir=new File(Environment.getExternalStorageDirectory() + "/"+localTempImgDir); 
		if(!dir.exists())dir.mkdirs(); 

		Intent intent=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		File f=new File(dir, localTempImgFileName);//localTempImgDir和localTempImageFileName是自己定义的名字 
		Uri u=Uri.fromFile(f); 
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0); 
		intent.putExtra(MediaStore.EXTRA_OUTPUT, u); 
		startActivityForResult(intent, GET_IMAGE_VIA_CAMERA); 
		} catch (ActivityNotFoundException  e) {
			}
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if(resultCode==RESULT_OK ) 
		{ 
		switch(requestCode) 
		{ 
		case GET_IMAGE_VIA_CAMERA:
		File f=new File(Environment.getExternalStorageDirectory() 
				
		+"/"+localTempImgDir+"/"+localTempImgFileName); 
		try { 
		   Uri u = 
		   Uri.parse(android.provider.MediaStore.Images.Media.insertImage(dContext.getContentResolver(), 
		   f.getAbsolutePath(), null, null)); 
		   //u就是拍摄获得的原始图片的uri，剩下的你想干神马坏事请便…… 
		} catch (FileNotFoundException e) { 
		   // TODO Auto-generated catch block 
		   e.printStackTrace(); 
		} 
		break; 
		} 
		} 
		super.onActivityResult(requestCode, resultCode, data); 
		} 


	public void onActivityCreated(Bundle savedInstanceState) {
		dContext=getActivity();
		super.onActivityCreated(savedInstanceState);
		//SampleAdapter adapter = new SampleAdapter(getActivity());
		List <HashMap<String, Object>> data=new ArrayList<HashMap<String,Object>>();		
		for(int i=0;i<DMenuName.length;i++){
		HashMap<String,Object>item=new HashMap<String, Object>();
		item.put("name",DMenuName[i]);
		item.put("image", DmenuImageId[i]);
		item.put("menuto",R.drawable.abi_menu_to);
		data.add(item);
		}

		/*SimpleAdapter adapter=new SimpleAdapter(getActivity(), data, R.layout.abi_item_menu,
				new String[]{"image","name","menuto"}, 
				new int[]{R.id.abiim_image,R.id.abitv_nenuname,R.id.abiim_image2});
		setListAdapter(adapter);*/
	
		SimpleAdapter gridadapter=new SimpleAdapter(getActivity(), data, R.layout.aei_griditem_menu,
				new String[]{"image","name"}, 
				new int[]{R.id.aeiim_image,R.id.aeitv_text});
		gridview.setAdapter(gridadapter);
		
	}
	public void onListItemClick(ListView lv, View v, int position, long id) {
		
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
		mImageContent.setImageUri(Uri.parse("http://bcs.duapp.com/mytreasure/aaa_treasure.jpg"));
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
			.setMessage(R.string.abouttext)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setNegativeButton("返回", null).show();
		}

}
