package com.sankari.erika.codetick.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.Classes.Token;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * Enables users to login to the app with their Wakatime account.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Stores the class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Checks if activity has received sign in data from Wakatime and if token exists.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Debug.loadDebug(this);

        checkForWakatimeData();
        checkForExistingToken();

        setContentView(R.layout.activity_login);

        Button wakatimeSignInButton = (Button) findViewById(R.id.wakatime_sign_in_button);
        wakatimeSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    /**
     * Checks if activity has received sign in data from Wakatime.
     */
    @Override
    protected void onStart() {
        super.onStart();
        checkForWakatimeData();
    }

    /**
     * Checks if an access token exists for the user in app preferences.
     * <p>
     * Starts main activity if user has an access token. If access token has expired, tries to
     * refresh token. If refresh token is invalid, prompts user to login again.
     */
    private void checkForExistingToken() {
        Debug.print(TAG, "checkForExistingToken", "CHECKING FOR TOKEN", 5);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = prefs.getString("token", null);
        long expires = prefs.getLong("expires", 0);
        String refreshToken = prefs.getString("refreshToken", null);

        Debug.print(TAG, "checkForExistingToken", "TOKEN: " + accessToken, 1);
        Debug.print(TAG, "checkForExistingToken", "EXPIRY: " + new Date(expires).toString(), 1);
        Debug.print(TAG, "checkForExistingToken", "REFRESH_TOKEN: " + refreshToken, 1);

        if (accessToken != null && expires != 0 && refreshToken != null) {
            Date today = new Date();
            // If token has not expired yet.
            if (today.getTime() < expires) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                // If refresh token is not invalid and token has expired gets a new token with refresh token.
            } else if (!Token.isInvalidRefreshToken()) {
                ApiHandler handler = new ApiHandler(this);
                handler.refreshToken(refreshToken, true);
            } else {
                Snackbar.make(findViewById(R.id.wakatime_sign_in_button),
                        R.string.login_activity_error_refreshing_token,
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    /**
     * Saves token to app preferences.
     */
    private void checkForWakatimeData() {
        final Uri data = this.getIntent().getData();

        if (data != null && data.getScheme().equals("codeticklogin") && data.getFragment() != null) {
            Token token = Util.parseTokenUrl(data.getFragment());

            System.out.println(token.getAccessToken());
            Debug.print(TAG, "checkForWakatimeData", "TOKEN: " + token.getAccessToken(), 1);
            Debug.print(TAG, "checkForWakatimeData", "EXPIRY: " + new Date(token.getExpires()).toString(), 1);
            Debug.print(TAG, "checkForWakatimeData", "REFRESH_TOKEN: " + token.getRefreshToken(), 1);

            if (token.getAccessToken() != null && token.getRefreshToken() != null && token.getExpires() != 0) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().putString("token", token.getAccessToken()).apply();
                prefs.edit().putString("refreshToken", token.getRefreshToken()).apply();
                prefs.edit().putLong("expires", token.getExpires()).apply();

                Token.setInvalidRefreshToken(false);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(R.id.wakatime_sign_in_button),
                        R.string.login_activity_error_logging_in,
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    /**
     * Logs user in to Wakatime utilising OAuth 2.0.
     */
    private void attemptLogin() {
        Properties config = new Properties();
        String appId = "";
        String redirectUri = "";

        // Loads app information from file.
        try {
            config.load(this.getAssets().open("config.properties"));
            appId = config.getProperty("id");
            redirectUri = config.getProperty("redirectUri");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority("wakatime.com")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", appId)
                .appendQueryParameter("scope", "email, read_logged_time, read_stats")
                .appendQueryParameter("redirect_uri", redirectUri)
                .appendQueryParameter("response_type", "token");
        final Intent browser = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
        startActivity(browser);
    }

    /**
     * Closes application on back button press.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

