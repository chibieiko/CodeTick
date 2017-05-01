package com.sankari.erika.codetick.Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.TodayHandler;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.Listeners.OnTodaySummaryLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Adapters.TodayAdapter;
import com.sankari.erika.codetick.Utils.CustomDividerItemDecoration;

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
        System.out.println("TODAY HANDLER: " + todayHandler);
        if (todayHandler == null) {
            todayHandler = new TodayHandler(handler);
        }

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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.today_recycler_view);

        // Defines where to show the refresh icon.
        swipeRefreshLayout = (SwipeRefreshLayout) rootView;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                todayHandler.getTodayDetails();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        todayHandler.getTodayDetails();

        todaySummary = new TodaySummary();
        todayAdapter = new TodayAdapter(todaySummary);

        recyclerView.setAdapter(todayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(
                ContextCompat.getDrawable(getContext(), R.drawable.item_decorator)));

        return rootView;
    }

    @Override
    public void onTodaySummarySuccessfullyLoaded(TodaySummary obj) {
        // Set values from server.
        todaySummary.setTodayProjectList(obj.getTodayProjectList());
        todaySummary.setTotalTime(obj.getTotalTime());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                todayAdapter.notifyDataSetChanged();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onTodaySummaryLoadError(String error) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "Error getting data from Wakatime's server... Try again later", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
