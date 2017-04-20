package com.madjiejie.answer.modules.user.bean;

/**
 * @author Created by name on 2017/4/10-20:41.
 * @brief
 * @attention
 */

public class Student
{
	public int id;
	private int classId;
	public String name;
	public String className;
	public String studentNo;
	public String sex;
	public String email;
	public String password;
	
	public Student ( int id, String name, String className, String studentNo, String sex, String email, String password )
	{
		this.id = id;
		this.name = name;
		this.className = className;
		this.studentNo = studentNo;
		this.sex = sex;
		this.email = email;
		this.password = password;
	}
	
	public Student()
	{
		
	}
	
	
	
	public int getId ()
	{
		return id;
	}
	
	public void setId ( int id )
	{
		this.id = id;
	}
	
	public int getClassId ()
	{
		return classId;
	}
	
	public void setClassId ( int classId )
	{
		this.classId = classId;
	}
	
	public String getName ()
	{
		return name;
	}
	
	public void setName ( String name )
	{
		this.name = name;
	}
	
	public String getClassName ()
	{
		return className;
	}
	
	public void setClassName ( String className )
	{
		this.className = className;
	}
	
	public String getStudentNo ()
	{
		return studentNo;
	}
	
	public void setStudentNo ( String studentNo )
	{
		this.studentNo = studentNo;
	}
	
	public String getSex ()
	{
		return sex;
	}
	
	public void setSex ( String sex )
	{
		this.sex = sex;
	}
	
//	public String toString()
//	{
//		return "id:"+id+"classId"+"name"+name+
//	}
}
