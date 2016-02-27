package com.desarrolladorandroid.moviepreviews;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

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
    ProgressBar progressBar;

    public FragmentForecast() {
    }

    public static FragmentForecast newInstance() {
        FragmentForecast fragmentForecast = new FragmentForecast();
        return fragmentForecast;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_forecast, null);
        gridView = (GridView) vista.findViewById(R.id.gridView);
        adapter = new ImageAdapter(getContext(), new ArrayList<ObjectMovie>());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ObjectMovie movie = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, movie);
                startActivity(intent);
            }
        });
        progressBar = (ProgressBar) vista.findViewById(R.id.loading_spinner);
        setHasOptionsMenu(true);
        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchMovieTask().execute();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragmentforecast, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                new FetchMovieTask().execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, ObjectMovie[]> {
        String w;

        public FetchMovieTask() {
            w = getResources().getString(R.string.webService);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

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

        @Override
        protected void onPostExecute(ObjectMovie[] objectMovies) {
            if (objectMovies != null) {
                adapter.clear();
                for (ObjectMovie dayForecastStr : objectMovies) {
                    adapter.add(dayForecastStr);
                }
                progressBar.setVisibility(View.GONE);

                // New data is back from the server.  Hooray!
            }
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

}
