package com.justcodeit.httprequest.http;

class ApiException extends RuntimeException {
    public static final int PALN_NOT_EXIST = 201;


    public ApiException(int code) {
        this(getApiExceptionMessage(code));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case -1:
                return "系统繁忙";
            case 101:
                return "参数不正确";
            case 102:
                return "该用户不存在";
            case 103:
                return "密码错误";
            case 104:
                return "用户已被注销";
            case 105:
                return "数据列表为空";
            case PALN_NOT_EXIST /*201*/:
                return "方案不存在";
            default:
                return "未知错误";
        }
    }
}
