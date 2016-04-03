package com.example.romina.payments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.romina.payments.model.CardIssuer;
import com.example.romina.payments.model.PayerCost;
import com.example.romina.payments.model.PaymentMethod;

import java.math.BigDecimal;

public class PaymentActivity extends AppCompatActivity implements AmountFragment.AmountFragmentListener,PaymentMethodsFragment.PaymentMethodsFragmentListener,CardIssuersFragment.CardIssuersFragmentListener, InstallmentsFragment.InstallmentsFragmentListener{

    private BigDecimal mAmount;
    private PaymentMethod mPaymentMethod;
    private CardIssuer mCardIssuer;
    private PayerCost mPayercost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        if (savedInstanceState == null) {
            AmountFragment amountFragment =  AmountFragment.newInstance();
            pushFragment(amountFragment,false);
        }else{
            mPaymentMethod = savedInstanceState.getParcelable("paymentMethod");
            mCardIssuer = savedInstanceState.getParcelable("cardIssuer");
            mPayercost = savedInstanceState.getParcelable("payerCost");
            String amount = savedInstanceState.getString("amount");
            if (amount != null) {
                mAmount = new BigDecimal(amount);
            }
        }
    }

    @Override
    public void onInputAmount(BigDecimal amount) {
        mAmount = amount;
        PaymentMethodsFragment paymentMethodsFragment =  PaymentMethodsFragment.newInstance();
       pushFragment(paymentMethodsFragment,true);

    }

    @Override
    public void onSelectedPaymentMethod(PaymentMethod paymentMethod) {
        mPaymentMethod = paymentMethod;
        CardIssuersFragment cardIssuersFragment =  CardIssuersFragment.newInstance(mPaymentMethod.getId());
        pushFragment(cardIssuersFragment,true);

    }

    @Override
    public void onSelectedCardIssuer(CardIssuer cardIssuer) {
        mCardIssuer = cardIssuer;
        InstallmentsFragment installmentsFragment = InstallmentsFragment.newInstance(mAmount.toString(),mPaymentMethod.getId(),mCardIssuer.getId());
        pushFragment(installmentsFragment,true);
    }

    @Override
    public void selectedPayerCost(PayerCost payerCost) {
        mPayercost = payerCost;
        FragmentManager fm = getSupportFragmentManager();
        showConfirmationAlert();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void showConfirmationAlert() {
        String message = getString(R.string.alert_amount)+": $ "+mAmount.toString()+"\n";
        message+= getString(R.string.alert_payment_method)+": "+mPaymentMethod.getText()+" ("+mCardIssuer.getText()+")"+"\n";
        message+= mPayercost.getText();

        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.btn_ok), null)
                .setNegativeButton(getString(R.string.btn_cancel),null)
                .show();
    }

    private void pushFragment(Fragment fragment, boolean backStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        if (backStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("paymentMethod", mPaymentMethod);
        outState.putParcelable("cardIssuer", mCardIssuer);
        outState.putParcelable("payerCost", mPayercost);
        if (mAmount != null) {
            outState.putString("amount",mAmount.toString());    
        }
    }
}
