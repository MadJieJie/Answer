package com.madjiejie.answer.modules.main.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.madjiejie.answer.R;
import com.madjiejie.answer.adapter.recyclerview.adapter.CommonAdapter;
import com.madjiejie.answer.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.madjiejie.answer.adapter.recyclerview.base.ViewHolder;
import com.madjiejie.answer.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.madjiejie.answer.base.Biz;
import com.madjiejie.answer.base.Constant;
import com.madjiejie.answer.modules.main.adapter.StudentDrawerAdapter;
import com.madjiejie.answer.modules.main.adapter.TeacherDrawerAdapter;
import com.madjiejie.answer.modules.main.base.MainConstant;
import com.madjiejie.answer.modules.notice.ui.NoticeActivity;
import com.madjiejie.answer.modules.question.bean.Question;
import com.madjiejie.answer.modules.question.ui.QuestionActivity;
import com.madjiejie.answer.modules.welcome.ui.WelcomeActivity;
import com.madjiejie.answer.utils.often.LogUtils;
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends RxAppCompatActivity
{
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	private XRecyclerView mXRecyclerView;
	private CommonAdapter< Question > mAdapter;
	private List< Question > mData = new ArrayList<>();
	private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
	private TextView mTVName;
	private TextView mTVTitle;
	private TextView mTVNotice ;
	private TextView mTVCreateTime ;
	private BaseAdapter mBaseAdapter;
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initDrawer();
		initToolbar();
		initRecyclerView();
	}
	
	
	private void initToolbar ()
	{
		mToolbar = ( Toolbar ) findViewById(R.id.toolbar);
		mToolbar.setTitle("首页");
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
		toggle.syncState();
		mDrawerLayout.setDrawerListener(toggle);
		mToolbar.setNavigationOnClickListener(v ->
		{
			if ( mDrawerLayout.isDrawerOpen(Gravity.LEFT) )
			{
				mDrawerLayout.closeDrawer(Gravity.LEFT);
			} else
			{
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
		});
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
		getTheBestNewNotice(Biz.QUERY_ONE_NEW_NOTICE);
		getAllDataForServer(Biz.QUERY_ALL_QUESTION);
		mBaseAdapter.notifyDataSetChanged();
	}
	
	private void initDrawer ()
	{
		mListView = ( ListView ) findViewById(R.id.lv_drawer);
		mDrawerLayout = ( DrawerLayout ) findViewById(R.id.drawerLayout);
		
		if ( Constant.USER_TYPE == Constant.STUDENT )
		{
			mBaseAdapter = new StudentDrawerAdapter(this, mDrawerLayout);
		} else
		{
			mBaseAdapter = new TeacherDrawerAdapter(this, mDrawerLayout);
		}
		mListView.setAdapter(mBaseAdapter);
	}
	
	private void initRecyclerView ()
	{
		
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
				
				if ( position == 1 )
				{
					
				} else
				{
					LogUtils.d("DEBUG", "onItemClick: " + position);
					Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
					intent.putExtra(MainConstant.QUESTION_ID, mData.get(position-2).id);
					intent.putExtra(MainConstant.STUDENT_NAME, mData.get(position-2).studentName);
					intent.putExtra(MainConstant.QUESTION_TITLE, mData.get(position-2).questionTitle);
					intent.putExtra(MainConstant.QUESTION_CONTENT, mData.get(position-2).questionContent);
					intent.putExtra(MainConstant.CREATE_TIME, mData.get(position-2).createTime);
					startActivity(intent);
				}
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				return false;
			}
		});
		
		mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
		
		View topNotice = LayoutInflater.from(this).inflate(R.layout.item_notice, null);
		 mTVName = ( TextView ) topNotice.findViewById(R.id.tv_teacherName_main);
		 mTVTitle = ( TextView ) topNotice.findViewById(R.id.tv_noticeTitle_main);
		mTVNotice = ( TextView ) topNotice.findViewById(R.id.tv_noticeContent_main);
		 mTVCreateTime = ( TextView ) topNotice.findViewById(R.id.tv_createTime_main);
		
		topNotice.setOnClickListener(v ->
		{            //点击事件
			startActivity(new Intent(this, NoticeActivity.class));
		});
		
		mHeaderAndFooterWrapper.addHeaderView(topNotice);
		mXRecyclerView = ( XRecyclerView ) findViewById(R.id.recyclerView);
		mXRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
		
		mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener()       //上下拉监听
		{
			@Override
			public void onRefresh ()
			{
				getAllDataForServer(Biz.QUERY_ALL_QUESTION);
				mXRecyclerView.refreshComplete();
			}
			
			@Override
			public void onLoadMore ()
			{
				new Handler().postDelayed(() ->
				{
					mXRecyclerView.loadMoreComplete();
				}, 500);
			}
			
		});
		
		mXRecyclerView.setAdapter(mHeaderAndFooterWrapper);
		
	}
	
	/**
	 * Get all questions for server.
	 *
	 * @param biz Biz.QUERY_ALL_QUESTION
	 */
	private void getAllDataForServer ( final String biz )
	{
		RetrofitSingleton.getInstance()
				.getAllQuestion(biz)
				.compose(this.bindToLifecycle())
				.subscribe(questions ->
				{
					mData.clear();
					mData.addAll(questions);
					mHeaderAndFooterWrapper.notifyDataSetChanged();
				});
	}
	
	/**
	 * Get the best new notice for server to adapter top notice's item.
	 *
	 * @param biz
	 */
	private void getTheBestNewNotice ( final String biz )
	{
		RetrofitSingleton.getInstance()
				.getOneNewNotice(biz)
				.compose(this.bindToLifecycle())
				.subscribe(notice ->
				{
					mTVName.setText(notice.teacherName);
					mTVTitle.setText(notice.noticeTitle);
					mTVNotice.setText(notice.noticeContent);
					mTVCreateTime.setText(notice.createTime);
				});
	}
	
	@Override
	public boolean onCreateOptionsMenu ( Menu menu )
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		switch ( item.getItemId() )
		{
			case R.id.action_sign_out:
				startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
				break;
			case R.id.action_quit:
				finish();
				break;
			default:break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
