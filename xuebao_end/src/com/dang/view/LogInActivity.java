package com.dang.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.api.FrontiaAuthorization;
import com.baidu.frontia.api.FrontiaStatistics;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaAuthorizationListener.AuthorizationListener;
import com.baidu.frontia.api.FrontiaAuthorizationListener.UserInfoListener;
import com.dang.tool.Data;
import com.dang.treasure.R;

public class LogInActivity extends Activity {
	private FrontiaStatistics stat;
	
	private TextView mResultTextView;
	private FrontiaAuthorization mAuthorization;
	private final static String Scope_Basic = "basic";
  	
	private final static String Scope_Netdisk = "netdisk";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.afbd_activity_login);
		mAuthorization = Frontia.getAuthorization();
		setupViews();
	}

	private void setupViews() {
		stat = Frontia.getStatistics();
		mResultTextView = (TextView)findViewById(R.id.social_result);

		Button sinaweiboButton = (Button) findViewById(R.id.sinaBtn);
		sinaweiboButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startSinaWeiboLogin();
			}

		});

		Button sinaweiboCancelButton = (Button) findViewById(R.id.sinaQuitBtn);
		sinaweiboCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startSinaWeiboLogout();
			}

		});
		
		Button sinaweiboStatusButton = (Button) findViewById(R.id.sinaStatusBtn);
		sinaweiboStatusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startSinaWeiboStatus();
			}

		});
		
		Button sinaweiboUserInfoButton = (Button) findViewById(R.id.sinaInfoBtn);
		sinaweiboUserInfoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startSinaWeiboUserInfo();
			}

		});

		Button qqweiboButton = (Button) findViewById(R.id.tecentBtn);
		qqweiboButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startQQWeibo();
			}

		});

		Button qqweiboCancelButton = (Button) findViewById(R.id.tecentQuitBtn);
		qqweiboCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startQQWeiboLogout();
			}

		});
		
		Button qqweiboStatusButton = (Button) findViewById(R.id.tecentStatusBtn);
		qqweiboStatusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startQQWeiboStatus();
			}

		});
		
		Button qqweiboUserInfoButton = (Button) findViewById(R.id.tecentInfoBtn);
		qqweiboUserInfoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startQQWeiboUserInfo();
			}

		});

		Button qqzoneButton = (Button) findViewById(R.id.qqzoneBtn);
		qqzoneButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startQQZone();
			}

		});

		Button qqzoneCancelButton = (Button) findViewById(R.id.qqzoneQuitBtn);
		qqzoneCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startQQZoneLogout();
			}

		});
		
		Button qqzoneStatusButton = (Button) findViewById(R.id.qqzoneStatusBtn);
		qqzoneStatusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startQQZoneStatus();
			}

		});
		
		Button qqzoneUserInfoButton = (Button) findViewById(R.id.qqzoneInfoBtn);
		qqzoneUserInfoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startQQZoneUserInfo();
			}

		});
		
		Button kaixinButton = (Button) findViewById(R.id.kaixinBtn);
		kaixinButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startKaixin();
			}

		});

		Button kaixinCancelButton = (Button) findViewById(R.id.kaixinQuitBtn);
		kaixinCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startKaixinLogout();
			}

		});
		
		Button kaixinStatusButton = (Button) findViewById(R.id.kaixinStatusBtn);
		kaixinStatusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startKaixinStatus();
			}

		});
		
		Button kaixinUserInfoButton = (Button) findViewById(R.id.kaixinInfoBtn);
		kaixinUserInfoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startKaixinUserInfo();
			}

		});
		
		Button renrenButton = (Button) findViewById(R.id.renrenBtn);
		renrenButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startRenren();
			}

		});

		Button renrenCancelButton = (Button) findViewById(R.id.renrenQuitBtn);
		renrenCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startRenrenLogout();
			}

		});
		
		Button renrenStatusButton = (Button) findViewById(R.id.renrenStatusBtn);
		renrenStatusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startRenrenStatus();
			}

		});
		
		Button renrenUserInfoButton = (Button) findViewById(R.id.renrenInfoBtn);
		renrenUserInfoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startRenrenUserInfo();
			}

		});
		
		Button baiduButton = (Button) findViewById(R.id.baiduBtn);
		baiduButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startBaidu();
			}

		});

		Button baiduCancelButton = (Button) findViewById(R.id.baiduQuitBtn);
		baiduCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startBaiduLogout();
			}

		});
		
		Button baiduStatusButton = (Button) findViewById(R.id.baiduStatusBtn);
		baiduStatusButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startBaiduStatus();
			}

		});
		
		Button baiduUserInfoButton = (Button) findViewById(R.id.baiduInfoBtn);
		baiduUserInfoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startBaiduUserInfo();
			}

		});

		Button allCancelButton = (Button) findViewById(R.id.allQuit);
		allCancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startAllLogout();
			}

		});

	}

	protected void startKaixinUserInfo() {
		userinfo(MediaType.KAIXIN.toString());
	}
	
	protected void startRenrenUserInfo() {
		userinfo(MediaType.RENREN.toString());
	}

	protected void startQQZoneUserInfo() {
		userinfo(MediaType.QZONE.toString());
		
	}

	protected void startQQWeiboUserInfo() {
		userinfo(MediaType.QQWEIBO.toString());
		
	}

	protected void startSinaWeiboUserInfo() {
		userinfo(MediaType.SINAWEIBO.toString());
		
	}

	protected void startBaiduUserInfo() {
		userinfo(MediaType.BAIDU.toString());
		
	}

	protected void startKaixinStatus() {
		boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.KAIXIN.toString());
		if (result) {
			mResultTextView.setText("已经登录开心网帐号");
		} else {
			mResultTextView.setText("未登录开心网帐号");
		}
	}
	
	protected void startRenrenStatus() {
		boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.RENREN.toString());
		if (result) {
			mResultTextView.setText("已经登录人人网帐号");
		} else {
			mResultTextView.setText("未登录人人网帐号");
		}
	}

	protected void startQQZoneStatus() {
		boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.QZONE.toString());
		if (result) {
			mResultTextView.setText("已经登录QQ空间帐号");
		} else {
			mResultTextView.setText("未登录QQ空间帐号");
		}
	}

	protected void startQQWeiboStatus() {
		boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.QQWEIBO.toString());
		if (result) {
			mResultTextView.setText("已经登录QQ微博帐号");
		} else {
			mResultTextView.setText("未登录QQ微博帐号");
		}
	}

	protected void startSinaWeiboStatus() {
		boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.SINAWEIBO.toString());
		if (result) {
			mResultTextView.setText("已经登录新浪微博帐号");
		} else {
			mResultTextView.setText("未登录新浪微博帐号");
		}
	}

	protected void startBaiduStatus() {
		boolean result = mAuthorization.isAuthorizationReady(FrontiaAuthorization.MediaType.BAIDU.toString());
		if (result) {
			mResultTextView.setText("已经登录百度帐号");
		} else {
			mResultTextView.setText("未登录百度帐号");
		}
	}

	protected void startBaiduLogout() {
		boolean result = mAuthorization.clearAuthorizationInfo(
				FrontiaAuthorization.MediaType.BAIDU.toString());
		if (result) {
		    Frontia.setCurrentAccount(null);
			mResultTextView.setText("百度退出成功");
		} else {
			mResultTextView.setText("百度退出失败");
		}
	}

	protected void startBaidu() {
		ArrayList<String> scope = new ArrayList<String>();
    	scope.add(Scope_Basic);
    	scope.add(Scope_Netdisk);
		mAuthorization.authorize(this,FrontiaAuthorization.MediaType.BAIDU.toString(), scope, new AuthorizationListener() {

					@Override
					public void onSuccess(FrontiaUser result) {
					    Frontia.setCurrentAccount(result);
						if (null != mResultTextView) {
                            mResultTextView.setText(
                                    "social id: " + result.getId() + "\n"
                                            + "token: " + result.getAccessToken() + "\n"
                                            + "expired: " + result.getExpiresIn());
						}
					}

					@Override
					public void onFailure(int errCode, String errMsg) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errCode
									+ ", errMsg:" + errMsg);
						}
					}

					@Override
					public void onCancel() {
						if (null != mResultTextView) {
							mResultTextView.setText("cancel");
						}
					}

				});
	}

	private void userinfo(String accessToken) {
		mAuthorization.getUserInfo(accessToken, new UserInfoListener() {

			@Override
			public void onSuccess(FrontiaUser.FrontiaUserDetail result) {
				if (null != mResultTextView) {
					String resultStr = "username:" + result.getName() + "\n"
							+ "birthday:" + result.getBirthday() + "\n"
							+ "city:" + result.getCity() + "\n"
							+ "province:" + result.getProvince() + "\n"
							+ "sex:" + result.getSex() + "\n"
							+ "pic url:" + result.getHeadUrl() + "\n";
					mResultTextView.setText(resultStr);
				}
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				if (null != mResultTextView) {
					mResultTextView.setText("errCode:" + errCode
							+ ", errMsg:" + errMsg);
				}
			}
			
		});
	}

	private void startSinaWeiboLogin() {
		mAuthorization.enableSSO(MediaType.SINAWEIBO.toString(),Data.SINA_APP_KEY);
		mAuthorization.authorize(this,
				FrontiaAuthorization.MediaType.SINAWEIBO.toString(),
				new AuthorizationListener() {

					@Override
					public void onSuccess(FrontiaUser result) {
					    Frontia.setCurrentAccount(result);
						if (null != mResultTextView) {
							String log = "social id: " + result.getId() + "\n"
                                    + "token 象征;记号: " + result.getAccessToken() + "\n"
                                    + "expired 文件、协议: " + result.getExpiresIn();

							mResultTextView.setText(log);
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode错误代码:" + errorCode
									+ ", errMsg:" + errorMessage);
						}
					}

					@Override
					public void onCancel() {
						if (null != mResultTextView) {
							mResultTextView.setText("cancel");
						}
					}

				});
	}

	private void startSinaWeiboLogout() {
		boolean result = mAuthorization.clearAuthorizationInfo(
				FrontiaAuthorization.MediaType.SINAWEIBO.toString());
		if (result) {
		    Frontia.setCurrentAccount(null);
			mResultTextView.setText("新浪微博退出成功");
		} else {
			mResultTextView.setText("新浪微博退出失败");
		}
	}

	private void startQQWeibo() {
		mAuthorization.authorize(this, FrontiaAuthorization.MediaType.QQWEIBO.toString(),
				new AuthorizationListener() {

					@Override
					public void onSuccess(FrontiaUser result) {
					    Frontia.setCurrentAccount(result);
						if (null != mResultTextView) {

                            String log = "social id: " + result.getId() + "\n"
                                    + "token: " + result.getAccessToken() + "\n"
                                    + "expired: " + result.getExpiresIn();

							mResultTextView.setText(log);
                            Log.d("SocialDialog", log);
                        }
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errorCode
									+ ", errMsg:" + errorMessage);
						}
					}

					@Override
					public void onCancel() {
						if (null != mResultTextView) {
							mResultTextView.setText("cancel");
						}
					}

				});
	}

	private void startQQWeiboLogout() {
		boolean result = mAuthorization.clearAuthorizationInfo(
				FrontiaAuthorization.MediaType.QQWEIBO.toString());
		if (result) {
		    Frontia.setCurrentAccount(null);
			mResultTextView.setText("qq微博退出成功");
		} else {
			mResultTextView.setText("qq微博退出失败");
		}
	}
	
	private void startQQZone() {
		mAuthorization.authorize(this, FrontiaAuthorization.MediaType.QZONE.toString(),
				new AuthorizationListener() {

					@Override
					public void onSuccess(FrontiaUser result) {
					    Frontia.setCurrentAccount(result);
						if (null != mResultTextView) {
                            String log = "social id: " + result.getId() + "\n"
                                    + "token: " + result.getAccessToken() + "\n"
                                    + "expired: " + result.getExpiresIn();

                            mResultTextView.setText(log);
                            Log.d("SocialDialog", log);
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errorCode
									+ ", errMsg:" + errorMessage);
						}
					}

					@Override
					public void onCancel() {
						if (null != mResultTextView) {
							mResultTextView.setText("cancel");
						}
					}

				});
	}

	private void startQQZoneLogout() {
		boolean result = mAuthorization.clearAuthorizationInfo(
				FrontiaAuthorization.MediaType.QZONE.toString());
		if (result) {
		    Frontia.setCurrentAccount(null);
			mResultTextView.setText("qq空间退出成功");
		} else {
			mResultTextView.setText("qq空间退出失败");
		}
	}

	private void startKaixin() {
		mAuthorization.authorize(this, FrontiaAuthorization.MediaType.KAIXIN.toString(),
				new AuthorizationListener() {

					@Override
					public void onSuccess(FrontiaUser result) {
					    Frontia.setCurrentAccount(result);
						if (null != mResultTextView) {
							mResultTextView.setText("token:"
									+ result.getAccessToken());
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errorCode
									+ ", errMsg:" + errorMessage);
						}
					}

					@Override
					public void onCancel() {
						if (null != mResultTextView) {
							mResultTextView.setText("cancel");
						}
					}

				});
	}
	
	private void startRenren() {
		mAuthorization.authorize(this, FrontiaAuthorization.MediaType.RENREN.toString(),
				new AuthorizationListener() {

					@Override
					public void onSuccess(FrontiaUser result) {
					    Frontia.setCurrentAccount(result);
						if (null != mResultTextView) {
							mResultTextView.setText("token:"
									+ result.getAccessToken());
						}
					}

					@Override
					public void onFailure(int errorCode, String errorMessage) {
						if (null != mResultTextView) {
							mResultTextView.setText("errCode:" + errorCode
									+ ", errMsg:" + errorMessage);
						}
					}

					@Override
					public void onCancel() {
						if (null != mResultTextView) {
							mResultTextView.setText("cancel");
						}
					}

				});
	}

	private void startKaixinLogout() {
		boolean result = mAuthorization.clearAuthorizationInfo(
				FrontiaAuthorization.MediaType.KAIXIN.toString());
		if (result) {
		    Frontia.setCurrentAccount(null);
			mResultTextView.setText("开心网退出成功");
		} else {
			mResultTextView.setText("开心网退出失败");
		}
	}
	
	private void startRenrenLogout() {
		boolean result = mAuthorization.clearAuthorizationInfo(
				FrontiaAuthorization.MediaType.RENREN.toString());
		if (result) {
		    Frontia.setCurrentAccount(null);
			mResultTextView.setText("人人网退出成功");
		} else {
			mResultTextView.setText("人人网退出失败");
		}
	}

	private void startAllLogout() {
		boolean result = mAuthorization.clearAllAuthorizationInfos();
		if (result) {
		    Frontia.setCurrentAccount(null);
			mResultTextView.setText("所有登录退出成功");
		} else {
			mResultTextView.setText("所有登录退出失败");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (null != mAuthorization) {
			mAuthorization.onActivityResult(requestCode, resultCode, data);
		}
	}
	//统计
	public void onResume() {
		super.onResume();
		stat.pageviewStart(this, "LogIn");
	}

	public void onPause() {
		super.onPause();
		stat.pageviewEnd(this, null);
	}
	
	
}
