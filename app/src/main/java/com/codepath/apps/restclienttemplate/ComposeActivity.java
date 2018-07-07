package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
/*

    This activity is the activity that allows you to compose a new tweet.
    Text from the edit text field is then passed to twitter client to post
    to your twitterfeed
    This activity is also used to reply to a tweet. If the reply icon is clicked
    on the individual tweet in recycler view this activity is shown with the edit text set to the
    specific user that the tweet is replying or commenting on
 */

public class ComposeActivity extends AppCompatActivity {

  private TwitterClient client;
  Tweet tweet;
  private boolean isReply;
  EditText etCompose;
  String username;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_compose);

      isReply = getIntent().getBooleanExtra("isReply", false);
      username = getIntent().getStringExtra("username");

    if (isReply) {
      // make the text field have the the @username before the text of the reply tweet
      etCompose = (EditText) findViewById(R.id.etCompose);
      etCompose.setText("@"+username);

    }
  }

  public void onSubmit(View v) {
      EditText etCompose = (EditText) findViewById(R.id.etCompose);
      //convert text by the user into a string and pass to ...
      String message = etCompose.getText().toString();
      //sendTweet client call which posts the tweet to twitter API
      //create client class
      client = TwitterApplication.getRestClient(this);

      client.sendTweet(
          message,
          new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              super.onSuccess(statusCode, headers, response);

              try {
                tweet = Tweet.fromJSON(response);

                // Prepare data intent
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("newTweet", Parcels.wrap(tweet));
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for// response
                finish(); // closes the activity, pass data to parent

              } catch (JSONException e) {
                e.printStackTrace();
              }
            }

            @Override
            public void onFailure(
                int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
              super.onFailure(statusCode, headers, throwable, errorResponse);
              Toast.makeText(
                      ComposeActivity.this,
                      "failure connecting to sending tweet",
                      Toast.LENGTH_LONG)
                  .show();
            }
          });

  }
}
