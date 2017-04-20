package com.madjiejie.answer.modules.user.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.madjiejie.answer.R;
import com.madjiejie.answer.base.Constant;
import com.madjiejie.answer.base.Result;
import com.madjiejie.answer.utils.often.AbstractSimpleClass;
import com.madjiejie.answer.utils.often.ImagesUtils;
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.madjiejie.answer.utils.often.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public class TeacherInfoActivity extends RxAppCompatActivity implements View.OnClickListener
{
	
	private Toolbar mToolbar;
	private ImageView mIvAvatar;
	private EditText mEtName;
	private TextView mTvMan, mTvWoman;
	private String mSex = Constant.sTeacher.sex;
	private String mName = Constant.sTeacher.name;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_info);
		findView();
		initToolbar();
		initView();
	}
	
	private void findView ()
	{
		mToolbar = ( Toolbar ) findViewById(R.id.toolbar);
		
		mIvAvatar = ( ImageView ) findViewById(R.id.iv_avatar);
		mEtName = ( EditText ) findViewById(R.id.et_name);
		
		mTvMan = ( TextView ) findViewById(R.id.tv_man);
		mTvMan.setOnClickListener(this);
		mTvWoman = ( TextView ) findViewById(R.id.tv_woman);
		mTvWoman.setOnClickListener(this);
	}
	
	private void initToolbar ()
	{
		mToolbar.setTitle(getResources().getString(R.string.title_activity_user_info));
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mToolbar.setNavigationOnClickListener(v -> finish());
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
		mIvAvatar.setImageBitmap(ImagesUtils.drawHeadIcon(this, Constant.sTeacher.name, true, sex));
		
		mEtName.setText(Constant.sTeacher.name);
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
						}
						else
						{
							ToastUtils.showShort("更新用户信息失败,请检查输入是否错误");
						}
					}
				});
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
		}
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
			mTvWoman.setEnabled(true);
			mTvMan.setEnabled(true);
			return true;
		} else if ( id == R.id.action_save )
		{
			mTvWoman.setEnabled(false);
			mTvMan.setEnabled(false);
			Constant.sTeacher.sex = mSex;
			Gson gson = new Gson();
			String json = gson.toJson(Constant.sTeacher);
			sendReviseUserInfo("update_teacher_info",json);         /**Send data to server*/
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
