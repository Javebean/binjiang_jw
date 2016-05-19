package com.binjiang.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.binjiang.http.CallRemote;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class teaTest {

	private String baseUrl = "http://localhost:8081/schedule/";
	
	CallRemote call = new CallRemote();
	@Test
	public void test1Load(){
		Map<String,String> param = new HashMap<String, String>();
		/*param.put("username", "20122344003");
		param.put("password", "888888");*/
		/*param.put("username", "20132308020");
		param.put("password", "001221");*/
		param.put("username", "000519");
		param.put("password", "050125");
		/*param.put("username", "20122344004");
		param.put("password", "ch2oh2o");*/
		 		
		param.put("role", "RadioButton1");//老师：RadioButton1 学生：RadioButton3
		 String httpPostFunc = CallRemote.httpPostFunc(baseUrl+"loginbjxy.do", param);
		 System.out.println("登陆成功："+httpPostFunc);
		 
		// testgetStu_pro();
		// testgetStu_grade();
	}
	
	 
	/**
	 * 根据课程查询学生名单
	 * @param type 0:laoshi
	 * @return
	 */
	@Test
	public void testqueryStudentsByCourse(){
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"query_stu_bycourse.do", null);
		System.out.println("名单："+httpGetFunc);
	}
	/**
	 * 点击浏览。浏览学生名单
	 * @param type 0:laoshi
	 * @return
	 */
	@Test
	public void testclick_brower_stunames(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("param", "GcGridView1$ctl02$LinkButton1");
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"click_brower_stunames.do", param);
		System.out.println("名单："+httpGetFunc);
	}
	
	
	/**
	 * 查询老师或者学生单独的上课课表
	 * @param type 0:laoshi
	 * @return
	 */
	@Test
	public void test2Tea_stu_self_course(){
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
	public void test3Tea_rollcall(){
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"tea_rollcall.do", null);
		System.out.println("点名册："+httpGetFunc);
	}
	
	
	/**
	 * 老师点击系部
	 */
	@Test
	public void test4click_rollcall_document(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("document_value", "滨江学院计算机系");
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"click_rollcall_document.do", param);
		System.out.println("点名册1："+httpGetFunc);
	}
	/**
	 * 老师点击专业
	 */
	@Test
	public void test5click_rollcall_profession(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("profession_value", "2344");
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"click_rollcall_profession.do", param);
		System.out.println("点名册1："+httpGetFunc);
	}
	
	/**
	 * 老师点击专业
	 */
	@Test
	public void test6click_rollcall_grade(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("grade_value", "2012");
		
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"click_rollcall_grade.do", param);
		System.out.println("点名册1："+httpGetFunc);
	}
	
	
	/**
	 * 查询班级点名册
	 */
	@Test
	public void test8class_rollcall_names(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("DropDownList4", "滨江学院计算机系");
		param.put("DropDownList5", "2344");
		param.put("DropDownList3", "2012");
		param.put("DropDownList6", "12软件2");
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"class_rollcall_names.do", param);
		System.out.println("点名册@："+httpGetFunc);
	}
	
	/**
	 * 查询班级点名册---下一页
	 */
	@Test
	public void test8next_class_rollcall_names(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("DropDownList4", "滨江学院计算机系");
		param.put("DropDownList5", "2344");
		param.put("DropDownList3", "2012");
		param.put("DropDownList6", "12软件2");
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"next_class_rollcall_names.do", param);
		System.out.println("下一页："+httpGetFunc);
	}
	/**
	 * 查询班级点名册---下一页
	 */
	@Test
	public void test9previous_class_rollcall_names(){
		Map<String,String> param = new HashMap<String, String>();
		param.put("DropDownList4", "滨江学院计算机系");
		param.put("DropDownList5", "2344");
		param.put("DropDownList3", "2012");
		param.put("DropDownList6", "12软件2");
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"previous_class_rollcall_names.do", param);
		System.out.println("上一页："+httpGetFunc);
	}
	
	@Test
	public void testz0QuitSystem(){
		String httpGetFunc = CallRemote.httpGetFunc(baseUrl+"quit_system.do", null);
		System.out.println(httpGetFunc);
	}
	
	
	@Test
	public void test3333(){
		Map<String, String[]> p = new HashMap<String, String[]>();
		String []s1 = new String[]{"1z","2x"};
		p.put("aaa", s1);
		
		Object[] key_arr = p.values().toArray();
		for(Object o: key_arr){
			System.out.println(Arrays.toString((String[]) o));
		}
		
	}

}
