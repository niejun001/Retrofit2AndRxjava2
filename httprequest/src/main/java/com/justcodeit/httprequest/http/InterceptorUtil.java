package com.justcodeit.httprequest.http;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.justcodeit.httprequest.bean.BaseEntity;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;

/*
* 拦截器工具类
* */
public class InterceptorUtil {

    public static String TAG = "----";
    private static String Token = "";
    public final static Charset UTF8 = Charset.forName("UTF-8");

    //日志拦截器
    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w(TAG, "log: " + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }
    /**
     * token验证的拦截器1
     * @return
     */
    public static void tokenInterceptor1(){
        new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拿到请求体,并添加header携带上token
                Request mRequest = chain.request().newBuilder()
                        .addHeader("Token", Token)
                        .build();
                //拿到响应体
                Response mResponse = chain.proceed(mRequest);

                if (mResponse.code()==201){
                    //重新获取新token
                    //这用了一个特殊接口来获取新的Token
                    Call<String> call = RetrofitFactory.getInstance().API().loginByToken("123456", Token);
                    //拿到请求体
                    Request tokenRequest = call.request();
                    //获取响应体
                    Response tokenResponse = chain.proceed(tokenRequest);
                    //我这假设新的token是在header里返回的
                    //我们拿到新的token头
                    List<String> listHeader = tokenResponse.headers().values("Token");
                    if (listHeader != null) {
                        //重新赋值到新的token
                        Token = listHeader.get(0);
                    }

                    //这是只需要替换掉之前的token重新请求接口就行了
                    Request newRequest = mRequest.newBuilder()
                            .header("Token", Token)
                            .build();
                    return chain.proceed(newRequest);
                }
                return mResponse;
            }
        };
    }

    /**
     * token验证的拦截器
     * @return
     */
    public static Interceptor tokenInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                /**
                 * 1.拦截到返回的数据
                 * 2.判断token是否失效
                 * 3.失效获取新的token
                 * 4.重新请求接口
                 */

                //拿到请求体,并添加header携带上token
                Request mRequest = chain.request().newBuilder()
                        .addHeader("Token", Token)
                        .build();
                //拿到响应体
                Response mResponse = chain.proceed(mRequest);
                ResponseBody responseBody = mResponse.body();

                //得到缓冲源
                BufferedSource source = responseBody.source();

                //请求全部
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();
                Charset charset = UTF8;

                MediaType contentType = responseBody.contentType();

                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                //读取返回数据
                String bodyString = buffer.clone().readString(charset);
                //Log.e(TAG, bodyString);
                if (bodyString != null) {
                    //处理返回的数据，数据转化为对象
                    BaseEntity bean = JSON.parseObject(bodyString, BaseEntity.class);
                    //假设当返回的code为42444时token失效
                    if (bean.getCode() == 42444) {
                        //重新获取新token
                        //这用了一个特殊接口来获取新的Token
                        Call<String> call = RetrofitFactory.getInstance().API().loginByToken("123456", Token);
                        //拿到请求体
                        Request tokenRequest = call.request();
                        //获取响应体
                        Response tokenResponse = chain.proceed(tokenRequest);
                        //我这假设新的token是在header里返回的
                        //我们拿到新的token头
                        List<String> listHeader = tokenResponse.headers().values("Token");
                        if (listHeader != null) {
                            //重新赋值到新的token
                            Token = listHeader.get(0);
                        }

                        //这是只需要替换掉之前的token重新请求接口就行了
                        Request newRequest = mRequest.newBuilder()
                                .header("Token", Token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                }

                return chain.proceed(mRequest);
            }
        };

    }
}
