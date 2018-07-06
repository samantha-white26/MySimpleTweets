package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    public List<Tweet> mTweets;
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
        holder.tvUsername.setText("@"+tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvCreatedAt.setText(getRelativeTimeAgo(tweet.createAt));

        //glide to put in the image
        GlideApp.with(context).load(tweet.user.profileImgUrl).into(holder.ivProfileImage);

    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
  // create viewholder class

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // the views I want to display
    public ImageView ivProfileImage;
    public TextView tvUsername;
    public TextView tvBody;
    public TextView tvCreatedAt;
    public Button reply;

    // constructor takes in inflated layout ... What is an inflated layout??
    public ViewHolder(View itemView) {
      super(itemView);

      // perform findbiewby id lookups

      ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
      tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
      tvBody = (TextView) itemView.findViewById(R.id.tvDBody);
      tvCreatedAt = (TextView) itemView.findViewById(R.id.tvCreatedAt);
      reply = (Button) itemView.findViewById(R.id.reply);
      reply.setOnClickListener(this);

      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int position = getAdapterPosition();
        Tweet tweet = mTweets.get(position);

        if (position != RecyclerView.NO_POSITION) {
            //If reply button has been clicked
            if (v.getId() == R.id.reply) {
                Intent intent = new Intent(context, ComposeActivity.class);
                intent.putExtra("isReply", true);
                intent.putExtra("username", tweet.user.screenName);
                context.startActivity(intent);

            }
            //anywhere else is clicked in itemview
            else {
                // create an intent for an activity
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                // set up so the information we want to pass will be passed to the new activtiy
                intent.putExtra("tweetDetails", Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        }


    }


    }

}
