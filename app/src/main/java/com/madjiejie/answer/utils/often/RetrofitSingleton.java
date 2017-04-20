package com.madjiejie.answer.utils.often;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.madjiejie.answer.BuildConfig;
import com.madjiejie.answer.base.BaseApplication;
import com.madjiejie.answer.base.IApi;
import com.madjiejie.answer.base.Result;
import com.madjiejie.answer.modules.answer.bean.Answer;
import com.madjiejie.answer.modules.notice.bean.Notice;
import com.madjiejie.answer.modules.question.bean.Question;
import com.madjiejie.answer.modules.user.bean.Student;
import com.madjiejie.answer.modules.user.bean.StudentClass;
import com.madjiejie.answer.modules.user.bean.Teacher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * this is Retrofit Singleton to help to get API data.
 */
public class RetrofitSingleton
{
	
	private static IApi sApiService = null;
	private static Retrofit sRetrofit = null;
	private static OkHttpClient sOkHttpClient = null;
	
	private void init ()
	{
		initOkHttp();
		initRetrofit();
		sApiService = sRetrofit.create(IApi.class);
	}
	
	private RetrofitSingleton ()
	{
		init();
	}

//	private RetrofitUser ()
//	{
//		init();
//	}
	
	public static RetrofitSingleton getInstance ()
	{
		return SingletonHolder.INSTANCE;
	}
	
	/**
	 * 单例持有者
	 */
	private static class SingletonHolder
	{
		private static final RetrofitSingleton INSTANCE = new RetrofitSingleton();
	}
	
	private static void initOkHttp ()
	{
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		if ( BuildConfig.DEBUG )
		{
			// https://drakeet.me/retrofit-2-0-okhttp-3-0-config
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
			builder.addInterceptor(loggingInterceptor);
		}
		// 缓存 http://www.jianshu.com/p/93153b34310e
		File cacheFile = new File(BaseApplication.getAppCacheDir(), "/answer");/**缓存路径*/
		Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
		Interceptor cacheInterceptor = chain ->
		{
			Request request = chain.request();
			if ( ! Utils.isNetworkConnected(BaseApplication.getAppContext()) )
			{
				request = request.newBuilder()
						          .cacheControl(CacheControl.FORCE_CACHE)
						          .build();
			}
			Response response = chain.proceed(request);
			Response.Builder newBuilder = response.newBuilder();
			if ( Utils.isNetworkConnected(BaseApplication.getAppContext()) )
			{
				int maxAge = 0;
				// 有网络时 设置缓存超时时间0个小时
				newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
			} else
			{
				// 无网络时，设置超时为4周
				int maxStale = 60 * 60 * 24 * 28;
				newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
			}
			return newBuilder.build();
		};
		builder.cache(cache).addInterceptor(cacheInterceptor);
		//设置超时
		builder.connectTimeout(8, TimeUnit.SECONDS);
		builder.readTimeout(8, TimeUnit.SECONDS);
		builder.writeTimeout(8, TimeUnit.SECONDS);
		//错误重连
		builder.retryOnConnectionFailure(true);
		sOkHttpClient = builder.build();
	}
	
	private static void initRetrofit ()
	{
		sRetrofit = new Retrofit.Builder()
				            .baseUrl(IApi.HOST_URL)          /**访问URL*/
				            .client(sOkHttpClient)
				            .addConverterFactory(GsonConverterFactory.create())
				            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				            .build();
	}
	
	/**
	 * 出现错误的时候可以截断观察者的接收
	 *
	 * @param t
	 */
	public static void disposeFailureInfo ( Throwable t )
	{
		if ( t.toString().contains("GaiException") || t.toString().contains("SocketTimeoutException") ||
				     t.toString().contains("UnknownHostException") )
		{
			ToastUtils.showShort("网络问题");
		} else if ( t.toString().contains("API没有") )
		{
//			OrmLite.getInstance().delete(new WhereBuilder(CityORM.class).where("name=?", Utils.replaceInfo(t.getMessage())));
			LogUtils.w(Utils.replaceInfo(t.getMessage()));
			ToastUtils.showShort("错误: " + t.getMessage());
		}
//        PLog.w(t.getMessage());
	}
	
	public IApi getApiService ()
	{
		return sApiService;
	}
	
