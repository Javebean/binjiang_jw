package com.binjiang.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.binjiang.http.CallRemote;
import com.binjiang.util.htmlUtil;

@RestController
public class CourseData {

	private String outResultString = "";
	private String bjxyuri ="";
	Map<String, String> selectCourseMustParam = null;
	
	@RequestMapping(value="/loginbjxy.do",method=RequestMethod.POST,produces="text/html;charset=utf-8")
	public String LoginBJXY(String username,String password,String role){
		//init httpclient
		new CallRemote();
		JSONObject obj = new JSONObject();
		try {
			bjxyuri = CallRemote.getRedirectUrl();
			//访问滨江教务登陆首页
			Map<String, String> login_param = CallRemote.getLogin_param();
			login_param.put("TextBox1", username);
			login_param.put("TextBox2", password);
			login_param.put("js", role);
			
			
			CallRemote.httpGetFunc(bjxyuri, login_param);
			bjxyuri = bjxyuri.substring(0, bjxyuri.lastIndexOf("/")+1);
			//跳转到课表页面
			outResultString = CallRemote.httpGetFunc(bjxyuri+"public/kebiaoall.aspx", null);
			String parseUserInfo = htmlUtil.parseUserInfo(outResultString);
			
			//返回数据 
			JSONObject dataInfo = new JSONObject();
			dataInfo.put("stu_info", parseUserInfo);//学生基本信息
			dataInfo.put("stu_departments", htmlUtil.parseDocument(outResultString));
			dataInfo.put("default_kb", htmlUtil.parseClassCourse(outResultString));
			
			obj.put("status", "ok");
			obj.put("data", dataInfo);
			
			return obj.toString();
		} catch (ClientProtocolException e) {
			obj.put("status", "fail");
			return obj.toString();
		} catch (IOException e) {
			obj.put("status", "fail");
			return obj.toString();
		}
		
	}
	
	
	/*点击系部*/
	@RequestMapping(value="/click_stu_departments.do",method=RequestMethod.GET, produces="text/html;charset=utf-8")
	public String getStu_Profession(String doc_value){
		JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("__EVENTTARGET", "StudentJZ1$DropDownListXY");
			param.put("StudentJZ1$DropDownListXY", doc_value);
			param.put("__EVENTARGUMENT", "");
			param.put("__LASTFOCUS", "");
			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/kebiaoall.aspx", param);
			JSONArray obj = htmlUtil.parseProcess(outResultString);
			result.put("status", "ok");
			result.put("data", obj);
			return result.toString();
		}catch(Exception e){
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	
	/*点击专业*/
	@RequestMapping(value="/click_stu_profession.do",method=RequestMethod.GET, produces="text/html;charset=utf-8")
	public String getStu_Grade(String profession_value){
		JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("__EVENTTARGET", "StudentJZ1$DropDownListZYMC");
			param.put("StudentJZ1$DropDownListZYMC", profession_value);
			
			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/kebiaoall.aspx", param);
			
			JSONArray obj = htmlUtil.parseGrade(outResultString);
			result.put("status", "ok");
			result.put("data", obj);
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	
	/*点击年级*/
	@RequestMapping(value="/click_stu_grade.do",method=RequestMethod.GET, produces="text/html;charset=utf-8")
	public String getStu__Class(String grade_value){
		JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("__EVENTTARGET", "StudentJZ1$DropDownListDQSZJ");
			param.put("StudentJZ1$DropDownListDQSZJ",grade_value);

			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/kebiaoall.aspx", param);
			JSONArray obj = htmlUtil.parseClass(outResultString);
			result.put("status", "ok");
			result.put("data", obj);
			return result.toString();
		}catch(Exception e){
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	
	/*获得课表*/
	@RequestMapping(value="/get_class_course.do",method=RequestMethod.POST, produces="text/html;charset=utf-8")
	public String getClassCourse(String doc_value,String profession_value,
			String grade_value,String class_value,String school_year,String semester){
			JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("StudentJZ1$DropDownListXY", doc_value);
			param.put("StudentJZ1$DropDownListZYMC", profession_value);
			param.put("StudentJZ1$DropDownListDQSZJ", grade_value);
			param.put("StudentJZ1$DropDownListXZB", class_value);
			param.put("XN1$DropDownListXN", school_year);
			param.put("XQ1$DropDownListXQ", semester);
			param.put("__EVENTARGUMENT", "");
			param.put("__LASTFOCUS", "");
			param.put("Button1", "查询");
			/*param.put("ButtonList", "班级列表模式"); 
			param.put("btnExport", "课表输出");*/
			//获得课表
			 outResultString = CallRemote.httpPostFunc(bjxyuri+"public/kebiaoall.aspx", param);
			 result.put("status", "ok");
			 result.put("data", htmlUtil.parseClassCourse(outResultString));
			 return result.toString();
		}catch(Exception e){
			result.put("status", "fail");
			return result.toString();
		}
	}
	
	
	/**
	 * @param lev : 0是普通课程成绩     1是等级考试成绩
	 * @return
	 */
	@RequestMapping(value="/get_stu_score.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String get_stu_score(int lev){
		String scorestr = null;
		JSONArray scoreArr = null;
		if(0==lev){
			scorestr = CallRemote.httpGetFunc(bjxyuri+"student/chengji.aspx", null);
			scoreArr = htmlUtil.parseOrdinaryLevScore(scorestr);
		}else if(1==lev){
			scorestr = CallRemote.httpGetFunc(bjxyuri+"student/djkscjcx.aspx", null);
			scoreArr = htmlUtil.parseOrdinaryLevScore(scorestr);
		}
		return scoreArr.toString();
	}
	
	
	
	/**
	 * @param lev 0是网上平叫    1是学生自评 其实是打开评教的表格
	 * @return
	 */
	@RequestMapping(value="/online_evaluate_page.do",produces="text/html;charset=utf-8",method=RequestMethod.GET)
	public String online_evaluate_page(int lev){
		JSONArray parserOnlineEvalute = null;
		if(lev==0){
			outResultString = CallRemote.httpGetFunc(bjxyuri+"student/wspj.aspx", null);
			parserOnlineEvalute = htmlUtil.parserOnlineEvalute(outResultString);
		}else if(1==lev){
			outResultString = CallRemote.httpGetFunc(bjxyuri+"student/xszp.aspx", null);
			parserOnlineEvalute = htmlUtil.parseStudentEvalute(outResultString);
		}
		
		
		return parserOnlineEvalute.toString();
	}
	
	
	@RequestMapping(value="/online_evaluate.do",produces="text/html;charset=utf-8",method=RequestMethod.GET)
	public String online_evaluate(String param){
		if(param==null){
			JSONObject obj = new JSONObject();
			obj.put("status", "fail");
			obj.put("reason", "没有参数");
			return  obj.toString();
		}
		JSONArray arr = null;
		try{
			outResultString = CallRemote.httpGetFunc(bjxyuri+"student/"+param, null);
			arr = htmlUtil.parseSingleOnlineEv(outResultString);
		}catch(Exception e){
			e.printStackTrace();
		}
		return arr.toString();
	}
	
	@RequestMapping(value="/submit_online_evaluate.do",produces="text/html;charset=utf-8",method=RequestMethod.GET)
	public String submit_online_evalueate(HttpServletRequest request,String submitUrl){
		JSONObject result = new JSONObject();
		Map<String, String[]> pmap = request.getParameterMap();
		Map<String,String> param = new HashMap<String, String>();
		for(Map.Entry<String, String[]> p:pmap.entrySet()){
			if(!"submitUrl".equals(p.getKey())){
				param.put(p.getKey(), p.getValue()[0]);
			}
		}
		param.put("Button2", "提  交");
		/*param.put("btnReturn", "返  回");*/
		param.putAll(htmlUtil.getSelectCourseMustParam(outResultString));
		try{
			String result1 = CallRemote.httpPostFunc(bjxyuri+"student/"+submitUrl, param);
			boolean evaState = htmlUtil.evaState(result1);
			if(evaState){
				result.put("status", "ok");
			}else{
				result.put("status", "fail");
			}
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "fail");
			return result.toString();
		}
		
		
	}
	
	@RequestMapping(value="/submit_student_evaluate.do",produces="text/html;charset=utf-8",method=RequestMethod.GET)
	public String studentEvalueate(HttpServletRequest request){
		JSONObject result = new JSONObject();
		Map<String, String[]> pmap = request.getParameterMap();
		Map<String,String> param = new HashMap<String, String>();
		for(Map.Entry<String, String[]> p:pmap.entrySet()){
			param.put(p.getKey(), p.getValue()[0]);
		}
		param.putAll(htmlUtil.getSelectCourseMustParam(outResultString));
		param.put("btnSave", "保存");
		 
		try{
			String result1 = CallRemote.httpPostFunc(bjxyuri+"student/xszp.aspx", param);
			boolean evaState = htmlUtil.evaState_stu(result1);
			if(evaState){
				result.put("status", "ok");
			}else{
				result.put("status", "fail");
			}
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	
	
	@RequestMapping(value="/quit_system.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public  String  quitSystem(){ 
		JSONObject result = new JSONObject();
		try{
			Map<String, String> map = new HashMap<String, String>();
			map.put("action", "quit");
			String result1 = CallRemote.httpGetFunc(bjxyuri+"default.aspx", map);
			boolean homePage = htmlUtil.isHomePage(result1);
			if(homePage){
				result.put("status", "ok");
			}else{
				result.put("status", "fail");
			}
		}catch(Exception e){
			result.put("status", "fail");
		}

		return result.toString();
		
	}
	
	
	/**
	 * 查询老师或者学生单独的上课课表
	 * @param type 0:laoshi
	 * @return
	 */
	@RequestMapping(value="tea_stu_self_course.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String tea_stu_self_course(int type){
		JSONObject result = new JSONObject();
		JSONArray parseClassCourse = null;
		try{
			if(type==0){
				outResultString = CallRemote.httpGetFunc(bjxyuri+"public/jskebiaoall.aspx", null);
			}else{
				outResultString = CallRemote.httpGetFunc(bjxyuri+"student/mykebiaoall1.aspx", null);
			}
			parseClassCourse = htmlUtil.parseClassCourse(outResultString);
			result.put("status", "ok");
			result.put("data", parseClassCourse);
			return result.toString();
		}catch(Exception e){
			result.put("status", "fail");
			return result.toString();
		}
	}
	
	
	/**
	 * 点击 查看班级学生名册
	 * @return
	 */
	@RequestMapping(value="tea_rollcall.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String tea_rollcall(){
		JSONObject result = new JSONObject();
		try{
			outResultString = CallRemote.httpGetFunc(bjxyuri+"public/xsmddy.aspx", null);
			result.put("status", "ok");
			result.put("data", htmlUtil.parseXueYuan(outResultString,"DropDownList4"));
			return result.toString();
			
		}catch(Exception e){
			result.put("status", "fail");
			return result.toString();
		}
		
	}
	
	/**
	 * 点击 查看班级学生名册--点击系部
	 * @return
	 */
	@RequestMapping(value="click_rollcall_document.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String click_rollcall_document(String document_value){
		JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("__EVENTTARGET", "DropDownList4");
			param.put("DropDownList4", document_value);
			param.put("__EVENTARGUMENT", "");
			param.put("__LASTFOCUS", "");
			param.put("__VIEWSTATEENCRYPTED", "");
			param.put("Button1", "查询");
			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/xsmddy.aspx", param);
			JSONArray obj = htmlUtil.parseXueYuan(outResultString,"DropDownList5");
			
			result.put("status", "ok");
			result.put("data", obj);
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	/**
	 * 点击 查看班级学生名册--点击专业
	 * @return
	 */
	@RequestMapping(value="click_rollcall_profession.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String click_rollcall_profession(String profession_value){
		JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("__EVENTTARGET", "DropDownList5");
			param.put("DropDownList5", profession_value);
			param.put("__EVENTARGUMENT", "");
			param.put("__LASTFOCUS", "");
			param.put("__VIEWSTATEENCRYPTED", "");
			param.put("Button1", "查询");
			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/xsmddy.aspx", param);
			JSONArray obj = htmlUtil.parseXueYuan(outResultString, "DropDownList3");
			
			result.put("status", "ok");
			result.put("data", obj);
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	
	/**
	 * 点击 查看班级学生名册--点击年级
	 * @return
	 */
	@RequestMapping(value="click_rollcall_grade.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String click_rollcall_grade(String grade_value){
		JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("__EVENTTARGET", "DropDownList3");
			param.put("DropDownList3", grade_value);
			param.put("__EVENTARGUMENT", "");
			param.put("__LASTFOCUS", "");
			param.put("__VIEWSTATEENCRYPTED", "");
			param.put("Button1", "查询");
			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/xsmddy.aspx", param);
			JSONArray obj = htmlUtil.parseXueYuan(outResultString, "DropDownList6");
			
			result.put("status", "ok");
			result.put("data", obj);
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	/**
	 * 点击 查看班级学生名册 
	 * @return
	 */
	@RequestMapping(value="class_rollcall_names.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String class_rollcall_names(HttpServletRequest request){
		JSONObject result = new JSONObject();
		try{
			Map<String, String[]> pmap = request.getParameterMap();
			Map<String,String> param = new HashMap<String, String>();
			for(Map.Entry<String, String[]> p:pmap.entrySet()){
				param.put(p.getKey(), p.getValue()[0]);
			}
			param.putAll(htmlUtil.getSelectCourseMustParam(outResultString));
			param.put("__EVENTARGUMENT", "");
			param.put("__LASTFOCUS", "");
			param.put("__VIEWSTATEENCRYPTED", "");
			/*param.put("divepage$First", "首页");
			param.put("divepage$Previous", "上一页");
			param.put("divepage$Next", "下一页");
			param.put("divepage$Lastly", "末页");
			param.put("divepage$SelectPage", "");
			param.put("divepage$Select", "确定");*/
			param.put("Button1", "查询");
			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/xsmddy.aspx", param);
			result.put("status", "ok");
			result.put("data", htmlUtil.parseonline1(outResultString, "GridView1"));
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	
	/**
	 * 点击 查看班级学生名册 ----下一页
	 * @return
	 */
	@RequestMapping(value="next_class_rollcall_names.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String next_class_rollcall_names(HttpServletRequest request){
		JSONObject result = new JSONObject();
		try{
			Map<String, String[]> pmap = request.getParameterMap();
			Map<String,String> param = new HashMap<String, String>();
			for(Map.Entry<String, String[]> p:pmap.entrySet()){
				param.put(p.getKey(), p.getValue()[0]);
			}
			param.putAll(htmlUtil.getSelectCourseMustParam(outResultString));
			param.put("__EVENTARGUMENT", "");
			param.put("__LASTFOCUS", "");
			param.put("__VIEWSTATEENCRYPTED", "");
			param.put("divepage$Next", "下一页");
			/*param.put("divepage$First", "首页");
			param.put("divepage$Previous", "上一页");
			param.put("divepage$Lastly", "末页");
			param.put("divepage$SelectPage", "");
			param.put("divepage$Select", "确定");*/
			/*param.put("Button1", "查询");*/
			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/xsmddy.aspx", param);
			result.put("status", "ok");
			result.put("data", htmlUtil.parseonline1(outResultString, "GridView1"));
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	@RequestMapping(value="previous_class_rollcall_names.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public String previous_class_rollcall_names(HttpServletRequest request){
		JSONObject result = new JSONObject();
		try{
			Map<String, String[]> pmap = request.getParameterMap();
			Map<String,String> param = new HashMap<String, String>();
			for(Map.Entry<String, String[]> p:pmap.entrySet()){
				param.put(p.getKey(), p.getValue()[0]);
			}
			param.putAll(htmlUtil.getSelectCourseMustParam(outResultString));
			param.put("__EVENTARGUMENT", "");
			param.put("__LASTFOCUS", "");
			param.put("__VIEWSTATEENCRYPTED", "");
			param.put("divepage$Previous", "上一页");
			/*param.put("divepage$First", "首页");
			param.put("divepage$Next", "下一页");
			param.put("divepage$Lastly", "末页");
			param.put("divepage$SelectPage", "");
			param.put("divepage$Select", "确定");*/
			/*param.put("Button1", "查询");*/
			outResultString = CallRemote.httpPostFunc(bjxyuri+"public/xsmddy.aspx", param);
			result.put("status", "ok");
			result.put("data", htmlUtil.parseonline1(outResultString, "GridView1"));
			return result.toString();
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "fail");
			return result.toString();
			
		}
	}
	
	
}
