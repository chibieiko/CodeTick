package com.sankari.erika.codetick.Fragments;

import android.os.Bundle;
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

    public TodayFragment() {}

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
        todayHandler.setTodayListener(TodayFragment.this);

        rootView = inflater.inflate(R.layout.fragment_today, container, false);

        recyclerView = (RecyclerView) rootView.findViewById (R.id.today_stats);
        todayHandler.getTodayDetails();

        todaySummary = new TodaySummary();
        todayAdapter = new TodayAdapter(rootView.getContext(), todaySummary);

        recyclerView.setAdapter(todayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        return rootView;
    }

    @Override
    public void onTodaySummarySuccessfullyLoaded(TodaySummary obj) {
        System.out.println("GETTING FEEDBACK ON SUCCESS");
        System.out.println("total time coding: " + obj.getTotalTime());
        todaySummary.setProjectList(obj.getProjectList());
        todaySummary.setTotalTime(obj.getTotalTime());

        System.out.println(todaySummary);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                todayAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onTodaySummaryLoadError(String error) {
        System.out.println("Error: " + error);
    }
}
