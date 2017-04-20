package com.madjiejie.answer.utils.often;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Created by MadJieJie on 2017/3/11-16:37.
 * @brief
 * @attention
 */

public class AbstractSimpleClass
{
	
	public abstract static class Next< T > implements Observer< T >
	{
		@Override
		public void onSubscribe ( Disposable d )
		{
			
		}
		
		
		@Override
		public void onError ( Throwable e )
		{
			LogUtils.d(e.toString());
//			RetrofitSingleton.disposeFailureInfo(e);
		}
		
		@Override
		public void onComplete ()
		{
			ToastUtils.showShort("Complete");
		}
	}
	
	public abstract static class NextAndonComplete< T > implements Observer< T >
	{
		@Override
		public void onSubscribe ( Disposable d )
		{
			
		}
		
		
		@Override
		public void onError ( Throwable e )
		{
			RetrofitSingleton.disposeFailureInfo(e);
		}
		
	}
	
	
}
