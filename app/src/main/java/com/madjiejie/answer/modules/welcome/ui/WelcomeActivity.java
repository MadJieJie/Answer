package com.madjiejie.answer.modules.welcome.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.madjiejie.answer.R;
import com.madjiejie.answer.base.BaseApplication;
import com.madjiejie.answer.base.Biz;
import com.madjiejie.answer.base.Constant;
import com.madjiejie.answer.base.Result;
import com.madjiejie.answer.modules.main.ui.MainActivity;
import com.madjiejie.answer.modules.user.bean.Student;
import com.madjiejie.answer.modules.user.bean.Teacher;
import com.madjiejie.answer.utils.often.AbstractSimpleClass;
import com.madjiejie.answer.utils.often.RetrofitSingleton;
import com.madjiejie.answer.utils.often.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import static com.madjiejie.answer.utils.often.Utils.isEmail;

public class WelcomeActivity extends RxAppCompatActivity implements View.OnClickListener
{
	
	private Button mBtnReg, mBtnLogin;
	
	@Override
	protected void onCreate ( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		initView();
		
	}
	
	private void initView ()
	{
		if ( Integer.parseInt(android.os.Build.VERSION.SDK + "") >= 19 )
		{
			// level 19+
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		
		mBtnLogin = ( Button ) findViewById(R.id.btn_login_welcome);
		mBtnReg = ( Button ) findViewById(R.id.btn_reg_welcome);
		mBtnLogin.setOnClickListener(this);
		mBtnReg.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick ( View v )
	{
		switch ( v.getId() )
		{
			case R.id.btn_login_welcome:/**登录*/
				login();
				break;
			case R.id.btn_reg_welcome:  /**注册*/
				register();
				break;
			default:
				break;
		}
	}
	
	/**
	 * 注册
	 */
	private void register ()
	{
		final View view = LayoutInflater.from(WelcomeActivity.this)
				                  .inflate(R.layout.dialog_register, null);
		final EditText etUName = ( EditText ) view.findViewById(R.id.et_u_name);
		final EditText etUPassword = ( EditText ) view.findViewById(R.id.et_u_password);
		final EditText etEMail = ( EditText ) view.findViewById(R.id.et_u_email);
		final RadioButton mRb_student = ( RadioButton ) view.findViewById(R.id.rb_student_welcome);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.reg))
				.setView(view)
				.setNegativeButton(getResources().getString(R.string.cancel), null)
				.setPositiveButton(getResources().getString(R.string.reg), ( dialog, which ) ->
				{
					String password = etUPassword.getText().toString();
					String email = etEMail.getText().toString();
					String name = etUName.getText().toString();
					
					if ( name.equals("") || password.equals("") || email.equals("") )
					{
						Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.name_email_passwd_not_null), Toast.LENGTH_SHORT).show();
						return;
					}
					if ( ! isEmail(email) )     //check is mail
					{
						Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
						return;
					}
					if ( mRb_student.isChecked() )
					{
						getRegisterResult(Biz.UserType.STUDENT, email, password, name);
					} else
					{
						getRegisterResult(Biz.UserType.TEACHER, email, password, name);
					}
					
				});
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	/**
	 * 登入
	 */
	private void login ()
	{
		final View view = LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.dialog_login, null);
		final EditText etEMail = ( EditText ) view.findViewById(R.id.et_u_email);
		final EditText etUPasswd = ( EditText ) view.findViewById(R.id.et_u_password);
		final RadioButton mRb_student = ( RadioButton ) view.findViewById(R.id.rb_student_welcome);
		
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.login))
				.setView(view)
				.setNegativeButton(getResources().getString(R.string.cancel), null)
				.setPositiveButton(getResources().getString(R.string.login), ( dialog, which ) ->
				{
					String email = etEMail.getText() + "";
					String passwd = etUPasswd.getText() + "";
					
					if ( email.equals("") || passwd.equals("") )
					{
						ToastUtils.showShort(BaseApplication.sAppContext, getResources().getString(R.string.email_passwd_not_null));
						return;
					}
					
					if ( mRb_student.isChecked() )      //看单选钮选择哪种类型
					{
						Constant.USER_TYPE = Constant.STUDENT;
						getLoginStudentInfo(Biz.UserType.STUDENT, email, passwd);
					} else
					{
						Constant.USER_TYPE = Constant.TEACHER;
						getLoginTeacherInfo(Biz.UserType.TEACHER, email, passwd);
					}
					
				/*	if ( ! Utils.isEmail(email) )       //check is email.
					{
						ToastUtils.showShort(BaseApplication.sAppContext,getResources().getString(R.string.enter_email));
//									rr_pb.setVisibility(View.GONE);
						return;
					}*/
					
				});
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);        //点击外部不可消失
		dialog.show();
	}
	
	/**
	 * 处理注册事务
	 *
	 * @param userType 用户类型
	 * @param account  账号
	 * @param password 密码
	 * @param userName 用户名
	 */
	private void getRegisterResult ( final String userType, final String account, final String password, final String userName )
	{
		RetrofitSingleton.getInstance()
				.getRegisterResult(userType, account, password, userName)
				.compose(this.bindToLifecycle())
				.subscribe(new AbstractSimpleClass.NextAndonComplete< Result >()
				{
					@Override
					public void onNext ( Result result )
					{
						ToastUtils.showShort(result.result);
					}
					
					@Override
					public void onComplete ()
					{
						
					}
				});
	}
	
	/**
	 * @param userType
	 * @param account
	 * @param password
	 */
	private void getLoginTeacherInfo ( final String userType, final String account, final String password )
	{
		RetrofitSingleton.getInstance()
				.getLoginTeacherInfo(userType, account, password)
				.compose(this.bindToLifecycle())
				.subscribe(new AbstractSimpleClass.Next< Teacher >()
				{
					@Override
					public void onNext ( Teacher teacher )
					{
						if ( teacher.id > 0 )
						{
							Constant.sTeacher = teacher;        //更新用户信息
							startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
							finish();
							ToastUtils.showShort("登入成功");
						} else
						{
							ToastUtils.showShort("登入失败,请检查账号和密码");
						}
					}
				});
	}
	
	/**
	 * @param userType
	 * @param account
	 * @param password
	 */
	private void getLoginStudentInfo ( final String userType, final String account, final String password )
	{
		RetrofitSingleton.getInstance()
				.getLoginStudentInfo(userType, account, password)
				.compose(this.bindToLifecycle())
				.subscribe(new AbstractSimpleClass.Next< Student >()
				{
					@Override
					public void onNext ( Student student )
					{
						if ( student.id > 0 )
						{
							Constant.sStudent = student;        //更新用户信息
							startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
							finish();
							ToastUtils.showShort("登入成功");
						} else
						{
							ToastUtils.showShort("登入失败,请检查账号和密码");
						}
					}
				});
		
	}
	
}
	
	