	/**
	 * 返回-被观察者
	 *
	 * @param account
	 * @param password
	 * @return
	 */
	public Observable< Result > getRegisterResult ( final String biz, final String account, final String password, final String userName )
	{
		
		return Observable.create(( ObservableOnSubscribe< Result > ) e -> sApiService.register(biz, account, password, userName)
				                                                                  .enqueue(new Callback< Result >()
				                                                                  {
					                                                                  @Override
					                                                                  public void onResponse ( Call< Result > call, retrofit2.Response< Result > response )
					                                                                  {
						                                                                  if ( response.isSuccessful() )
							                                                                  e.onNext(response.body());
					                                                                  }
					
					                                                                  @Override
					                                                                  public void onFailure ( Call< Result > call, Throwable t )
					                                                                  {
						                                                                  ToastUtils.showShort("请求网络失败");
						                                                                  LogUtils.e(t.toString());
					                                                                  }
				                                                                  })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * Get student login info.
	 *
	 * @param biz
	 * @param account
	 * @param password
	 * @return
	 */
	public Observable< Student > getLoginStudentInfo ( final String biz, final String account, final String password )
	{
		
		return Observable.create(( ObservableOnSubscribe< Student > ) e -> sApiService.studentLogin(biz, account, password)
				                                                                   .enqueue(new Callback< Student >()
				                                                                   {
					                                                                   @Override
					                                                                   public void onResponse ( Call< Student > call, retrofit2.Response< Student > response )
					                                                                   {
						                                                                   if ( response.isSuccessful() )
							                                                                   e.onNext(response.body());
					                                                                   }
					
					                                                                   @Override
					                                                                   public void onFailure ( Call< Student > call, Throwable t )
					                                                                   {
						                                                                   ToastUtils.showShort("请求网络失败");
						                                                                   LogUtils.e(t.toString());
					                                                                   }
				                                                                   })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * Get teacher login info.
	 *
	 * @param biz
	 * @param account
	 * @param password
	 * @return
	 */
	public Observable< Teacher > getLoginTeacherInfo ( final String biz, final String account, final String password )
	{
		
		return Observable.create(( ObservableOnSubscribe< Teacher > ) e -> sApiService.teacherLogin(biz, account, password)
				                                                                   .enqueue(new Callback< Teacher >()
				                                                                   {
					                                                                   @Override
					                                                                   public void onResponse ( Call< Teacher > call, retrofit2.Response< Teacher > response )
					                                                                   {
						                                                                   if ( response.isSuccessful() )
							                                                                   e.onNext(response.body());
					                                                                   }
					
					                                                                   @Override
					                                                                   public void onFailure ( Call< Teacher > call, Throwable t )
					                                                                   {
						                                                                   ToastUtils.showShort("请求网络失败");
						                                                                   LogUtils.e(t.toString());
					                                                                   }
				                                                                   })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	
	/**
	 * Get teacher login info.
	 *
	 * @param biz
	 * @return
	 */
	public Observable< List< StudentClass > > getClassInfo ( final String biz )
	{
		
		return Observable.create(( ObservableOnSubscribe< List< StudentClass > > ) e -> sApiService.getClassInfo(biz)
				                                                                                .enqueue(new Callback< List< StudentClass > >()
				                                                                                {
					                                                                                @Override
					                                                                                public void onResponse ( Call< List< StudentClass > > call, retrofit2.Response< List< StudentClass > > response )
					                                                                                {
						                                                                                if ( response.isSuccessful() )
							                                                                                e.onNext(response.body());
					                                                                                }
					
					                                                                                @Override
					                                                                                public void onFailure ( Call< List< StudentClass > > call, Throwable t )
					                                                                                {
						                                                                                ToastUtils.showShort("请求网络失败");
						                                                                                LogUtils.e(t.toString());
					                                                                                }
				                                                                                })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * Get teacher login info.
	 *
	 * @param biz
	 * @return
	 */
	public Observable< Result > sendReviseUserInfo ( final String biz, final String json )
	{
		
		return Observable.create(( ObservableOnSubscribe< Result > )
				                         e -> sApiService.sendReviseInfo(biz, json)
						                              .enqueue(new Callback< Result >()
						                              {
							                              @Override
							                              public void onResponse ( Call< Result > call, retrofit2.Response< Result > response )
							                              {
								                              if ( response.isSuccessful() )
									                              e.onNext(response.body());
							                              }
							
							                              @Override
							                              public void onFailure ( Call< Result > call, Throwable t )
							                              {
								                              ToastUtils.showShort("请求网络失败");
								                              LogUtils.e(t.toString());
							                              }
						                              })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * send Question to server for save.
	 *
	 * @param biz
	 * @return
	 */
	public Observable< Result > sendQuestion ( final String biz, final String json )
	{
		
		return Observable
				       .create(( ObservableOnSubscribe< Result > )
						               e -> sApiService.sendQuestion(biz, json)
								                    .enqueue(new Callback< Result >()
								                    {
									                    @Override
									                    public void onResponse ( Call< Result > call, retrofit2.Response< Result > response )
									                    {
										                    if ( response.isSuccessful() )
											                    e.onNext(response.body());
									                    }
									
									                    @Override
									                    public void onFailure ( Call< Result > call, Throwable t )
									                    {
										                    ToastUtils.showShort("请求网络失败");
										                    LogUtils.e(t.toString());
									                    }
								                    })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * @param biz
	 * @return
	 */
	public Observable< ArrayList< Question > > getAllQuestion ( final String biz )
	{
		
		return Observable
				       .create(( ObservableOnSubscribe< ArrayList< Question > > )
						               e -> sApiService
								                    .getAllQuestion(biz)
								                    .enqueue(new Callback< ArrayList< Question > >()
								                    {
									                    @Override
									                    public void onResponse ( Call< ArrayList< Question > > call, retrofit2.Response< ArrayList< Question > > response )
									                    {
										                    if ( response.isSuccessful() )
											                    e.onNext(response.body());
									                    }
									
									                    @Override
									                    public void onFailure ( Call< ArrayList< Question > > call, Throwable t )
									                    {
										                    ToastUtils.showShort("请求网络失败");
										                    LogUtils.e(t.toString());
									                    }
								                    })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * @param biz
	 * @param json
	 * @return
	 */
	public Observable< Result > sendAnswer ( final String biz, final String json )
	{
		
		return Observable
				       .create(( ObservableOnSubscribe< Result > )
						               e -> sApiService.sendAnswer(biz, json)
								                    .enqueue(new Callback< Result >()
								                    {
									                    @Override
									                    public void onResponse ( Call< Result > call, retrofit2.Response< Result > response )
									                    {
										                    if ( response.isSuccessful() )
											                    e.onNext(response.body());
									                    }
									
									                    @Override
									                    public void onFailure ( Call< Result > call, Throwable t )
									                    {
										                    ToastUtils.showShort("请求网络失败");
										                    LogUtils.e(t.toString());
									                    }
								                    })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * @param biz
	 * @return
	 */
	public Observable< ArrayList< Answer > > getAnswer ( final String biz, final int questionId )
	{
		
		return Observable
				       .create(( ObservableOnSubscribe< ArrayList< Answer > > )
						               e -> sApiService
								                    .getAnswer(biz, questionId)
								                    .enqueue(new Callback< ArrayList< Answer > >()
								                    {
									                    @Override
									                    public void onResponse ( Call< ArrayList< Answer > > call, retrofit2.Response< ArrayList< Answer > > response )
									                    {
										                    if ( response.isSuccessful() )
											                    e.onNext(response.body());
									                    }
									
									                    @Override
									                    public void onFailure ( Call< ArrayList< Answer > > call, Throwable t )
									                    {
										                    ToastUtils.showShort("请求网络失败");
										                    LogUtils.e(t.toString());
									                    }
								                    })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * send Notice to server for save.
	 *
	 * @param biz
	 * @return
	 */
	public Observable< Result > sendNotice ( final String biz, final String json )
	{
		
		return Observable
				       .create(( ObservableOnSubscribe< Result > )
						               e -> sApiService.sendNotice(biz, json)
								                    .enqueue(new Callback< Result >()
								                    {
									                    @Override
									                    public void onResponse ( Call< Result > call, retrofit2.Response< Result > response )
									                    {
										                    if ( response.isSuccessful() )
											                    e.onNext(response.body());
									                    }
									
									                    @Override
									                    public void onFailure ( Call< Result > call, Throwable t )
									                    {
										                    ToastUtils.showShort("请求网络失败");
										                    LogUtils.e(t.toString());
									                    }
								                    })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 * @param biz
	 * @return
	 */
	public Observable< ArrayList< Notice > > getAllNotice ( final String biz )
	{
		
		return Observable
				       .create(( ObservableOnSubscribe< ArrayList< Notice > > )
						               e -> sApiService
								                    .getAllNotice(biz)
								                    .enqueue(new Callback< ArrayList< Notice > >()
								                    {
									                    @Override
									                    public void onResponse ( Call< ArrayList< Notice > > call, retrofit2.Response< ArrayList< Notice > > response )
									                    {
										                    if ( response.isSuccessful() )
											                    e.onNext(response.body());
									                    }
									
									                    @Override
									                    public void onFailure ( Call< ArrayList< Notice > > call, Throwable t )
									                    {
										                    ToastUtils.showShort("请求网络失败");
										                    LogUtils.e(t.toString());
									                    }
								                    })).compose(RxUtils.rxSchedulerHelper());
		
	}
	
	/**
	 *
	 * @param biz
	 * @return
	 */
	public Observable< Notice > getOneNewNotice ( final String biz )
	{
		
		return Observable
				       .create(( ObservableOnSubscribe< Notice > )
						               e -> sApiService
								                    .getOneNewNotice(biz)
								                    .enqueue(new Callback< Notice >()
								                    {
									                    @Override
									                    public void onResponse ( Call< Notice > call, retrofit2.Response< Notice > response )
									                    {
										                    if ( response.isSuccessful() )
											                    e.onNext(response.body());
									                    }
									
									                    @Override
									                    public void onFailure ( Call< Notice > call, Throwable t )
									                    {
										                    ToastUtils.showShort("请求网络失败");
										                    LogUtils.e(t.toString());
									                    }
								                    })).compose(RxUtils.rxSchedulerHelper());
		
		
	}
	
	
}
