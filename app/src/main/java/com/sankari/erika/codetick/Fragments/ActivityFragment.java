package com.sankari.erika.codetick.Fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sankari.erika.codetick.Adapters.ActivityAdapter;
import com.sankari.erika.codetick.ApiHandlers.ActivityHandler;
import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.Classes.ActivitySummary;
import com.sankari.erika.codetick.Classes.DaySummary;
import com.sankari.erika.codetick.Listeners.OnActivitySummaryLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.CustomDividerItemDecoration;

import java.util.ArrayList;

/**
 * Displays user's coding activity for the past two weeks.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ActivityFragment extends Fragment implements OnActivitySummaryLoadedListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Used to fetch activity data from Wakatime's server.
     */
    private static ActivityHandler activityHandler;

    /**
     * Handles UI updates for the recycler view.
     */
    private ActivityAdapter activityAdapter;

    /**
     * Swipe refresh layout.
     */
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Holds activity summary data.
     */
    private ActivitySummary activitySummary;

    /**
     * Required empty constructor.
     */
    public ActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     *
     * @param sectionNumber section number
     * @param apiHandler api handler
     * @return activity fragment instance
     */
    public static ActivityFragment newInstance(int sectionNumber, ApiHandler apiHandler) {
        activityHandler = new ActivityHandler(apiHandler);
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates the recycler view and gets activity summary data.
     *
     * @param inflater           used to inflate the view
     * @param container          view group
     * @param savedInstanceState saved instance state
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activityHandler.setActivitySummaryListener(this);

        View rootView = inflater.inflate(R.layout.fragment_activity, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.activity_recycler_view);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                activityHandler.getActivitySummary();
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        activityHandler.getActivitySummary();

        activitySummary = new ActivitySummary();
        ArrayList<DaySummary> days = new ArrayList<>();
        activitySummary.setDaySummaryList(days);
        activityAdapter = new ActivityAdapter(activitySummary, getContext());

        recyclerView.setAdapter(activityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(
                ContextCompat.getDrawable(getContext(), R.drawable.item_decorator), getContext(), true));

        return rootView;
    }

    /**
     * Updates UI with activity summary data from Wakatime's server.
     *
     * @param obj activity summary object
     */
    @Override
    public void onActivitySummaryLoadedSuccessfully(ActivitySummary obj) {
        // Set values from server.
        activitySummary.setAverage(obj.getAverage());
        activitySummary.setTotal(obj.getTotal());
        activitySummary.setDaySummaryList(obj.getDaySummaryList());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                activityAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Shows snackbar with error.
     * <p>
     * Only called if there is an error fetching data from Wakatime's server.
     *
     * @param error describes the error
     */
    @Override
    public void onActivitySummaryLoadError(String error) {
        final String message = error;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Clears swipe refresh layout on pause to prevent fragment overlapping.
     */
    @Override
    public void onPause() {
        super.onPause();

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.clearAnimation();
        }
    }
}