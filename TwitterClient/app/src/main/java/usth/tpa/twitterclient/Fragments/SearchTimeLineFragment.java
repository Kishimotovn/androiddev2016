//package usth.tpa.twitterclient.Fragments;
//
//import android.os.Bundle;
//import android.support.v4.app.ListFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.SearchView;
//
//import com.twitter.sdk.android.tweetui.SearchTimeline;
//import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
//
//import usth.tpa.twitterclient.MainActivity;
//import usth.tpa.twitterclient.R;
//
//public class SearchTimeLineFragment extends ListFragment {
//
//    public TweetTimelineListAdapter adapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        MainActivity activity = (MainActivity) getActivity();
//
//
//        final SearchTimeline userTimeline = new SearchTimeline.Builder()
//                .query("#naruto")
//                .build();
//         adapter = new TweetTimelineListAdapter.Builder(activity)
//                .setTimeline(userTimeline)
//                .build();
//        setListAdapter(adapter);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_user_time_line, container, false);
//    }
//}
