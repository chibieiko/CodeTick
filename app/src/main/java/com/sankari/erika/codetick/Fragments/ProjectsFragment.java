package com.sankari.erika.codetick.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.sankari.erika.codetick.Utils.CustomDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Displays all the projects user has ever logged time on in Wakatime.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ProjectsFragment extends android.support.v4.app.Fragment implements OnProjectListLoadedListener {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Used to fetch project data from Wakatime's server.
     */
    private static ProjectHandler projectHandler;

    /**
     * Swipe refresh layout.
     */
    private SwipeRefreshLayout swipeRefresh;

    /**
     * Handles UI updates for the recycler view.
     */
    private ProjectAdapter projectAdapter;

    /**
     * Indicates whether user has searched projects or not.
     */
    private boolean hasSearched = false;

    /**
     * List containing all project list items, gets modified during search.
     */
    private List<ProjectListItem> projectList = new ArrayList<>();

    /**
     * List that contains all project list items, does not get modified during search.
     */
    private List<ProjectListItem> originalProjectList = new ArrayList<>();

    /**
     * Required empty constructor.
     */
    public ProjectsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     *
     * @param sectionNumber section number
     * @param handler       api handler
     * @return project fragment instance
     */
    public static ProjectsFragment newInstance(int sectionNumber, ApiHandler handler) {
        projectHandler = new ProjectHandler(handler);
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Sets fragment to have options menu.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
    }

    /**
     * Adds search view to action bar.
     *
     * @param menu     menu
     * @param inflater used to inflate the menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflates the menu. Adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.project_fragment_search_hint));

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                swipeRefresh.setEnabled(false);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                swipeRefresh.setEnabled(true);
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

    /**
     * Returns project listing to original state after searching.
     */
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

    /**
     * Creates the recycler view and gets project list.
     *
     * @param inflater           used to inflate the view
     * @param container          view group
     * @param savedInstanceState saved instance state
     * @return inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        projectHandler.setProjectListLoadedListener(this);

        View rootView = inflater.inflate(R.layout.fragment_projects, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.project_recycler_view);

        swipeRefresh = (SwipeRefreshLayout) rootView;
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                projectHandler.getProjectListing();
            }
        });
        swipeRefresh.setRefreshing(true);
        projectHandler.getProjectListing();

        projectAdapter = new ProjectAdapter(projectList);
        recyclerView.setAdapter(projectAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(
                ContextCompat.getDrawable(getContext(), R.drawable.item_decorator), getContext(), false));

        // Inflate the layout for this fragment.
        return rootView;
    }

    /**
     * Updates UI with project list from Wakatime's server.
     *
     * @param projects project list
     */
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
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }

                projectAdapter.notifyDataSetChanged();
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
    public void onProjectListLoadError(String error) {
        final String message = error;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
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

        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(false);
            swipeRefresh.destroyDrawingCache();
            swipeRefresh.clearAnimation();
        }
    }
}
