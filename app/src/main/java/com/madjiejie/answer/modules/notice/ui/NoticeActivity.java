package com.madjiejie.answer.modules.notice.ui;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.madjiejie.answer.R;
import com.madjiejie.answer.adapter.recyclerview.adapter.CommonAdapter;
import com.madjiejie.answer.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.madjiejie.answer.adapter.recyclerview.base.ViewHolder;
import com.madjiejie.answer.base.Biz;
import com.madjiejie.answer.modules.notice.bean.Notice;
import com.madjiejie.answer.utils.often.LogUtils;
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class NoticeActivity extends RxAppCompatActivity
{
	private Toolbar mToolbar;
	private XRecyclerView mXRecyclerView;
	private CommonAdapter< Notice > mAdapter;
	private List< Notice > mData = new ArrayList<>();
	
	/** Parameters */
	
	
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
		getAllDataForServer(Biz.QUERY_ALL_NOTICE);
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
		mToolbar = ( Toolbar ) findViewById(R.id.toolbar);
		mToolbar.setTitle("公告");
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(v -> finish());
	}
	
	
	private void initRecyclerView ()
	{
		
		mAdapter = new CommonAdapter< Notice >(this, R.layout.item_notice, mData)
		{
			@Override
			protected void convert ( ViewHolder holder, Notice info, int position )
			{
				holder
						.setText(R.id.tv_teacherName_main, info.teacherName)
						.setText(R.id.tv_noticeTitle_main, info.noticeTitle)
						.setText(R.id.tv_noticeContent_main, info.noticeContent)
						.setText(R.id.tv_createTime_main, info.createTime.substring(0,info.createTime.length()-5));
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
		
		
		mXRecyclerView = ( XRecyclerView ) findViewById(R.id.recyclerView);
		mXRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));  //false:end fron
		mXRecyclerView.setLoadingMoreEnabled(false);
		mXRecyclerView.setPullRefreshEnabled(false);
//		mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener()       //上下拉监听+
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
		
		mXRecyclerView.setAdapter(mAdapter);
		
	}
	
	/**
	 * Get this question below that answers for server.
	 *
	 * @param biz Biz.QUERY_ALL_QUESTION
	 */
	private void getAllDataForServer ( final String biz )
	{
		RetrofitSingleton.getInstance()
				.getAllNotice(biz)
				.compose(this.bindToLifecycle())
				.subscribe(notices ->
				{
					mData.clear();
					mData.addAll(notices);
					LogUtils.d("DEBUG", "getAllDataForServer: "+mData.size());
					mAdapter.notifyDataSetChanged();
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
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
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
