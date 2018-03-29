package com.example.android.topmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RvMainItemAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";

    private static final String API_KEY= "PASTE YOUR KEY HERE !!!";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String SORT_POPULAR = "popular?api_key=";
    private static final String SORT_TOP_RATED = "top_rated?api_key=";
    private static final String POSTER_URL_BASE = "http://image.tmdb.org/t/p/w500/";

    private static final String POSTER_PATH = "poster_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";


    private String API_LAST_URL;
    private static final String URL_POPULAR = BASE_URL+SORT_POPULAR+API_KEY;
    private static final String URL_TOP_RATED = BASE_URL+SORT_TOP_RATED+API_KEY;


    public static final String EXTRA_IMAGE_URL = "imageUrl";
    public static final String EXTRA_ORIGINAL_TITLE = "originalTitle";
    public static final String EXTRA_OVERVIEW = "overview";
    public static final String EXTRA_VOTE_AVERAGE = "voteAverage";
    public static final String EXTRA_RELEASE_DATE = "releaseDate";


    private RecyclerView mRecyclerView;
    private RvMainItemAdapter mRvMainItemAdapter;
    private ArrayList<RvMainItem> mRvMainItemList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Top Movies");
        API_LAST_URL = URL_TOP_RATED;
        parseJson();
    }

    private void parseJson() {

        if (isOnline()) {
            mRecyclerView = findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

            mRvMainItemList = new ArrayList<>();

            mRequestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_LAST_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray =  response.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject  result = jsonArray.getJSONObject(i);

                                    String imageUrl = POSTER_URL_BASE + result.getString(POSTER_PATH);
                                    String originalTitle = result.getString(ORIGINAL_TITLE);
                                    String overview = result.getString(OVERVIEW);
                                    int voteAverage = result.getInt(VOTE_AVERAGE);
                                    String releaseDate =  result.getString(RELEASE_DATE);


                                    mRvMainItemList.add(new RvMainItem(imageUrl, originalTitle, overview, voteAverage, releaseDate));


                                }

                                mRvMainItemAdapter = new RvMainItemAdapter(MainActivity.this, mRvMainItemList);
                                mRecyclerView.setAdapter(mRvMainItemAdapter);
                                mRvMainItemAdapter.setOnItemClickListener(MainActivity.this);

                            } catch (JSONException e) {
                                e.printStackTrace();

                                Log.v(TAG,"volley problem");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                }
            });

            mRequestQueue.add(request);

        } else {
            getSupportActionBar().setTitle("You Have No Connection!");

        }

    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this,  DetailActivity.class);
        RvMainItem clickedItem = mRvMainItemList.get(position);

        detailIntent.putExtra(EXTRA_IMAGE_URL, clickedItem.getmImageUrl());
        detailIntent.putExtra(EXTRA_ORIGINAL_TITLE, clickedItem.getmOriginalTitle());
        detailIntent.putExtra(EXTRA_OVERVIEW, clickedItem.getmOverview());
        detailIntent.putExtra(EXTRA_VOTE_AVERAGE, clickedItem.getmVoteAverage());
        detailIntent.putExtra(EXTRA_RELEASE_DATE, clickedItem.getmReleaseDate());

        startActivity(detailIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_sort_popular:
                getSupportActionBar().setTitle("Popular Movies");
                API_LAST_URL = URL_POPULAR;
                parseJson();
                return true;
            case R.id.menu_sort_top:
                getSupportActionBar().setTitle("Top Movies");
                API_LAST_URL = URL_TOP_RATED;
                parseJson();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

