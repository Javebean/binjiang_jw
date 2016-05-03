package com.binjiang.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class htmlUtil {

	/**
	 * 获取登录学生的基本信息
	 * 形如：软件工程 2012 级 xxx(2012xxxxx)同学,您好,欢迎使用教学管理系统
	 * @param html
	 * @return
	 */
	public static String parseUserInfo(String html){
		Document doc = Jsoup.parse(html);
		String text = doc.select("table table").first().text();
		return text;
	}
	
	
	/**
	 * 解析学校系部
	 * @param html
	 * @return
	 */
	public static JSONArray parseDocument(String html){
		Document doc = Jsoup.parse(html);
		Element select = doc.getElementById("StudentJZ1_DropDownListXY");
		Elements options = select.select("option");
		JSONArray array = new JSONArray();
		JSONObject obj = null;
		for(Element o : options){
			obj = new JSONObject();
			obj.put(o.val(), o.text());
			array.put(obj);
		}
		return array;
	}
	
	
	/**
	 * 解析刚进系统的课表
	 * @param html
	 * @return
	 */
	public static JSONArray parseClassCourse(String html){
		JSONArray arr = new JSONArray();
		JSONObject tr_item = null;
		JSONArray tr_item_value = null;
		String time = null;
		
		Document doc = Jsoup.parse(html);
		Element trss= doc.getElementById("TABLE1");
		Elements trs = trss.select("tr");
		int len = trs.size();
		for(int i=1;i<len;i++){
			Elements tds = trs.get(i).select("td");
			tr_item = new JSONObject();
			tr_item_value = new JSONArray();
			for(int j=0;j<tds.size();j++){
				if(j==0){
					time = tds.get(j).text();
				}else{
					tr_item_value.put(tds.get(j).text());
				}
			}
			tr_item.put(time, tr_item_value);
			arr.put(tr_item);
		}
		return arr;
	}
	
	
	/**
	 * 得到查询课表的必要的参数
	 * @return
	 */
	public static Map<String,String> getSelectCourseMustParam(String html){
		Map<String,String> kbMap = new LinkedHashMap<String, String>();
		Document doc = Jsoup.parse(html);
		Elements __VIEWSTATE = doc.select("input[name=__VIEWSTATE]");
		Elements __EVENTVALIDATION = doc.select("input[name=__EVENTVALIDATION]");
		kbMap.put("__VIEWSTATE", __VIEWSTATE.val());
		kbMap.put("__EVENTVALIDATION", __EVENTVALIDATION.val());
		return kbMap;
	}
	
	/**
	 * 根据系部 解析专业
	 * @param html
	 * @return
	 */
	public static JSONArray parseProcess(String html){
		JSONArray result = new JSONArray();
		Document doc = Jsoup.parse(html);
		Elements element = doc.getElementById("StudentJZ1_DropDownListZYMC").select("option");
		JSONObject pro = null;
		for(Element e : element){
			pro =  new JSONObject();
			pro.put(e.val(), e.text());
			result.put(pro);
		}
		return result;
	}
	
	
	
	/**
	 * 根据专业 解析年级
	 * @param html
	 * @return
	 */
	public static JSONArray parseGrade(String html){
		JSONArray result = new JSONArray();
		Document doc = Jsoup.parse(html);
		Elements element1 = doc.getElementById("StudentJZ1_DropDownListDQSZJ").select("option");
		for(Element e : element1){
			result.put(e.val());
		}
		return result;
	}
	
	/**
	 * 根据年级 解析班级
	 * @param html
	 * @return
	 */
	public static JSONArray parseClass(String html){
		JSONArray result = new JSONArray();
		Document doc = Jsoup.parse(html);
		Elements element1 = doc.getElementById("StudentJZ1_DropDownListXZB").select("option");
		JSONObject cla = null;
		for(Element e : element1){
			cla =  new JSONObject();
			cla.put(e.val(), e.text());
			result.put(cla);
		}
		return result;
	}
	
	/**
	 * @param html
	 * @return 是否回到主页
	 */
	public static boolean isHomePage(String html){
		Document parse = Jsoup.parse(html);
		Element e = parse.getElementById("TextBox1");
		if(e!=null){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	/**
	 * @return 解析学生普通成绩& 等级考试成绩
	 */
	public static JSONArray parseOrdinaryLevScore(String html){
		JSONArray arr = new JSONArray();
		Document doc = Jsoup.parse(html);
		Element table = doc.getElementById("GridView1");
		Elements trs = table.select("tr");
		int tr_num = trs.size();
		String[] th = new String[trs.get(0).select("th").size()];
		Elements ths = null;
		JSONObject obj = null;
		
		for(int i=0;i<tr_num;i++){
			if(i==0){//把第一个tr中的th存到数组里
				ths = trs.get(i).select("th");
				int index = 0;
				for(Element t : ths){
					th[index++] = t.text();
				}
			}else{
				ths = trs.get(i).select("td");
				int index = 0;
				obj = new JSONObject();
				for(Element t : ths){
					obj.put(th[index++], t.text());
				}
				arr.put(obj);
			}
			
		}
		
		return arr;
	}
	
	
	//网上评教。两个表格
	public static JSONArray parserOnlineEvalute(String html){
		JSONArray arr = new JSONArray();//放两个表格
		JSONObject obj1 = new JSONObject();
		JSONArray parseonline1 = parseonline1(html,"GridView1");
		obj1.put("普通课程评教", parseonline1);
		
		JSONObject obj2 = new JSONObject();
		JSONArray parseonline2 = parseonline1(html,"GridView2");
		obj2.put("普通课程评教", parseonline2);
		
		arr.put(obj1);
		arr.put(obj2);
		return arr;
	}
	
	public static JSONArray parseonline1(String html,String tableId){
		JSONArray arr = new JSONArray();
		Document doc = Jsoup.parse(html);
		Element table = doc.getElementById(tableId);
		Elements trs = table.select("tr");
		int tr_num = trs.size();
		String[] th = new String[trs.get(0).select("th").size()];
		Elements ths = null;
		JSONObject obj = null;
		
		for(int i=0;i<tr_num;i++){
			if(i==0){//把第一个tr中的th存到数组里
				ths = trs.get(i).select("th");
				int index = 0;
				for(Element t : ths){
					th[index++] = t.text();
				}
			}else{
				ths = trs.get(i).select("td");
				int index = 0;
				obj = new JSONObject();
				for(Element t : ths){
					if(index==4){
						obj.put(th[index++],t.select("a").attr("href"));
					}else{
						obj.put(th[index++], t.text());
					}
				}
				arr.put(obj);
			}
			
		}
		
		return arr;
	}
	
	
	/**
	 * 解析网上评教对老师个人的评价表面
	 * @return
	 */
	public static JSONArray parseSingleOnlineEv(String html){
		
		
		String[] titleRate = {"","","优（10分）","良（8分）","中（6分）","差（0分）"};
		JSONArray arr = new JSONArray();
		Document doc = Jsoup.parse(html);
		Element table = doc.getElementById("GridView1");
		Elements trs = table.select("tr");
		int tr_num = trs.size();
		String[] th = new String[trs.get(0).select("th").size()];
		Elements ths = null;
		JSONObject obj = null;
		
		for(int i=0;i<tr_num;i++){
			if(i==0){//把第一个tr中的th存到数组里
				ths = trs.get(i).select("th");
				int index = 0;
				for(Element t : ths){
					if(index>1){
						th[index] = titleRate[index];
					}else{
						th[index] = t.text().trim();
					}
					index++;
				}
			}else{
				ths = trs.get(i).select("td");
				int index = 0;
				obj = new JSONObject();
				for(Element t : ths){
					if(index>1){
						obj.put(th[index], t.select("input").attr("name")+","+t.select("input").val());
					}else{
						obj.put(th[index], t.text());
					}
					index++;
				}
				arr.put(obj);
			}
			
		}
		
		return arr;
		
	}
	
	/**
	 * 网上评教----判断页面是否有“评教成功”。有就是评价成功，没有就是失败
	 * @return
	 */
	public static boolean evaState(String html){
		return html.contains("评教成功");
	}
	
	/**
	 * 学生自评-----判断页面是否有“保存成功”。有就是保存成功，没有就是失败
	 * @return
	 */
	public static boolean evaState_stu(String html){
		return html.contains("保存成功");
	}
	
	
	/**
	 * @param html
	 * @return
	 */
	public static JSONArray parseStudentEvalute(String html){
		JSONArray arr = new JSONArray();
		Document doc = Jsoup.parse(html);
		Element table = doc.getElementById("GcGridView1");
		Elements trs = table.select("tr");
		int tr_num = trs.size();
		String[] th = new String[trs.get(0).select("th").size()];
		Elements ths = null;
		JSONObject obj = null;
		
		for(int i=0;i<tr_num;i++){
			if(i==0){//把第一个tr中的th存到数组里
				ths = trs.get(i).select("th");
				int index = 0;
				for(Element t : ths){
					th[index++] = t.text();
				}
			}else{
				ths = trs.get(i).select("td");
				int index = 0;
				obj = new JSONObject();
				for(Element t : ths){
					if(index==5){
						JSONObject score = new JSONObject();
						score.put("name", t.select("select").attr("name"));
						Elements op = t.select("select").select("option");
						for(Element o :op){
							score.put(o.text(), o.val());
						}
						obj.put(th[index++], score);
							
					}else{
						obj.put(th[index++], t.text());
					}
					
				}
				arr.put(obj);
			}
			
		}
		
		return arr;
	}
	
	
	/**
	 * 解析-根据教师点名 -- 学院
	 * @param html
	 * @return
	 */
	public static JSONArray parseXueYuan(String html){
		JSONArray result = new JSONArray();
		Document doc = Jsoup.parse(html);
		Elements element = doc.getElementById("DropDownList4").select("option");
		JSONObject pro = null;
		for(Element e : element){
			pro =  new JSONObject();
			pro.put(e.val(), e.text());
			result.put(pro);
		}
		return result;
	}
	/**
	 * 解析-根据教师点名 -- 专业
	 * @param html
	 * @return
	 */
	public static JSONArray parseZhuanYe(String html){
		JSONArray result = new JSONArray();
		Document doc = Jsoup.parse(html);
		Elements element = doc.getElementById("DropDownList5").select("option");
		JSONObject pro = null;
		for(Element e : element){
			pro =  new JSONObject();
			pro.put(e.val(), e.text());
			result.put(pro);
		}
		return result;
	}
	
}
