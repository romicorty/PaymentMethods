package com.example.romina.payments.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Installment {

    @SerializedName("payer_costs")
    private List<PayerCost> mPayerCosts;


    public List<PayerCost> getPayerCosts() {
        return mPayerCosts;
    }
}
