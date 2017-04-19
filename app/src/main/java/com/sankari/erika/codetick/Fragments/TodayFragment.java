package com.sankari.erika.codetick.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.TodayHandler;
import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Views.TodayAdapter;

import java.util.ArrayList;

/**
 * Created by erika on 4/16/2017.
 */

public class TodayFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ArrayList<User> users = new ArrayList<>();
    private static TodayHandler todayHandler;

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
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById (R.id.today_stats);

        todayHandler.getTodayDetails();

        TodayAdapter todayAdapter = new TodayAdapter(rootView.getContext(), users);

        recyclerView.setAdapter(todayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        return rootView;
    }
}
