package com.sankari.erika.codetick.Fragments;

/**
 * Created by erika on 4/16/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        System.out.println("TAB POSITION: " + position);
        switch (position) {
            case 0:
                return TodayFragment.newInstance(position);
            case 1:
                return ProjectsFragment.newInstance(position);
            case 2:
                return ActivityFragment.newInstance(position);
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
