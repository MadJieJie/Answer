package com.madjiejie.answer.modules.notice.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.madjiejie.answer.base.BaseApplication;
import com.madjiejie.answer.modules.notice.bean.Notice;
import com.madjiejie.answer.utils.dbhelper.SQLiteOpenUtils;

import java.util.ArrayList;

/**
 * @author Created by name on 2017/4/14-8:18.
 * @brief
 * @attention
 */

public class NoticeDB
{
	private static final String SQL_INSERT_DATA = "INSERT INTO tb_notice(class_id,teacher_id,class_name,teacher_name,notice_title,notice_content,create_time) " +
			                                              "VALUES(?,?,?,?,?,?,?)";
	private static final String SQL_UPDATE_DATA = "UPDATE tb_notice " +
			                                              "SET notice_title = ?," +
			                                              "SET notice_content = ?," +
			                                              "SET create_time = ? ";
	private static final String SQL_QUERY_ALL_DATA = "SELECT id,class_id,teacher_id,class_name,teacher_name,notice_title,notice_content,create_time FROM tb_notice";
	
	private static final String SQL_DELETE_DATA = "DELETE FROM tb_notice WHERE id = ?";
	
	private static class NoticeDBInstance
	{
		private static SQLiteOpenUtils  helper = new SQLiteOpenUtils(BaseApplication.getAppContext());
	}
	
	
	/**
	 * Close DB open helper.
	 */
	public static void openDBHelper ()
	{
		if ( NoticeDBInstance.helper == null )
			NoticeDBInstance.helper = new SQLiteOpenUtils(BaseApplication.getAppContext());
	}
	
	/**
	 * Close DB open helper.
	 */
	public static void closeOpenDBHelper ()
	{
		if ( NoticeDBInstance.helper != null )
			NoticeDBInstance.helper.close();
	}
	
	
	/**
	 * Insert a Notice element to database.
	 *
	 * @return false:The reason may be that elements is null or fail that insert into database.
	 */
	public static boolean insertNotice ( Notice notice )
	{
		SQLiteDatabase db = NoticeDBInstance.helper.getWritableDatabase();
		
		try
		{
			if ( notice != null )
			{
				db.execSQL(SQL_INSERT_DATA,
						new String[]{ notice.classId + "", notice.teacherId + "",
								notice.className, notice.teacherName, notice.noticeTitle, notice.noticeContent, notice.createTime });
				return true;
			}
		} catch( SQLException e )
		{
			e.printStackTrace();
		} finally
		{
			if ( db != null )
			{
				db = null;
			}
//			db.close();
		}
		
		return false;
	}
	
	/**
	 * Update a Notice element to database.
	 *
	 * @return false:The reason may be that elements is null or fail that insert into database.
	 */
	public static boolean updateNotice ( Notice notice )
	{
		SQLiteDatabase db = NoticeDBInstance.helper.getWritableDatabase();
		
		try
		{
			if ( notice != null )
			{
				db.execSQL(SQL_UPDATE_DATA,
						new String[]{ notice.noticeTitle, notice.noticeContent, notice.createTime });
				return true;
			}
		} catch( SQLException e )
		{
			e.printStackTrace();
		} finally
		{
			if ( db != null )
				db = null;
//			db.close();
		}
		
		return false;
	}
	
	
	/**
	 * Query all for get all data.
	 *
	 * @return return data's list.
	 */
	public static ArrayList< Notice > queryNoticeItem ()
	{
		SQLiteDatabase db = NoticeDBInstance.helper.getWritableDatabase();
		Cursor cursor = null;
		ArrayList< Notice > list = new ArrayList< Notice >();
		
		try
		{
			cursor = db.rawQuery(SQL_QUERY_ALL_DATA, null);
			while ( cursor.moveToNext() )
			{
				Notice notice = new Notice();
				notice.id = cursor.getInt(cursor.getColumnIndex("id"));
				notice.classId = cursor.getInt(cursor.getColumnIndex("class_id"));
				notice.teacherId = cursor.getInt(cursor.getColumnIndex("teacher_id"));
				notice.className = cursor.getString(cursor.getColumnIndex("class_name"));
				notice.teacherName = cursor.getString(cursor.getColumnIndex("teacher_name"));
				notice.noticeTitle = cursor.getString(cursor.getColumnIndex("notice_title"));
				notice.noticeContent = cursor.getString(cursor.getColumnIndex("notice_content"));
				notice.createTime = cursor.getString(cursor.getColumnIndex("create_time"));
				list.add(notice);
			}
		} catch( SQLException e )
		{
			e.printStackTrace();
		} finally
		{
			if ( cursor != null )
				cursor.close();
			if ( db != null )
				db = null;
//				db.close();
			
		}
		
		return list;
	}
	
	/**
	 * Use id delete notice's data.
	 *
	 * @param id Notice' id.
	 * @return false:error
	 */
	public static boolean deleteNotice ( final int id )
	{
		SQLiteDatabase db = NoticeDBInstance.helper.getWritableDatabase();
		
		try
		{
			if ( id != 0 )
			{
				db.execSQL(SQL_DELETE_DATA, new String[]{ id + "" });
				return true;
			}
		} catch( SQLException e )
		{
			e.printStackTrace();
		} finally
		{
			if ( db != null )
				db = null;
//			db.close();
		}
		
		return false;
	}
	
	
}
