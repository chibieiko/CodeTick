package com.sankari.erika.codetick.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.R;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    /**
     * Api handler.
     */
    private ApiHandler apiHandler;

    /**
     * Sets api handler.
     *
     * @param fm      fragment manager
     * @param handler api handler
     */
    public SectionsPagerAdapter(FragmentManager fm, ApiHandler handler) {
        super(fm);
        apiHandler = handler;
    }

    /**
     * Instantiates the fragment for the given page.
     *
     * @param position tab page number
     * @return a placeholderFragment
     */
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return TodayFragment.newInstance(position, apiHandler);
            case 1:
                return ProjectsFragment.newInstance(position, apiHandler);
            case 2:
                return ActivityFragment.newInstance(position, apiHandler);
        }

        return null;
    }

    /**
     * Gets tab count.
     *
     * @return number of tab pages
     */
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    /**
     * Gets tab page's title.
     *
     * @param position tab page number
     * @return tab page's title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return apiHandler.getContext().getString(R.string.tab_today);
            case 1:
                return apiHandler.getContext().getString(R.string.tab_projects);
            case 2:
                return apiHandler.getContext().getString(R.string.tab_activity);
        }
        return null;
    }
}
