package com.example.romina.payments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.romina.payments.model.CardIssuer;
import com.example.romina.payments.model.PaymentMethod;

import java.math.BigDecimal;

public class PaymentActivity extends AppCompatActivity implements AmountFragment.AmountFragmentListener,PaymentMethodsFragment.PaymentMethodsFragmentListener,CardIssuersFragment.CardIssuersFragmentListener {

    private BigDecimal mAmount;
    private PaymentMethod mPaymentMethod;
    private CardIssuer mCardIssuer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        AmountFragment amountFragment =  AmountFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, amountFragment);
        ft.commit();
    }

    @Override
    public void onInputAmount(BigDecimal amount) {
        mAmount = amount;
        PaymentMethodsFragment paymentMethodsFragment =  PaymentMethodsFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, paymentMethodsFragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void onSelectedPaymentMethod(PaymentMethod paymentMethod) {
        mPaymentMethod = paymentMethod;
        CardIssuersFragment cardIssuersFragment =  CardIssuersFragment.newInstance(mPaymentMethod.getId());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, cardIssuersFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onSelectedCardIssuer(CardIssuer cardIssuer) {
        mCardIssuer = cardIssuer;
    }
}
