package com.uvchip.mediacenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaStatistics;
import com.baidu.mobstat.SendStrategyEnum;
import com.dang.service.MenuAdapt;
import com.dang.service.MenuAdapt2;
import com.dang.service.MenuAdapt3;
import com.dang.service.MenuAdapt4;
import com.dang.tool.Data;
import com.dang.treasure.R;
import com.dang.view.DCloud;
import com.dang.view.DBluetooth;
import com.dang.view.DWeb;
import com.hc.module.animtab.AnimTabLayout;
import com.hc.module.animtab.AnimTabLayout.OnTabChangeListener;
import com.hc.modules.aboutus.AboutUs;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.uvchip.mediacenter.filebrowser.Browser;
import com.uvchip.mediacenter.filebrowser.FileBrowser;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.animation.Interpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private String TAG = "MainActivity";
	private ViewPager mViewPager;						//叶卡内容
	// private PagerTitleStrip mPagerTitleStrip;
	private FileBrowser mFileBrowser;
	private DCloud dCloud;
	private DWeb dWeb;
	//private MusicFileBrowser mMusicFileBrowser;
	//private VideoFileBrowser mVideoFileBrowser;
	//private ImageFileBrowser mImageFileBrowser;
	private DBluetooth dBluetooth;
	private List<View> mViews;							//Tab页面内容
	private List<String> mTitles;
	public static int mScreenWidth;
	private AnimTabLayout mAnimTab;
	private SDCardChangeReceiver mReceiver;
	private CanvasTransformer mTransformer;  
	private FrontiaStatistics stat;//百度统计
	
	private final int MENU_ABOUT_US     = Menu.FIRST;
	private SlidingMenu menu;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置标题
				setTitle("Attach");
		setContentView(R.layout.activity_main);
		boolean a=Frontia.init(MainActivity.this, Data.APIKEY);
		
		dInitialize();//初始化  建文件夹
		mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		mFileBrowser = new FileBrowser(this);
		dCloud=new DCloud(this);
		dWeb=new DWeb(this);
		//mMusicFileBrowser = new MusicFileBrowser(this);
		//mVideoFileBrowser = new VideoFileBrowser(this);
		//mImageFileBrowser = new ImageFileBrowser(this);
		dBluetooth=new DBluetooth(this);
		initView();
		mReceiver = new SDCardChangeReceiver();

		//初始化滑动菜单
		initAnimation();  
		initSlidingMenu();  
	}
	private void initSlidingMenu() {
		DisplayMetrics dm = new DisplayMetrics();getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		
		// 设置滑动菜单的属性值
		menu = new SlidingMenu(this);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//设置滑动的屏幕范围，该设置为边缘区域都可以滑动
		menu.setFadeDegree(0.35f);//SlidingMenu滑动时的渐变程度
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);//使SlidingMenu附加在Activity上
		menu.setMode(SlidingMenu.LEFT);//设置左滑菜单
		menu.setShadowDrawable(R.drawable.shadow);//设置阴影图片
		menu.setShadowWidthRes(R.dimen.shadow_width);//设置阴影图片的宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);//SlidingMenu划出时主页面显示的剩余宽度
		menu.setBehindWidth((int)width/5*3);//设置SlidingMenu菜单的宽度
		menu.setBehindCanvasTransformer(mTransformer); 
		// 设置滑动菜单的视图界面
		menu.setMenu(R.layout.aea_menu_background);	//设置menu的布局文件
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuAdapt3()).commit();
		menu.showMenu(true);//默认打开菜单
	
	}
	
	//动画效果
	private static Interpolator interp = new Interpolator() {   
        public float getInterpolation(float t) {  
            t -= 1.0f;  
            return t * t * t + 1.0f;  
        }         
    };  
	/* private void initAnimation(){                 
	        mTransformer = new CanvasTransformer(){  
	            @Override  
	            public void transformCanvas(Canvas canvas, float percentOpen) {  
	                canvas.translate(0, canvas.getHeight() * (1 - interp.getInterpolation(percentOpen)));         
	            }         
	        };  
	 }*/
	 private void initAnimation(){  
	        mTransformer = new CanvasTransformer(){  
	            @Override  
	            public void transformCanvas(Canvas canvas, float percentOpen) {  
	                float scale = (float) (percentOpen*0.25 + 0.75);  
	                canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);                
	            }  
	              
	        };  
	    } 
	
	private void dInitialize() {
		File file=new File(Data.Myfilepath);
		if(!file.exists()){
			file.mkdirs();
		}
		Frontia.init(this.getApplicationContext(),Data.APIKEY);
        stat = Frontia.getStatistics();

        stat.setAppDistributionChannel(Data.channel, true);//设置应用发布渠道
        stat.setSessionTimeout(30);//测试时，可以使用1秒钟session过期，这样不断的启动退出会产生大量日志。
        stat.enableExceptionLog();//开启异常日志
        stat.setReportId(Data.ReportID);//reportId必须在mtj网站上注册生成，该设置也可以在AndroidManifest.xml中填写
        //第一个参数为日志发送策略
        //第二个参数为日志发送策略设备周期性发送
        //第三个参数为日志发送间隔
        //第四个参数为是否只在wifi情况下发送日志
        stat.start(SendStrategyEnum.SET_TIME_INTERVAL, 10, 10, true);
	}
	@Override
	protected void onResume() {
		super.onResume();
		stat.pageviewStart(this, "Main");
		registerSDCardChangeReceiver();
	}
	@Override
	protected void onPause() {
		super.onPause();
		stat.pageviewEnd(this, null);
		unregisterReceiver(mReceiver);
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		Browser browser = getCurrBrowser();
		//点击返回键关闭滑动菜单
			if (menu.isMenuShowing()) {
				menu.showContent();
			} else {
	
				if (browser != null) {
					if (!browser.onBackPressed()) {
						super.onBackPressed();
					}
				}
			}
	}

    private void registerSDCardChangeReceiver(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mReceiver, filter);
    }
	PagerAdapter mPagerAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return 400;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
