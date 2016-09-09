package com.dang.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.micode.notes.ui.NotesListActivity;

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
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
public class MenuAdapt extends ListFragment {
	
	private Context dContext;
	//分享
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	
	private String[] DMenuName= new String[]{"账户","备份","还原","网络","笔记","时光","分享","反馈","关于","退出"};
	private int[]DmenuImageId=new int[]{R.drawable.abi_menu_login,R.drawable.abi_menu_buckups,
			R.drawable.abi_menu_recover,R.drawable.abi_menu_browser,R.drawable.abi_menu_note,R.drawable.abi_menu_diary,R.drawable.abi_menu_social,
			R.drawable.abi_menu_feedback,R.drawable.abi_menu_about,R.drawable.abi_menulog_out};

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.aea_list_menu, null);
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

		SimpleAdapter adapter=new SimpleAdapter(getActivity(), data, R.layout.abi_item_menu,
				new String[]{"image","name","menuto"}, 
				new int[]{R.id.abiim_image,R.id.abitv_nenuname,R.id.abiim_image2});
		setListAdapter(adapter);
	}
	public void onListItemClick(ListView lv, View v, int position, long id) {
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
			.setMessage(R.string.abouttext)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setNegativeButton("返回", null).show();
		}

}
