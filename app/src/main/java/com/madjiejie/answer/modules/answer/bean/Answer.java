package com.madjiejie.answer.modules.answer.bean;

/**
 * @author Created by MadJieJie on 2017/4/9-11:38.
 * @brief
 * @attention
 */

public class Answer
{
	public int id;
	public int questionId;
	public int teacherId;
	public String teacherName;
	public String answerContent;
	public String answerTime;
	
	public Answer ( String teacherName, String answerContent, String answerTime )
	{
		this.teacherName = teacherName;
		this.answerContent = answerContent;
		this.answerTime = answerTime;
	}
	
	public Answer()
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
	
	public int getQuestionId ()
	{
		return questionId;
	}
	
	public void setQuestionId ( int questionId )
	{
		this.questionId = questionId;
	}
	
	public int getTeacherId ()
	{
		return teacherId;
	}
	
	public void setTeacherId ( int teacherId )
	{
		this.teacherId = teacherId;
	}
	
	public String getTeacherName ()
	{
		return teacherName;
	}
	
	public void setTeacherName ( String teacherName )
	{
		this.teacherName = teacherName;
	}
	
	public String getAnswerContent ()
	{
		return answerContent;
	}
	
	public void setAnswerContent ( String answerContent )
	{
		this.answerContent = answerContent;
	}
	
	public String getAnswerTime ()
	{
		return answerTime;
	}
	
	public void setAnswerTime ( String answerTime )
	{
		this.answerTime = answerTime;
	}
}
