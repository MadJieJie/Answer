package com.madjiejie.answer.modules.manuscript.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.madjiejie.answer.R;
import com.madjiejie.answer.adapter.recyclerview.adapter.CommonAdapter;
import com.madjiejie.answer.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.madjiejie.answer.adapter.recyclerview.base.ViewHolder;
import com.madjiejie.answer.modules.question.ui.EditQuestionActivity;
import com.madjiejie.answer.modules.question.bean.Question;
import com.madjiejie.answer.modules.question.db.QuestionDB;
import com.madjiejie.answer.utils.often.RxUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

import static com.madjiejie.answer.modules.manuscript.base.EditTextConstant.CONTENT;
import static com.madjiejie.answer.modules.manuscript.base.EditTextConstant.CREATE_TIME;
import static com.madjiejie.answer.modules.manuscript.base.EditTextConstant.ID;
import static com.madjiejie.answer.modules.manuscript.base.EditTextConstant.TITLE;

public class QuestionManuscriptActivity extends RxAppCompatActivity
{
	
	private XRecyclerView mXRecyclerView;
	private CommonAdapter< Question > mAdapter;
	private List< Question > mData = new ArrayList<>();
	private static boolean FIRST_UPDATE = true;
	
	@Override
	protected void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manuscript);
		findView();
		initView();
		initToolbar();
		initRecyclerView();
	}
	
	@Override
	protected void onStop ()
	{
		super.onStop();
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
		if(!FIRST_UPDATE)
		{
			getAllDataForDB();
			mAdapter.notifyDataSetChanged();
		}
		else
		{
			FIRST_UPDATE =false;
		}
	}
	
	
	@Override
	protected void onDestroy ()
	{
		super.onDestroy();
	}
	
	//初始化Toolbar
	private void initToolbar ()
	{
		Toolbar toolbar = ( Toolbar ) findViewById(R.id.toolbar);
		toolbar.setTitle("草稿箱");
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar.setNavigationOnClickListener(v -> finish());
	}
	
	private void findView ()
	{
		
	}
	
	private void initView ()
	{
		
	}
	
	private void initRecyclerView ()
	{
		
		getAllDataForDB();          //Add data.
		
		mAdapter = new CommonAdapter< Question >(this, R.layout.item_main, mData)
		{
			@Override
			protected void convert ( ViewHolder holder, Question info, int position )
			{
				holder
						.setText(R.id.tv_noticeTitle_main, info.questionTitle)
						.setText(R.id.tv_questionContent_main, info.questionContent)
						.setText(R.id.tv_studentName_main, info.studentName)
						.setText(R.id.tv_createTime_main, info.createTime);
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				Intent intent = new Intent(QuestionManuscriptActivity.this, EditQuestionActivity.class);
				intent.putExtra(ID, mData.get(position - 1).id);
				intent.putExtra(TITLE, mData.get(position - 1).questionTitle);
				intent.putExtra(CONTENT, mData.get(position - 1).questionContent);
				intent.putExtra(CREATE_TIME, mData.get(position - 1).createTime);
				startActivity(intent);
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				return false;
			}
		});
		
		mXRecyclerView = ( XRecyclerView ) findViewById(R.id.recyclerView);
		
		mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		mXRecyclerView.setLoadingMoreEnabled(false);
		mXRecyclerView.setAdapter(mAdapter);
		
	}
	
	private void getAllDataForDB ()
	{
		Observable
				.create(( ObservableEmitter< ArrayList< Question > > e ) ->
				{
					e.onNext(QuestionDB.queryQuestionItem());
					e.onComplete();
				})
				.compose(this.bindToLifecycle())
				.compose(RxUtils.rxSchedulerHelper())
				.subscribe(( questions ) ->
				{
					mData.clear();
					mData.addAll(questions);
				});
	}
	
	
}
