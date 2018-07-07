package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TweetDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tvDUsername) TextView tvDUsername;
    @BindView(R.id.tvDHandle) TextView tvDHandle;
    @BindView(R.id.tvDBody) TextView tvDBody;
    @BindView(R.id.ivProfileImg) ImageView ivProfileImg;
    @BindView(R.id.tvDTimestamp) TextView tvDTimestamp;
    @BindView(R.id.favorite) Button favorite;


    Tweet tweet;
    private TwitterClient client;

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
                .transforms(new CenterCrop(), new RoundedCorners(15))
                .into(ivProfileImg);


    }

    public void onFavorite(View v){
        client = TwitterApplication.getRestClient(this);


        client.favorite(tweet.uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try{
                    tweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                favorite.setBackgroundResource(R.drawable.ic_vector_heart);
                //Toast.makeText(TweetDetailsActivity.this, "success connecting to favoriting tweet", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(TweetDetailsActivity.this, "failure connecting to favoriting tweet", Toast.LENGTH_LONG).show();

            }
        });
    }




    public void onRetweet(View v) {
        client = TwitterApplication.getRestClient(this);


        client.reTweet(tweet.uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try{
                    tweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(TweetDetailsActivity.this, "ReTweeted", Toast.LENGTH_LONG).show();



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(TweetDetailsActivity.this, "failure connecting to sending tweet", Toast.LENGTH_LONG).show();

            }
        });
    }


}



