package usth.tpa.twitterclient;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import usth.tpa.twitterclient.Fragments.CollectionFragment;
import usth.tpa.twitterclient.Fragments.SearchTimeLineFragment;
import usth.tpa.twitterclient.Fragments.SmartFragmentStatePagerAdapter;
import usth.tpa.twitterclient.Fragments.UserTimeLineFragment;

public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    private String userName = "";
    private TabPagerAdapter adapterViewPager;
    private ViewPager viewPager;
    MaterialTabHost tabHost;
    ListFragment currentFragment;
    static SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        //Getting values from intent
        userName = intent.getStringExtra(LoginActivity.KEY_USERNAME);
        String profileImageUrl = intent.getStringExtra(LoginActivity.KEY_PROFILE_IMAGE_URL);

        adapterViewPager = new TabPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);

        viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        tabHost.setSelectedNavigationItem(position);
                    }
                }
        );

        viewPager.setAdapter(adapterViewPager);

        tabHost.addTab(
                tabHost.newTab()
                        .setText(userName)
                        .setTabListener(this)
        );

        tabHost.addTab(
                tabHost.newTab()
                        .setText("search #naruto")
                        .setTabListener(this)
        );

        tabHost.addTab(
                tabHost.newTab()
                        .setText("fabric collection")
                        .setTabListener(this)
        );
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    public String getUserName() {
        return userName;
    }

    public static class TabPagerAdapter extends SmartFragmentStatePagerAdapter<ListFragment> {
        private static int NUM_ITEMS = 3;

        public TabPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public ListFragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    final ListFragment newOne = new UserTimeLineFragment();
                    swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            swipeLayout.setRefreshing(true);

                            ((UserTimeLineFragment)newOne).adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                                @Override
                                public void success(Result<TimelineResult<Tweet>> result) {
                                    swipeLayout.setRefreshing(false);
                                }

                                @Override
                                public void failure(TwitterException exception) {
                                    Log.d("TwitterKit", "Cant refresh", exception);
                                }
                            });
                        }
                    });
                    return newOne;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    final ListFragment newSearchFragment = new SearchTimeLineFragment();
                    swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            swipeLayout.setRefreshing(true);

                            ((SearchTimeLineFragment)newSearchFragment).adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                                @Override
                                public void success(Result<TimelineResult<Tweet>> result) {
                                    swipeLayout.setRefreshing(false);
                                }

                                @Override
                                public void failure(TwitterException exception) {
                                    Log.d("TwitterKit", "Cant refresh", exception);
                                }
                            });
                        }
                    });
                    return newSearchFragment;
                case 2:
                    final ListFragment newCollectionFragment = new CollectionFragment();
                    swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            swipeLayout.setRefreshing(true);

                            ((CollectionFragment)newCollectionFragment).adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                                @Override
                                public void success(Result<TimelineResult<Tweet>> result) {
                                    swipeLayout.setRefreshing(false);
                                }

                                @Override
                                public void failure(TwitterException exception) {
                                    Log.d("TwitterKit", "Cant refresh", exception);
                                }
                            });
                        }
                    });
                    return newCollectionFragment;
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

}
