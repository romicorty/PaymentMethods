package com.example.romina.payments.network;

import com.example.romina.payments.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {

    void getPaymentMethods(String baseUrl, String uri, String publicKey, final ServiceCallback<List<PaymentMethod>> paymentMethodsCallback);
}
