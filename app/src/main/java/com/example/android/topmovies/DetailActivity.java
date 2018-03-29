package com.example.android.topmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.android.topmovies.MainActivity.EXTRA_IMAGE_URL;
import static com.example.android.topmovies.MainActivity.EXTRA_ORIGINAL_TITLE;
import static com.example.android.topmovies.MainActivity.EXTRA_OVERVIEW;
import static com.example.android.topmovies.MainActivity.EXTRA_RELEASE_DATE;
import static com.example.android.topmovies.MainActivity.EXTRA_VOTE_AVERAGE;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Movie Details");


        Intent intent = getIntent();

        String imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL);
        String originalTitle = intent.getStringExtra(EXTRA_ORIGINAL_TITLE);
        String overview = intent.getStringExtra(EXTRA_OVERVIEW);
        int voteAverage = intent.getIntExtra(EXTRA_VOTE_AVERAGE, 0);
        String releaseDate = intent.getStringExtra(EXTRA_RELEASE_DATE);

        ImageView imageView = findViewById(R.id.image_detail);
        TextView textViewOriginalTitle = findViewById(R.id.text_original_title);
        TextView textViewOverview = findViewById(R.id.text_overview);
        TextView textViewVoteAverage = findViewById(R.id.text_vote_average);
        TextView textViewReleaseDate = findViewById(R.id.text_release_date);


        Picasso.with(this)
                .load(imageUrl)
                .into(imageView);

        textViewOriginalTitle.setText(originalTitle);
        textViewOverview.setText(overview);
        textViewVoteAverage.setText("Vote: " + voteAverage);
        textViewReleaseDate.setText(releaseDate);


    }
}
