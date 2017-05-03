package com.sankari.erika.codetick.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sankari.erika.codetick.Adapters.ProjectAdapter;
import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.ProjectHandler;
import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.Listeners.OnProjectListLoadedListener;
import com.sankari.erika.codetick.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private SearchView searchView;
    private boolean hasSearched = false;

    private List<ProjectListItem> projectList = new ArrayList<>();
    private List<ProjectListItem> originalProjectList = new ArrayList<>();

    // Required empty constructor.
    public ProjectsFragment() {}

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

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflates the menu. Adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_today, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {}

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (hasSearched) {
                    returnListToOriginalState();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.equals("")) {
                    hasSearched = true;
                    projectList.clear();
                    for (ProjectListItem projectListItem : originalProjectList) {
                        if (projectListItem.getName().toLowerCase().contains(newText.toLowerCase())) {
                            projectList.add(projectListItem);
                        }
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            projectAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    returnListToOriginalState();
                }

                return false;
            }
        });
    }

    private void returnListToOriginalState() {
        hasSearched = false;
        projectList.clear();
        projectList.addAll(originalProjectList);
        Collections.sort(projectList, new Comparator<ProjectListItem>() {
            @Override
            public int compare(ProjectListItem o1, ProjectListItem o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                projectAdapter.notifyDataSetChanged();
            }
        });
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

        // Inflate the layout for this fragment.
        return rootView;
    }

    @Override
    public void onProjectListSuccessfullyLoaded(List<ProjectListItem> projects) {
        projectList.clear();
        projectList.addAll(projects);
        originalProjectList.clear();
        originalProjectList.addAll(projects);

        Collections.sort(projectList, new Comparator<ProjectListItem>() {
            @Override
            public int compare(ProjectListItem o1, ProjectListItem o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });

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
