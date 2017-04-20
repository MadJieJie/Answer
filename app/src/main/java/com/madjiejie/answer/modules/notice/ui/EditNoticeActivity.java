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
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.madjiejie.answer.utils.often.RxUtils;
import com.madjiejie.answer.utils.often.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import static com.madjiejie.answer.modules.manuscript.base.EditTextConstant.CONTENT;
import static com.madjiejie.answer.modules.manuscript.base.EditTextConstant.CREATE_TIME;
import static com.madjiejie.answer.modules.manuscript.base.EditTextConstant.ID;
import static com.madjiejie.answer.modules.manuscript.base.EditTextConstant.TITLE;

public class EditNoticeActivity extends RxAppCompatActivity
{
	/** Widget */
	private EditText et_title;
	private EditText et_content;
	private TextView tv_createTime;
	
	/** Parameters */
	private static int mID = 0;
	
	private boolean isSuccess;
	private static final boolean SUCCESS = true;
	private static final boolean FAIL = false;
	
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
		mID = getIntent().getIntExtra(ID, 0);
		et_title.setText(getIntent().getStringExtra(TITLE));
		et_content.setText(getIntent().getStringExtra(CONTENT));
		tv_createTime.setText(getIntent().getStringExtra(CREATE_TIME));
	}
	
	private void initToolBar ()
	{
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);
		toolbar.setTitle("编辑");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(v -> finish());
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
			case R.id.action_save:          //update database's data.
				updateQuestionToDB();
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
		
	}
	
	private void updateQuestionToDB ()
	{
		Observable
				.create(( ObservableEmitter< Notice > e ) ->
				{
					Notice notice = new Notice();
					notice.noticeTitle = et_title.getText().toString();
					notice.noticeContent = et_content.getText().toString();
					notice.createTime = tv_createTime.getText().toString();
					e.onNext(notice);
					e.onComplete();
				})
				.compose(this.bindToLifecycle())
				.compose(RxUtils.rxSchedulerHelper())
				.subscribe(NoticeDB:: updateNotice);
	}
	
	
	/**
	 * Send question's data to server for save.
	 *
	 * @param biz
	 * @param json
	 * @return
	 */
	private boolean sendNoticeToServer ( final String biz, final String json )
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
							NoticeDB.deleteNotice(mID);
							isSuccess = SUCCESS;
							finish();
						} else
						{
							ToastUtils.showShort("发布失败,请检查");
							isSuccess = false;
						}
					}
				});
		return isSuccess;
	}

//	@Override
//	public void onBackPressed ()
//	{
//		dealWithExit();
//	}
	
	
}
