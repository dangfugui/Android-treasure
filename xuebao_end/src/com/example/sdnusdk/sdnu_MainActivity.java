package com.example.sdnusdk;

import com.dang.treasure.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.sdnu.i.util.oauth.AppSDNU;
import cn.edu.sdnu.i.util.oauth.Constants;
import cn.edu.sdnu.i.util.oauth.Oauth;

public class sdnu_MainActivity  extends Activity{
	TextView text;
	TextView textDisp ;
	private String resultDisp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sdnu_mainactivity);
		Oauth.startThread(mHandler, AppSDNU.get(Constants.BASE_URL)   //获取卡通信息.
				+ AppSDNU.get(Constants.REST_URL) +"people/get",Oauth.METHOD,sdnu_MainActivity.this);
		textDisp = (TextView)findViewById(R.id.textView1);
		((Button)findViewById(R.id.getPeople)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 Oauth.startThread(mHandler, AppSDNU.get(Constants.BASE_URL)   //获取卡通信息.
							+ AppSDNU.get(Constants.REST_URL) +"people/get",Oauth.METHOD,sdnu_MainActivity.this);	
			}			
		});
		((Button)findViewById(R.id.logout)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//置空操作.注销其实就是 清空  token对应的值.
				Oauth.setToken("", "");
				//清空储存密钥
				sdnu_Utils.saveTokenValue(sdnu_MainActivity.this, "", sdnu_Utils.TOKENVALUE, Context.MODE_PRIVATE);
				Toast.makeText(getApplication(), "注销成功", Toast.LENGTH_SHORT).show();
			}			
		});		
	}	
	private  Handler mHandler =new Handler(new Handler.Callback(){		
		String result = null;
		@Override
		public boolean  handleMessage(Message msg){
			result = (String)msg.obj;
			switch (msg.what) {  			
			 case  Oauth.ERROR:
            	Toast.makeText(sdnu_MainActivity.this, "网络无连接,请检查网络!", Toast.LENGTH_SHORT).show();
            	break;	
			case Oauth.METHOD:
				decodeMethod(result);
            default: 	
                break;  
            } 			
			return false;
		}
		
	}); 

	private void decodeMethod(String result2) {
		// TODO Auto-generated method stub
		resultDisp+= (result2+"\n");
		textDisp.setText(resultDisp);
		text=(TextView) findViewById(R.id.sdnu_dang);
		String dang = "";
		dang+="欢迎登陆智慧山师\n学号:"+result2.substring(result2.indexOf("identityNumber")+14+3,result2.indexOf("name")-3);
		dang+="\n姓名:"+result2.substring(result2.indexOf("name")+4+3,result2.indexOf("idCard")-3);
		dang+="\n性别:"+result2.substring(result2.indexOf("sex")+3+3,result2.indexOf("nation")-3);
		dang+="\n学院:"+result2.substring(result2.indexOf("organizationName")+16+3,result2.indexOf("\"}"));
		text.setText(dang);
	}	
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	
	
	

	
}
