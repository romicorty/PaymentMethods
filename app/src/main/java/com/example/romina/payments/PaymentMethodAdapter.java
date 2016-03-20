package com.example.romina.payments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.romina.payments.model.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodAdapter extends ArrayAdapter<PaymentMethod> {

    private ArrayList<PaymentMethod> mPaymentMethods;
    private int mResourceId;

    public PaymentMethodAdapter(Context context, int resource,List<PaymentMethod> paymentMethods) {
        super(context, resource, paymentMethods);
        mPaymentMethods = new ArrayList<>(paymentMethods);
        mResourceId = resource;
    }

    static class PaymentMethodViewHolder {
        TextView txName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PaymentMethodViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new PaymentMethodViewHolder();
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(mResourceId,parent,false);
            viewHolder.txName = (TextView)convertView.findViewById(R.id.txName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (PaymentMethodViewHolder)convertView.getTag();
        }

        PaymentMethod paymentMethod = mPaymentMethods.get(position);
        viewHolder.txName.setText(paymentMethod.getName());

        return convertView;
    }

    public ArrayList<PaymentMethod> getPaymentMethods() {
        return mPaymentMethods;
    }
}
