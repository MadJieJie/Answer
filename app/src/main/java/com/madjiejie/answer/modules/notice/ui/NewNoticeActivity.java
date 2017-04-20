package com.madjiejie.answer.modules.notice.ui;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.madjiejie.answer.R;
import com.madjiejie.answer.base.Biz;
import com.madjiejie.answer.base.Constant;
import com.madjiejie.answer.base.Result;
import com.madjiejie.answer.modules.notice.bean.Notice;
import com.madjiejie.answer.modules.notice.db.NoticeDB;
import com.madjiejie.answer.utils.often.AbstractSimpleClass;
import com.madjiejie.answer.utils.often.DateUtils;
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.madjiejie.answer.utils.often.RxUtils;
import com.madjiejie.answer.utils.often.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;


public class NewNoticeActivity extends RxAppCompatActivity
{
	/** Widget */
	private EditText et_title;
	private EditText et_content;
	private TextView tv_createTime;
	
	/** Parameters */
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_edit);
		findView();
		initView();
		initToolBar();
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	}
	
	private void findView ()
	{
		et_title = ( EditText ) findViewById(R.id.et_title);
		et_content = ( EditText ) findViewById(R.id.et_content);
		tv_createTime = ( TextView ) findViewById(R.id.tv_createTime);
	}
	
	private void initView ()
	{
		tv_createTime.setText(DateUtils.getNowDateAndTime());
	}
	
	private void initToolBar ()
	{
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(v -> finish());
		setTitle("公告");
	}
	
	/**
	 * Send notice's data to server for save.
	 *
	 * @param biz
	 * @param json
	 */
	private void sendNoticeToServer ( final String biz, final String json )
	{
		RetrofitSingleton.getInstance()
				.sendNotice(biz, json)
				.compose(this.bindToLifecycle())
				.subscribe(new AbstractSimpleClass.Next< Result >()
				{
					
					@Override
					public void onNext ( Result result )
					{
						if ( result.result.equals("success") )
						{
							ToastUtils.showShort("发布提问成功");
							finish();
						} else
						{
							ToastUtils.showShort("发布失败,请检查");
						}
					}
				});
		
	}
	
	
	private void insertQuestionToDB (final int classId,final String className)
	{
		Observable
				.create(( ObservableEmitter< Notice > e ) ->
				{
					Notice notice = new Notice();
					notice.classId = classId;
					notice.teacherId = Constant.sTeacher.id;
					notice.className = className;
					notice.teacherName = Constant.sTeacher.name;
					notice.noticeTitle = et_title.getText().toString();
					notice.noticeContent = et_content.getText().toString();
					notice.createTime = tv_createTime.getText().toString();
					e.onNext(notice);
					e.onComplete();
				})
				.compose(this.bindToLifecycle())
				.compose(RxUtils.rxSchedulerHelper())
				.subscribe(notice -> {
					NoticeDB.insertNotice(notice);
					finish();
					ToastUtils.showShort("保存数据成功");
				});
	}
	
	/**
	 * 从XML中读出menu配置
	 *
	 * @param menu
	 * @return
	 */
	@Override
	public boolean onCreateOptionsMenu ( Menu menu )
	{
		getMenuInflater().inflate(R.menu.menu_edit, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		switch ( item.getItemId() )
		{
			case R.id.action_save:          //插入数据库
				insertQuestionToDB(0,"NULL");
				break;
			case R.id.action_publish:
				Notice notice = new Notice();
				notice.classId = 0;
				notice.teacherId = Constant.sTeacher.id;
				notice.className = "NULL";
				notice.teacherName = Constant.sTeacher.name;
				notice.noticeTitle = et_title.getText().toString();
				notice.noticeContent = et_content.getText().toString();
				notice.createTime = tv_createTime.getText().toString();
				Gson gson = new Gson();
				String json = gson.toJson(notice);
				sendNoticeToServer(Biz.INSERT_NOTICE, json);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
	}
	
	@Override
	protected void onStop ()
	{
		super.onStop();
		//如果APP处于后台，或者手机锁屏，则启用密码锁
//		if (
////				CommonUtil.isAppOnBackground(getApplicationContext()) ||
//				CommonUtil.isLockScreeen(getApplicationContext()) )
//		{
//			saveNoteData(true);//处于后台时保存数据
//		}
		
	}

//	@Override
//	public void onBackPressed ()
//	{
////		dealWithExit();
//	}
}
