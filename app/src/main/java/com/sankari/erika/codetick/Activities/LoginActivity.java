package com.sankari.erika.codetick.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sankari.erika.codetick.ApiHandler;
import com.sankari.erika.codetick.Classes.Token;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Util;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // PreferenceManager.getDefaultSharedPreferences(this).edit().clear().commit();

        checkForWakatimeData();
        checkForExistingToken();

        setContentView(R.layout.activity_login);

        Button wakatimeSignInButton = (Button) findViewById(R.id.wakatime_sign_in_button);
        wakatimeSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForWakatimeData();
    }

    private void checkForExistingToken() {
        System.out.println("CHECKING FOR TOKEN");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = prefs.getString("token", null);
        long expires = prefs.getLong("expires", 0);
        String refreshToken = prefs.getString("refreshToken", null);

        System.out.println("Token: " + accessToken);
        System.out.println("Expiry: " + new Date(expires));
        System.out.println("Refresh: " + refreshToken);

        if (accessToken != null && expires != 0 && refreshToken != null) {
            Date today = new Date();
            // If token has not expired yet.
            if (today.getTime() < expires) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                // If token has expired get a new one with refresh token.
            } else {
                ApiHandler handler = new ApiHandler(this);
                handler.refreshToken(refreshToken, true);
            }
        }
    }

    private void checkForWakatimeData() {
        final Uri data = this.getIntent().getData();
        System.out.println("DATA = " + data);
        if (data != null) {
            System.out.println("SCHEME = " + data.getScheme());
            System.out.println("FRAGMENT = " + data.getFragment());
        }

        if (data != null && data.getScheme().equals("codeticklogin") && data.getFragment() != null) {

            Token token = Util.parseTokenUrl(data.getFragment());

            System.out.println("TOKEN: " + token.getAccessToken());
            System.out.println("EXPIRES: " + new Date(token.getExpires()));
            System.out.println("REFRESH_TOKEN: " + token.getRefreshToken());

            if (token.getAccessToken() != null && token.getRefreshToken() != null && token.getExpires() != 0) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                prefs.edit().putString("token", token.getAccessToken()).apply();
                prefs.edit().putString("refreshToken", token.getRefreshToken()).apply();
                prefs.edit().putLong("expires", token.getExpires()).apply();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                // todo show snackbar
                System.out.println("ERROR missing token parts");
            }
        } else {
            // todo show snackbar
            System.out.println("ERROR in login");
        }
    }

    private void attemptLogin() {
        final Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https")
                .authority("wakatime.com")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", "0drYEiKHnVnUgwxY7N8dY4Hs")
                .appendQueryParameter("scope", "email, read_logged_time, read_stats")
                .appendQueryParameter("redirect_uri", "codeticklogin://redirect")
                .appendQueryParameter("response_type", "token");
        final Intent browser = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
        startActivity(browser);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

