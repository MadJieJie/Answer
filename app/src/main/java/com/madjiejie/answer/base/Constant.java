package com.madjiejie.answer.base;

import com.madjiejie.answer.modules.user.bean.Student;
import com.madjiejie.answer.modules.user.bean.Teacher;

/**
 * @author Created by man on 2017/4/10-20:35.
 * @brief 用于存储基本的用户数据
 * @attention
 */

public class Constant
{
	public static String MODE;
	
	public static final int MAN = 1;
	public static final int WOMAN = 2;
	
	public static final int STUDENT = 3;
	public static final int TEACHER = 4;
	
	public static int USER_TYPE = STUDENT;
	
	
	public static Student sStudent  =  new Student(1,"李明","2013级电信一班","201312700002","男","name@foxmail.com","123456");
	
	public static Teacher sTeacher = new Teacher(1,"教师","男","name@foxmail.com","123456");
	
	
}
