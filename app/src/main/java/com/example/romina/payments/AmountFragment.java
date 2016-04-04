package com.example.romina.payments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AmountFragment extends Fragment {

    private AmountFragmentListener mListener;
    @Bind(R.id.tx_amount)
    EditText mTxAmount;


    public AmountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static AmountFragment newInstance() {
        return new AmountFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amount, container, false);
        ButterKnife.bind(this,view);
        getActivity().setTitle(getActivity().getString(R.string.amount_title));

        return view;
    }

    @OnClick(R.id.btn_next)
    void onNextButtonClick() {
        if (mListener != null) {
            if (mTxAmount.getText().toString().isEmpty()) {
                showEmptyAmountAlert(getActivity());
            }else {
                BigDecimal amount = new BigDecimal(mTxAmount.getText().toString());
                mListener.onInputAmount(amount);
            }
        }
    }

    private void showEmptyAmountAlert(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.empty_amount_alert_message_title))
                .setMessage(context.getString(R.string.empty_amount_alert_message))
                .setPositiveButton(context.getString(R.string.btn_ok), null)
                .show();
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AmountFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AmountFragmentListener");
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

    public interface AmountFragmentListener {
        void onInputAmount(BigDecimal amount);
    }

}
