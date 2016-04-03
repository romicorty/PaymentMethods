package com.example.romina.payments.network;

import android.support.annotation.NonNull;

import com.example.romina.payments.BuildConfig;
import com.example.romina.payments.model.CardIssuer;
import com.example.romina.payments.model.Installment;
import com.example.romina.payments.model.PayerCost;
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

public class PaymentServiceRetrofitImpl implements PaymentService {

    public interface PaymentServiceApi {
        @GET("/{uri}")
        void getPaymentMethods(@Path("uri") String uri,@Query("public_key")String publicKey,Callback<List<PaymentMethod>> paymentMethods);

        @GET("/{uri}")
        void getCardIssuers(@Path("uri") String uri,@Query("public_key")String publicKey,@Query("payment_method_id")String paymentMethodId,Callback<List<CardIssuer>> cardIssuers);

        @GET("/{uri}")
        void getInstallments(@Path("uri") String uri,@Query("public_key")String publicKey,@Query("amount")String amount,@Query("payment_method_id")String paymentMethodId,@Query("issuer.id")String issuer,Callback<List<Installment>> installments);
    }

    private PaymentServiceApi api;

    @Override
    public void getPaymentMethods(String baseUrl, String uri, String publicKey,final ServiceCallback<List<PaymentMethod>> paymentMethodsCallback) {
        RestAdapter.Builder builder = new RestAdapter.Builder();

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        RestAdapter restAdapter = builder.setEndpoint(baseUrl).build();
        this.api = restAdapter.create(PaymentServiceApi.class);
        api.getPaymentMethods(uri, publicKey, new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> paymentMethods, Response response) {
                List<PaymentMethod> filteredPaymentMethods = filterPaymentMethods(paymentMethods, "credit_card");
                paymentMethodsCallback.success(filteredPaymentMethods);
            }

            @Override
            public void failure(RetrofitError error) {
                paymentMethodsCallback.failure(error.getMessage());
            }
        });
    }

    @Override
    public void getCardIssuers(String baseUrl, String uri, String publicKey,String paymentMethod, final ServiceCallback<List<CardIssuer>> cardIssuersCallback) {
        RestAdapter.Builder builder = new RestAdapter.Builder();

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        RestAdapter restAdapter = builder.setEndpoint(baseUrl).build();
        this.api = restAdapter.create(PaymentServiceApi.class);
        api.getCardIssuers(uri, publicKey, paymentMethod, new Callback<List<CardIssuer>>() {
            @Override
            public void success(List<CardIssuer> cardIssuers, Response response) {
                cardIssuersCallback.success(cardIssuers);
            }

            @Override
            public void failure(RetrofitError error) {
                cardIssuersCallback.failure(error.getMessage());
            }
        });
    }

    @Override
    public void getPayerCosts(String baseUrl, String uri, String publicKey, String amount, String paymentMethod, String issuer, final ServiceCallback<List<PayerCost>> payerCostsCallback) {
        RestAdapter.Builder builder = new RestAdapter.Builder();

        if (BuildConfig.DEBUG) {
            builder.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        RestAdapter restAdapter = builder.setEndpoint(baseUrl).build();
        this.api = restAdapter.create(PaymentServiceApi.class);
        api.getInstallments(uri, publicKey, amount, paymentMethod, issuer, new Callback<List<Installment>>() {
            @Override
            public void success(List<Installment> installments, Response response) {
                List<PayerCost> payerCosts = installments.get(0).getPayerCosts();
                payerCostsCallback.success(payerCosts);
            }

            @Override
            public void failure(RetrofitError error) {
                payerCostsCallback.failure(error.getMessage());
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
