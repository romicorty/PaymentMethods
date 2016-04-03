package com.example.romina.payments.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PaymentMethod implements Parcelable, ImageTextModel{

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("payment_type_id")
    private String mPaymentTypeId;

    @SerializedName("secure_thumbnail")
    private String mImageURL;

    protected PaymentMethod(Parcel in) {
        mName = in.readString();
        mPaymentTypeId = in.readString();
        mId = in.readString();
        mImageURL = in.readString();
    }

    public static final Creator<PaymentMethod> CREATOR = new Creator<PaymentMethod>() {
        @Override
        public PaymentMethod createFromParcel(Parcel in) {
            return new PaymentMethod(in);
        }

        @Override
        public PaymentMethod[] newArray(int size) {
            return new PaymentMethod[size];
        }
    };

    public String getPaymentTypeId() {
        return mPaymentTypeId;
    }

    public String getId() {
        return mId;
    }

    public String getImageURL() {
        return mImageURL;
    }

    @Override
    public String getText() {
        return mName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mPaymentTypeId);
        dest.writeString(mImageURL);
    }
}
