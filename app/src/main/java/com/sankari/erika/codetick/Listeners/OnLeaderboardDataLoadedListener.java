package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.LeaderboardItem;

import java.util.List;

/**
 * Created by erika on 5/3/2017.
 */

public interface OnLeaderboardDataLoadedListener {
    void onLeaderboardDataSuccessfullyLoaded(List<LeaderboardItem> leaderboardList);
    void onLeaderboardDataLoadError(String error);
}
