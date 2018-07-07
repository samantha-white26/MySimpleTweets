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
/*
    Shows information about a tweet in it's own page
    Displays the username, when it was posted, the image of the twitter user,
    and the body of the text
    also users can favorite and retweet this specific tweet from this page
 */
public class TweetDetailsActivity extends AppCompatActivity {

    //bind xmlviews to java objects
    @BindView(R.id.tvDUsername) TextView tvDUsername;
    @BindView(R.id.tvDHandle) TextView tvDHandle;
    @BindView(R.id.tvDBody) TextView tvDBody;
    @BindView(R.id.ivProfileImg) ImageView ivProfileImg;
    @BindView(R.id.tvDTimestamp) TextView tvDTimestamp;
    @BindView(R.id.favorite) Button favorite;

    //define global variables
    Tweet tweet;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        ButterKnife.bind(this);
        //access tweet passed to this activity..extract tweet
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweetDetails"));
        //set text fields and image from tweet
        tvDHandle.setText("@"+tweet.user.screenName);
        tvDUsername.setText(tweet.user.name);
        tvDBody.setText(tweet.body);
        tvDTimestamp.setText(TweetAdapter.getRelativeTimeAgo(tweet.createAt));
        //display image
        GlideApp.with(this)
                .load(tweet.user.profileImgUrl)
                .transforms(new CenterCrop(), new RoundedCorners(15))
                .into(ivProfileImg);


    }

    //when favorite button is clicked
    public void onFavorite(View v){
        client = TwitterApplication.getRestClient(this);

    //call favorite in twitter client...posts info that thus tweet has been favorited by the user
        client.favorite(tweet.uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try{
                    tweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //change the favorite icon to indicate the user has favorited this tweet
                favorite.setBackgroundResource(R.drawable.ic_vector_heart);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(TweetDetailsActivity.this, "failure connecting to favoriting tweet", Toast.LENGTH_LONG).show();

            }
        });
    }



    //retweet button in lower right corner pressed
    public void onRetweet(View v) {
        client = TwitterApplication.getRestClient(this);

        //twitter client called to retweet a specific tweet
        //following Twitter Api retweet call
        client.reTweet(tweet.uid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess (int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try{
                    tweet = Tweet.fromJSON(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //alert user
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



