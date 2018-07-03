package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;

    //pass in the tweets array into the construction
    //declare our constructor
    //This is confusing to me
    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;
    }

    //for each row, inflate the layout and pass into view holder class


    //invoked/created when need to make a new row
    //in other cases as user scrolls down the user, on bindview holder gets called
    //inflates the layout and stores the findbyid viewholders

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout that we created
        //get context
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        //inflate the tweet row
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;

    }

    //bind the values based on the position of the element... position used to look up which tweet to
    //look up and populate
    //Where does this position come from?
    //passes the position of a previously cached viewholder and repopulates
    //based on the position... but again what is this position?

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //which tweet object to show based on position
        Tweet tweet = mTweets.get(position);
        Log.i("awesometest", "position:" + position);

        //populate the views according to the data above
        //right now just textview
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);

        //glide to put in the image
        GlideApp.with(context).load(tweet.user.profileImgUrl).into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }
//create viewholder class

    public static class ViewHolder extends RecyclerView.ViewHolder{
        //the views I want to display
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;


        //constructor takes in inflated layout ... What is an inflated layout??
        public ViewHolder (View itemView){
            super(itemView);

            //perform findbiewby id lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);


        }
    }
}
