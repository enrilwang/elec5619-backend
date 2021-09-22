package net.guides.springboot2.springboot2webappjsp.controllers;

public class Result {
    private int code;
    private String msg;
    private Object data;


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




}
