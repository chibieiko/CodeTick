package com.sankari.erika.codetick.Activities;

import android.os.Bundle;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

public class MainActivity extends BaseActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private final String TAG = this.getClass().getName();
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate", "ON MAIN ACTIVITY CREATE", 5);

        ApiHandler handler = new ApiHandler(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), handler);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up tabs for ViewPager.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseActivity.mainActivityVisible = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        BaseActivity.mainActivityVisible = true;
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
