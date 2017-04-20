package com.madjiejie.answer.modules.question.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Created by MadJieJie on 2017/4/13-16:36.
 * @brief
 * @attention
 */

public class Question implements Serializable
{
	private static final long serialVersionUID = -1L;
	
	@SerializedName ( "id" )
	public int id;
	
	@SerializedName ( "studentId" )
	public int studentId;
	
	@SerializedName ( "studentName" )
	public String studentName;
	
	@SerializedName ( "questionTitle" )
	public String questionTitle;
	
	@SerializedName ( "questionContent" )
	public String questionContent;
	
	@SerializedName ( "createTime" )
	public String createTime;
	
	
	public int getId ()
	{
		return id;
	}
	
	public void setId ( int id )
	{
		this.id = id;
	}
	
	public int getStudentId ()
	{
		return studentId;
	}
	
	public void setStudentId ( int studentId )
	{
		this.studentId = studentId;
	}
	
	public String getStudentName ()
	{
		return studentName;
	}
	
	public void setStudentName ( String studentName )
	{
		this.studentName = studentName;
	}
	
	public String getQuestionTitle ()
	{
		return questionTitle;
	}
	
	public void setQuestionTitle ( String questionTitle )
	{
		this.questionTitle = questionTitle;
	}
	
	public String getQuestionContent ()
	{
		return questionContent;
	}
	
	public void setQuestionContent ( String questionContent )
	{
		this.questionContent = questionContent;
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
