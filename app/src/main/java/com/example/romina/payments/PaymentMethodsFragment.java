package com.example.romina.payments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import butterknife.OnClick;
import butterknife.OnItemClick;


public class PaymentMethodsFragment extends Fragment{

    private static final String PAYMENT_METHODS  = "paymentMethods";
    private PaymentMethodsFragmentListener mListener;
    private ImageTextModelAdapter mAdapter;
    private ArrayList<PaymentMethod> mPaymentMethods;
    @Bind(R.id.fragment_list_listView)
    ListView mListView;
    @Bind(R.id.fragment_list_networkError)
    View mNetworkError;
    @Bind(R.id.fragment_list_emptyList)
    View mEmptyList;
    @Bind(R.id.fragment_list_loadingList)
    View mLoadingList;
    @Bind(R.id.empty_list_tex)
    TextView mEmptyListTex;


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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        mNetworkError.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.GONE);
        getActivity().setTitle(getActivity().getString(R.string.payment_method_title));
        return view;
    }

    @OnClick(R.id.btn_retry)
    void retry() {
        loadPaymentMethods();
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

    @OnItemClick(R.id.fragment_list_listView)
    void onItemClick(int position) {
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
            mEmptyListTex.setText(R.string.empty_payment_methods);
            mEmptyList.setVisibility(View.VISIBLE);
        }else {
            mEmptyList.setVisibility(View.GONE);
        }
    }

}
