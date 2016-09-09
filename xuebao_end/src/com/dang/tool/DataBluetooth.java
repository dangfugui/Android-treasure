package com.dang.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.R.bool;
import android.content.ContentValues;
import android.util.Log;

public class DataBluetooth {
	public static String key="<key>";
	public static String keytext="<te>";
	private static List <HashMap<String, Object>> list=new ArrayList<HashMap<String,Object>>();
	private static HashMap<String,Object>data=new HashMap<String, Object>();
	private static HashMap<String,Object>namelist=new HashMap<String, Object>();
	private static boolean isshort=true;
	
	public static int getsize(){
		return  data.size()+1;
	}
	
	public static String getuser(String title) {
		String result="<<"+title+">>__统计\n";
		Collections.sort(list, new ComparatorValues());
		for(HashMap<String, Object>user:list){
			result=result+"--id:"+user.get("id")+"--name:"+user.get("name")+":"+user.get("text")+"\n";
		}
		return result;
	}
	
	/**
	 * 接受数据
	 * @param keyname 蓝牙名
	 * @param address 蓝牙地址
	 * @return 更新的内容
	 */
	public static String send(String keyname, String address) {
		if(keyname.indexOf(key)==-1){
			return null;
		}
		String oldname =(String) data.get(address);
		String text=keyname.substring(keyname.indexOf(key)+key.length(),keyname.lastIndexOf(key));
		String thename=keyname.substring(0, keyname.indexOf(key));
		String theid=keyname.substring(keyname.lastIndexOf(key)+key.length(), keyname.length());
		if(theid==""){
			theid="00";
		}
		String tjtext="";
		if(text.indexOf(keytext)!=-1){
			tjtext=text.substring(keytext.length());
			text=null;
		}
		//Log.d("tt", "text:"+text+"name"+thename+"id"+theid);
		if(null==namelist.get(theid)){
			namelist.put(theid, thename);
			 HashMap<String,Object>alist=new HashMap<String, Object>();
			 alist.put("id", theid);
			 alist.put("name", thename);
			 alist.put("text", tjtext);
			 list.add(alist);			
		}else{
			/*namelist.remove(theid);
			HashMap<String,Object>alist=new HashMap<String, Object>();
			 alist.put("id", theid);
			 alist.put("name", thename);
			 alist.put("text", tjtext);
			 list.add(alist);	*/	
		}
		
		if(oldname==null){
			data.put(address, text);
			return text;
		}
		if(!oldname.equals(text)){
			data.put(address, text);
			return text;
		}
		return null;
	}
	/**
	 * 得到签到详情
	 * @param title 标题
	 * @return 签到详情
	 */
	public static String getlist(String title){
		if(list.size()==0){
			return "";
		}
		int counter=0;
		String result="";
		String result2="";
		Collections.sort(list, new ComparatorValues());
		HashMap<String, Object> alist=list.get(0);
		String stortid1;
		String beforeid1;
		String stortid2;
		if(alist.get("id").toString().length()>4){
			stortid1=alist.get("id").toString().substring(alist.get("id").toString().length()-4);
			beforeid1=alist.get("id").toString().substring(0,alist.get("id").toString().length()-4);
			isshort=false;
		}else{
			stortid1=alist.get("id").toString();
			beforeid1="";
		}
		int start=Integer.parseInt(stortid1);
		
		HashMap<String, Object> alist2=list.get(list.size()-1);
		if(alist.get("id").toString().length()>4){
			 stortid2=alist2.get("id").toString().substring(alist2.get("id").toString().length()-4);
			 beforeid1=alist.get("id").toString().substring(0,alist.get("id").toString().length()-4);
			 isshort=false;
		}else{
			stortid2=alist2.get("id").toString();
		}

		int end=Integer.parseInt(stortid2);
		
		
		for(int aa=start;aa<=end;aa++){			
			String a=aa+"";
			while(!isshort&&a.length()<4){
				a="0"+a;
			}
			String name=(String) namelist.get(beforeid1+a);
			if(name==null){
				result=result+"--id:"+beforeid1+a+"!!"+beforeid1+a+"未到\n";
				counter++;
				result2=result2+"\t未到id:"+beforeid1+a+"\n";
			}else{
				result=result+"--id:"+beforeid1+a+"--name:"+name+"\n";
			}
		}
		result="<<"+title+">>——全部\n"+"搜索到人数:"+list.size()+"未到:"+counter+"总计:"+(list.size()+counter)+"\n"+result;
		result2="<<"+title+">>——未到\t\t"+"未到:"+counter+"\n"+result2;
		result2=result2+"<<"+title+">>——表尾\n";
		return result+result2;		
	}
	
	
	//排序函数
	public static final class ComparatorValues implements Comparator<HashMap<String, Object>>{		
		public int compare(HashMap<String, Object> lhs,HashMap<String, Object> rhs) {
			// TODO 自动生成的方法存根
			String id1=(String) lhs.get("id");
			String id2=(String) rhs.get("id");
			int result = id1.compareTo(id2);
			if(result>0){
				result=1;
			}else if(result<0){
				result=-1;
			}
			return result;
		}


    }
}
