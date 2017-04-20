package com.madjiejie.answer.base;

import com.madjiejie.answer.modules.answer.bean.Answer;
import com.madjiejie.answer.modules.notice.bean.Notice;
import com.madjiejie.answer.modules.question.bean.Question;
import com.madjiejie.answer.modules.user.bean.Student;
import com.madjiejie.answer.modules.user.bean.StudentClass;
import com.madjiejie.answer.modules.user.bean.Teacher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Created by MadJieJie on 2017/4/11-20:48.
 * @brief
 * @attention
 */

public interface IApi
{
	String HOST_URL = "http://119.29.11.95:8080/Server/";        //记住是无线局域网,不要连 WLAN-IP4
	
	
	@POST ( "register" )
	@FormUrlEncoded
	Call< Result > register ( @Field ( "biz" ) String biz,                       //用户类型
	                          @Field ( "email" ) String register_account,           //账号
	                          @Field ( "password" ) String register_password,    //密码
	                          @Field ( "user_name" ) String user_name );             //用户名
	
	
	@POST ( "login" )
	@FormUrlEncoded
	Call< Student > studentLogin ( @Field ( "biz" ) String biz,              //用户类型
	                               @Field ( "email" ) String account,        //账号
	                               @Field ( "password" ) String password ); //密码
	
	@POST ( "login" )
	@FormUrlEncoded
	Call< Teacher > teacherLogin ( @Field ( "biz" ) String biz,              //用户类型
	                               @Field ( "email" ) String account,        //账号
	                               @Field ( "password" ) String password ); //密码
	
	@POST ( "class_info" )
	@FormUrlEncoded
	Call< List< StudentClass > > getClassInfo ( @Field ( "biz" ) String biz );    //事务类型
	
	@POST ( "revise_info" )
	@FormUrlEncoded
	Call< Result > sendReviseInfo ( @Field ( "biz" ) String biz,         //事务类型
	                                @Field ( "json" ) String json );
	
	@POST ( "insert_question" )
	@FormUrlEncoded
	Call< Result > sendQuestion ( @Field ( "biz" ) String biz,         //事务类型
	                              @Field ( "json" ) String json );
	
	@POST ( "query_question" )
	@FormUrlEncoded
	Call< ArrayList< Question > > getAllQuestion ( @Field ( "biz" ) String biz );       //事务类型
	
	@POST ( "insert_answer" )
	@FormUrlEncoded
	Call< Result > sendAnswer ( @Field ( "biz" ) String biz,         //事务类型
	                            @Field ( "json" ) String json );
	
	@POST ( "query_answer" )
	@FormUrlEncoded
	Call< ArrayList< Answer > > getAnswer ( @Field ( "biz" ) String biz,        //事务类型
	                                        @Field ( "question_id" ) int questionId );
	
	@POST ( "insert_notice" )
	@FormUrlEncoded
	Call< Result > sendNotice ( @Field ( "biz" ) String biz,         //事务类型
	                            @Field ( "json" ) String json );
	
	@POST ( "query_notice" )
	@FormUrlEncoded
	Call< ArrayList< Notice > > getAllNotice ( @Field ( "biz" ) String biz );       //事务类型
	
	@POST ( "query_notice" )
	@FormUrlEncoded
	Call< Notice > getOneNewNotice ( @Field ( "biz" ) String biz );       //事务类型
}
