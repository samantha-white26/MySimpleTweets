package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    //The attributes I want to store...
    public String body;
    public long uid; //database id for the tweet
    public User user;
    public String createAt;

    //deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        //create tweet object
        Tweet tweet = new Tweet();

        //extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;
    }

}
