package com.sankari.erika.codetick.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    private LeaderboardHandler leaderboardHandler;
    private List<LeaderboardItem> leaderboardList = new ArrayList<>();
    private LeaderboardAdapter leaderboardAdapter;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ApiHandler apiHandler = new ApiHandler(this);
        final LeaderboardHandler leaderboardHandler = new LeaderboardHandler(apiHandler);
        leaderboardHandler.setLeaderboardListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.leaderboard_recycler_view);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.activity_leaderboard);
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
                Snackbar.make(findViewById(R.id.drawer_layout), reason, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
