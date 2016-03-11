package usth.tpa.twitterclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

//    private static final String TWITTER_KEY = "xUTgR6354Q5u2c8REbyG6gcjw";
//    private static final String TWITTER_SECRET = "rjgGNbZ49zxRivVwd0SMJAkkmsLsXXZesvykHvcDlAlYTeRCc6";

    private static final String TWITTER_KEY = "kZcCTzsuVujOYUfAX46RO4ckR";
    private static final String TWITTER_SECRET = "ToZTn1CbWU6i0RtQEApBHPgq4oH4kcByDgoaZmumPJjCfxTUEp";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE_URL = "image_url";

    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_login);
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
                String msg = "TwitterKit" + " Login with Twitter failure! (#" + exception + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void login(Result<TwitterSession> result) {

        //Creating a twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String username = session.getUserName();

        //This code will fetch the profile image URL
        //Getting the account service of the user logged in
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void failure(TwitterException e) {
                        //If any error occurs handle it here
                    }

                    @Override
                    public void success(Result<User> userResult) {
                        //If it succeeds creating a User object from userResult.data
                        User user = userResult.data;

                        //Getting the profile image url
                        String profileImage = user.profileImageUrl.replace("_normal", "");

                        //Creating an Intent
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        //Adding the values to intent
                        intent.putExtra(KEY_USERNAME,username);
                        intent.putExtra(KEY_PROFILE_IMAGE_URL, profileImage);

                        //Starting intent
                        startActivity(intent);
                    }
                });
    }

}
