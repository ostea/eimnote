package com.comtop.eimnote.http;

import com.google.gson.annotations.SerializedName;

/**
 * Author chaos
 * Description:
 * DATE: 2018/5/4
 * Email: oscc92@gmail.com
 */
public class Response<T> {

    @SerializedName("data")
    private T data;
    @SerializedName("")
    private String message;
    @SerializedName("state")
    private Integer state;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    private boolean isOk() {
        return state.intValue() == 0;
    }

}
