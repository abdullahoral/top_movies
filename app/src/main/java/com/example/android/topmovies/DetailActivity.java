package com.example.android.topmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements RvVideosAdapter.OnItemClickListener {

    private static final String TAG = "DetailActivity";

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = BuildConfig.API_KEY;

    public String MOVIE_ID;
    public String VIDEOS_URL;
    public String REVIEWS_URL;

    private static final String VIDEOS_KEY = "key";
    private static final String VIDEOS_NAME = "name";
    private static final String REVIEWS_AUTHOR = "author";
    private static final String REVIEWS_CONTENT = "content";

    private RequestQueue mRequestQueue;

    private EmptyRecyclerView mRvVideos;
    private RvVideosAdapter mRvVideosItemAdapter;
    public List<RvVideosItem> mRvVideosItemList;

    private EmptyRecyclerView mRvReviews;
    private RvReviewsAdapter mRvReviewsItemAdapter;
    public List<RvReviewsItem> mRvReviewsItemList;

    private AppDatabase mDb;
    Button mButton;
    RvMainItem mDbRvMainItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Movie Details");

        mDb = AppDatabase.getsInstance(getApplicationContext());

        Intent intent = getIntent();
        final RvMainItem rvMainItemObject = intent.getParcelableExtra("rvMainItemObject");

        String imageUrl = rvMainItemObject.getImageUrl();
        String originalTitle = rvMainItemObject.getOriginalTitle();
        String overview = rvMainItemObject.getOverview();
        int voteAverage = rvMainItemObject.getVoteAverage();
        String releaseDate = rvMainItemObject.getReleaseDate();
        int movieId = rvMainItemObject.getId();
        MOVIE_ID = String.valueOf(movieId);
        VIDEOS_URL = BASE_URL + MOVIE_ID + "/videos?api_key=" + API_KEY;
        REVIEWS_URL = BASE_URL + MOVIE_ID + "/reviews?api_key=" + API_KEY;

        ImageView imageView = findViewById(R.id.image_detail);
        TextView textViewOriginalTitle = findViewById(R.id.text_original_title);
        TextView textViewOverview = findViewById(R.id.text_overview);
        TextView textViewVoteAverage = findViewById(R.id.text_vote_average);
        TextView textViewReleaseDate = findViewById(R.id.text_release_date);

        Picasso.with(this)
                .load(imageUrl)
                .into(imageView);

        textViewOriginalTitle.setText(originalTitle);
        textViewOverview.setText("Overview: " + overview);
        textViewVoteAverage.setText("Vote: " + voteAverage);
        textViewReleaseDate.setText("Release Date: " + releaseDate);

        mButton = findViewById(R.id.button_favorite);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDbRvMainItem = mDb.rvMainItemDao().loadRvMainItemById(rvMainItemObject.getId());

            }
        });



        if (mDbRvMainItem != null) {
            mButton.setText("MARK AS UNFAVORITE");
        }

        mButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mDbRvMainItem == null) {
                            mDb.rvMainItemDao().insertMovie(rvMainItemObject);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mButton.setText("MARK AS UNFAVORITE");
                                }
                            });

                        } else {
                            mDb.rvMainItemDao().deleteMovie(rvMainItemObject);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mButton.setText("MARK AS FAVORITE");
                                }
                            });
                        }
                    }
                });

            }
        });

        parseVideosJson();
        parseReviewsJson();

    }
    private void parseVideosJson() {

        if (isOnline()) {

            mRvVideos = findViewById(R.id.rv_videos);
            mRvVideos.setHasFixedSize(true);
            mRvVideos.setLayoutManager(new LinearLayoutManager(this));
            mRvVideos.setEmptyView(findViewById(R.id.empty_videos));

            mRvVideosItemList = new ArrayList<>();

            mRequestQueue = Volley.newRequestQueue(this);

            final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

            JsonObjectRequest requestVideos = new JsonObjectRequest(Request.Method.GET, VIDEOS_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                loading.dismiss();
                                JSONArray jsonArray = response.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject result = jsonArray.getJSONObject(i);

                                    String videosKey = result.getString(VIDEOS_KEY);
                                    String videosName = result.getString(VIDEOS_NAME);

                                    mRvVideosItemList.add(new RvVideosItem(videosKey, videosName));
                                }


                                mRvVideosItemAdapter = new RvVideosAdapter(DetailActivity.this, mRvVideosItemList);
                                mRvVideos.setAdapter(mRvVideosItemAdapter);
                                mRvVideosItemAdapter.notifyDataSetChanged();
                                mRvVideosItemAdapter.setOnItemClickListener(DetailActivity.this);

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

            mRequestQueue.add(requestVideos);

        } else {
            getSupportActionBar().setTitle("You Have No Connection!");

        }

    }

    private void parseReviewsJson() {

        if (isOnline()) {
            mRvReviews = findViewById(R.id.rv_reviews);
            mRvReviews.setHasFixedSize(true);
            mRvReviews.setLayoutManager(new LinearLayoutManager(this));
            mRvReviews.setEmptyView(findViewById(R.id.empty_reviews));

            mRvReviewsItemList = new ArrayList<>();

            mRequestQueue = Volley.newRequestQueue(this);

            final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

            JsonObjectRequest requestReviews = new JsonObjectRequest(Request.Method.GET, REVIEWS_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                loading.dismiss();
                                JSONArray jsonArray = response.getJSONArray("results");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject result = jsonArray.getJSONObject(i);

                                    String reviewsAuthor = result.getString(REVIEWS_AUTHOR);
                                    String reviewsContent = result.getString(REVIEWS_CONTENT);

                                    mRvReviewsItemList.add(new RvReviewsItem(reviewsAuthor, reviewsContent));
                                }

                                mRvReviewsItemAdapter = new RvReviewsAdapter(DetailActivity.this, mRvReviewsItemList);
                                mRvReviews.setAdapter(mRvReviewsItemAdapter);
                                mRvReviewsItemAdapter.notifyDataSetChanged();


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

            mRequestQueue.add(requestReviews);


        } else {
            getSupportActionBar().setTitle("You Have No Connection!");
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    @Override
    public void onItemClick(int position) {

        RvVideosItem clickedItem = mRvVideosItemList.get(position);
        String clickedVideosKey = clickedItem.getmVideoKey();
        String clickedVideosUrl = "https://m.youtube.com/watch?v=" + clickedVideosKey;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(clickedVideosUrl)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDbRvMainItem != null) {
            mButton.setText("MARK AS UNFAVORITE");
        }

    }
}
