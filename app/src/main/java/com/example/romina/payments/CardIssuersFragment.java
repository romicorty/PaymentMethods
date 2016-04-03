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

import com.example.romina.payments.model.CardIssuer;
import com.example.romina.payments.network.PaymentService;
import com.example.romina.payments.network.PaymentServiceRetrofitImpl;
import com.example.romina.payments.network.ServiceCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CardIssuersFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String ARG_PAYMENT_METHOD = "paymentMethod";

    private CardIssuersFragmentListener mListener;
    private String mPaymentMethod;
    private ImageTextModelAdapter mAdapter;
    @Bind(R.id.cardIssuers_listView)
    ListView mListView;
    @Bind(R.id.cardIssuers_networkError)
    View mNetworkError;
    @Bind(R.id.cardIssuers_emptyList)
    View mEmptyList;
    @Bind(R.id.cardIssuers_loadingList)
    View mLoadingList;

    public static CardIssuersFragment newInstance(String paymentMethod) {
        CardIssuersFragment fragment = new CardIssuersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PAYMENT_METHOD, paymentMethod);
        fragment.setArguments(args);
        return fragment;
    }

    public CardIssuersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPaymentMethod = getArguments().getString(ARG_PAYMENT_METHOD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_issuers, container, false);
        ButterKnife.bind(this,view);
        mListView.setOnItemClickListener(this);
        mNetworkError.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.GONE);
        Button button = (Button) view.findViewById(R.id.btn_retry);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadCardIssuers();
            }
        });

        getActivity().setTitle(getActivity().getString(R.string.card_issuer_title));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadCardIssuers();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CardIssuersFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CardIssuersFragmentListener");
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
        CardIssuer cardIssuer = (CardIssuer) mAdapter.getItem(position);
        if (mListener != null) {
            mListener.onSelectedCardIssuer(cardIssuer);
        }
    }

    public interface CardIssuersFragmentListener {
        public void onSelectedCardIssuer(CardIssuer cardIssuer);
    }

    public void loadCardIssuers(){
        String baseUrl = "https://api.mercadopago.com";
        String uri = "v1/payment_methods/card_issuers";
        String publicKey = "444a9ef5-8a6b-429f-abdf-587639155d88";
        PaymentService service = new PaymentServiceRetrofitImpl();
        mLoadingList.setVisibility(View.VISIBLE);
        mNetworkError.setVisibility(View.GONE);
        service.getCardIssuers(baseUrl, uri, publicKey, mPaymentMethod, new ServiceCallback<List<CardIssuer>>() {
            @Override
            public void success(List<CardIssuer> cardIssuers) {
                updateListView(cardIssuers);
            }

            @Override
            public void failure(String error) {
                mLoadingList.setVisibility(View.GONE);
                mNetworkError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void updateListView(List<CardIssuer> cardIssuers) {

        mAdapter = new ImageTextModelAdapter(this.getActivity(),R.layout.image_text_model_item,cardIssuers);
        mListView.setAdapter(mAdapter);
        mLoadingList.setVisibility(View.GONE);
        mNetworkError.setVisibility(View.GONE);

        if (cardIssuers.isEmpty()) {
            mEmptyList.setVisibility(View.VISIBLE);
        }else {
            mEmptyList.setVisibility(View.GONE);
        }
    }
}
