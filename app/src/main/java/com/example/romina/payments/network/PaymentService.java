package com.example.romina.payments.network;

import com.example.romina.payments.model.CardIssuer;
import com.example.romina.payments.model.PaymentMethod;

import java.util.List;

public interface PaymentService {

    void getPaymentMethods(String baseUrl, String uri, String publicKey, final ServiceCallback<List<PaymentMethod>> paymentMethodsCallback);
    void getCardIssuers(String baseUrl, String uri, String publicKey,String paymentMethod, final ServiceCallback<List<CardIssuer>> cardIssuersCallback);

}
