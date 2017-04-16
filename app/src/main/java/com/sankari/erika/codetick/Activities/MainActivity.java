package com.sankari.erika.codetick.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.sankari.erika.codetick.ApiHandler;
import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Fragments.SectionsPagerAdapter;
import com.sankari.erika.codetick.Fragments.TodayFragment;
import com.sankari.erika.codetick.Listeners.OnDataLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.DownloadAndPlaceImage;
import com.sankari.erika.codetick.Utils.Util;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnDataLoadedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        System.out.println("ON MAIN ACTIVITY CREATE");
        handler = new ApiHandler(this);
        handler.addListener(this);
        handler.getUserDetails("https://wakatime.com/api/v1/users/current");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("MAIN ON START");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_today, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Close navigation drawer with back button if it is open.
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
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
    public void onDataSuccessfullyLoaded(Object obj) {
        final User user = (User) obj;
        System.out.println("MAIN: USER IS: " + user);
        final TextView userName = (TextView) findViewById(R.id.username);
        final TextView userEmail = (TextView) findViewById(R.id.user_email);

        new DownloadAndPlaceImage((ImageView) findViewById(R.id.user_image)).execute(user.getPhoto());

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userName.setText(user.getName());
                userEmail.setText(user.getEmail());
            }
        });
    }

    @Override
    public void onDataLoadError(String error) {
        System.out.println("MAIN: USER ERROR IS: " + error);
    }
}
