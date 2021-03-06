package com.madjiejie.answer.utils.often;

import android.content.Context;
import android.widget.Toast;

import com.madjiejie.answer.base.BaseApplication;


/**
 * Toast统一管理类
 */
public class ToastUtils
{
	private static Toast mToast;
	public static boolean isShow = true;

	private ToastUtils ()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void showShort ( String message )
	{
//		if ( ! isShow ) return;
//		if ( mToast == null )
			Toast.makeText(BaseApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
//		else
//			mToast.setText(message);
//		mToast.show();
	}

	public static void showLong ( String message )
	{
//		if ( ! isShow ) return;
//		if ( mToast == null )
		Toast.makeText(BaseApplication.getAppContext(), message, Toast.LENGTH_LONG).show();
//		else
//			mToast.setText(message);
//		mToast.show();
	}

	/**
	 * 短时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showShort ( Context context, CharSequence message )
	{
		if ( ! isShow ) return;
		if ( mToast == null )
			mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		else
			mToast.setText(message);
		mToast.show();
	}

	/**
	 * 短时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showShort ( Context context, int message )
	{
		if ( ! isShow ) return;
		if ( mToast == null )
			mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		else
			mToast.setText(message);
		mToast.show();
	}

	/**
	 * 长时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showLong ( Context context, CharSequence message )
	{
		if ( ! isShow ) return;
		if ( mToast == null )
			mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		else

			mToast.setText(message);
		mToast.show();
	}

	/**
	 * 长时间显示Toast
	 *
	 * @param context
	 * @param message
	 */
	public static void showLong ( Context context, int message )
	{
		if ( ! isShow ) return;
		if ( mToast == null )
			mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		else
			mToast.setText(message);
		mToast.show();
	}

	/**
	 * 自定义显示Toast时间
	 *
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show ( Context context, CharSequence message, int duration )
	{
		if ( ! isShow ) return;
		if ( mToast == null )
			mToast = Toast.makeText(context, message, duration);
		else
			mToast.setText(message);
		mToast.show();
	}

	/**
	 * 自定义显示Toast时间
	 *
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show ( Context context, int message, int duration )
	{
		if ( ! isShow ) return;
		if ( mToast == null )
			mToast = Toast.makeText(context, message, duration);
		else
			mToast.setText(message);
		mToast.show();
	}

}