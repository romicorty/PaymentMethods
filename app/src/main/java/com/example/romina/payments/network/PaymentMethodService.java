package com.example.romina.payments.network;

import com.example.romina.payments.model.PaymentMethod;

import java.util.List;

import retrofit.Callback;

public interface PaymentMethodService {

    void getPaymentMethods(String baseUrl, String uri, String publicKey, final Callback<List<PaymentMethod>> paymentMethodsCallback);
}
