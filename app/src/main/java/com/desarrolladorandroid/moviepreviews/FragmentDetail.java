package com.desarrolladorandroid.moviepreviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by pato_ on 27/02/2016.
 */
public class FragmentDetail extends Fragment {
    ObjectMovie movie;
    ImageView foto;

    public FragmentDetail() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        foto = (ImageView) view.findViewById(R.id.imagedetail);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE)) {
            movie = (ObjectMovie) intent.getSerializableExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE);
            getActivity().setTitle(movie.getTitle());
        }
        Picasso.with(getContext()).load(getString(R.string.pad2) + movie.getBackdrop_path()).into(foto);
        return view;
    }
}
