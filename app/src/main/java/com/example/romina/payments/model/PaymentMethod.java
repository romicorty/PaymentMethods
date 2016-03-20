package com.example.romina.payments.model;


import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class PaymentMethod implements Parcelable{
    @SerializedName("name")
    private String name;

    @SerializedName("payment_type_id")
    private String paymentTypeId;

    protected PaymentMethod(Parcel in) {
        name = in.readString();
        paymentTypeId = in.readString();
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

    public String getName() {
        return name;
    }

    public String getPaymentTypeId() {
        return paymentTypeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(paymentTypeId);
    }
}
