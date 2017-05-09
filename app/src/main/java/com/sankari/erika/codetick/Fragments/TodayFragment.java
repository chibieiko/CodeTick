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

import com.sankari.erika.codetick.Adapters.TodayAdapter;
import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.TodayHandler;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.Listeners.OnTodaySummaryLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.CustomDividerItemDecoration;

/**
 * Displays user's coding activity for today.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class TodayFragment extends android.support.v4.app.Fragment implements OnTodaySummaryLoadedListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Used to fetch today data from Wakatime's server.
     */
    private static TodayHandler todayHandler;

    /**
     * Handles UI updates for the recycler view.
     */
    private TodayAdapter todayAdapter;

    /**
     * Holds today summary data.
     */
    private TodaySummary todaySummary = null;

    /**
     * Swipe refresh layout.
     */
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Required empty constructor.
     */
    public TodayFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     *
     * @param sectionNumber section number
     * @param handler       api handler
     * @return today fragment
     */
    public static TodayFragment newInstance(int sectionNumber, ApiHandler handler) {
        if (todayHandler == null) {
            todayHandler = new TodayHandler(handler);
        }

        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates the recycler view and gets today's data.
     *
     * @param inflater           used to inflate the view
     * @param container          view group
     * @param savedInstanceState saved instance state
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        todayHandler.setTodayListener(this);

        View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.today_recycler_view);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView;
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                todayHandler.getTodayDetails();
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        todayHandler.getTodayDetails();

        todaySummary = new TodaySummary();
        todayAdapter = new TodayAdapter(todaySummary, getContext());

        recyclerView.setAdapter(todayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(
                ContextCompat.getDrawable(getContext(), R.drawable.item_decorator), getContext(), true));

        return rootView;
    }

    /**
     * Updates UI with today data from Wakatime's server.
     *
     * @param obj today object
     */
    @Override
    public void onTodaySummarySuccessfullyLoaded(TodaySummary obj) {
        // Set values from server.
        todaySummary.setTodayProjectList(obj.getTodayProjectList());
        todaySummary.setTotalTime(obj.getTotalTime());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                todayAdapter.notifyDataSetChanged();
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
    public void onTodaySummaryLoadError(String error) {
        final String message = error;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                Snackbar.make(getActivity().findViewById(R.id.drawer_layout),
                        message,
                        Snackbar.LENGTH_LONG)
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
