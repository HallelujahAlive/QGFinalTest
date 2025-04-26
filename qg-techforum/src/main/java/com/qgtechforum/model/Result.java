package com.qgtechforum.model;

//Result类主要的作用是用于封装要返回给前端的数据
public class Result {
    //code是状态码，用于表示请求的状态
    private int code;
    //message是消息，用于表示请求的状态
    private String message;
    //data是数据，用于表示请求的结果
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
