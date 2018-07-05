package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    Tweet tweet;
    TextWatcher tw;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //text watcher
//        tw = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length()<){
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        }


        //EditText etCompose = (EditText) findViewById(R.id.etCompose);
        //String status = etCompose.getText().toString();

        //call it body maybe instead of status
    }

    public void onSubmit(View v) {
        EditText etCompose = (EditText) findViewById(R.id.etCompose);
        String message = etCompose.getText().toString();

        client = TwitterApplication.getRestClient(this);

        client.sendTweet(message, new JsonHttpResponseHandler() {
          @Override
          public void onSuccess (int statusCode, Header[] headers, JSONObject response) {
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


              Toast.makeText(ComposeActivity.this, "successful sending tweet", Toast.LENGTH_LONG).show();



          }

          @Override
          public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
              super.onFailure(statusCode, headers, throwable, errorResponse);
              Toast.makeText(ComposeActivity.this, "failure connecting to sending tweet", Toast.LENGTH_LONG).show();

          }
        });
    }
}
