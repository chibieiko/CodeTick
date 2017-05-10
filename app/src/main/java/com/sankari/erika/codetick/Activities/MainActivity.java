package com.sankari.erika.codetick.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.Fragments.SectionsPagerAdapter;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;

/**
 * Contains today, project and activity fragments.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
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

    /**
     * Stores the class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Navigation view containing the navigation drawer.
     */
    private NavigationView navigationView;

    /**
     * Creates section pager adapter, view pager and tab layout.
     *
     * @param savedInstanceState saved instance state
     */
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

    /**
     * Indicates to base activity that main activity is visible now.
     */
    @Override
    protected void onPause() {
        super.onPause();
        BaseActivity.mainActivityVisible = false;
    }

    /**
     * Indicates to base activity that main activity is not visible anymore.
     * <p>
     * In addition sets today item in navigation drawer checked.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        BaseActivity.mainActivityVisible = true;
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
