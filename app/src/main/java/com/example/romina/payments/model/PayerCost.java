package com.example.romina.payments.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PayerCost implements ImageTextModel, Parcelable{

    @SerializedName("recommended_message")
    private String mRecommendedMessage;

    protected PayerCost(Parcel in) {
        mRecommendedMessage = in.readString();
    }

    public static final Creator<PayerCost> CREATOR = new Creator<PayerCost>() {
        @Override
        public PayerCost createFromParcel(Parcel in) {
            return new PayerCost(in);
        }

        @Override
        public PayerCost[] newArray(int size) {
            return new PayerCost[size];
        }
    };

    @Override
    public String getImageURL() {
        return null;
    }

    @Override
    public String getText() {
        return mRecommendedMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRecommendedMessage);
    }
}
