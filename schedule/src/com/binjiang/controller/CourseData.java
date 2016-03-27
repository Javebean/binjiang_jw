package com.binjiang.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	public String getStu_Profession(String docId){
		JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("__EVENTTARGET", "StudentJZ1$DropDownListXY");
			param.put("StudentJZ1$DropDownListXY", docId);
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
	public String getClassCourse(String docName,String processName,
			String grade,String className,String school_year,String semester){
			JSONObject result = new JSONObject();
		try{
			Map<String, String> param = htmlUtil.getSelectCourseMustParam(outResultString);
			param.put("StudentJZ1$DropDownListXY", docName);
			param.put("StudentJZ1$DropDownListZYMC", processName);
			param.put("StudentJZ1$DropDownListDQSZJ", grade);
			param.put("StudentJZ1$DropDownListXZB", className);
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
	
	
	@RequestMapping(value="/quit_system.do",method=RequestMethod.GET,produces="text/html;charset=utf-8")
	public  String  quitSystem(){ 
		Map<String, String> map = new HashMap<String, String>();
		map.put("action", "quit");
		String result = CallRemote.httpGetFunc(bjxyuri+"default.aspx", map);

		return result;
		
	}
	
}
