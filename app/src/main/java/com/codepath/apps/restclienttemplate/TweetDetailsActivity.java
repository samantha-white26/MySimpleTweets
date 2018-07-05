package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvDUsername) TextView tvDUsername;
    @BindView(R.id.tvDHandle) TextView tvDHandle;
    @BindView(R.id.tvDBody) TextView tvDBody;
    @BindView(R.id.ivProfileImg) ImageView ivProfileImg;
    @BindView(R.id.tvDTimestamp) TextView tvDTimestamp;

    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        ButterKnife.bind(this);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweetDetails"));

        tvDHandle.setText("@"+tweet.user.screenName);
        tvDUsername.setText(tweet.user.name);
        tvDBody.setText(tweet.body);
        //static??
        tvDTimestamp.setText(TweetAdapter.getRelativeTimeAgo(tweet.createAt));

        GlideApp.with(this)
                .load(tweet.user.profileImgUrl)
                //.transform(new RoundedCornersTransformation(25, 10))
                .into(ivProfileImg);





    }
}




//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movie_details);
//
//        ButterKnife.bind(this);
//        //intialize client
//        client = new AsyncHttpClient();
//
////        tvTitle = (TextView) findViewById(R.id.tvTitle);
////        tvOverview = (TextView) findViewById(R.id.tvOverview);
////        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
//
//        //unwrap the movie passed from the intent using its simple key name as the key and then use this specific movie
//        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
//        config = Parcels.unwrap(getIntent().getParcelableExtra("image_poster"));
//
//        //log error with movie unwrapping
//        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
//
//        //set title and overview
//        tvTitle.setText(movie.getTitle());
//        tvOverview.setText(movie.getOverview());
//
//        //vote average is 1-10 so we need to divide by 2
//        float voteAverage = movie.getVoteAverage().floatValue();
//        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
//
//
//
//        //  boolean isPortrait =  context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
//
//
//        //build url for poster image
//        String imageUrl = null;
//
//        //If in portrait mode, load the poster image
////        if (isPortrait) {
////            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
////        } else {
////            //landscape
////            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
////        }
////
////        //get the correct placeholder and imageview for the current orientation
////        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
////        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;
//
//
//        //load image using glide
//        GlideApp.with(this)
//                .load(config.getImageUrl(config.getPosterSize(), movie.getPosterPath()))
//                .transform(new RoundedCornersTransformation(25, 10))
//                .placeholder(R.drawable.flicks_movie_placeholder)
//                .error(R.drawable.flicks_movie_placeholder)
//                .into(ivYoutube);
//
//
//        getReviews();
//
//
//
//
//    }