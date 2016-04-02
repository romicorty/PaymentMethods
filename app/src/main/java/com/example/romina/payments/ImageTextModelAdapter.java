package com.example.romina.payments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.romina.payments.model.ImageTextModel;

import java.util.List;

public class ImageTextModelAdapter <T extends  ImageTextModel> extends ArrayAdapter<T> {

    private int mResourceId;

    public ImageTextModelAdapter(Context context, int resource, List<T> imageTextModels) {
        super(context, resource, imageTextModels);
        mResourceId = resource;
    }

    static class ImageTextModelViewHolder {
        TextView txName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageTextModelViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ImageTextModelViewHolder();
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(mResourceId,parent,false);
            viewHolder.txName = (TextView)convertView.findViewById(R.id.imageTextModelItem_txName);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ImageTextModelViewHolder)convertView.getTag();
        }

        ImageTextModel imageTextModel = getItem(position);
        viewHolder.txName.setText(imageTextModel.getText());

        return convertView;
    }
}
