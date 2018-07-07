package com.justcodeit.httprequest.http;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    public int HTTP_TIME=30000;
    public String BASE_URL="https://coding.net/u/niejun001/p/xv/git/raw/master/";

    public static ApiService mApiService;
    private static RetrofitFactory mRetrofitFactory;

    private RetrofitFactory(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(HTTP_TIME, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIME, TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIME, TimeUnit.SECONDS)
                //.addInterceptor(InterceptorUtil.tokenInterceptor())//token验证
                .addInterceptor(InterceptorUtil.LogInterceptor())//日志拦截
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())//添加gson转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava转换器
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    public static RetrofitFactory getInstance() {
        synchronized (RetrofitFactory.class) {
            if (mRetrofitFactory == null) {
                mRetrofitFactory = new RetrofitFactory();
            }
        }
        return mRetrofitFactory;
    }

    public ApiService API(){
        return mApiService;
    }
}
