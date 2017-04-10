package com.sankari.erika.codetick.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sankari.erika.codetick.R;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    protected Activity loginActivity;

    // UI references.
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginActivity = this;

        checkForWakatimeData();

        setContentView(R.layout.activity_login);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void checkForWakatimeData() {
        System.out.println("KUULIN JOTAIN OLEN SIIS CHECK FOR WAKATIME DATA");
        final Uri data = this.getIntent().getData();
        if (data != null) {
            System.out.println("DATA = " + data);
            System.out.println("SHCEME = " + data.getScheme());
            System.out.println("FRAGMENT = " + data.getFragment());
            System.out.println("PARAMETER = " + data.getQueryParameter("code"));
        }

        if (data != null && data.getScheme().equals("codeticklogin") && data.getQueryParameter("code") != null) {
            final String accessToken = data.getQueryParameter("code").replaceFirst("code=", "");
            if (accessToken != null) {
                System.out.println("ACCESS TOKEN:" + accessToken);
                Intent intent = new Intent(this, TodayActivity.class);
                intent.putExtra("token", accessToken);
                startActivity(intent);
                // handleSignInResult(...);
            } else {
                System.out.println("SAIN PALAUTETTA MUT EN ACCESS TOKENIA");
                System.out.println(accessToken);
                // handleSignInResult(...);
            }
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
                .appendQueryParameter("response_type", "code");
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

