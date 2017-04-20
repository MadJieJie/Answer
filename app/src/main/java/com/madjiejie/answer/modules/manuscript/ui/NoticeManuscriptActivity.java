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
import com.madjiejie.answer.modules.notice.bean.Notice;
import com.madjiejie.answer.modules.notice.db.NoticeDB;
import com.madjiejie.answer.modules.notice.ui.EditNoticeActivity;
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

public class NoticeManuscriptActivity extends RxAppCompatActivity
{
	
	private XRecyclerView mXRecyclerView;
	private CommonAdapter< Notice > mAdapter;
	private List< Notice > mData = new ArrayList<>();
	private static boolean FIRST_UPDATE = true;
	
	@Override
	protected void onCreate ( @Nullable Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_question);
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
		
		mAdapter = new CommonAdapter< Notice >(this, R.layout.item_notice, mData)
		{
			@Override
			protected void convert ( ViewHolder holder, Notice info, int position )
			{
				holder
						.setText(R.id.tv_teacherName_main, info.teacherName)
						.setText(R.id.tv_noticeTitle_main, info.noticeTitle)
						.setText(R.id.tv_noticeContent_main, info.noticeContent)
						.setText(R.id.tv_createTime_main, info.createTime);
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				Intent intent = new Intent(NoticeManuscriptActivity.this, EditNoticeActivity.class);
				intent.putExtra(ID, mData.get(position - 1).id);
				intent.putExtra(TITLE, mData.get(position - 1).noticeTitle);
				intent.putExtra(CONTENT, mData.get(position - 1).noticeContent);
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
				.create(( ObservableEmitter< ArrayList< Notice > > e ) ->
				{
					e.onNext(NoticeDB.queryNoticeItem());
					e.onComplete();
				})
				.compose(this.bindToLifecycle())
				.compose(RxUtils.rxSchedulerHelper())
				.subscribe(( notice ) ->
				{
					mData.clear();
					mData.addAll(notice);
				});
	}
	
	
}
