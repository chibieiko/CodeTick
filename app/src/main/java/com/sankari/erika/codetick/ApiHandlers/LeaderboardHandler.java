package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.LeaderboardItem;
import com.sankari.erika.codetick.Listeners.OnLeaderboardDataLoadedListener;
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
 * Created by erika on 5/3/2017.
 */

public class LeaderboardHandler {
    private final String TAG = this.getClass().getName();
    private OnLeaderboardDataLoadedListener leaderboardListener;
    private ApiHandler apiHandler;
    private List<LeaderboardItem> leaderboardList = new ArrayList<>();

    public LeaderboardHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    public void setLeaderboardListener(OnLeaderboardDataLoadedListener leaderboardListener) {
        this.leaderboardListener = leaderboardListener;
    }

    public void getLeaderboardData() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL + "/leaders").newBuilder();
        String url = urlBuilder.build().toString();

        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    leaderboardListener.onLeaderboardDataLoadError("Error connecting to Wakatime's server. Try again later");
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();

                    Debug.print(TAG, "getLeaderboardData:onResponse", result, 4);
                    Debug.print(TAG, "getLeaderboardData:onResponse", "code: " + response.code(), 4);

                    if (response.code() == 200) {
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

                            Debug.print(TAG, "onResponse", "LEADERBOARD DETAILS READY", 4);
                            leaderboardListener.onLeaderboardDataSuccessfullyLoaded(leaderboardList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        leaderboardListener.onLeaderboardDataLoadError("Error fetching data from Wakatime's server. Try again later");
                    }
                }
            });

        } else {
            leaderboardListener.onLeaderboardDataLoadError("Error fetching data from Wakatime's server. Try again later");
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
