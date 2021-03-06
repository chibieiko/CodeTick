package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.LeaderboardItem;
import com.sankari.erika.codetick.Listeners.OnLeaderboardDataLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fetches leaderboard data from Wakatime's server.
 * <p>
 * Passes data onwards through OnLeaderboardDataLoadedListener.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class LeaderboardHandler {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Used to pass leaderboard data onwards.
     */
    private OnLeaderboardDataLoadedListener leaderboardListener;

    /**
     * Api handler instance.
     */
    private ApiHandler apiHandler;

    /**
     * Receives the api handler.
     *
     * @param apiHandler api handler
     */
    public LeaderboardHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    /**
     * Sets the leaderboard listener.
     *
     * @param leaderboardListener leaderboard listener
     */
    public void setLeaderboardListener(OnLeaderboardDataLoadedListener leaderboardListener) {
        this.leaderboardListener = leaderboardListener;
    }

    /**
     * Tries to fetch leaderboard data.
     * <p>
     * On success creates a leaderboard list containing leaderboard items and calls
     * leaderboard listener's onLeaderboardDataSuccessfullyLoaded method. On error calls
     * onLeaderboardDataLoadError.
     */
    public void getLeaderboardData() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL + "/leaders").newBuilder();
        String url = urlBuilder.build().toString();

        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    leaderboardListener.onLeaderboardDataLoadError(apiHandler.getContext().getResources().getString(R.string.error_connecting));
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();

                    Debug.print(TAG, "getLeaderboardData:onResponse", result, 6);
                    Debug.print(TAG, "getLeaderboardData:onResponse", "code: " + response.code(), 6);

                    if (response.code() == 200) {
                        List<LeaderboardItem> leaderboardList = new ArrayList<>();
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONArray data = new JSONArray(resultObject.getString("data"));

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject tempObj = data.getJSONObject(i);
                                JSONObject runningTotal = tempObj.getJSONObject("running_total");
                                JSONArray languagesArray = runningTotal.getJSONArray("languages");
                                JSONObject userObj = tempObj.getJSONObject("user");

                                LeaderboardItem leaderboardItem = new LeaderboardItem();
                                leaderboardItem.setRank(tempObj.getInt("rank"));
                                leaderboardItem.setName(userObj.getString("display_name"));
                                leaderboardItem.setTotal(runningTotal.getLong("total_seconds"));
                                leaderboardItem.setAverage(runningTotal.getLong("daily_average"));
                                ArrayList<String> languages = new ArrayList<>();
                                for (int j = 0; j < languagesArray.length(); j++) {
                                    JSONObject tempLangObj = languagesArray.getJSONObject(j);
                                    languages.add(tempLangObj.getString("name"));
                                }

                                leaderboardItem.setLanguages(languages);
                                leaderboardList.add(leaderboardItem);
                            }

                            Debug.print(TAG, "onResponse", "LEADERBOARD DETAILS READY", 6);
                            leaderboardListener.onLeaderboardDataSuccessfullyLoaded(leaderboardList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        leaderboardListener.onLeaderboardDataLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
                    }
                }
            });

        } else {
            leaderboardListener.onLeaderboardDataLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
