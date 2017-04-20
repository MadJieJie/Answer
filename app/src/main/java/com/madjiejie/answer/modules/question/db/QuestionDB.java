package com.madjiejie.answer.modules.question.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.madjiejie.answer.base.BaseApplication;
import com.madjiejie.answer.modules.question.bean.Question;
import com.madjiejie.answer.utils.dbhelper.SQLiteOpenUtils;

import java.util.ArrayList;

/**
 * @author Created by name on 2017/4/14-8:18.
 * @brief
 * @attention
 */

public class QuestionDB
{
	private static final String SQL_INSERT_DATA = "INSERT INTO tb_question(student_id,student_name,question_title,question_content,create_time) " +
			                                              "VALUES(?,?,?,?,?)";
	private static final String SQL_UPDATE_DATA = "UPDATE tb_question " +
			                                              "SET question_title = ?," +
			                                              "SET question_content = ?," +
			                                              "SET create_time = ? ";
	private static final String SQL_QUERY_ALL_DATA = "SELECT id,student_id,student_name,question_title,question_content,create_time FROM tb_question";
	
	private static final String SQL_DELETE_DATA = "DELETE FROM tb_question WHERE id = ?";
	
	private static class QuestionDBInstance
	{
		private static SQLiteOpenUtils helper = new SQLiteOpenUtils(BaseApplication.getAppContext());
	}
	
	
	/**
	 * Close DB open helper.
	 */
	public static void openDBHelper ()
	{
		if ( QuestionDBInstance.helper == null )
			QuestionDBInstance.helper = new SQLiteOpenUtils(BaseApplication.getAppContext());
	}
	
	/**
	 * Close DB open helper.
	 */
	public static void closeOpenDBHelper ()
	{
		if ( QuestionDBInstance.helper != null )
			QuestionDBInstance.helper.close();
	}
	
	
	/**
	 * Insert a Question element to database.
	 *
	 * @return false:The reason may be that elements is null or fail that insert into database.
	 */
	public static boolean insertQuestion ( Question question )
	{
		SQLiteDatabase db = QuestionDBInstance.helper.getWritableDatabase();
		ArrayList< Question > list = new ArrayList< Question >();
		
		try
		{
			if ( question != null )
			{
				db.execSQL(SQL_INSERT_DATA,
						new String[]{ question.id + "", question.studentName, question.questionTitle, question.questionContent, question.createTime });
				return true;
			}
		} catch( SQLException e )
		{
			e.printStackTrace();
		} finally
		{
			if ( db != null )
				db = null;
//				db.close();
		}
		
		return false;
	}
	
	/**
	 * Update a Question element to database.
	 *
	 * @return false:The reason may be that elements is null or fail that insert into database.
	 */
	public static boolean updateQuestion ( Question question )
	{
		SQLiteDatabase db = QuestionDBInstance.helper.getWritableDatabase();
		ArrayList< Question > list = new ArrayList< Question >();
		
		try
		{
			if ( question != null )
			{
				db.execSQL(SQL_UPDATE_DATA,
						new String[]{ question.questionTitle, question.questionContent, question.createTime });
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
	public static ArrayList< Question > queryQuestionItem ()
	{
		SQLiteDatabase db = QuestionDBInstance.helper.getWritableDatabase();
		Cursor cursor = null;
		ArrayList< Question > list = new ArrayList< Question >();
		
		try
		{
			cursor = db.rawQuery(SQL_QUERY_ALL_DATA, null);
			while ( cursor.moveToNext() )
			{
				Question question = new Question();
				question.id = cursor.getInt(cursor.getColumnIndex("id"));
				question.studentId = cursor.getInt(cursor.getColumnIndex("student_id"));
				question.studentName = cursor.getString(cursor.getColumnIndex("student_name"));
				question.questionTitle = cursor.getString(cursor.getColumnIndex("question_title"));
				question.questionContent = cursor.getString(cursor.getColumnIndex("question_content"));
				question.createTime = cursor.getString(cursor.getColumnIndex("create_time"));
				list.add(question);
			}
		} catch( SQLException e )
		{
			e.printStackTrace();
		} finally
		{
			if ( db != null )
				db = null;
//				db.close();
			if ( cursor != null )
				cursor.close();
		}
		
		return list;
	}
	
	/**
	 * Use id delete question's data.
	 *
	 * @param id Question' id.
	 * @return false:error
	 */
	public static boolean deleteQuestion ( final int id )
	{
		SQLiteDatabase db = QuestionDBInstance.helper.getWritableDatabase();
		
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
//				db.close();
		}
		
		return false;
	}
	
	
}
