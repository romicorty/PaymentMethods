package com.example.romina.payments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.romina.payments.model.PaymentMethod;
import com.example.romina.payments.network.PaymentService;
import com.example.romina.payments.network.PaymentServiceRetrofitImpl;
import com.example.romina.payments.network.ServiceCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PaymentMethodsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String PAYMENT_METHODS  = "paymentMethods";
    private PaymentMethodsFragmentListener mListener;
    private ImageTextModelAdapter mAdapter;
    private ArrayList<PaymentMethod> mPaymentMethods;
    @Bind(R.id.paymentMethods_listView)
    ListView mListView;
    @Bind(R.id.paymentMethods_networkError)
    View mNetworkError;
    @Bind(R.id.paymentMethods_emptyList)
    View mEmptyList;
    @Bind(R.id.paymentMethods_loadingList)
    View mLoadingList;


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
        ButterKnife.bind(this,view);
        mListView.setOnItemClickListener(this);
        mNetworkError.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.GONE);
        Button button = (Button) view.findViewById(R.id.btn_retry);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadPaymentMethods();
            }
        });

        getActivity().setTitle(getActivity().getString(R.string.payment_method_title));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            loadPaymentMethods();
        }else {
            mPaymentMethods = savedInstanceState.getParcelableArrayList(PAYMENT_METHODS);
            if (mPaymentMethods != null)  {
                updateListView(mPaymentMethods);
            }else {
                loadPaymentMethods();
            }
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PaymentMethod paymentMethod = (PaymentMethod)mAdapter.getItem(position);
        if (mListener != null) {
            mListener.onSelectedPaymentMethod(paymentMethod);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPaymentMethods != null) {
            outState.putParcelableArrayList(PAYMENT_METHODS, mPaymentMethods);
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
                mPaymentMethods = new ArrayList<PaymentMethod>(paymentMethods);
                updateListView(mPaymentMethods);
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
            TextView tex = (TextView) getActivity().findViewById(R.id.empty_list_tex);
            tex.setText(R.string.empty_payment_methods);
            mEmptyList.setVisibility(View.VISIBLE);
        }else {
            mEmptyList.setVisibility(View.GONE);
        }
    }

}
