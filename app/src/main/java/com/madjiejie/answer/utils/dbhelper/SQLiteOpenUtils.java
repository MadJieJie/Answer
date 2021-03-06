package com.madjiejie.answer.utils.dbhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Created by MadJieJie on 2017/1/31-21:49.
 * @brief
 * @attention
 */

public class SQLiteOpenUtils extends SQLiteOpenHelper
{
	
	private final static String DB_NAME = "answer.db";      //数据库文件名
	private final static int DB_VERSION = 4;                // 数据库版本
	
	public SQLiteOpenUtils ( Context context )
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate ( SQLiteDatabase db )
	{
		//question table
		db.execSQL("CREATE TABLE tb_question(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				           "student_id INTEGER NOT NULL," +
				           "student_name VARCHAR(40) NOT NULL," +
				           "question_title VARCHAR(40) NOT NULL," +
				           "question_content text NOT NULL," +
				           "create_time datetime NOT NULL" +
				           ")");        //默认字符集utf8
		
		//notice table
		db.execSQL("CREATE TABLE tb_notice(id INTEGER PRIMARY KEY AUTOINCREMENT," +
				           "class_id INTEGER NOT NULL," +
				           "teacher_id INTEGER NOT NULL," +
				           "teacher_name VARCHAR(40) NOT NULL," +
				           "class_name VARCHAR(40) NOT NULL," +
				           "notice_title VARCHAR(255) NOT NULL," +
				           "notice_content text NOT NULL," +
				           "create_time datetime NOT NULL" +
				           ")");        //默认字符集utf8
		
		
		//创建分类表
//		db.execSQL("create table db_group(g_id INTEGER primary key autoincrement, " +
//				           "g_name varchar, g_order INTEGER, g_color varchar, g_encrypt INTEGER," +
//				           "g_create_time datetime, g_update_time datetime )");
		
		
		
		//创建笔记表
//		db.execSQL("create table db_note(n_id INTEGER primary key autoincrement, n_title varchar, " +
//				           "n_content varchar, n_group_id INTEGER, n_group_name varchar, n_type INTEGER, " +
//				           "n_bg_color varchar, n_encrypt INTEGER, n_create_time datetime," +
//				           "n_update_time datetime )");
		
		
		
		
//		db.execSQL("insert into db_group(g_name, g_order, g_color, g_encrypt, g_create_time, g_update_time) " +
//				           "values(?,?,?,?,?,?)", new String[]{ "默认笔记", "1", "#FFFFFF", "0", DateUtils.date2string(new Date()), DateUtils.date2string(new Date()) });
		
		
		
		
		//创建Table - bill
	/*	db.execSQL("CREATE TABLE bill(id INTEGER primary key autoincrement," +   //主键
				           "date DATE NOT NULL," +      //日期
				           "eat INTEGER NOT NULL," +    //吃
				           "traffic INTEGER NOT NULL," +
				           "buy INTEGER NOT NULL," +
				           "amusement INTEGER NOT NULL," +
				           "stay INTEGER NOT NULL," +
				           "ticket INTEGER NOT NULL," +
				           "treat INTEGER NOT NULL," +
				           "insurance INTEGER NOT NULL," +
				           "other INTEGER NOT NULL" +
				           ")");
		
		
		//创建Table - note
		db.execSQL("CREATE TABLE note(id INTEGER PRIMARY KEY AUTOINCREMENT," +      //主键
				           "title VARCHAR(40) NOT NULL," +                      //题目-限制15个中文字符
				           "content TEXT(65535) NOT NULL," +                            //内容
				           "create_time DATETIME NOT NULL," +                     //创建时间
				           "update_time DATETIME " +                     //更新时间
				           ")");
		
		
		
		
		//创建Table - travel_note
		db.execSQL("CREATE TABLE travel_note(id INTEGER PRIMARY KEY AUTOINCREMENT," +      //主键
				           "title VARCHAR(40) NOT NULL," +                      //题目-限制15个中文字符
				           "author VARCHAR(20) NOT NULL default 'anonymity'," +                      //作者
				           "content TEXT(65535) NOT NULL," +                            //内容
				           "create_time DATETIME NOT NULL," +                     //创建时间
				           "update_time DATETIME  " +                     //更新时间
				           ")");*/
	
		
	}
	
	@Override
	public void onUpgrade ( SQLiteDatabase db, int oldVersion, int newVersion )
	{
		
	}
}
