package com.madjiejie.answer.modules.main.bean;

/**
 * @author Created by MadJieJie on 2017/4/9-11:38.
 * @brief
 * @attention
 */

public class QuestionItem
{

	public String title;
	public String question;
	public String studentName;
	public String createTime;
	
	public QuestionItem ( String title, String question, String studentName, String createTime )
	{
		this.title = title;
		this.question = question;
		this.studentName = studentName;
		this.createTime = createTime;
	}
	
	public QuestionItem()
	{
		
	}
	
	public String getTitle ()
	{
		return title;
	}
	
	public void setTitle ( String title )
	{
		this.title = title;
	}
	
	public String getQuestion ()
	{
		return question;
	}
	
	public void setQuestion ( String question )
	{
		this.question = question;
	}
	
	public String getStudentName ()
	{
		return studentName;
	}
	
	public void setStudentName ( String studentName )
	{
		this.studentName = studentName;
	}
	
	public String getCreateTime ()
	{
		return createTime;
	}
	
	public void setCreateTime ( String createTime )
	{
		this.createTime = createTime;
	}
}
