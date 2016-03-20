package com.example.romina.payments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.romina.payments.model.PaymentMethod;
import com.example.romina.payments.network.PaymentMethodService;
import com.example.romina.payments.network.PaymentMethodServiceRetrofitImpl;
import com.example.romina.payments.network.ServiceCallback;

import java.util.List;

public class PaymentMethodListActivity extends AppCompatActivity {
    private static final String PAYMENT_METHODS = "paymentMethods";
    private PaymentMethodAdapter mAdapter;
    private ListView mListView;
    private View mNetworkError;
    private View mEmptyList;
    private View mLoadingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_list);
        mListView = (ListView)findViewById(R.id.listView);
        mNetworkError = findViewById(R.id.networkError);
        mEmptyList = findViewById(R.id.emptyList);
        mLoadingList = findViewById(R.id.loadingList);

        mNetworkError.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.GONE);

        Button button = (Button) findViewById(R.id.btn_retry);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadPaymentMethods();
            }
        });

        if (savedInstanceState != null) {
            List<PaymentMethod> paymentMethods = savedInstanceState.getParcelableArrayList(PAYMENT_METHODS);
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
        PaymentMethodService service = new PaymentMethodServiceRetrofitImpl();
        mLoadingList.setVisibility(View.VISIBLE);
        mNetworkError.setVisibility(View.GONE);
        service.getPaymentMethods(baseUrl, uri, publicKey, new ServiceCallback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> paymentMethods) {
                updateListView(paymentMethods);
            }

            @Override
            public void failure(String error) {
                mLoadingList.setVisibility(View.GONE);
                mNetworkError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateListView(List<PaymentMethod> paymentMethods) {
        mAdapter = new PaymentMethodAdapter(PaymentMethodListActivity.this,R.layout.payment_method_item,paymentMethods);
        mListView.setAdapter(mAdapter);
        mLoadingList.setVisibility(View.GONE);
        mNetworkError.setVisibility(View.GONE);

        if (paymentMethods.isEmpty()) {
            mEmptyList.setVisibility(View.VISIBLE);
        }else {
            mEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null) {
            outState.putParcelableArrayList(PAYMENT_METHODS, mAdapter.getPaymentMethods());
        }
    }
}
