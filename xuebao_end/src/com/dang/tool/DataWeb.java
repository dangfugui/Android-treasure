package com.dang.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.dang.service.AppData;
import com.dang.service.WebData;
import com.dang.treasure.R;

public class DataWeb {
	private Context mContext;
	private WebData webdata;
	public List<Map<String,Object>> groupData = new ArrayList<Map<String, Object>>();// 大组成员
	public List<List<Map<String, Object>>> childData = new ArrayList<List<Map<String, Object>>>();// 小组成员
	private String [] groupname =new String[]	{"收藏<长按添加收藏>","学宝资源","加油站","自学","书籍","观天下"};
	private int [] groupimage=new int[]			{R.drawable.abi_menu_login,R.drawable.aaa_treasure,
			R.drawable.aia_jiaoyouzan,R.drawable.aia_zixue,R.drawable.aia_book,R.drawable.aia_news};
	
	private String [] [] childname=new String [][]{ {},
			{"学习资源"},//学宝资源
			{"大学生励志网","大学生必备网","励志吧"},//动力
			{"我要自学网","网易公开课","多贝公开课","百度传课","爱奇艺教育","网易云课堂","新浪教育","中国知网","技术在线","小木虫","研学论坛"},//自学
			{"新华悦读","理想藏书","搜狐读书","微博有书","起点中文网","豆瓣读书","乌有之乡"},//书籍
			{"南方周末","凤凰网","强国社区","草根网","今日头条","新浪新闻"}//news
			};
	private int [] [] childimage=new int[] []{{},
			{R.drawable.aaa_treasure},//学宝资源
			{R.drawable.aic_liziwang,R.drawable.aic_bibei,R.drawable.aic_liziba},//动力
			{R.drawable.aic_zixuewang,R.drawable.aid_wanyigongkaike,R.drawable.aid_duobeigongkaike,R.drawable.aid_baiducuanke,//自学
				R.drawable .aid_aiqiyijiapyu,R.drawable.aid_wangyiyunketang,R.drawable.aid_xinlangjiaoyu,R.drawable.aid_ziwang,//自学
				R.drawable.aid_jisuzaixian,R.drawable.aid_xiaomucong,R.drawable.aid_yanxue},//自学
			{R.drawable.aie_xinhuayuedu,R.drawable.aie_cangsu,R.drawable.aie_souhudusu,R.drawable.aie_xinlangdusu,
					R.drawable.aie_qidian,R.drawable.aie_douban,R.drawable.aie_wuyong},//书籍
			{R.drawable.aif_nanfangzoumo,R.drawable.aif_fenghuang,R.drawable.aif_qiangguo,R.drawable.aif_caogen,R.drawable.aif_toutiao,R.drawable.aid_xinlangjiaoyu}//news
			};
	private String[][] childurl=new String[][]{{},
			{"http://pan.baidu.com/s/1ntmLp6h"},//学宝资源
			{"http://www.ggdxc.com","http://www.dxsbb.com/wap.asp","http://tieba.baidu.com/f?kw=%C0%F8%D6%BE"},//动力
			{"http://www.51zxw.net/default.aspx","http://open.163.com/","http://www.duobei.com/","http://m.chuanke.com/",//自学
				"http://edu.iqiyi.com/","http://study.163.com/#/index","http://edu.sina.com.cn/","http://wap.cnki.net/",//自学
				"http://211.68.23.76/welcome","http://emuch.net/","http://bbs.matwav.org/forum.php"},//自学
			{"http://www.xinhuanet.com/book/index.htm","http://www.lxbook.org/index.htm","http://nr.book.sohu.com/","http://book.weibo.cn/?pos=1000&PHPSESSID=d640d6b8741f13d09c54703697cb8441",//书籍
				"http://m.qidian.com/","http://book.douban.com/","http://www.wyzxsd.com/mobile/"},//书籍
			{"http://www.infzm.com/","http://3g.ifeng.com/","http://bbs1.people.com.cn/","http://www.caogen.com/","http://m.toutiao.com/","http://news.sina.cn/"}//news
			};
	
	public DataWeb(Context dContext) {
		mContext=dContext;
		webdata=new WebData(mContext);
		adddata();
	}
	private void adddata(){
		for(int i=0;i<groupname.length;i++){
			Map<String, Object> curGroupMap = new HashMap<String, Object>();
			curGroupMap.put("groupname",groupname[i]);
			curGroupMap.put("groupimage", groupimage[i]);
			groupData.add(curGroupMap);
			List<Map<String,Object>> children = new ArrayList<Map<String, Object>>();
			for(int j=0;j<childname[i].length;j++){
				Map<String, Object> curChildMap = new HashMap<String, Object>();
				curChildMap.put("childname",childname[i][j]);
				if(childimage[i][j]<10){
					childimage[i][j]=R.drawable.acw_web_null;
				}
				curChildMap.put("childimage",childimage[i][j]);
				curChildMap.put("childurl", childurl[i][j]);
				children.add(curChildMap);
			}
			if(i==0){
				Set<String> webs=webdata.getwebs();
				for(String aweb:webs){
					String name=aweb.substring(0, aweb.indexOf(webdata.webkey));
					String url=aweb.substring(aweb.indexOf(webdata.webkey)+webdata.webkey.length());
					Map<String, Object> curChildMap = new HashMap<String, Object>();
					curChildMap.put("childname",name);
					curChildMap.put("childurl", url);
					curChildMap.put("childimage",R.drawable.acw_web_my);
					children.add(curChildMap);
				}
			}
			childData.add(children);
		}	
	}
	public List<Map<String,Object>> getGroupData() {
		adddata();
		return groupData;
	}
	public List<List<Map<String, Object>>> getChildData() {
		adddata();
		return childData;
	}
}
