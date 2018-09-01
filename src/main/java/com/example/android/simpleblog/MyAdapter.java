package com.example.android.simpleblog;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MyAdapter extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public MyAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slides, view, false);
        ImageView myImage =  myImageLayout
                .findViewById(R.id.image);
        TextView place =  myImageLayout
                .findViewById(R.id.place);
        ImageView place_image =  myImageLayout
                .findViewById(R.id.crown);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.image_placeholder);

        Glide.with(context).applyDefaultRequestOptions(requestOptions).load(images.get(position)).into(myImage);
        switch (position){
            case 0:
                String k;
                k = position+1 + "st place ";
               place.setText( k );
                place_image.setImageResource(R.drawable.gold_crown_android);
                break;
            case 1:
                String i = position+1 + "nd place ";
                place.setText( i);
             place_image.setImageResource(R.drawable.silver_crown_android);
                break;
            case 2:
                String j = position+1 + "rd place ";
                place.setText( j );
                place_image.setImageResource(R.drawable.bronze_crown_android);
                break;
            default:
                String l = position+1 + "th place ";
                place.setText( l );

        }
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}