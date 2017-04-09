package com.madjiejie.answer.modules.model.ui.view;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.madjiejie.answer.R;
import com.madjiejie.answer.adapter.recyclerview.adapter.CommonAdapter;
import com.madjiejie.answer.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.madjiejie.answer.adapter.recyclerview.base.ViewHolder;
import com.madjiejie.answer.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.madjiejie.answer.modules.model.adapter.DrawerAdapter;
import com.madjiejie.answer.modules.model.bean.QuestionItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
	
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout ;
	private ListView mListView;
	private XRecyclerView mXRecyclerView;
	private CommonAdapter<QuestionItem > mAdapter ;
	private List<QuestionItem > mData ;
	private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
	
	@Override
	public boolean onPrepareOptionsMenu ( Menu menu )
	{
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initDrawer();
		initToolbar();
		initRecyclerView();
	}
	
	
	
	//初始化Toolbar
	private void initToolbar ()
	{
		mToolbar = ( Toolbar ) findViewById(R.id.toolBar);
		mToolbar.setTitle("Home");
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
		toggle.syncState();
		mDrawerLayout.setDrawerListener(toggle);
		mToolbar.setNavigationOnClickListener(v->
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
	
	//初始化抽屉
	private void initDrawer ()
	{
		mListView = ( ListView ) findViewById(R.id.lv_drawer);
		mDrawerLayout = ( DrawerLayout ) findViewById(R.id.drawerlayout);
		DrawerAdapter adapter = new DrawerAdapter(this, mDrawerLayout);
		mListView.setAdapter(adapter);
	}
	
	private void initRecyclerView()
	{
		mData = new ArrayList<QuestionItem >();
		mData.add(new QuestionItem());
		
		mXRecyclerView = ( XRecyclerView ) findViewById(R.id.recyclerView);
		mAdapter = new CommonAdapter< QuestionItem >(this,R.layout.item_main_layout,mData)
		{
			@Override
			protected void convert ( ViewHolder holder, QuestionItem info, int position )
			{
//				holder.setText();
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
		
//		mHeaderAndFooterWrapper.addHeaderView();
		
		mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener()       //上下拉监听
		{
			@Override
			public void onRefresh ()
			{
				
			}
			
			@Override
			public void onLoadMore ()
			{
				
			}
		});
		
		mXRecyclerView.setAdapter(mHeaderAndFooterWrapper);
		
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
//			case :break;
//			default:break;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
