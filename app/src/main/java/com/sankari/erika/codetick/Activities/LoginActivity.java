package com.sankari.erika.codetick.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Debug.loadDebug(this);

        checkForWakatimeData();
        checkForExistingToken();

        setContentView(R.layout.activity_login);

        Button wakatimeSignInButton = (Button) findViewById(R.id.wakatime_sign_in_button);
        wakatimeSignInButton.setOnClickListener((View view) -> {
            attemptLogin();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForWakatimeData();
    }

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
            }
        }
    }

    private void checkForWakatimeData() {
        final Uri data = this.getIntent().getData();

        if (data != null && data.getScheme().equals("codeticklogin") && data.getFragment() != null) {
            Token token = Util.parseTokenUrl(data.getFragment());

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
                        "Error, please try login again",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

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
}