//			 ((ViewPager)container).removeView(mViews.get(position % mViews.size()));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitles.get(position);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			try {
				((ViewPager) container).addView(
						mViews.get(position % mViews.size()), 0);
			} catch (Exception e) {
			}
			return mViews.get(position % mViews.size());
		}
	};
	BaseAdapter tabAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new TextView(MainActivity.this);
			}
			((TextView) convertView).setText(mTitles.get(position));
			((TextView) convertView).setTextAppearance(MainActivity.this,R.style.tvTitle);
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return mTitles.size();
		}
	};

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(4 * 50);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int index) {
				//Toast.makeText(getApplicationContext(), ""+index, 1).show();
				Log.d("dd",index+"");
				index=index;
				index = index % mViews.size();
				mAnimTab.moveTo(index);
				
				switch (index) {
				case 2:
					mFileBrowser.onResume();
					break;
				case 3:
					dCloud.onResume();
				/*case 1:
					mMusicFileBrowser.onResume();
					break;
				case 1:
					mVideoFileBrowser.onResume();
					break;
				case 2:
					mImageFileBrowser.onResume();
					break;*/
				case 1:
					dWeb.onResume();
				case 0:
					dBluetooth.onResume();
					break;
				default:
					break;
				}
			}
		});

		mViews = new ArrayList<View>();
		mViews.add(dBluetooth.getView());
		mViews.add(dWeb.getView());
		mViews.add(mFileBrowser.getView());
		mViews.add(dCloud.getView());
		//mViews.add(mMusicFileBrowser.getView());
		//mViews.add(mVideoFileBrowser.getView());
		//mViews.add(mImageFileBrowser.getView());
		mTitles = new ArrayList<String>();
		mTitles.add("  课堂  ");
		mTitles.add("  资源  ");
		mTitles.add("  文件  ");
		mTitles.add("  云盘  ");
		//mTitles.add(getString(R.string.music_browser));
		//mTitles.add(getString(R.string.video_browser));
		//mTitles.add(getString(R.string.image_browser));
		mAnimTab = (AnimTabLayout) findViewById(R.id.animTab);
//		mAnimTab.setBackgroundResource(R.drawable.topbar_bg);
		mAnimTab.setAdapter(tabAdapter);
		mAnimTab.setOnTabChangeListener(new OnTabChangeListener() {
			public void tabChange(int index) {
				int curr = mViewPager.getCurrentItem();
				int realIndex = curr % mViews.size();
				int toIndex = curr + (index - realIndex);
				Log.i(TAG, "index:" + index + " curr:" + curr + " realIndex:" + realIndex + " toIndex:" + toIndex);
				mViewPager.setCurrentItem(toIndex, false);
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.i(TAG, "newConfig========>" + newConfig);
		mFileBrowser.onConfigurationChanged(newConfig);
		super.onConfigurationChanged(newConfig);
	}

	private Browser getCurrBrowser(){
		int index = mViewPager.getCurrentItem() % mViews.size();
		//关于菜单
		switch (index) {
		case 2:
			return mFileBrowser;
		case 3:
			return dCloud;
		case 1:
			return dWeb;
		case 0:
			return dBluetooth;
		default:
			return null;
		}
		
	}
	
	//添加菜单
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		Browser browser = getCurrBrowser();
		if (browser != null) {
			browser.onPrepareOptionsMenu(menu);
		}
		menu.add(1, MENU_ABOUT_US, Menu.NONE, R.string.menu_about_us).setIcon(android.R.drawable.ic_menu_info_details);
		return true;
	}
	
	public boolean onCreateOptionsMenu(Menu menu1) {
		menu.showMenu();
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Browser browser = getCurrBrowser();
		if (browser != null) {
			if (browser.onOptionsItemSelected(item)){
				return true;
			}
		}
		switch (item.getItemId()) {
        case MENU_ABOUT_US:
        	AboutUs.getAboutUsDialog(this).show();
        	return true;
        default:
            break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		mFileBrowser.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void onContextMenuClosed(Menu menu) {
		mFileBrowser.onContextMenuClosed(menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		return mFileBrowser.onContextItemSelected(item);
	}
	

}
