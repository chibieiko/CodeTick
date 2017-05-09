package com.sankari.erika.codetick.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.UserHandler;
import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Listeners.OnUserDataLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.DownloadAndPlaceImage;
import com.sankari.erika.codetick.Utils.Urls;
import com.sankari.erika.codetick.Utils.Util;

/**
 * Provides a base for all the other activities that use the navigation drawer.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, OnUserDataLoadedListener {

    /**
     * Navigation drawer layout.
     */
    private DrawerLayout drawer;

    /**
     * A handler that fetches user information.
     */
    private UserHandler userHandler;

    /**
     * Contains user's details.
     */
    private User user;

    /**
     * Indicates whether main activity is visible or not.
     */
    public static boolean mainActivityVisible = false;

    /**
     * Indicates whether leaderboard activity is visible or not.
     */
    public static boolean leaderboardVisible = false;

    /**
     * Inflates the layout.
     * <p>
     * Adds a toolbar and navigation drawer to the activity.
     *
     * @param layoutResID layout resource id
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        // Set up navigation drawer.
        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

        FrameLayout activityContainer = (FrameLayout) drawer.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Creates a new user handler.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userHandler = new UserHandler(new ApiHandler(this));
    }

    /**
     * Closes navigation drawer with back button if drawer is open.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Handles navigation drawer's item clicks.
     *
     * @param item menu item
     * @return always true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_today:
                if (mainActivityVisible) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.nav_leaderboards:
                if (leaderboardVisible) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    Intent intent = new Intent(this, LeaderboardActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.nav_logout:
                Util.logout(this);
                break;
        }

        return true;
    }

    /**
     * Gets user's details.
     */
    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("BASE ON START");
        if (userHandler.getUserListener() == null) {
            userHandler.addUserListener(this);
        }

        if (user == null) {
            userHandler.addUserListener(this);
            userHandler.getUserDetails(Urls.BASE_URL + "/users/current");
        }
    }

    /**
     * Adds user listener for user handler.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (userHandler.getUserListener() == null) {
            userHandler.addUserListener(this);
        }
    }

    /**
     * Removes user listener from user handler.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (userHandler.getUserListener() != null) {
            userHandler.addUserListener(null);
        }

    }

    /**
     * Removes user listener from user handler.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (userHandler.getUserListener() != null) {
            userHandler.addUserListener(null);
        }
    }

    /**
     * Sets user data to navigation drawer.
     *
     * @param obj contains user information
     */
    @Override
    public void onUserDataSuccessfullyLoaded(User obj) {
        user = obj;

        final TextView userName = (TextView) findViewById(R.id.username);
        final TextView userEmail = (TextView) findViewById(R.id.user_email);
        new DownloadAndPlaceImage((ImageView) findViewById(R.id.user_image)).execute(user.getPhoto());

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (userName != null && userEmail != null) {
                    userName.setText(user.getName());
                    userEmail.setText(user.getEmail());
                }
            }
        });
    }

    /**
     * Shows snackbar with error.
     * <p>
     * Only called if there is an error fetching user details from Wakatime's server.
     *
     * @param error describes the error
     */
    @Override
    public void onUserDataLoadError(String error) {
        final String reason = error;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.drawer_layout), reason, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
