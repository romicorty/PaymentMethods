package com.example.romina.payments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.example.romina.payments.PaymentMethodAdapter;
import com.example.romina.payments.R;
import com.example.romina.payments.model.PaymentMethod;
import com.example.romina.payments.network.PaymentMethodService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PaymentMethodListActivity extends AppCompatActivity {
    private PaymentMethodAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_list);
        mListView = (ListView)findViewById(R.id.listView);
        if (savedInstanceState != null) {
            List<PaymentMethod> paymentMethods = (List<PaymentMethod>)savedInstanceState.getSerializable("paymentMethods");
            if (paymentMethods != null) {
                updateListView(paymentMethods);
            } else {
                loadPaymentMethods();
            }
        } else {
            loadPaymentMethods();
        }
    }

    public void loadPaymentMethods(){
        String baseUrl = "https://api.mercadopago.com";
        String uri = "v1/payment_methods";
        String publicKey = "444a9ef5-8a6b-429f-abdf-587639155d88";
        PaymentMethodService service = new PaymentMethodService();

        service.getPaymentMethods(baseUrl, uri, publicKey, new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> paymentMethods, Response response) {
                updateListView(paymentMethods);
            }

            @Override
            public void failure(RetrofitError error) {
                //TODO: Agregar pantalla de error
            }
        });
    }

    public void updateListView(List<PaymentMethod> paymentMethods) {
        mAdapter = new PaymentMethodAdapter(PaymentMethodListActivity.this,R.layout.payment_method_item,paymentMethods);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("paymentMethods", (Serializable) mAdapter.getmPaymentMethods());
    }
}
