package com.madjiejie.answer.modules.notice.bean;

/**
 * @author Created by teacherName on 2017/4/11-10:37.
 * @brief
 * @attention
 */

public class Notice
{
	public int id;
	public int classId;
	public int teacherId;
	public String className;
	public String teacherName;
	public String noticeTitle;
	public String noticeContent;
	public String createTime;
	
	public Notice ( String teacherName, String noticeTitle, String noticeContent, String createTime )
	{
		this.teacherName = teacherName;
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.createTime = createTime;
	}
	public Notice()
	{
		
	}
}
