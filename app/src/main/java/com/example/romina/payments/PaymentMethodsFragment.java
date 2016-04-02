package com.example.romina.payments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.romina.payments.model.PaymentMethod;
import com.example.romina.payments.network.PaymentService;
import com.example.romina.payments.network.PaymentServiceRetrofitImpl;
import com.example.romina.payments.network.ServiceCallback;

import java.util.List;


public class PaymentMethodsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private PaymentMethodsFragmentListener mListener;
    private ImageTextModelAdapter mAdapter;
    //TODO: Cargar con ButterKnif elementos de la vista
    private ListView mListView;
    private View mNetworkError;
    private View mEmptyList;
    private View mLoadingList;


    public static PaymentMethodsFragment newInstance() {
        return new PaymentMethodsFragment();
    }

    public PaymentMethodsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_methods, container, false);
        mListView = (ListView) view.findViewById(R.id.paymentMethods_listView);
        mListView.setOnItemClickListener(this);
        mEmptyList = view.findViewById(R.id.paymentMethods_emptyList);
        mLoadingList = view.findViewById(R.id.paymentMethods_loadingList);
        mNetworkError = view.findViewById(R.id.paymentMethods_networkError);
        mNetworkError.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.GONE);
        Button button = (Button) view.findViewById(R.id.btn_retry);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadPaymentMethods();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPaymentMethods();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PaymentMethodsFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PaymentMethodsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PaymentMethod paymentMethod = (PaymentMethod)mAdapter.getItem(position);
        if (mListener != null) {
            mListener.onSelectedPaymentMethod(paymentMethod);
        }
    }

    public interface PaymentMethodsFragmentListener {
        public void onSelectedPaymentMethod(PaymentMethod paymentMethod);
    }

    public void loadPaymentMethods(){
        String baseUrl = "https://api.mercadopago.com";
        String uri = "v1/payment_methods";
        String publicKey = "444a9ef5-8a6b-429f-abdf-587639155d88";
        PaymentService service = new PaymentServiceRetrofitImpl();
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

        mAdapter = new ImageTextModelAdapter(this.getActivity(),R.layout.image_text_model_item,paymentMethods);
        mListView.setAdapter(mAdapter);
        mLoadingList.setVisibility(View.GONE);
        mNetworkError.setVisibility(View.GONE);

        if (paymentMethods.isEmpty()) {
            mEmptyList.setVisibility(View.VISIBLE);
        }else {
            mEmptyList.setVisibility(View.GONE);
        }
    }

}
