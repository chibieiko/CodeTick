package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.LeaderboardItem;

import java.util.List;

/**
 * Listens for when leaderboard data has loaded.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public interface OnLeaderboardDataLoadedListener {

    /**
     * Provides leaderboard list.
     *
     * @param leaderboardList list containing leaderboard items
     */
    void onLeaderboardDataSuccessfullyLoaded(List<LeaderboardItem> leaderboardList);

    /**
     * Provides error message.
     *
     * @param error error message
     */
    void onLeaderboardDataLoadError(String error);
}
