package com.madjiejie.answer.modules.answer.ui;


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
import com.madjiejie.answer.modules.answer.bean.Answer;
import com.madjiejie.answer.modules.main.base.MainConstant;
import com.madjiejie.answer.utils.often.AbstractSimpleClass;
import com.madjiejie.answer.utils.often.DateUtils;
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.madjiejie.answer.utils.often.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


public class NewAnswerActivity extends RxAppCompatActivity
{
	/** Widget */
	private EditText et_content;
	private TextView tv_createTime;
	
	/** Parameters */
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer);
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
		setTitle("回答");
	}
	
	/**
	 * Send question's data to server for save.
	 *
	 * @param biz
	 * @param json
	 */
	private void sendQuestionToServer ( final String biz, final String json )
	{
		RetrofitSingleton.getInstance()
				.sendAnswer(biz, json)
				.compose(this.bindToLifecycle())
				.subscribe(new AbstractSimpleClass.Next< Result >()
				{
					
					@Override
					public void onNext ( Result result )
					{
						if ( result.result.equals("success") )
						{
							ToastUtils.showShort("回答问题成功");
							finish();
						} else
						{
							ToastUtils.showShort("回答失败,请检查");
						}
					}
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
		getMenuInflater().inflate(R.menu.menu_answer, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		switch ( item.getItemId() )
		{
			case R.id.action_publish:       //Send data to server.
				Answer answer = new Answer();
				answer.setQuestionId(getIntent().getIntExtra(MainConstant.QUESTION_ID, 0));
				answer.setTeacherId(Constant.sTeacher.id);
				answer.setTeacherName(Constant.sTeacher.name);
				answer.setAnswerContent(et_content.getText().toString());
				answer.setAnswerTime(tv_createTime.getText().toString());
				Gson gson = new Gson();
				String json = gson.toJson(answer);
				sendQuestionToServer(Biz.INSERT_ANSWER, json);
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
