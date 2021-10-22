package net.guides.springboot2.springboot2webappjsp.controllers;

import javax.servlet.http.HttpServletResponse;

public class Result {
//    code 1:success, 0:failed
    private int code;
    private String msg;
    private Object data;
    private String token;

    public Result() {

    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
