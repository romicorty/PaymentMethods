package com.example.romina.payments.model;


import com.google.gson.annotations.SerializedName;

public class PayerCost implements ImageTextModel{

    @SerializedName("recommended_message")
    private String mRecommendedMessage;

    public String getRecommendedMessage() {
        return mRecommendedMessage;
    }

    @Override
    public String getImageURL() {
        return null;
    }

    @Override
    public String getText() {
        return mRecommendedMessage;
    }
}
