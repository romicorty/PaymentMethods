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

import com.example.romina.payments.model.CardIssuer;
import com.example.romina.payments.network.PaymentService;
import com.example.romina.payments.network.PaymentServiceRetrofitImpl;
import com.example.romina.payments.network.ServiceCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class CardIssuersFragment extends Fragment{
    private static final String ARG_PAYMENT_METHOD = "paymentMethod";
    private static final String CARD_ISSUERS = "cardIssuers";
    private ArrayList<CardIssuer> mCardIssuers;
    private CardIssuersFragmentListener mListener;
    private String mPaymentMethod;
    private ImageTextModelAdapter mAdapter;
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
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        mNetworkError.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.GONE);
        getActivity().setTitle(getActivity().getString(R.string.card_issuer_title));

        return view;
    }

    @OnClick(R.id.btn_retry)
    void retry() {
        loadCardIssuers();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            loadCardIssuers();
        }else {
            mCardIssuers = savedInstanceState.getParcelableArrayList(CARD_ISSUERS);
            if (mCardIssuers != null)  {
                updateListView(mCardIssuers);
            }else {
                loadCardIssuers();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (CardIssuersFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCardIssuers != null) {
            outState.putParcelableArrayList(CARD_ISSUERS, mCardIssuers);
        }

    }

    @OnItemClick(R.id.fragment_list_listView)
    public void onItemClick(int position) {
        CardIssuer cardIssuer = (CardIssuer) mAdapter.getItem(position);
        if (mListener != null) {
            mListener.onSelectedCardIssuer(cardIssuer);
        }
    }

    public interface CardIssuersFragmentListener {
        void onSelectedCardIssuer(CardIssuer cardIssuer);
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
                mCardIssuers = new ArrayList<CardIssuer>(cardIssuers);
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
            mEmptyListTex.setText(R.string.empty_card_issuers);
            mEmptyList.setVisibility(View.VISIBLE);
        }else {
            mEmptyList.setVisibility(View.GONE);
        }
    }
}
