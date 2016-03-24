//package usth.tpa.twitterclient.Fragments;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.ListFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.twitter.sdk.android.tweetui.CollectionTimeline;
//import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
//
//import usth.tpa.twitterclient.MainActivity;
//import usth.tpa.twitterclient.R;
//
//
//public class CollectionFragment extends ListFragment {
//    public TweetTimelineListAdapter adapter;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Collection "Fabric Picks"
//        final CollectionTimeline timeline = new CollectionTimeline.Builder()
//                .id(569961150045896704L)
//                .build();
//        adapter = new TweetTimelineListAdapter.Builder(getActivity())
//                .setTimeline(timeline)
//                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
//                .build();
//        setListAdapter(adapter);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_collection, container, false);
//    }
//}
