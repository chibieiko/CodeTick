package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.TodayProject;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.Listeners.OnTodaySummaryLoadedListener;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Urls;
import com.sankari.erika.codetick.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by erika on 4/19/2017.
 */

public class TodayHandler {

    private final String TAG = this.getClass().getName();
    private OnTodaySummaryLoadedListener todayListener;
    private ApiHandler apiHandler;

    public TodayHandler(ApiHandler handler) {
        this.apiHandler = handler;
    }

    public OnTodaySummaryLoadedListener getTodayListener() {
        return todayListener;
    }

    public void setTodayListener(OnTodaySummaryLoadedListener todayListener) {
        this.todayListener = todayListener;
    }

    public void getTodayDetails() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL +"/users/current/summaries").newBuilder();
        urlBuilder.addQueryParameter("start", Util.convertDateToProperFormat(new Date()));
        urlBuilder.addQueryParameter("end", Util.convertDateToProperFormat(new Date()));
        String url = urlBuilder.build().toString();

        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    System.out.println(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();

                    Debug.print(TAG, "onResponse", result, 6);
                    Debug.print(TAG, "onResponse", "code: " + response.code(), 6);

                    if (response.code() == 200) {
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONArray summaryArray = new JSONArray(resultObject.getString("data"));
                            JSONArray projectsArray = null;

                            for (int i = 0; i < summaryArray.length(); i++) {
                                JSONObject obj = summaryArray.getJSONObject(0);
                                projectsArray = obj.getJSONArray("projects");
                            }

                            ArrayList<TodayProject> todayProjects = new ArrayList<>();
                            long totalTimeToday = 0;

                            for (int i = 0; i < projectsArray.length(); i++) {
                                JSONObject tempObject = projectsArray.getJSONObject(i);
                                todayProjects.add(new TodayProject(
                                        tempObject.getString("name"),
                                        tempObject.getInt("percent"),
                                        tempObject.getInt("hours"),
                                        tempObject.getInt("minutes"),
                                        tempObject.getLong("total_seconds")));
                                totalTimeToday += tempObject.getLong("total_seconds");
                            }

                            TodaySummary todaySummary = new TodaySummary();
                            todaySummary.setTodayProjectList(todayProjects);
                            todaySummary.setTotalTime(totalTimeToday);

                            Debug.print(TAG, "onResponse", "NYT OLIS VALMIS SUMMARY", 5);
                            todayListener.onTodaySummarySuccessfullyLoaded(todaySummary);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        todayListener.onTodaySummaryLoadError("Error fetching today's stats from Wakatime's server...");
                    }
                }
            });
        } else {
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
