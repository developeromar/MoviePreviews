package com.desarrolladorandroid.moviepreviews;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by cosanchez on 26/02/2016.
 */
public class ImageAdapter extends ArrayAdapter<ObjectMovie> {

    public ImageAdapter(Context context, ArrayList<ObjectMovie> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_layout, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_item);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg ").into(imageView);
        return convertView;
    }
}
