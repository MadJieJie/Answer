package com.madjiejie.answer.modules.question.ui;


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
import com.madjiejie.answer.modules.question.bean.Question;
import com.madjiejie.answer.modules.question.db.QuestionDB;
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

public class EditQuestionActivity extends RxAppCompatActivity
{
	/** Widget */
	private EditText et_title;
	private EditText et_content;
	private TextView tv_createTime;
	
	/** Parameters */
	private static int mID = 0;
	private static final int CUT_TITLE_LENGTH = 15;     //截取的标题长度
	
	private String mMode;      //获取传输过来的字符串标志位
	
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
				Question question = new Question();
				question.setStudentId(Constant.sStudent.id);
				question.setStudentName(Constant.sStudent.name);
				question.setQuestionTitle(et_title.getText().toString());
				question.setQuestionContent(et_content.getText().toString());
				question.setCreateTime(tv_createTime.getText().toString());
				Gson gson = new Gson();
				String json = gson.toJson(question);
				sendQuestionToServer(Biz.INSERT_QUESTION, json); //When success that send data to server,then delete from database.
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
				.create(( ObservableEmitter< Question > e ) ->
				{
					Question question = new Question();
					question.setQuestionTitle(et_title.getText().toString());
					question.setQuestionContent(et_content.getText().toString());
					question.setCreateTime(tv_createTime.getText().toString());
					e.onNext(question);
					e.onComplete();
				})
				.compose(this.bindToLifecycle())
				.compose(RxUtils.rxSchedulerHelper())
				.subscribe(QuestionDB:: updateQuestion);
	}
	
	
	/**
	 * Send question's data to server for save.
	 *
	 * @param biz
	 * @param json
	 * @return
	 */
	private boolean sendQuestionToServer ( final String biz, final String json )
	{
		
		RetrofitSingleton.getInstance()
				.sendQuestion(biz, json)
				.compose(this.bindToLifecycle())
				.subscribe(new AbstractSimpleClass.Next< Result >()
				{
					
					@Override
					public void onNext ( Result result )
					{
						if ( result.result.equals("success") )
						{
							ToastUtils.showShort("发布提问成功");
							QuestionDB.deleteQuestion(mID);
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
