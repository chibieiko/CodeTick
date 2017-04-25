package com.sankari.erika.codetick.Fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sankari.erika.codetick.Adapters.ProjectAdapter;
import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.ProjectHandler;
import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.Listeners.OnProjectListLoadedListener;
import com.sankari.erika.codetick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectsFragment extends android.support.v4.app.Fragment implements OnProjectListLoadedListener {

    private final String TAG = this.getClass().getName();

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static ProjectHandler projectHandler;
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayoutProjects;
    private ProjectAdapter projectAdapter;
    private RecyclerView recyclerView;
    private List<ProjectListItem> projectList = new ArrayList<>();

    // Required empty constructor.
    public ProjectsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ProjectsFragment newInstance(int sectionNumber, ApiHandler handler) {
        projectHandler = new ProjectHandler(handler);
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        System.out.println("MINUT LUOTIIN");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        projectHandler.setProjectListLoadedListener(this);

        rootView = inflater.inflate(R.layout.fragment_projects, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.project_recycler_view);

        swipeRefreshLayoutProjects = (SwipeRefreshLayout) rootView;
        swipeRefreshLayoutProjects.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                projectHandler.getProjectListing();
            }
        });
        swipeRefreshLayoutProjects.setRefreshing(true);
        projectHandler.getProjectListing();

        projectAdapter = new ProjectAdapter(projectList);
        recyclerView.setAdapter(projectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration
                (recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onProjectListSuccessfullyLoaded(List<ProjectListItem> projects) {
        System.out.println("GOT IT");
        projectList.clear();
        // projectList.addAll(projects);

        for (ProjectListItem projectListItem : projects) {
            projectList.add(projectListItem);
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                projectAdapter.notifyDataSetChanged();
                if (swipeRefreshLayoutProjects.isRefreshing()) {
                    swipeRefreshLayoutProjects.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onProjectListLoadError(String error) {
        Snackbar.make(getActivity().findViewById(R.id.drawer_layout),
                "Error getting data from Wakatime's server... Try again later",
                Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
