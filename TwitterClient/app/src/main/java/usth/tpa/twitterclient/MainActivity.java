package usth.tpa.twitterclient;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import usth.tpa.twitterclient.Fragments.SmartFragmentStatePagerAdapter;
import usth.tpa.twitterclient.Fragments.TweetsFragment;
import usth.tpa.twitterclient.Helpers.VolleySingleton;

public class MainActivity extends AppCompatActivity {

    private String userName = "";
    static SwipeRefreshLayout swipeLayout;
    public static String DATA = "transaction_data";

    //Image Loader object
    private ImageLoader imageLoader;

    //NetworkImageView Ojbect
    private NetworkImageView profileImage;
    private NetworkImageView bannerImage;

    //TextView object
    private TextView textViewUsername;
    private ViewPager viewPager;

    private MyPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImage = (NetworkImageView) findViewById(R.id.profileImage);
        textViewUsername = (TextView) findViewById(R.id.userName);
        bannerImage = (NetworkImageView) findViewById(R.id.bannerImage);

        Intent intent = getIntent();

        //Getting values from intent
        userName = intent.getStringExtra(LoginActivity.KEY_USERNAME);
        String profileImageUrl = intent.getStringExtra(LoginActivity.KEY_PROFILE_IMAGE_URL);

        //Loading image
        imageLoader = VolleySingleton.getInstance(this).getImageLoader();
        imageLoader.get(profileImageUrl, ImageLoader.getImageListener(profileImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert), 0, 0, ImageView.ScaleType.CENTER);
        profileImage.setImageUrl(profileImageUrl, imageLoader);

        if (intent.getStringArrayExtra(LoginActivity.KEY_BANNER_IMAGE_URL) != null) {
            String bannerImageURL = intent.getStringExtra(LoginActivity.KEY_BANNER_IMAGE_URL);

            imageLoader.get(bannerImageURL, ImageLoader.getImageListener(bannerImage, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert), 0, 0, ImageView.ScaleType.CENTER);
            bannerImage.setImageUrl(bannerImageURL, imageLoader);
        }

        //Setting the username in textview
        textViewUsername.setText("@" + userName);
        Bundle bundle = new Bundle();
        bundle.putString(DATA, userName);

        viewPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        adapterViewPager.mArguments = bundle;

        viewPager.setAdapter(adapterViewPager);


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
    }

    public String getUserName() {
        return userName;
    }

    public static class MyPagerAdapter extends SmartFragmentStatePagerAdapter<TweetsFragment> {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        private Bundle mArguments;

        public void setArguments(Bundle bundle) {
            mArguments = bundle;
        }

        public Bundle getArguments() {
            return mArguments;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public TweetsFragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    String username = this.getArguments().getString(MainActivity.DATA);
                    return TweetsFragment.newInstance(username);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return TweetsFragment.newInstance("naruto");
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
