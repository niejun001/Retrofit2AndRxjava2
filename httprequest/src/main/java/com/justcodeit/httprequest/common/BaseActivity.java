package com.justcodeit.httprequest.common;

import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/*
 * @package com.justcodeit.httprequest.common
 * @fileName BaseActivity.java
 * @date on 2018/7/6 15:28
 * @author just code it
 * @email 1216953180@qq.com
 * @describe 基类，设置请求线程
 */
public class BaseActivity extends AppCompatActivity{

    public <T> ObservableTransformer<T,T> setThread(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
