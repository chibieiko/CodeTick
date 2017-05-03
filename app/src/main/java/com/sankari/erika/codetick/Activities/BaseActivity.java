package com.sankari.erika.codetick.Activities;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.sankari.erika.codetick.Fragments.SectionsPagerAdapter;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Util;

public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        // Set up navigation drawer.
        //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_base);
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
        System.out.println("pressed menuitem " + item);
        switch (item.getItemId()) {
            case R.id.nav_today:
                // todo change to today fragment if in leaderboards fragment

                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_leaderboards:

                Intent intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_logout:
                Util.logout(this);
                break;
        }

        return true;
    }
}
