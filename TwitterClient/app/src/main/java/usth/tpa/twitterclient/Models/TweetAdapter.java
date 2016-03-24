package usth.tpa.twitterclient.Models;

/**
 * Created by Kishi-Air on 3/23/16.
 */
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import usth.tpa.twitterclient.R;
import usth.tpa.twitterclient.Helpers.Helper;
//import usth.tpa.twitterclient.Helpers.MediaActivity;
import usth.tpa.twitterclient.Helpers.WebHelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class TweetAdapter  extends ArrayAdapter<Tweet> {

    private Context context;
    private ArrayList<Tweet> tweets;
    private DisplayImageOptions options;
    ImageLoader imageLoader;

    public TweetAdapter(Context context, int viewResourceId, ArrayList<Tweet> tweets) {
        super(context, viewResourceId, tweets);
        this.context = context;
        this.tweets = tweets;
        imageLoader = Helper.initializeImageLoader(context);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.placeholder)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public View getView(int posicao, View view, ViewGroup parent){
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_tweets_row, parent, false);
        }

        final Tweet tweet = tweets.get(posicao);

        if (tweet != null) {

            TextView name = (TextView) view.findViewById(R.id.name);
            TextView username = (TextView) view.findViewById(R.id.username);
            ImageView imagem = (ImageView) view.findViewById(R.id.profile_image);
            ImageView photo = (ImageView) view.findViewById(R.id.photo);
            TextView message = (TextView) view.findViewById(R.id.message);
            TextView retweetCount = (TextView) view.findViewById(R.id.retweet_count);
            TextView date = (TextView) view.findViewById(R.id.date);

            name.setText(tweet.getname());
            username.setText("@" + tweet.getusername());
            date.setText(tweet.getData());
            message.setText(Html.fromHtml(tweet.getmessage()));
            message.setTextSize(TypedValue.COMPLEX_UNIT_SP, WebHelper.getTextViewFontSize(context));
            retweetCount.setText(Helper.formatValue(tweet.getRetweetCount()));
            imageLoader.displayImage(tweet.geturlProfileImage(), imagem);

            if (tweet.getImageUrl() != null){
                photo.setVisibility(View.VISIBLE);
                imageLoader.displayImage(tweet.getImageUrl(), photo, options);

                photo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
//
//                        Intent commentIntent = new Intent(context, MediaActivity.class);
//                        commentIntent.putExtra(MediaActivity.TYPE, MediaActivity.TYPE_IMG);
//                        commentIntent.putExtra(MediaActivity.URL, tweet.getImageUrl());
//                        context.startActivity(commentIntent);

                    }
                });
            } else {
                photo.setImageDrawable(null);
                photo.setVisibility(View.GONE);
            }

            view.findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    String link = ("http://twitter.com/" + tweet.getusername() + "/status/" + tweet.getTweetId());
                    final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
                    context.startActivity(intent);
                }
            });


        }

        return view;
    }
}