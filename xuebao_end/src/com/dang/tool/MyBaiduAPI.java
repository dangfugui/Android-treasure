package com.dang.tool;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.uvchip.files.FileManager.ViewMode;

public class MyBaiduAPI {
	private Context dContext;
	//分享
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	
	boolean result;
	public MyBaiduAPI(Context context) {
		dContext=context;
	}
	
	/**
	 * 
	 * @param title 分享标题
	 * @param content  分享内容
	 * @param Url  点击打开的网页
	 */
	public boolean showsocial(String title,String content,String Url ) {
		result=false;
		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(dContext);
		mSocialShare.setClientId(MediaType.SINAWEIBO.toString(), Data.SINA_APP_KEY);
		mSocialShare.setClientId(MediaType.QZONE.toString(), "100358052");
		mSocialShare.setClientId(MediaType.QQFRIEND.toString(), "100358052");
		mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "百度");
		mSocialShare.setClientId(MediaType.WEIXIN.toString(), "wx329c742cb69b41b8");
		mImageContent.setTitle(title);
		mImageContent.setContent(content);
		mImageContent.setLinkUrl( Url);
		mImageContent.setImageUri(Uri.parse("http://bcs.duapp.com/mytreasure/aaa_treasure.jpg"));
		mSocialShare.show(((Activity) dContext).getWindow().getDecorView(), mImageContent, FrontiaTheme.DARK,  new FrontiaSocialShareListener() {
			public void onSuccess() {
				Toast.makeText(dContext, "分享成功",0).show();
				result = true;
			}
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(dContext, "分享失败:"+arg1, 1).show();
			}
			public void onCancel() {
			}
		});
		return result;
	}

}
