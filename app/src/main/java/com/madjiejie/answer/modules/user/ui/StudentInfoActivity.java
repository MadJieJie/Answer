package com.madjiejie.answer.modules.user.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.madjiejie.answer.R;
import com.madjiejie.answer.adapter.recyclerview.adapter.CommonAdapter;
import com.madjiejie.answer.adapter.recyclerview.adapter.MultiItemTypeAdapter;
import com.madjiejie.answer.adapter.recyclerview.base.ViewHolder;
import com.madjiejie.answer.base.Constant;
import com.madjiejie.answer.base.Result;
import com.madjiejie.answer.modules.user.bean.StudentClass;
import com.madjiejie.answer.utils.often.AbstractSimpleClass;
import com.madjiejie.answer.utils.often.ImagesUtils;
import com.madjiejie.answer.utils.often.LogUtils;
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.madjiejie.answer.utils.often.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class StudentInfoActivity extends RxAppCompatActivity implements View.OnClickListener
{
	
	private Toolbar mToolbar;
	private ImageView mIvAvatar;
	private EditText mEtName;
	private EditText mEt_class;
	private EditText mEt_studentNo;
	private TextView mTvMan, mTvWoman;
	private String mSex = Constant.sStudent.sex;
	private String mName = Constant.sStudent.name;
	private String mClass = Constant.sStudent.className;
	private String mStudentNo = Constant.sStudent.studentNo;
	private List< StudentClass > mData = new ArrayList< StudentClass >();
	private CommonAdapter< StudentClass > mAdapter;
	private RecyclerView mRecyclerView;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_info);
		findView();
		initToolbar();
		initView();
		
		mEtName.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged ( CharSequence charSequence, int i, int i1, int i2 )
			{
				
			}
			
			@Override
			public void onTextChanged ( CharSequence charSequence, int i, int i1, int i2 )
			{
				
			}
			
			@Override
			public void afterTextChanged ( Editable editable )
			{
				mName = editable.toString();
				mIvAvatar.setImageBitmap(ImagesUtils.drawHeadIcon(StudentInfoActivity.this, mName, true, 1));
			}
		});
		
	}
	
	private void findView ()
	{
		mToolbar = ( Toolbar ) findViewById(R.id.toolbar);
		
		mIvAvatar = ( ImageView ) findViewById(R.id.iv_avatar);
		mEtName = ( EditText ) findViewById(R.id.et_name);
		mEt_class = ( EditText ) findViewById(R.id.et_class);
		mEt_studentNo = ( EditText ) findViewById(R.id.et_studentNo);
		
		mTvMan = ( TextView ) findViewById(R.id.tv_man);
		mTvWoman = ( TextView ) findViewById(R.id.tv_woman);
		
	}
	
	private void initView ()
	{
		int sex = Constant.MAN;
		
		if ( mSex.equals("女") )
		{
			sex = Constant.WOMAN;
			mTvWoman.setBackgroundColor(getResources().getColor(R.color.main_bule));
			mTvMan.setBackgroundColor(getResources().getColor(android.R.color.white));
		} else
		{
			mTvMan.setBackgroundColor(getResources().getColor(R.color.main_bule));
			mTvWoman.setBackgroundColor(getResources().getColor(android.R.color.white));
		}
		
		
		mIvAvatar.setImageBitmap(ImagesUtils.drawHeadIcon(this, mName, true, sex));
		
		mEtName.setText(Constant.sStudent.name);
		mEt_class.setText(Constant.sStudent.className);
		mEt_studentNo.setText(Constant.sStudent.studentNo);
		
		mTvMan.setOnClickListener(this);
		mTvWoman.setOnClickListener(this);
		mEt_class.setOnClickListener(this);
	}
	
	private void initToolbar ()
	{
		mToolbar.setTitle(getResources().getString(R.string.title_activity_user_info));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(v -> finish());
	}
	
	/**
	 * Use dialog display class.
	 */
	private void choiceClass ()
	{
		getClassInfo("get_class");
		
		final View view = LayoutInflater.from(this).inflate(R.layout.dialog_choice_class, null);
		 mRecyclerView = ( RecyclerView ) view.findViewById(R.id.recyclerView);
		mAdapter = new CommonAdapter< StudentClass >(this, R.layout.item_class, mData)
		{
			@Override
			protected void convert ( ViewHolder holder, StudentClass info, int position )
			{
				holder.setText(R.id.tv_class, info.className);
			}
		};
		
		mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener()
		{
			@Override
			public void onItemClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				mClass = mData.get(position).className;
				CardView cardView  = ( CardView ) view.findViewById(R.id.cv_cardView);
				cardView.setCardBackgroundColor(getResources().getColor(R.color.blue));
			}
			
			@Override
			public boolean onItemLongClick ( View view, RecyclerView.ViewHolder holder, int position )
			{
				return false;
			}
		});
		
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setAdapter(mAdapter);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
				.setTitle("请选择您所在的班级")
				.setView(view)
				.setNegativeButton(getString(R.string.cancel), null)
				.setPositiveButton("确定", ( dialog, which ) ->
				{
					mEt_class.setText(mClass);
					dialog.dismiss();
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/**
	 * Connect server for get all class.
	 *
	 * @param biz
	 */
	private void getClassInfo ( final String biz )
	{
		RetrofitSingleton.getInstance()
				.getClassInfo(biz)
				.compose(this.bindToLifecycle())
				.subscribe(new AbstractSimpleClass.Next< List< StudentClass > >()
				{
					@Override
					public void onNext ( List< StudentClass > list )
					{
						mData.clear();
						mData.addAll(list);
						mAdapter.notifyDataSetChanged();
						for ( StudentClass studentClass : mData )
							LogUtils.d(studentClass.className);
					}
				});
	}
	
	/**
	 * Send data to server.
	 * @param biz
	 */
	private void sendReviseUserInfo(final String biz,final String json)
	{
		RetrofitSingleton.getInstance()
				.sendReviseUserInfo(biz,json)
				.compose(this.bindToLifecycle())
				.subscribe(new AbstractSimpleClass.Next< Result >()
				{
					@Override
					public void onNext ( Result result )
					{
						if(result.result.equals("success"))
						{
							ToastUtils.showShort("更新用户信息成功");
							mEt_class.setEnabled(false);
							mEt_studentNo.setEnabled(false);
						}
						else
						{
							ToastUtils.showShort("更新用户信息失败,请检查输入是否错误");
						}
					}
				});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu ( Menu menu )
	{
		getMenuInflater().inflate(R.menu.menu_user_info, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected ( MenuItem item )
	{
		int id = item.getItemId();
		if ( id == R.id.action_edit )
		{
			mEt_class.setEnabled(true);
			mEt_studentNo.setEnabled(true);
			return true;
		} else if ( id == R.id.action_save )
		{
			mEt_studentNo.setEnabled(true);
			Constant.sStudent.className = mEt_class.getText().toString();
			Constant.sStudent.sex = mSex;
			Constant.sStudent.studentNo = mEt_studentNo.getText().toString();
			Gson gson = new Gson();
			String json = gson.toJson(Constant.sStudent);
			sendReviseUserInfo("update_student_info",json);         /**Send data to server*/
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick ( View view )
	{
		if ( view.getId() == R.id.tv_man )
		{
			mSex = "男";
			mTvMan.setBackgroundColor(getResources().getColor(R.color.main_bule));
			mTvWoman.setBackgroundColor(getResources().getColor(android.R.color.white));
			mIvAvatar.setImageBitmap(ImagesUtils.drawHeadIcon(this, mName, true, Constant.MAN));
		} else if ( view.getId() == R.id.tv_woman )
		{
			mSex = "女";
			mTvWoman.setBackgroundColor(getResources().getColor(R.color.main_bule));
			mTvMan.setBackgroundColor(getResources().getColor(android.R.color.white));
			mIvAvatar.setImageBitmap(ImagesUtils.drawHeadIcon(this, mName, true, Constant.WOMAN));
		} else if ( view.getId() == R.id.et_class )
		{
			choiceClass();
		}
	}
	
}
