package com.sankari.erika.codetick.Fragments;

/**
 * Created by erika on 4/16/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final String TAG = this.getClass().getName();
    private ApiHandler apiHandler;

    public SectionsPagerAdapter(FragmentManager fm, ApiHandler handler) {
        super(fm);
        apiHandler = handler;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        System.out.println("TAB POSITION: " + position);
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

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TODAY";
            case 1:
                return "PROJECTS";
            case 2:
                return "ACTIVITY";
        }
        return null;
    }
}
