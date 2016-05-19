package com.binjiang.test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.binjiang.http.CallRemote;
import com.binjiang.util.htmlUtil;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testSome {

	private String baseUrl = "http://localhost:8081/schedule/";
	
	CallRemote call = new CallRemote();
	@Test
	public void test1Load(){
		Map<String,String> param = new HashMap<String, String>();
		/*param.put("username", "20122344003");
		param.put("password", "888888");*/
		param.put("username", "20132308020");
		param.put("password", "001221");
		/*param.put("username", "000519");
		param.put("password", "050125");*/
		/*param.put("username", "20122344004");
		param.put("password", "ch2oh2o");*/
		 		
		param.put("role", "RadioButton3");//老师：RadioButton1 学生：RadioButton3
		 String httpPostFunc = CallRemote.httpPostFunc(baseUrl+"loginbjxy.do", param);
		 System.out.println("登陆成功："+httpPostFunc);
		 
		// testgetStu_pro();
		// testgetStu_grade();
	}
	
	
	@Test
	public void test2getStu_pro(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("doc_value", "52");
		String httpPostFunc = CallRemote.httpGetFunc(baseUrl+"click_stu_departments.do", param);
		System.out.println("点击系部："+httpPostFunc);
		
	}
	
	@Test
	public void test3getStu_grade(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("profession_value", "2344");
		String httpPostFunc = CallRemote.httpGetFunc(baseUrl+"click_stu_profession.do", param);
		System.out.println("点击专业："+httpPostFunc);
	}
	
	@Test
	public void test4getStu_class(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("grade_value", "2012");
		String httpPostFunc = CallRemote.httpGetFunc(baseUrl+"click_stu_grade.do", param);
		System.out.println(httpPostFunc);
	}
	@Test
	public void test5SelectCourse(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("doc_value","52");
		param.put("profession_value","2344");
		param.put("grade_value","2012");
		param.put("class_value","12软件1");
		param.put("school_year","2013-2014");
		param.put("semester","1");
		String httpGetFunc = CallRemote.httpPostFunc(baseUrl+"get_class_course.do", param);
		System.out.println(httpGetFunc);
		
	}
	@Test
	public void test6SelectScore(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("lev","0");
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"get_stu_score.do", param);
		System.out.println(httpGetFunc);
		
	}
	
	@Test
	public void test7Online_eva_page(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("lev","1");
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"online_evaluate_page.do", param);
		System.out.println("网上评教page"+httpGetFunc);
		
	}
	
	
	@Test
	public void test8Online_eva(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("param","wspjdf.aspx?xkkh=(2015-2016-2)-1000018-002200-1&jszgh=002200&pjmc=期中");
		/*param.put("param","wspjdf.aspx?xkkh=(2015-2016-2)-1004435-207810-2&jszgh=207810&pjmc=期中");*/
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"online_evaluate.do", param);
		System.out.println("网上评教33"+httpGetFunc);
		
	}
	
	@Test
	public void test8submit_student_eva(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("GcGridView1$ctl02$ddlfz", "10");
		param.put("GcGridView1$ctl03$ddlfz", "10");
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"submit_student_evaluate.do", param);
		System.out.println("学生自评"+httpGetFunc);
		
	}
	 
	
	/*@Test
	public void test9SubmitOnline_eav(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("GridView1$ctl02$radiobtn", "RadioButton1");
		param.put("GridView1$ctl03$radiobtn", "RadioButton1");
		param.put("GridView1$ctl04$radiobtn", "RadioButton1");
		param.put("GridView1$ctl05$radiobtn", "RadioButton1");
		param.put("GridView1$ctl06$radiobtn", "RadioButton1");
		param.put("GridView1$ctl07$radiobtn", "RadioButton1");
		param.put("GridView1$ctl08$radiobtn", "RadioButton1");
		param.put("GridView1$ctl09$radiobtn", "RadioButton1");
		param.put("GridView1$ctl10$radiobtn", "RadioButton1");
		param.put("GridView1$ctl11$radiobtn", "RadioButton1");
		param.put("TextBox4", "good");
		//param.put("submitUrl", "wspjdf.aspx?xkkh=(2015-2016-2)-1000018-002200-1&jszgh=002200&pjmc=期中");
		param.put("submitUrl", "wspjdf.aspx?xkkh=(2015-2016-2)-1004435-207810-2&jszgh=207810&pjmc=期中");
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"submit_online_evaluate.do", param);
		System.out.println("提交网上评教："+httpGetFunc);
		
	}*/
	
	
	
	
	
	/**
	 * 查询老师或者学生单独的上课课表
	 * @param type 0:laoshi
	 * @return
	 */
	@Test
	public void testTea_stu_self_course(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("type", "0");
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"tea_stu_self_course.do", param);
		System.out.println("单独："+httpGetFunc);
	}
	
	
	/**
	 * 点击 查看班级学生名册
	 * @return
	 */
	@Test
	public void testTea_rollcall(){
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"tea_rollcall.do", null);
		System.out.println("点名册："+httpGetFunc);
	}
	
	
	/**
	 * 老师点击系部
	 */
	@Test
	public void testclick_rollcall_document(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("document_value", "滨江学院计算机系");
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"click_rollcall_document.do", param);
		System.out.println("点名册1："+httpGetFunc);
	}
	/**
	 * 老师点击专业
	 */
	@Test
	public void testclick_rollcall_profession(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("profession_value", "2344");
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"click_rollcall_profession.do", param);
		System.out.println("点名册1："+httpGetFunc);
	}
	
	/**
	 * 老师点击专业
	 */
	@Test
	public void testclick_rollcall_grade(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("grade_value", "2012");
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"click_rollcall_grade.do", param);
		System.out.println("点名册1："+httpGetFunc);
	}
	
	
	
	
	
	@Test
	public void testQuitSystem(){
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"quit_system.do", null);
		System.out.println(httpGetFunc);
	}
	
	
}