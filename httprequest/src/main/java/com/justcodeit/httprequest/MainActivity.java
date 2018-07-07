package com.justcodeit.httprequest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.justcodeit.httprequest.bean.BaseEntity;
import com.justcodeit.httprequest.bean.User;
import com.justcodeit.httprequest.common.BaseActivity;
import com.justcodeit.httprequest.http.BaseObserver;
import com.justcodeit.httprequest.http.RetrofitFactory;

/*
 * @package com.justcodeit.httprequest
 * @fileName MainActivity.java
 * @date on 2018/7/6 15:30
 * @author just code it
 * @email 1216953180@qq.com
 * @describe 主activity
 */

public class MainActivity extends BaseActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = ((TextView) findViewById(R.id.tv));
    }

    //获取数据
    public void getData(View view) {
        RetrofitFactory.getInstance().API()
                .getTestData()
                .compose(this.<BaseEntity<User>>setThread())
                .subscribe(new BaseObserver<User>(this) {
                    @Override
                    protected void onSuccess(BaseEntity<User> userBaseEntity) throws Exception {
                        Log.e("onSuccess", userBaseEntity.getCode()
                                + "," + userBaseEntity.isSuccess()
                                + "," + userBaseEntity.getMsg()
                                + "---" + userBaseEntity.getData().getName()
                                + "," + userBaseEntity.getData().getPwd());
                        tv.setText("返回的数据：code: " + userBaseEntity.getCode() +
                                " ,success?: " + userBaseEntity.isSuccess() +
                                " ,msg: " + userBaseEntity.getMsg() +
                                " ,username: " + userBaseEntity.getData().getName() +
                                " ,pwd: " + userBaseEntity.getData().getPwd());
                    }

                    @Override
                    protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {
                        if (isNetWorkError) {
                            Toast.makeText(MainActivity.this, "请检查网络是否连接", Toast.LENGTH_SHORT).show();
                        }
                        tv.setText("失败了，错误信息：" + e);
                        Log.e("onFailure", "e:" + e + "是否是网络错误" + isNetWorkError);
                    }
                });
    }
}
