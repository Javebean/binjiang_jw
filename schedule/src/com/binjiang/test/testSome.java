package com.binjiang.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.binjiang.http.CallRemote;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testSome {

	private String baseUrl = "http://localhost:8081/schedule/";
	
	@Test
	public void test1Load(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("username", "20122344003");
		param.put("password", "888888");
		/*		param.put("username", "20132308020");
		param.put("password", "001221");
		 */		param.put("role", "RadioButton3");
		 String httpPostFunc = CallRemote.httpPostFunc(baseUrl+"loginbjxy.do", param);
		 System.out.println("登陆成功："+httpPostFunc);
		 
		// testgetStu_pro();
		// testgetStu_grade();
	}
	
	
	@Test
	public void test2getStu_pro(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("docId", "52");
		String httpPostFunc = CallRemote.httpGetFunc(baseUrl+"get_stu_profession.do", param);
		System.out.println("点击系部："+httpPostFunc);
		
	}
	
	@Test
	public void test3getStu_grade(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("profession_value", "2344");
		String httpPostFunc = CallRemote.httpGetFunc(baseUrl+"get_stu_grade.do", param);
		System.out.println("点击专业："+httpPostFunc);
	}
	
	@Test
	public void test4getStu_class(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("grade_value", "2012");
		String httpPostFunc = CallRemote.httpGetFunc(baseUrl+"get_stu_class.do", param);
		System.out.println(httpPostFunc);
	}
	@Test
	public void test5SelectCourse(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("docName","52");
		param.put("processName","2344");
		param.put("grade","2012");
		param.put("className","12软件1");
		param.put("school_year","2013-2014");
		param.put("semester","1");
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"select_class_course.do", param);
		System.out.println(httpGetFunc);
		
	}

}
