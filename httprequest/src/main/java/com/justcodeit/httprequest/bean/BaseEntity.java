package com.justcodeit.httprequest.bean;

/*
* @package com.justcodeit.httprequest.bean
* @fileName BaseEntity.java
* @date on 2018/7/6 15:30
* @author just code it
* @email 1216953180@qq.com
* @describe 数据基类
*/

public class BaseEntity<T> {
    private static int SUCCESS_CODE = 1;
    private int code;
    private String msg;
    private T data;

    public boolean isSuccess(){
        return getCode() == SUCCESS_CODE;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
