package usth.tpa.twitterclient.Fragments;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import usth.tpa.twitterclient.MainActivity;
import usth.tpa.twitterclient.R;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class UserTimeLineFragment extends ListFragment {
    public TweetTimelineListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();

        String username = activity.getUserName();

        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(username)
                .build();
        adapter = new TweetTimelineListAdapter.Builder(activity)
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_time_line, container, false);
    }
}
