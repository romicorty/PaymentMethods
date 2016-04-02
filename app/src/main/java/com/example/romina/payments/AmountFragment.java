package com.example.romina.payments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;


public class AmountFragment extends Fragment {

    private AmountFragmentListener mListener;

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
        Button btn = (Button) view.findViewById(R.id.btn_next);
        final EditText txAmount = (EditText)view.findViewById(R.id.tx_amount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    BigDecimal amount = new BigDecimal(txAmount.getText().toString());
                    mListener.onInputAmount(amount);
                }
            }
        });

        return view;
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

    public interface AmountFragmentListener {
        public void onInputAmount(BigDecimal amount);
    }

}
