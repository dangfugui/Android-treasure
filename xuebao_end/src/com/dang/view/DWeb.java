package com.dang.view;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zirco.ui.activities.BrowserMainActivity;

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
import com.baidu.mobstat.d;
import com.dang.service.AppData;
import com.dang.service.Buckups;
import com.dang.service.WebData;
import com.dang.tool.DataBluetooth;
import com.dang.tool.DataWeb;
import com.dang.treasure.R;
import com.uvchip.mediacenter.filebrowser.Browser;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DWeb extends Browser{

	private List<Map<String, Object>> groupData;// 大组成员
	private List<List<Map<String, Object>>> childData;// 小组成员
	private ExAdapter adapter;
	private ExpandableListView exList;
	private Button btfind;
	private EditText etfind;
	private  WebData webdata;
	private DataWeb dateweb;
	final boolean DEBUG = false;
	private boolean click=true;
	private Context dContext;
	static{
		TAG = DBluetooth.class.getCanonicalName();
		
	}
	private boolean onResume = false;
	
	
	public DWeb(Context context) {
		super(context);
		dContext=context;
		initView();
		mViewMode = ViewMode.LISTVIEW;
	}
	public void onResume(){
		childData=new DataWeb(dContext).getChildData();
		adapter.notifyDataSetChanged();  
		if (!onResume) {//如果菜单为空 载入菜单
			QueryMenu();
			onResume = true;
		}
	}

	private void initView() {
		mView = mInflater.inflate(R.layout.aca_web_browser, null);
		exList = (ExpandableListView) mView.findViewById(R.id.acael_list);	
		etfind=(EditText) mView.findViewById(R.id.acaet_find);
		btfind=(Button) mView.findViewById(R.id.acabt_find);
		 webdata=new WebData(dContext);
		 dateweb=new DataWeb(dContext);
		btfind.setOnClickListener(this);
		groupData=dateweb.groupData;
		childData=dateweb.childData;
		QueryMenu();
	}
	
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.acabt_find:
		String url=etfind.getText().toString();
		Intent intent=new Intent(dContext,BrowserMainActivity.class);
		intent.putExtra("URL",url);
		dContext.startActivity(intent);
		break;

	default:
		break;
	}	
	}
	//点击时间	
	public void QueryMenu() {
		// 为大小组中添加数据
		
				adapter = new ExAdapter(dContext);
				exList.setAdapter(adapter);
				exList.setGroupIndicator(null);// 不设置大组指示器图标，因为我们自定义设置了
				exList.setDivider(null);// 设置图片可拉伸的
				//exList.expandGroup(0);//默认打开第一组
				exList.setOnChildClickListener(new OnChildClickListener() {
					public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
						if(click){
						String url=childData.get(groupPosition).get(childPosition).get("childurl").toString();
						if(url.length()>3){
							Intent intent=new Intent(dContext,BrowserMainActivity.class);
							intent.putExtra("URL",url);
							dContext.startActivity(intent);
						}else{
							Toast.makeText(dContext, groupPosition+""+childPosition, 0).show();
						}}
						click=true;
						return false;
					}
				});
				
				exList.setOnItemLongClickListener(new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,View view, int position, long id) {
						click=false;
						if(view.findViewById(R.id.acitv_groupurl) != null){
							TextView tvname=(TextView) view.findViewById(R.id.acitv_groupname);
							TextView tvurl=(TextView) view.findViewById(R.id.acitv_groupurl);
							String name =tvname.getText().toString();
							String url=tvurl.getText().toString();
							//Toast.makeText(dContext, tvname.getText()+">>"+tvurl.getText(), 1).show();
							if(webdata.addweb(name, url)){
								Toast.makeText(dContext,"已添加到收藏夹", 1).show();
							}else{
								Toast.makeText(dContext,"已从收藏夹删除", 1).show();
							}
						}else{
							LayoutInflater inflater=((Activity) dContext).getLayoutInflater();
							final View layout=inflater.inflate(R.layout.acd_dia_addweb, null);
							final EditText edmyname = (EditText)layout.findViewById(R.id.acddet_name);
							final EditText edmyurl=(EditText)layout.findViewById(R.id.acdet_url);
							AlertDialog.Builder dialog=new AlertDialog.Builder(dContext);
							dialog.setTitle("添加")
							.setView(layout);
							dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {				
									webdata.addweb(edmyname.getText().toString(), edmyurl.getText().toString());
								}
								
							});
							dialog.show();
						}
						childData=new DataWeb(dContext).getChildData();
						adapter.notifyDataSetChanged(); 
						return false;
					}
				});
	}
	// 关键代码是这个可扩展的listView适配器
		class ExAdapter extends BaseExpandableListAdapter {
			Context context;

			public ExAdapter(Context context) {
				super();
				this.context = context;
			}

			// 得到大组成员的view
			public View getGroupView(int groupPosition, boolean isExpanded,
					View convertView, ViewGroup parent) {
				View view = convertView;
				if (view == null) {
					//view = mInflater.inflate(R.layout.acx_exitem_webgroup, parent, false);
					LayoutInflater inflater = (LayoutInflater) dContext. getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(R.layout.acx_exitem_webgroup, null);
				}

				TextView title = (TextView) view.findViewById(R.id.acxtv_groupname);
				title.setText(getGroup(groupPosition)+"  ["+getChildrenCount(groupPosition)+"]");// 设置大组成员名称
				ImageView logimage=(ImageView) view.findViewById(R.id.acxim_image);
				logimage.setBackgroundResource( (Integer) groupData.get(groupPosition).get("groupimage"));
				ImageView image = (ImageView) view.findViewById(R.id.acxim_image2);// 是否展开大组的箭头图标
				if (isExpanded)// 大组展开时
					image.setBackgroundResource(R.drawable.acx_group_open);
				else
					// 大组合并时
					image.setBackgroundResource(R.drawable.acx_group_close);

				return view;
			}

			// 得到大组成员的id
			public long getGroupId(int groupPosition) {
				return groupPosition;
			}

			// 得到大组成员名称
			public Object getGroup(int groupPosition) {
				return groupData.get(groupPosition).get("groupname").toString();
			}

			// 得到大组成员总数
			public int getGroupCount() {
				return groupData.size();

			}

			// 得到小组成员的view
			public View getChildView(int groupPosition, int childPosition,
					boolean isLastChild, View convertView, ViewGroup parent) {
				View view = convertView;
				if (view == null) {
					LayoutInflater inflater = LayoutInflater.from(context);
					view = inflater.inflate(R.layout.aci_item_web, null);
				}
				final TextView title = (TextView) view.findViewById(R.id.acitv_groupname);
				title.setText(childData.get(groupPosition).get(childPosition).get("childname").toString());// 大标题
				final TextView url = (TextView) view.findViewById(R.id.acitv_groupurl);
				url.setText(childData.get(groupPosition).get(childPosition).get("childurl").toString());
				final ImageView logimage=(ImageView) view.findViewById(R.id.aciim_image);
				logimage.setBackgroundResource((Integer) childData.get(groupPosition).get(childPosition).get("childimage"));
				return view;
			}

			// 得到小组成员id
			public long getChildId(int groupPosition, int childPosition) {
				return childPosition;
			}

			// 得到小组成员的名称
			public Object getChild(int groupPosition, int childPosition) {
				return childData.get(groupPosition).get(childPosition)
						.get("childname").toString();
			}

			// 得到小组成员的数量
			public int getChildrenCount(int groupPosition) {
				return childData.get(groupPosition).size();
			}

			/**
			 * Indicates whether the child and group IDs are stable across changes
			 * to the underlying data. 表明大組和小组id是否稳定的更改底层数据。
			 * 
			 * @return whether or not the same ID always refers to the same object
			 * @see Adapter#hasStableIds()
			 */
			public boolean hasStableIds() {
				return true;
			}

			// 得到小组成员是否被选择
			public boolean isChildSelectable(int groupPosition, int childPosition) {
				
				return true;
			}
		

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
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根
		
	}
}
