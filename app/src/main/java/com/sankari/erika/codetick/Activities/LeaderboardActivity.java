package com.sankari.erika.codetick.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;

import com.sankari.erika.codetick.Adapters.LeaderboardAdapter;
import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.LeaderboardHandler;
import com.sankari.erika.codetick.Classes.LeaderboardItem;
import com.sankari.erika.codetick.Listeners.OnLeaderboardDataLoadedListener;
import com.sankari.erika.codetick.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Showcases leaderboard list.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class LeaderboardActivity extends BaseActivity implements OnLeaderboardDataLoadedListener {

    /**
     * A swipe refresh layout.
     */
    private SwipeRefreshLayout swipeRefresh;

    /**
     * List containing all leaderboard items, gets modified during search.
     */
    private List<LeaderboardItem> leaderboardList = new ArrayList<>();

    /**
     * List that contains all leaderboard items, does not get modified during search.
     */
    private List<LeaderboardItem> originalList = new ArrayList<>();

    /**
     * A recyclerview adapter that handles ui updates.
     */
    private LeaderboardAdapter leaderboardAdapter;

    /**
     * A navigation view containing navigation drawer.
     */
    private NavigationView navigationView;

    /**
     * Indicates whether user has searched or not.
     */
    private boolean hasSearched = false;

    /**
     * Creates a recycler view and gets leaderboard data.
     *
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ApiHandler apiHandler = new ApiHandler(this);
        final LeaderboardHandler leaderboardHandler = new LeaderboardHandler(apiHandler);
        leaderboardHandler.setLeaderboardListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.leaderboard_recycler_view);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.activity_leaderboard);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leaderboardHandler.getLeaderboardData();
            }
        });

        swipeRefresh.setRefreshing(true);
        leaderboardHandler.getLeaderboardData();

        leaderboardAdapter = new LeaderboardAdapter(leaderboardList);
        recyclerView.setAdapter(leaderboardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration
                (recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    /**
     * Updates UI with leaderboard data from Wakatime's server.
     *
     * @param newLeaderboardList contains a list of leaderboard items fresh from the Wakatime server
     */
    @Override
    public void onLeaderboardDataSuccessfullyLoaded(List<LeaderboardItem> newLeaderboardList) {
        leaderboardList.clear();
        leaderboardList.addAll(newLeaderboardList);
        originalList.clear();
        originalList.addAll(newLeaderboardList);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }

                leaderboardAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Shows snackbar with error.
     * <p>
     * Only called if there is an error fetching leaderboard data from Wakatime's server.
     *
     * @param error describes the error
     */
    @Override
    public void onLeaderboardDataLoadError(String error) {
        final String reason = error;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }

                Snackbar.make(findViewById(R.id.drawer_layout), reason, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Adds search view to action bar.
     *
     * @param menu options menu
     * @return a call to super
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu. Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.leaderboard_activity_search_hint));

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
                    leaderboardList.clear();
                    for (LeaderboardItem leaderboardItem : originalList) {
                        if (leaderboardItem.getName().toLowerCase().contains(newText.toLowerCase())) {
                            leaderboardList.add(leaderboardItem);
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            leaderboardAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    returnListToOriginalState();
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Returns leaderboard listing to original state after searching.
     */
    private void returnListToOriginalState() {
        hasSearched = false;
        leaderboardList.clear();
        leaderboardList.addAll(originalList);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                leaderboardAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Sets leaderboard item in navigation view as checked.
     * <p>
     * Also indicates to base activity that leaderboard activity is now visible.
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        BaseActivity.leaderboardVisible = true;
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    /**
     * Indicates to base activity that leaderboard activity is not visible anymore.
     */
    @Override
    protected void onPause() {
        super.onPause();
        BaseActivity.leaderboardVisible = false;
    }
}
