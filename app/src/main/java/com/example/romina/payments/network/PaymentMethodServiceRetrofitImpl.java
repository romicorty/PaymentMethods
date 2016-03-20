package com.example.romina.payments.network;

import android.support.annotation.NonNull;

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

public class PaymentMethodServiceRetrofitImpl implements PaymentMethodService{

    public interface PaymentMethodServiceApi{
        @GET("/{uri}")
        void getPaymentMethods(@Path("uri") String uri,@Query("public_key")String publicKey,Callback<List<PaymentMethod>> paymentMethods);
    }

    private PaymentMethodServiceApi api;

    @Override
    public void getPaymentMethods(String baseUrl, String uri, String publicKey,final Callback<List<PaymentMethod>> paymentMethodsCallback) {
        RestAdapter.Builder builder = new RestAdapter.Builder();

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        RestAdapter restAdapter = builder.setEndpoint(baseUrl).build();
        this.api = restAdapter.create(PaymentMethodServiceApi.class);
        api.getPaymentMethods(uri, publicKey, new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> paymentMethods, Response response) {
                List<PaymentMethod> filteredPaymentMethods = filterPaymentMethods(paymentMethods, "credit_card");
                paymentMethodsCallback.success(filteredPaymentMethods,response);
            }

            @Override
            public void failure(RetrofitError error) {
                paymentMethodsCallback.failure(error);
            }
        });
    }

    @NonNull
    private List<PaymentMethod> filterPaymentMethods(List<PaymentMethod> paymentMethods, String paymentTypeId) {
        List<PaymentMethod> filteredList = new ArrayList<>();
        if (paymentMethods != null) {
            for (PaymentMethod paymentMethod : paymentMethods) {
                if (paymentTypeId.equalsIgnoreCase(paymentMethod.getPaymentTypeId())){
                    filteredList.add(paymentMethod);
                }
            }
        }
        return filteredList;
    }


}
