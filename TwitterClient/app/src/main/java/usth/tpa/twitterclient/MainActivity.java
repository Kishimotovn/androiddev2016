package usth.tpa.twitterclient;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import usth.tpa.twitterclient.Fragments.SmartFragmentStatePagerAdapter;
import usth.tpa.twitterclient.Fragments.UserTimeLineFragment;

public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    private String userName = "";
    private TabPagerAdapter adapterViewPager;
    private ViewPager viewPager;
    MaterialTabHost tabHost;

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

        for (int i = 0; i < adapterViewPager.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText("pro")
                            .setTabListener(this)
            );
        }
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

    public static class TabPagerAdapter extends SmartFragmentStatePagerAdapter<UserTimeLineFragment> {
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
        public UserTimeLineFragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return new UserTimeLineFragment();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return new UserTimeLineFragment();
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
