package com.justcodeit.httprequest.http;

import com.justcodeit.httprequest.bean.BaseEntity;
import com.justcodeit.httprequest.bean.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/*
* @package com.justcodeit.httprequest.http
* @fileName ApiService.java
* @date on 2018/7/6 16:07
* @author just code it
* @email 1216953180@qq.com
* @describe 接口类
*/

public interface ApiService {

    //https://www.baidu.com/s?wd=哈哈哈
//    @GET(URLConfig.baidu_url)
//    Observable<BaseEntity<User>> getBaidu(@Query("wd") String name);

    @GET(URLConfig.test_url)
    Observable<BaseEntity<User>> getTestData();

    @POST(URLConfig.login_token_url)
    Call<String> loginByToken(@Query("mobile") String mobile, @Query("token") String cookie);

    //上传单张图片
    @POST("服务器地址")
    Observable<Object> imageUpload(@Part() MultipartBody.Part img);
    //上传多张图片
    @POST("服务器地址")
    Observable<Object> imagesUpload(@Part() List<MultipartBody.Part> imgs);
}
