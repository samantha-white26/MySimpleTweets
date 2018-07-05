package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    //reference to twitter client
    private TwitterClient client;

    //we're going to connect the adapter here

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    private final int REQUEST_CODE = 20;
    private SwipeRefreshLayout swipeContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //get access to twitter client
        client = TwitterApplication.getRestClient(this);

        //find the recyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        //instantiate the arraylist (data source)
        tweets = new ArrayList<>();
        //construct the adapter from this datasource (what does this mean)
        tweetAdapter = new TweetAdapter(tweets);

        //RecyclerView Setup(Layout manager, set the recycler view to use the
        //adapter... connect the recyclerview to the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        populateTimeline();




        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }



    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

                // Remember to CLEAR OUT old items before appending in the new ones
                tweets.clear();
                populateTimeline();
                // ...the data has come back, add new items to your adapter...
                tweetAdapter.addAll(tweets);
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onComposeAction(MenuItem mi) {
        // handle click here
        //Toast.makeText(this, "successfully clicked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras

            Tweet newTweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("newTweet"));
            tweets.add(0, newTweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);


            //Tweet tweet = intent.getExtras

            //String name = .getExtras().getString("name");
            //int code = data.getExtras().getInt("code", 0);
            // Toast the name to display temporarily on screen

        }
    }

    private void populateTimeline() {
    // make network request to get info from twitter api and make an anonymous class to deal with
    // response
    client.getHomeTimeline(
        new JsonHttpResponseHandler() {

          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d("TwitterClient", response.toString());
          }

          @Override
          public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            //   Log.d("TwitterClient", response.toString());

            // iterate through the JSON array
            // for each entry deserialize the JSON object
            for (int i = 0; i < response.length(); i++) {
              // convert each object to a tweet model
                Tweet tweet = null;
                try {
                    tweet = Tweet.fromJSON(response.getJSONObject(i));
                    // add that tweet model to our data source
                    tweets.add(tweet);
                    // notify the adapter that an
                    // item was added
                    tweetAdapter.notifyItemInserted(tweets.size()-1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
          }

          @Override
          public void onFailure(
              int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("TwitterClient", responseString);
            throwable.printStackTrace();
          }

          @Override
          public void onFailure(
              int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.d("TwitterClient", errorResponse.toString());
            throwable.printStackTrace();
          }

          @Override
          public void onFailure(
              int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            Log.d("TwitterClient", errorResponse.toString());
            throwable.printStackTrace();
          }
        });
    }
}
