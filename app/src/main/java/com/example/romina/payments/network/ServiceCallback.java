package com.example.romina.payments.network;


public interface ServiceCallback<T> {


    void success(T t);


    void failure(String error);

}
