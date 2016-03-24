package usth.tpa.twitterclient.Fragments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import usth.tpa.twitterclient.MainActivity;
import usth.tpa.twitterclient.R;
import usth.tpa.twitterclient.Models.Tweet;
import usth.tpa.twitterclient.Models.TweetAdapter;
import usth.tpa.twitterclient.Helpers.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

/**
 *  This activity is used to display a list of tweets
 */

public class TweetsFragment extends Fragment {

    private ListView listView;
    private LinearLayout ll;
    private View footerView;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;

    private TextView pDialog;
    private MyResultReceiver mResultReceiver;
    Activity mAct;

    String searchValue;

    String latesttweetid;
    String perpage = "15";

    String menu;

    Boolean initialload = true;
    Boolean isLoading = true;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ll = (LinearLayout) inflater.inflate(R.layout.fragment_list, container, false);

        listView = (ListView) ll.findViewById(R.id.list);
        footerView = (TextView) ll.findViewById(R.id.LoadingTextView);

        listView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (tweetAdapter == null)
                    return;

                if (tweetAdapter.getCount() == 0)
                    return;

                int l = visibleItemCount + firstVisibleItem;
                if (l >= totalItemCount && !isLoading) {
                    // It is time to add new data. We call the listener
                    isLoading = true;
                    new SearchTweetsTask().execute(searchValue);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });

        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext());

        // Add the list fragment's content view to the SwipeRefreshLayout, making sure that it fills
        // the SwipeRefreshLayout
        mSwipeRefreshLayout.addView(ll,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Make sure that the SwipeRefreshLayout will fill the fragment
        mSwipeRefreshLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        // Now return the SwipeRefreshLayout as this fragment's content view

        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading){
                    initialload = true;
                    isLoading = true;
                    latesttweetid = null;
                    tweets.clear();
                    listView.setAdapter(null);
                    new SearchTweetsTask().execute(searchValue);
                } else {
                    Toast.makeText(mAct, getString(R.string.already_loading), Toast.LENGTH_LONG).show();
                }
            }
        });

        mResultReceiver = new MyResultReceiver();

        return mSwipeRefreshLayout;
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    public static TweetsFragment newInstance(String data) {
        TweetsFragment fragment = new TweetsFragment();
        fragment.searchValue = data;
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        mAct = getActivity();
        new SearchTweetsTask().execute(searchValue);
    }

    private class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout {

        public ListFragmentSwipeRefreshLayout(Context context) {
            super(context);
        }

        /**
         * As mentioned above, we need to override this method to properly signal when a
         * 'swipe-to-refresh' is possible.
         *
         * @return true if the {@link android.widget.ListView} is visible and can scroll up.
         */
        @Override
        public boolean canChildScrollUp() {
//            final ListView listView = getListView();
            if (listView.getVisibility() == View.VISIBLE) {
                return canListViewScrollUp(listView);
            } else {
                return false;
            }
        }

    }


    public class MyResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
                // success
                if (!isLoading){
                    initialload = true;
                    isLoading = true;
                    latesttweetid = null;
                    tweets.clear();
                    listView.setAdapter(null);
                    new SearchTweetsTask().execute(searchValue);
                } else {
                    Toast.makeText(mAct, getString(R.string.already_loading), Toast.LENGTH_LONG).show();
                }
            } else {
                // failure
                Toast.makeText(getActivity(), "Nope, not tweeted shit cuz of shitty network!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean canListViewScrollUp(ListView listView) {
        if (android.os.Build.VERSION.SDK_INT >= 14) {
            // For ICS and above we can call canScrollVertically() to determine this
            return ViewCompat.canScrollVertically(listView, -1);
        } else {
            // Pre-ICS we need to manually check the first visible item and the child view's top
            // value
            return listView.getChildCount() > 0 &&
                    (listView.getFirstVisiblePosition() > 0
                            || listView.getChildAt(0).getTop() < listView.getPaddingTop());
        }
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.refresh:
//                if (!isLoading){
//                    initialload = true;
//                    isLoading = true;
//                    latesttweetid = null;
//                    tweets.clear();
//                    listView.setAdapter(null);
//                    new SearchTweetsTask().execute(searchValue);
//                } else {
//                    Toast.makeText(mAct, getString(R.string.already_loading), Toast.LENGTH_LONG).show();
//                }
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
    public void setRefreshing(boolean refreshing) {
    mSwipeRefreshLayout.setRefreshing(refreshing);
}

    public void updateList() {
        if (initialload){
            tweetAdapter = new TweetAdapter(mAct, R.layout.fragment_tweets_row, tweets);
            listView.setAdapter(tweetAdapter);
            initialload = false;
        } else {
            tweetAdapter.addAll(tweets);
            tweetAdapter.notifyDataSetChanged();
        }
        isLoading = false;
        setRefreshing(false);
    }

    //Connect to twitter api and get values.
    private class SearchTweetsTask extends AsyncTask<String, Void, Void>{

        private String URL_VALUE;
        private final String URL_BASE = "https://api.twitter.com";
        private final String URL_TIMELINE = URL_BASE + "/1.1/statuses/user_timeline.json?count="+perpage+"&screen_name=";
        private final String URL_SEARCH = URL_BASE + "/1.1/search/tweets.json?count="+perpage+"&q=";
        private final String URL_PARAM = "&max_id=";
        private final String URL_AUTH = URL_BASE + "/oauth2/token";

        private final String CONSUMER_KEY = getResources().getString(R.string.twitter_api_consumer_key);;
        private final String CONSUMER_SECRET = getResources().getString(R.string.twitter_api_consumer_secret_key);;

        private String authenticateApp(){

            HttpURLConnection connection = null;
            OutputStream os = null;
            BufferedReader br = null;
            StringBuilder reply = null;

            try {
                URL url = new URL(URL_AUTH);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);

                // Encoding keys
                String credentials = CONSUMER_KEY + ":" + CONSUMER_SECRET;
                String authorisation = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                String parameter = "grant_type=client_credentials";

                // Sending credentials
                connection.addRequestProperty("Authorization", authorisation);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                connection.connect();

                // sending parameters to method
                os = connection.getOutputStream();
                os.write(parameter.getBytes());
                os.flush();
                os.close();

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                reply = new StringBuilder();

                while ((line = br.readLine()) != null){
                    reply.append(line);
                }

                Log.d("Post response", String.valueOf(connection.getResponseCode()));
                Log.d("Json access token", reply.toString());

            } catch (Exception e) {
                Log.e("INFO", "Exception: " + Log.getStackTraceString(e));

            }finally{
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return reply.toString();
        }


        //Showing the progressdialog while loading data in background
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            if (initialload){
                pDialog = (TextView) ll.findViewById(R.id.LoadingTextView);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    listView.addFooterView(footerView);
                }
            } else {
                listView.addFooterView(footerView);
            }
        }



        //Get the latest tweets from the timeline of the user
        @Override
        protected Void doInBackground(String... param) {

            String searchValue = param[0];
            Boolean search = false;
            if (searchValue.startsWith("?")){
                URL_VALUE = URL_SEARCH;
                try {
                    searchValue = URLEncoder.encode(searchValue.substring(1), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                search = true;
            } else {
                URL_VALUE = URL_TIMELINE;
            }
            tweets = new ArrayList<Tweet>();
            HttpURLConnection connection = null;
            BufferedReader br = null;

            try {
                URL url;
                if (null != latesttweetid && latesttweetid != "") {
                    Long fromid = Long.parseLong(latesttweetid) - 1;
                    url = new URL(URL_VALUE + searchValue + URL_PARAM + Long.toString(fromid));
                }else {
                    url = new URL(URL_VALUE + searchValue);
                }
                Log.v("INFO", "Requesting: " + url.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                String jsonString = authenticateApp();
                JSONObject jsonAccess = new JSONObject(jsonString);
                String tokenHolder = jsonAccess.getString("token_type") + " " +
                        jsonAccess.getString("access_token");

                connection.setRequestProperty("Authorization", tokenHolder);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();

                // retrieve tweets from api
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder reply = new StringBuilder();

                while ((line = br.readLine()) != null){
                    reply.append(line);
                }

                Log.d("GET response", String.valueOf(connection.getResponseCode()));
                Log.d("JSON response", reply.toString());

                JSONArray jsonArray;
                JSONObject jsonObject;

                if (search){
                    JSONObject obj = new JSONObject(reply.toString());
                    jsonArray = obj.getJSONArray("statuses");
                } else {
                    jsonArray = new JSONArray(reply.toString());
                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject = (JSONObject) jsonArray.get(i);
                    Tweet tweet = new Tweet();

                    tweet.setname(jsonObject.getJSONObject("user").getString("name"));
                    tweet.setusername(jsonObject.getJSONObject("user").getString("screen_name"));
                    tweet.seturlProfileImage(jsonObject.getJSONObject("user").getString("profile_image_url"));
                    tweet.setmessage(jsonObject.getString("text"));
                    tweet.setRetweetCount(jsonObject.getInt("retweet_count"));
                    tweet.setData(jsonObject.getString("created_at"));
                    tweet.setTweetId(jsonObject.getString("id"));

                    try {
                        if (jsonObject.has("extended_entities")){
                            String mediaurl = ((JSONObject) jsonObject.getJSONObject("extended_entities").getJSONArray("media").get(0)).getString("media_url");
                            if (((JSONObject) jsonObject.getJSONObject("extended_entities").getJSONArray("media").get(0)).getString("type").equalsIgnoreCase("photo"))
                                tweet.setImageUrl(mediaurl);
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }


                    latesttweetid = jsonObject.getString("id");

                    tweets.add(i, tweet);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("INFO", "Exception: GET " + Log.getStackTraceString(e));

            }finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        //Populate listview with tweets after background task has been completed. If results are empty
        //then show error toast.
        @Override
        protected void onPostExecute(Void result){
            if (null != tweets && !tweets.isEmpty()) {
                updateList();
            } else {
                if (initialload == true){
                    Helper.noConnection(mAct, true);
                }
            }
            if (pDialog.getVisibility() == View.VISIBLE) {
                pDialog.setVisibility(View.GONE);
                Helper.revealView(listView,ll);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    listView.removeFooterView(footerView);
                }

            } else {
                listView.removeFooterView(footerView);
            }
        }

    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.refresh_menu, menu);
//    }


}
