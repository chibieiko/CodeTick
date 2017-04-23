package com.sankari.erika.codetick.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.UserHandler;
import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Fragments.SectionsPagerAdapter;
import com.sankari.erika.codetick.Listeners.OnUserDataLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.DownloadAndPlaceImage;
import com.sankari.erika.codetick.Utils.Urls;
import com.sankari.erika.codetick.Utils.Util;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnUserDataLoadedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private DrawerLayout drawer;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ApiHandler handler;
    private UserHandler userHandler;
    private User user;
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate", "ON MAIN ACTIVITY CREATE", 5);

        handler = new ApiHandler(this);
        userHandler = new UserHandler(handler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), handler);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up tabs for ViewPager.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Set up navigation drawer.
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MAIN ON START");
        if (userHandler.getUserListener() == null) {
            userHandler.addUserListener(this);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (userHandler.getUserListener() == null) {
            userHandler.addUserListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (userHandler.getUserListener() != null) {
            userHandler.addUserListener(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userHandler.getUserListener() != null) {
            userHandler.addUserListener(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu. Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_today, menu);
        if (user == null) {
            userHandler.addUserListener(this);
            userHandler.getUserDetails(Urls.BASE_URL + "/users/current");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Close navigation drawer with back button if it is open.
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_today:
                // todo change to today fragment if in leaderboards fragment

                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_leaderboards:
                break;

            case R.id.nav_logout:
                Util.logout(this);
                break;
        }

        return true;
    }

    @Override
    public void onUserDataSuccessfullyLoaded(User obj) {
        user = obj;
        Debug.print(TAG, "onUserDataSuccessfullyLoaded", "USER IS: " + user, 5);

        final TextView userName = (TextView) findViewById(R.id.username);
        final TextView userEmail = (TextView) findViewById(R.id.user_email);
        new DownloadAndPlaceImage((ImageView) findViewById(R.id.user_image)).execute(user.getPhoto());

        this.runOnUiThread(() -> {
            userName.setText(user.getName());
            userEmail.setText(user.getEmail());
        });
    }

    @Override
    public void onUserDataLoadError(String error) {
        final String reason = error;
        this.runOnUiThread(() -> {
            Snackbar.make(findViewById(R.id.drawer_layout), reason, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        Debug.print(TAG, "onUserDataLoadError", error, 5);
    }
}
