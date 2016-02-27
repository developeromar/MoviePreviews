package com.desarrolladorandroid.moviepreviews;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
        adapter = new ImageAdapter(getContext(), new ArrayList<ObjectMovie>());
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

    public class FetchMovieTask extends AsyncTask<Void, Void, ObjectMovie[]> {
        String w;

        public FetchMovieTask() {
            w = getResources().getString(R.string.webService);
        }

        @Override
        protected ObjectMovie[] doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastStringJSON = null;
            try {
                Uri uri = Uri.parse(getString(R.string.webService)).buildUpon()
                        .appendQueryParameter("sort_by", "popularity.desc")
                        .appendQueryParameter("api_key", BuildConfig.MOVIE_KEY)
                        .build();

                URL url = new URL(uri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                forecastStringJSON = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            try {
                return getJSONDataMovie(forecastStringJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private ObjectMovie[] getJSONDataMovie(String JSON) throws JSONException {
            ObjectMovie[] regreso = null;
            JSONObject principal = new JSONObject(JSON);
            JSONArray resultados = principal.getJSONArray("results");
            if (resultados.length() > 0) {
                regreso = new ObjectMovie[resultados.length()];
            } else {
                return null;
            }
            JSONObject temp;
            for (int i = 0; i < resultados.length(); i++) {
                temp = resultados.getJSONObject(i);
                regreso[i] = new ObjectMovie(temp.getString("poster_path"), temp.getBoolean("adult"), temp.getString("overview"), temp.getString("release_date"),
                        temp.getInt("id"), temp.getString("original_title"), temp.getString("original_language"), temp.getString("title"), temp.getString("backdrop_path"), temp.getDouble("popularity"),
                        temp.getInt("vote_count"), temp.getBoolean("video"), temp.getDouble("vote_average"));
            }
            return regreso;
        }
    }
    //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=035e65ca7fde5dd3f8cf3e4913504cd2
}
