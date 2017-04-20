package com.madjiejie.answer.modules.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.madjiejie.answer.R;
import com.madjiejie.answer.base.Constant;
import com.madjiejie.answer.modules.manuscript.ui.QuestionManuscriptActivity;
import com.madjiejie.answer.modules.more.MoreActivity;
import com.madjiejie.answer.modules.question.ui.NewQuestionActivity;
import com.madjiejie.answer.modules.user.ui.StudentInfoActivity;
import com.madjiejie.answer.utils.often.ImagesUtils;


public class StudentDrawerAdapter extends BaseAdapter
{
	private Drawable[] pics = new Drawable[4];
	private String[] strs = new String[4];
	
	private Context cxt;
	private DrawerLayout drawer;
	
	private static final int TYPE0 = 0;
	private static final int TYPE1 = 1;
	private static final int TYPE2 = 2;
	
	private static final int NEW_QUESTION_ACTIVITY = 2;
	private static final int MANUSCRIPT_ACTIVITY = 3;
	private static final int MORE_ACTIVITY = 4;
	
	
	public StudentDrawerAdapter ( Context cxt, DrawerLayout drawer )
	{
		this.cxt = cxt;
		this.drawer = drawer;
		initdates();
		
	}
	
	private void initdates ()
	{
		strs[0] = cxt.getResources().getString(R.string.home);
		strs[1] = cxt.getResources().getString(R.string.quiz);
		strs[2] = cxt.getResources().getString(R.string.manuscript);
		strs[3] = cxt.getResources().getString(R.string.more);
//		strs[2] = cxt.getResources().getString(R.string.enshrine);
		
		pics[0] = cxt.getResources().getDrawable(R.mipmap.ic_home);
		pics[1] = cxt.getResources().getDrawable(R.mipmap.ic_quiz);
		pics[2] = cxt.getResources().getDrawable(R.mipmap.ic_manuscript);
		pics[3] = cxt.getResources().getDrawable(R.mipmap.ic_more);
//		pics[2] = cxt.getResources().getDrawable(R.drawable.ic_enshrine);
	}
	
	@Override
	public int getItemViewType ( int position )
	{
		if ( position == 0 )
			return TYPE0;
		if ( position >= 1 || position <= 3 )
			return TYPE1;
		return TYPE2;
	}
	
	@Override
	public int getCount ()
	{
		return 5;
	}
	
	@Override
	public long getItemId ( int i )
	{
		return i;
	}
	
	@Override
	public Object getItem ( int i )
	{
		return i;
	}
	
	@Override
	public View getView ( int i, View view, ViewGroup viewGroup )
	{
		if ( getItemViewType(i) == TYPE0 )
		{
			return type0(i, view, viewGroup);
		}
		if ( getItemViewType(i) == TYPE1 )
		{
			return type1(i, view, viewGroup);
		}
		if ( getItemViewType(i) == TYPE2 )
		{
			
		}
		return view;
	}
	
	
	private View type0 ( int i, View view, ViewGroup viewGroup )
	{
		ViewHolder holder = new ViewHolder();
		if ( view == null )
		{
			view = LayoutInflater.from(cxt).inflate(R.layout.item_top_drawer, viewGroup, false);
			holder.iv_avatar = ( ImageView ) view.findViewById(R.id.iv_avatar);
			holder.tv_name = ( TextView ) view.findViewById(R.id.tv_name);
			view.setTag(holder);
		} else
		{
			holder = ( ViewHolder ) view.getTag();
		}
		//填充数据
		Drawable drawable = cxt.getResources().getDrawable(R.mipmap.ic_avatar);
		BitmapDrawable bd = ( BitmapDrawable ) drawable;
		int sex = Constant.sStudent.sex.equals("男") ? Constant.MAN : Constant.WOMAN;
		
		holder.iv_avatar.setImageBitmap(ImagesUtils.drawHeadIcon(cxt, Constant.sStudent.name, false, sex));
		holder.tv_name.setText(Constant.sStudent.name);
		
		view.setOnClickListener(v ->
		{
			cxt.startActivity(new Intent(cxt, StudentInfoActivity.class));
			drawer.closeDrawer(Gravity.LEFT);
		});
		
		return view;
	}
	
	private View type1 ( final int i, View view, ViewGroup viewGroup )
	{
		ViewHolder holder = new ViewHolder();
		if ( view == null )
		{
			view = LayoutInflater.from(cxt).inflate(R.layout.item_bottom_drawer, viewGroup, false);
			holder.imageView = ( ImageView ) view.findViewById(R.id.imageView);
			holder.textView = ( TextView ) view.findViewById(R.id.textView);
			view.setTag(holder);
		} else
		{
			holder = ( ViewHolder ) view.getTag();
		}
		//填充数据
		holder.imageView.setImageDrawable(pics[i - 1]);
		holder.textView.setText(strs[i - 1]);
		//设置监听器
		view.setOnClickListener(v ->
		{
			Intent intent = null;
			switch ( i )
			{
				case NEW_QUESTION_ACTIVITY:
					intent = new Intent(cxt, NewQuestionActivity.class);
					break;
				case MANUSCRIPT_ACTIVITY:
					intent = new Intent(cxt, QuestionManuscriptActivity.class);
					break;
              case MORE_ACTIVITY:
                  intent = new Intent(cxt, MoreActivity.class);
                  break;
				default:
					break;
			}
			if ( intent != null )
				cxt.startActivity(intent);
			drawer.closeDrawer(Gravity.LEFT);
		});
		return view;
	}
	
	class ViewHolder
	{
		ImageView imageView, iv_avatar;
		TextView textView, tv_name;
	}
	
}
