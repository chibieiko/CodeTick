package com.sankari.erika.codetick.Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.TodayHandler;
import com.sankari.erika.codetick.Classes.Project;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.Listeners.OnTodaySummaryLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Views.TodayAdapter;

import java.util.ArrayList;

/**
 * Created by erika on 4/16/2017.
 */

public class TodayFragment extends android.support.v4.app.Fragment implements OnTodaySummaryLoadedListener {

    private final String TAG = this.getClass().getName();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static TodayHandler todayHandler;
    private RecyclerView recyclerView;
    private View rootView;
    private TodayAdapter todayAdapter;
    private TodaySummary todaySummary = null;
    private SwipeRefreshLayout swipeRefreshLayout;

    public TodayFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TodayFragment newInstance(int sectionNumber, ApiHandler handler) {
        todayHandler = new TodayHandler(handler);
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        todayHandler.setTodayListener(this);

        rootView = inflater.inflate(R.layout.fragment_today, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.today_stats);

        // Defines where to show the refresh icon.
        swipeRefreshLayout = (SwipeRefreshLayout) rootView;
        swipeRefreshLayout.setOnRefreshListener(() -> {
                todayHandler.getTodayDetails();
        });
        swipeRefreshLayout.setRefreshing(true);
        todayHandler.getTodayDetails();

        todaySummary = new TodaySummary();
        todayAdapter = new TodayAdapter(todaySummary);

        recyclerView.setAdapter(todayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return rootView;
    }

    @Override
    public void onTodaySummarySuccessfullyLoaded(TodaySummary obj) {
        // Set values from server.
        todaySummary.setProjectList(obj.getProjectList());
        todaySummary.setTotalTime(obj.getTotalTime());

        getActivity().runOnUiThread(() -> {
            todayAdapter.notifyDataSetChanged();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onTodaySummaryLoadError(String error) {
        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "Error getting data from Wakatime's server... Try again later", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
