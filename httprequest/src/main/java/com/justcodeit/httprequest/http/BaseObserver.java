package com.justcodeit.httprequest.http;

import android.accounts.NetworkErrorException;
import android.content.Context;

import com.justcodeit.httprequest.bean.BaseEntity;
import com.justcodeit.httprequest.view.ProgressDialog;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/*
 * @package com.justcodeit.httprequest.http
 * @fileName BaseObserver.java
 * @date on 2018/7/6 16:22
 * @author just code it
 * @email 1216953180@qq.com
 * @describe 观察者
 */

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {
    private Context mContext;

    public BaseObserver(Context context) {
        mContext = context;
    }

    public BaseObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        //请求开始
        onRequestStart();
    }

    @Override
    public void onNext(BaseEntity<T> tBaseEntity) {
        //请求结束
        onRequestEnd();
        if (tBaseEntity.isSuccess()) {
            try {
                onSuccess(tBaseEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new ApiException(tBaseEntity.getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onError(Throwable e) {
        //请求结束
        onRequestEnd();
        try {
            if (e instanceof ConnectException
                    || e instanceof TimeoutException
                    || e instanceof NetworkErrorException
                    || e instanceof UnknownHostException) {
                onFailure(e, true);
            } else {
                onFailure(e, false);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    public void showDialog() {
        ProgressDialog.show(mContext, false, "请稍候...");
    }

    public void dismissDialog() {
        ProgressDialog.cancle();
    }

    protected void onRequestStart() {
        showDialog();
    }

    protected void onRequestEnd() {
        dismissDialog();
    }

    /*
     * 返回数据成功
     * */
    protected abstract void onSuccess(BaseEntity<T> tBaseEntity) throws Exception;

    /*
     * 返回数据失败
     * isNetWorkError 是否是网络错误
     * */
    protected abstract void onFailure(Throwable e, boolean isNetWorkError) throws Exception;
}
