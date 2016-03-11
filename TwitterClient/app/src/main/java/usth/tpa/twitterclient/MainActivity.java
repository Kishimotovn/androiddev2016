package usth.tpa.twitterclient;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import usth.tpa.twitterclient.Fragments.SmartFragmentStatePagerAdapter;
import usth.tpa.twitterclient.Fragments.TimeLineFragment;

public class MainActivity extends AppCompatActivity {

    private String userName = "";
    private TabPagerAdapter adapterViewPager;
    private ViewPager Tab;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        //Getting values from intent
        userName = intent.getStringExtra(LoginActivity.KEY_USERNAME);
        String profileImageUrl = intent.getStringExtra(LoginActivity.KEY_PROFILE_IMAGE_URL);

        adapterViewPager = new TabPagerAdapter(getSupportFragmentManager());
        Tab = (ViewPager) findViewById(R.id.pager);

        Tab.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        actionBar = getActionBar();
                        actionBar.setSelectedNavigationItem(position);
                    }
                }
        );

        Tab.setAdapter(adapterViewPager);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                Tab.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                Toast.makeText(getApplicationContext(), "Tab selected", 2000).show();
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        actionBar.addTab(actionBar.newTab().setText("Movie").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Movie").setTabListener(tabListener));
    }

    public String getUserName() {
        return userName;
    }

    public static class TabPagerAdapter extends SmartFragmentStatePagerAdapter<TimeLineFragment> {
        private static int NUM_ITEMS = 2;

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
        public TimeLineFragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new TimeLineFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new TimeLineFragment();
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
