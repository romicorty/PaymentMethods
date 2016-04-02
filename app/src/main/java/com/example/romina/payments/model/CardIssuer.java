package com.example.romina.payments.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CardIssuer implements Parcelable,ImageTextModel{

    @SerializedName("id")
    private String mId;

    @SerializedName("name")
    private String mName;

    @SerializedName("secure_thumbnail")
    private String mImageURL;

    protected CardIssuer(Parcel in) {
        mName = in.readString();
        mId = in.readString();
        mImageURL = in.readString();
    }

    public static final Creator<CardIssuer> CREATOR = new Creator<CardIssuer>() {
        @Override
        public CardIssuer createFromParcel(Parcel in) {
            return new CardIssuer(in);
        }

        @Override
        public CardIssuer[] newArray(int size) {
            return new CardIssuer[size];
        }
    };

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
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
        dest.writeString(mImageURL);
    }
}
