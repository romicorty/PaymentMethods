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

import com.example.romina.payments.model.PayerCost;
import com.example.romina.payments.network.PaymentService;
import com.example.romina.payments.network.PaymentServiceRetrofitImpl;
import com.example.romina.payments.network.ServiceCallback;

import java.util.List;


public class InstallmentsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private static final String ARG_AMOUNT = "amount";
    private static final String ARG_PAYMENT_METHOD_ID = "paymentMethod";
    private static final String ARG_CARD_ISSUER_ID = "cardIssuer";

    // TODO: Rename and change types of parameters
    private String mAmount;
    private String mPaymentMethodId;
    private String mCardIssuerId;

    private InstallmentsFragmentListener mListener;
    private ImageTextModelAdapter mAdapter;
    //TODO: Cargar con ButterKnif elementos de la vista
    private ListView mListView;
    private View mNetworkError;
    private View mEmptyList;
    private View mLoadingList;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_installments, container, false);
        mListView = (ListView) view.findViewById(R.id.installments_listView);
        mListView.setOnItemClickListener(this);
        mEmptyList = view.findViewById(R.id.installments_emptyList);
        mLoadingList = view.findViewById(R.id.installments_loadingList);
        mNetworkError = view.findViewById(R.id.installments_networkError);
        mNetworkError.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.GONE);
        Button button = (Button) view.findViewById(R.id.btn_retry);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadInstallments();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadInstallments();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (InstallmentsFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement InstallmentsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PayerCost payerCost = (PayerCost) mAdapter.getItem(position);
        if (mListener != null) {
            mListener.selectedPayerCost(payerCost);
        }
    }

    public interface InstallmentsFragmentListener {
        public void selectedPayerCost(PayerCost payerCost);
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
            mEmptyList.setVisibility(View.VISIBLE);
        }else {
            mEmptyList.setVisibility(View.GONE);
        }
    }

}
