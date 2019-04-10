package com.nosbielc.crawler.banco.rest.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Response<T> implements Serializable {

    private T data;
    private List<String> warns;
    private Error error;

    public Response() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getWarns() {
        if (this.warns == null) {
            this.warns = new ArrayList<String>();
        }
        return warns;
    }

    public void setWarns(List<String> errors) {
        this.warns = errors;
    }

    public void addWarns(String error) {
        if (this.warns == null) {
            this.warns = new ArrayList<String>();
        }

        this.warns.add(error);
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Response.class.getSimpleName() + "[", "]")
                .add("data=" + data)
                .add("warns=" + warns)
                .toString();
    }
}
