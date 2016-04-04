package com.example.romina.payments;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.romina.payments.model.ImageTextModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageTextModelAdapter <T extends  ImageTextModel> extends ArrayAdapter<T> {

    private int mResourceId;
    private Context mContext;

    public ImageTextModelAdapter(Context context, @LayoutRes int resource, List<T> imageTextModels) {
        super(context, resource, imageTextModels);
        mResourceId = resource;
        mContext = context;
    }

    static class ImageTextModelViewHolder {
        @Bind(R.id.imageTextModelItem_txName)
        TextView txName;
        @Bind(R.id.imageTextModelItem_image)
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageTextModelViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ImageTextModelViewHolder();
            LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(mResourceId,parent,false);
            ButterKnife.bind(viewHolder, convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ImageTextModelViewHolder)convertView.getTag();
        }

        ImageTextModel imageTextModel = getItem(position);
        viewHolder.txName.setText(imageTextModel.getText());

        if (imageTextModel.getImageURL() != null){
            viewHolder.img.setVisibility(View.VISIBLE);
            if(BuildConfig.DEBUG == true) {
                Picasso.with(mContext).setLoggingEnabled(true);
            }
            Picasso.with(mContext)
                    .load(imageTextModel.getImageURL())
                    .into(viewHolder.img);
        }else {
            viewHolder.img.setVisibility(View.GONE);
        }

        return convertView;
    }
}
