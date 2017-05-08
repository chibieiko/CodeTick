package com.sankari.erika.codetick.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
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

public class LeaderboardActivity extends BaseActivity implements OnLeaderboardDataLoadedListener{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private List<LeaderboardItem> leaderboardList = new ArrayList<>();
    private List<LeaderboardItem> originalList = new ArrayList<>();
    private LeaderboardAdapter leaderboardAdapter;
    private NavigationView navigationView;
    private SearchView searchView;
    private boolean hasSearched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ApiHandler apiHandler = new ApiHandler(this);
        final LeaderboardHandler leaderboardHandler = new LeaderboardHandler(apiHandler);
        leaderboardHandler.setLeaderboardListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.leaderboard_recycler_view);

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

    @Override
    public void onLeaderboardDataSuccessfullyLoaded(List<LeaderboardItem> newLeaderboardList) {
        leaderboardList.clear();
        leaderboardList.addAll(newLeaderboardList);
        originalList.clear();
        originalList.addAll(newLeaderboardList);

        System.out.println("LIST SIZE::::::" + leaderboardList.size());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                leaderboardAdapter.notifyDataSetChanged();
                if (swipeRefresh.isRefreshing()) {
                    swipeRefresh.setRefreshing(false);
                }
            }
        });
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu. Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_today, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search users");

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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        BaseActivity.leaderboardVisible = true;
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaseActivity.leaderboardVisible = false;
    }
}
