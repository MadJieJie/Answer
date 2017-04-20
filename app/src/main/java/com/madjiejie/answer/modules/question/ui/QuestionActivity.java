package com.madjiejie.answer.modules.question.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.madjiejie.answer.R;
import com.madjiejie.answer.adapter.recyclerview.adapter.CommonAdapter;
import com.madjiejie.answer.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.madjiejie.answer.adapter.recyclerview.base.ViewHolder;
import com.madjiejie.answer.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.madjiejie.answer.base.Biz;
import com.madjiejie.answer.base.Constant;
import com.madjiejie.answer.modules.answer.bean.Answer;
import com.madjiejie.answer.modules.answer.ui.NewAnswerActivity;
import com.madjiejie.answer.modules.main.base.MainConstant;
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class QuestionActivity extends RxAppCompatActivity
{
	private XRecyclerView mXRecyclerView;
	private CommonAdapter< Answer > mAdapter;
	private List< Answer > mData = new ArrayList<>();
	private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
	
	/** Parameters */
	private static final int CUT_TITLE_LENGTH = 15;     //截取的标题长度
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_question);
		findView();
		initView();
		initToolBar();
		initRecyclerView();
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
		getAllDataForServer(Biz.QUERY_ANSWER,getIntent().getIntExtra(MainConstant.QUESTION_ID,0));
	}
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	}
	
	private void findView ()
	{
	}
	
	private void initView ()
	{
	}
	
	
	private void initToolBar ()
	{
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);
		setTitle("问题");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(v -> finish());
	}
	
	
	private void initRecyclerView ()
	{
//		mData = new ArrayList<>();
//		mData.add(new Answer("教师", "同学你好,通过分析电位来判断两点对地电压是否相等。", DateUtils.getNowDateAndTime()));
		
		mAdapter = new CommonAdapter< Answer >(this, R.layout.item_answer_question, mData)
		{
			@Override
			protected void convert ( ViewHolder holder, Answer info, int position )
			{
				holder
						.setText(R.id.tv_name_lookQuestion, info.teacherName)
						.setText(R.id.tv_answerContent_lookQuestion, info.answerContent)
						.setText(R.id.tv_answerTime_lookQuestion, info.answerTime);
			}
		};
		
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				return false;
			}
		});
		
		mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
		
		View topQuestion = LayoutInflater.from(this).inflate(R.layout.item_top_question, null);
		TextView name = ( TextView ) topQuestion.findViewById(R.id.tv_studentName_lookQuestion);
		TextView title = ( TextView ) topQuestion.findViewById(R.id.tv_title_lookQuestion);
		TextView questionContent = ( TextView ) topQuestion.findViewById(R.id.tv_questionContent_lookQuestion);
		TextView createTime = ( TextView ) topQuestion.findViewById(R.id.tv_answerTime_lookQuestion);
		name.setText(getIntent().getStringExtra(MainConstant.STUDENT_NAME));
		title.setText(getIntent().getStringExtra(MainConstant.QUESTION_TITLE));
		questionContent.setText(getIntent().getStringExtra(MainConstant.QUESTION_CONTENT));
		createTime.setText(getIntent().getStringExtra(MainConstant.CREATE_TIME));
		mHeaderAndFooterWrapper.addHeaderView(topQuestion);
		
		mXRecyclerView = ( XRecyclerView ) findViewById(R.id.recyclerView);
		mXRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
		
		mXRecyclerView.setLoadingMoreEnabled(false);
//		mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener()       //上下拉监听
//		{
//			@Override
//			public void onRefresh ()
//			{
//				mXRecyclerView.refreshComplete();
//			}
//
//			@Override
//			public void onLoadMore ()
//			{
//				mXRecyclerView.loadMoreComplete();
//			}
//		});
		
		mXRecyclerView.setAdapter(mHeaderAndFooterWrapper);
		
	}
	
	/**
	 * Get this question below that answers for server.
	 * @param biz Biz.QUERY_ALL_QUESTION
	 */
	private void getAllDataForServer(final String biz,final int questionId)
	{
		RetrofitSingleton.getInstance()
				.getAnswer(biz,questionId)
				.compose(this.bindToLifecycle())
				.subscribe(questions -> {
					mData.clear();
					mData.addAll(questions);
					mHeaderAndFooterWrapper.notifyDataSetChanged();
				});
	}
	
	/**
	 * Create menu and get data for xml file.
	 *
	 * @param menu
	 * @return
	 */
	@Override
	public boolean onCreateOptionsMenu ( Menu menu )
	{
		if ( Constant.USER_TYPE == Constant.TEACHER )
			getMenuInflater().inflate(R.menu.menu_answer_question, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		if ( item.getItemId() == R.id.action_answer )
		{
			Intent intent =  new Intent(QuestionActivity.this, NewAnswerActivity.class);
			intent.putExtra(MainConstant.QUESTION_ID,getIntent().getIntExtra(MainConstant.QUESTION_ID,0));
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
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
