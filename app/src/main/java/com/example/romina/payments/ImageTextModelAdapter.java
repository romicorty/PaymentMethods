package com.example.romina.payments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.romina.payments.model.ImageTextModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageTextModelAdapter <T extends  ImageTextModel> extends ArrayAdapter<T> {

    private int mResourceId;
    private Context mContext;

    public ImageTextModelAdapter(Context context, int resource, List<T> imageTextModels) {
        super(context, resource, imageTextModels);
        mResourceId = resource;
        mContext = context;
    }

    static class ImageTextModelViewHolder {
        TextView txName;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageTextModelViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ImageTextModelViewHolder();
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(mResourceId,parent,false);
            viewHolder.txName = (TextView)convertView.findViewById(R.id.imageTextModelItem_txName);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imageTextModelItem_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ImageTextModelViewHolder)convertView.getTag();
        }

        ImageTextModel imageTextModel = getItem(position);
        viewHolder.txName.setText(imageTextModel.getText());

        if (imageTextModel.getImageURL() != null){
            viewHolder.img.setVisibility(View.VISIBLE);
            Picasso.with(mContext).setLoggingEnabled(true);
            Picasso.with(mContext)
                    .load(imageTextModel.getImageURL())
                    .into(viewHolder.img);
        }else {
            viewHolder.img.setVisibility(View.GONE);
        }

        return convertView;
    }
}
