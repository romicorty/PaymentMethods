package com.example.romina.payments.network;

import com.example.romina.payments.BuildConfig;
import com.example.romina.payments.model.PaymentMethod;
import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public class PaymentMethodService {

    public interface PaymentMethodServiceApi{
        @GET("/{uri}")
        void getPaymentMethods(@Path("uri") String uri,@Query("public_key")String publicKey,Callback<List<PaymentMethod>> paymentMethods);
    }

    private PaymentMethodServiceApi api;

    public void getPaymentMethods(String baseUrl, String uri, String publicKey, final Callback<List<PaymentMethod>> paymentMethodsCallback) {
        RestAdapter.Builder builder = new RestAdapter.Builder();

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        RestAdapter restAdapter = builder.setEndpoint(baseUrl).build();
        this.api = restAdapter.create(PaymentMethodServiceApi.class);
        api.getPaymentMethods(uri, publicKey, new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> paymentMethods, Response response) {
                List<PaymentMethod> selectedList = new ArrayList<>();
                if (paymentMethods != null) {
                    for (PaymentMethod paymentMethod : paymentMethods) {
                        if ("credit_card".equalsIgnoreCase(paymentMethod.getPayment_type_id())){
                            selectedList.add(paymentMethod);
                        }
                    }
                }
                paymentMethodsCallback.success(selectedList,response);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}