package com.example.android.topmovies;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RvMainItemAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";


    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String SORT_POPULAR = "popular?api_key=";
    private static final String SORT_TOP_RATED = "top_rated?api_key=";
    private static final String POSTER_URL_BASE = "http://image.tmdb.org/t/p/w500/";

    private static final String POSTER_PATH = "poster_path";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String ID = "id";

    private String API_LAST_URL;
    private static final String URL_POPULAR = BASE_URL + SORT_POPULAR + API_KEY;
    private static final String URL_TOP_RATED = BASE_URL + SORT_TOP_RATED + API_KEY;

    public static final String KEY_MOVIE = "rvMainItemObject";


    private RvMainItemAdapter mRvMainItemAdapter;
    private List<RvMainItem> mRvMainItemList;

    private RequestQueue mRequestQueue;

    private AppDatabase mDb;
    private MainViewModel mMainViewModel;

    private MenuItem mFavoriteMenuItem;

    @BindView(R.id.recycler_view) EmptyRecyclerView mRecyclerView;
    @BindView(R.id.empty_main) LinearLayout mEmptyMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setEmptyView(mEmptyMain);


        if (mMainViewModel.optionsItem != null) {
            onOptionsItemSelected(mMainViewModel.optionsItem);} else {
            getSupportActionBar().setTitle("Popular Movies");
            API_LAST_URL = URL_POPULAR;
            parseJson();
       }

    }

    private void parseJson() {

        if (isOnline()) {
            mRvMainItemList = new ArrayList<>();

            mRequestQueue = Volley.newRequestQueue(this);

            final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_LAST_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                loading.dismiss();
                                JSONArray jsonArray = response.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject result = jsonArray.getJSONObject(i);

                                    String imageUrl = POSTER_URL_BASE + result.getString(POSTER_PATH);
                                    String originalTitle = result.getString(ORIGINAL_TITLE);
                                    String overview = result.getString(OVERVIEW);
                                    int voteAverage = result.getInt(VOTE_AVERAGE);
                                    String releaseDate = result.getString(RELEASE_DATE);
                                    int id = result.getInt(ID);

                                    mRvMainItemList.add(new RvMainItem(imageUrl, originalTitle, overview, voteAverage, releaseDate, id));

                                }

                                mRvMainItemAdapter = new RvMainItemAdapter(MainActivity.this, mRvMainItemList);
                                mRecyclerView.setAdapter(mRvMainItemAdapter);
                                mRvMainItemAdapter.setOnItemClickListener(MainActivity.this);

                            } catch (JSONException e) {
                                e.printStackTrace();

                                Log.v(TAG, "volley problem");
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

        Intent detailIntent = new Intent(this, DetailActivity.class);
        detailIntent.putExtra(KEY_MOVIE, mRvMainItemList.get(position));
        startActivity(detailIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        mFavoriteMenuItem = menu.findItem(R.id.menu_sort_favorite);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mMainViewModel.optionsItem = item;
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
            case R.id.menu_sort_favorite:
                getSupportActionBar().setTitle("Favorite Movies");
                parseFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void parseFavorite(){

        mRvMainItemList = new ArrayList<>();
        mMainViewModel.getRvMainItems().observe(this, new Observer<List<RvMainItem>>() {
            @Override
            public void onChanged(@Nullable List<RvMainItem> rvMainItems) {
                mRvMainItemList =  rvMainItems;
                if (mMainViewModel.optionsItem == mFavoriteMenuItem){
                    mRvMainItemAdapter = new RvMainItemAdapter(MainActivity.this, mRvMainItemList);
                    mRecyclerView.setAdapter(mRvMainItemAdapter);
                    mRvMainItemAdapter.setOnItemClickListener(MainActivity.this);
                }

            }
        });
        mRvMainItemAdapter = new RvMainItemAdapter(MainActivity.this, mRvMainItemList);
        mRecyclerView.setAdapter(mRvMainItemAdapter);
        mRvMainItemAdapter.setOnItemClickListener(MainActivity.this);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

