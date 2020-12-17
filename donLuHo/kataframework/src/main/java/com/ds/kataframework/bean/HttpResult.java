package com.ds.kataframework.bean;

import com.ds.kataframework.http.constants.HttpStatusConstants;

public class HttpResult<T>  {
    /**
     * 默认约定返回 格式 ： {"errorcode":0,"message":"提示消息","data":{}}
     * status : 0
     * message : 提示消息
     * data : {}
     */
    protected int code = HttpStatusConstants.CODE_DEFAULT;//防止返回格式不给code
    protected T result;
    protected String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
