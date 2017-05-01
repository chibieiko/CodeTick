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
import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.Listeners.OnActivitySummaryLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.CustomDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends Fragment implements OnActivitySummaryLoadedListener {

    private final String TAG = this.getClass().getName();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static ActivityHandler activityHandler;
    private RecyclerView recyclerView;
    private View rootView;
    private ActivityAdapter activityAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivitySummary activitySummary;

    public ActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ActivityFragment newInstance(int sectionNumber, ApiHandler apiHandler) {
        activityHandler = new ActivityHandler(apiHandler);
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activityHandler.setActivitySummaryListener(this);

        rootView = inflater.inflate(R.layout.fragment_activity, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.activity_recycler_view);

        // Defines where to show the refresh icon.
        swipeRefreshLayout = (SwipeRefreshLayout) rootView;
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
        activityAdapter = new ActivityAdapter(activitySummary);

        recyclerView.setAdapter(activityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(
                ContextCompat.getDrawable(getContext(), R.drawable.item_decorator)));

        return rootView;
    }

    @Override
    public void onActivitySummaryLoadedSuccessfully(ActivitySummary obj) {
        // Set values from server.
        activitySummary.setAverage(obj.getAverage());
        activitySummary.setTotal(obj.getTotal());
        activitySummary.setDaySummaryList(obj.getDaySummaryList());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityAdapter.notifyDataSetChanged();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onActivitySummaryLoadError(String error) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "Error getting data from Wakatime's server... Try again later", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}