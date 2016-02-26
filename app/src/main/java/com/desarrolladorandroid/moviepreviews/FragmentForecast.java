package com.desarrolladorandroid.moviepreviews;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by cosanchez on 26/02/2016.
 */
public class FragmentForecast extends Fragment {
    GridView gridView;
    ImageAdapter adapter;

    public FragmentForecast() {
    }

    public static FragmentForecast newInstance() {
        FragmentForecast fragmentForecast = new FragmentForecast();
        return fragmentForecast;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_forecast, null);
        gridView = (GridView) vista.findViewById(R.id.gridView);
        //////sacar lins de imagenes
        ArrayList<ObjectMovie> arreglo = new ArrayList<ObjectMovie>();
        for (int i = 0; i < 25; i++) {
            arreglo.add(new ObjectMovie());
        }
        //////////////////
        adapter = new ImageAdapter(getContext(), arreglo);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "position " + position, Snackbar.LENGTH_LONG).show();
            }
        });
        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
