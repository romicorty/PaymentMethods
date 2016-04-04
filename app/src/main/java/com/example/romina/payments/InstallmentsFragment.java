package com.example.romina.payments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.romina.payments.model.PayerCost;
import com.example.romina.payments.network.PaymentService;
import com.example.romina.payments.network.PaymentServiceRetrofitImpl;
import com.example.romina.payments.network.ServiceCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;


public class InstallmentsFragment extends Fragment{

    private static final String ARG_AMOUNT = "amount";
    private static final String ARG_PAYMENT_METHOD_ID = "paymentMethod";
    private static final String ARG_CARD_ISSUER_ID = "cardIssuer";
    private static final String PAYER_COSTS = "payerCosts";

    private String mAmount;
    private String mPaymentMethodId;
    private String mCardIssuerId;

    private InstallmentsFragmentListener mListener;
    private ImageTextModelAdapter mAdapter;
    private ArrayList<PayerCost> mPayerCosts;
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

    public static InstallmentsFragment newInstance(String amount, String paymentMethod, String cardIssuer) {
        InstallmentsFragment fragment = new InstallmentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_AMOUNT, amount);
        args.putString(ARG_PAYMENT_METHOD_ID, paymentMethod);
        args.putString(ARG_CARD_ISSUER_ID,cardIssuer);
        fragment.setArguments(args);
        return fragment;
    }

    public InstallmentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAmount = getArguments().getString(ARG_AMOUNT);
            mPaymentMethodId = getArguments().getString(ARG_PAYMENT_METHOD_ID);
            mCardIssuerId = getArguments().getString(ARG_CARD_ISSUER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        mNetworkError.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.GONE);
        getActivity().setTitle(getActivity().getString(R.string.installment_title));

        return view;
    }

    @OnClick(R.id.btn_retry)
    void retry() {
        loadInstallments();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            loadInstallments();
        }else {
            mPayerCosts = savedInstanceState.getParcelableArrayList(PAYER_COSTS);
            if (mPayerCosts != null)  {
                updateListView(mPayerCosts);
            }else {
                loadInstallments();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (InstallmentsFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement InstallmentsFragmentListener");
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPayerCosts != null) {
            outState.putParcelableArrayList(PAYER_COSTS, mPayerCosts);
        }
    }

    @OnItemClick(R.id.fragment_list_listView)
    public void onItemClick(int position) {
        PayerCost payerCost = (PayerCost) mAdapter.getItem(position);
        if (mListener != null) {
            mListener.selectedPayerCost(payerCost);
        }
    }

    public interface InstallmentsFragmentListener {
        void selectedPayerCost(PayerCost payerCost);
    }

    public void loadInstallments(){
        String baseUrl = "https://api.mercadopago.com";
        String uri = "v1/payment_methods/installments";
        String publicKey = "444a9ef5-8a6b-429f-abdf-587639155d88";
        PaymentService service = new PaymentServiceRetrofitImpl();
        mLoadingList.setVisibility(View.VISIBLE);
        mNetworkError.setVisibility(View.GONE);
        service.getPayerCosts(baseUrl, uri, publicKey, mAmount, mPaymentMethodId, mCardIssuerId, new ServiceCallback<List<PayerCost>>() {
            @Override
            public void success(List<PayerCost> installments) {
                updateListView(installments);

            }

            @Override
            public void failure(String error) {
                mLoadingList.setVisibility(View.GONE);
                mNetworkError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateListView(List<PayerCost> payerCosts) {

        mAdapter = new ImageTextModelAdapter(this.getActivity(),R.layout.image_text_model_item,payerCosts);
        mListView.setAdapter(mAdapter);
        mLoadingList.setVisibility(View.GONE);
        mNetworkError.setVisibility(View.GONE);

        if (payerCosts.isEmpty()) {
            mEmptyListTex.setText(R.string.empty_installments);
            mEmptyList.setVisibility(View.VISIBLE);
        }else {
            mEmptyList.setVisibility(View.GONE);
        }
    }

}
