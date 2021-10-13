package net.guides.springboot2.springboot2webappjsp.controllers;

import javax.servlet.http.HttpServletResponse;

public class Result {
    private int code;
    private String msg;
    private Object data;
    private String cookie;
    private HttpServletResponse response;

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


    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse  response) {
        this.response = response;
    }




}
