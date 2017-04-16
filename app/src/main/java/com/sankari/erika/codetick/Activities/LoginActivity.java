package com.sankari.erika.codetick.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sankari.erika.codetick.R;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkForWakatimeData();

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

    private void checkForWakatimeData() {
        final Uri data = this.getIntent().getData();
        System.out.println("DATA = " + data);
        if (data != null) {
            System.out.println("SCHEME = " + data.getScheme());
            System.out.println("FRAGMENT = " + data.getFragment());
        }

        if (data != null && data.getScheme().equals("codeticklogin") && data.getFragment() != null) {

            String token = null;
            String expires = null;
            String refresh_token = null;
            String[] reply = data.getFragment().split("&");
            for (String replyContent : reply) {
                String[] replyContentParts = replyContent.split("=");
                if (replyContentParts[0].equals("access_token")) {
                    token = replyContentParts[1];
                } else if (replyContentParts[0].equals("refresh_token")) {
                    refresh_token = replyContentParts[1];
                } else if (replyContentParts[0].equals("expires_in")) {
                    expires = replyContentParts[1];
                }
            }

            System.out.println("TOKEN: " + token);
            System.out.println("EXPIRES: " + expires);
            System.out.println("REFRESH_TOKEN: " + refresh_token);

            if (token != null && refresh_token != null && expires != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("expires", expires);
                intent.putExtra("refresh_token", refresh_token);
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

