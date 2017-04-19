package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.Project;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.Listeners.OnTodaySummaryLoadedListener;
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
    OnTodaySummaryLoadedListener todayListener;
    ApiHandler apiHandler;

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
        // todo Get today total time and time per project and project name

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://wakatime.com/api/v1/users/current/summaries").newBuilder();
        urlBuilder.addQueryParameter("start", Util.convertDateToProperFormat(new Date()));
        urlBuilder.addQueryParameter("end", Util.convertDateToProperFormat(new Date()));
        String url = urlBuilder.build().toString();

        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    System.out.println("TODAY SUMMARY SUCCESS: " + result);
                    System.out.println("SUMMARY CODE: " + response.code());

                    if (response.code() == 200) {
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONArray summaryArray = new JSONArray(resultObject.getString("data"));
                            JSONArray projectsArray = null;

                            for (int i = 0; i < summaryArray.length(); i++) {
                                JSONObject obj = summaryArray.getJSONObject(0);
                                projectsArray = obj.getJSONArray("projects");
                            }

                            ArrayList<Project> projects = new ArrayList<>();
                            long totalTimeToday = 0;

                            for (int i = 0; i < projectsArray.length(); i++) {
                                JSONObject tempObject = projectsArray.getJSONObject(i);
                                projects.add(new Project(
                                        tempObject.getString("name"),
                                        tempObject.getInt("percent"),
                                        tempObject.getInt("hours"),
                                        tempObject.getInt("minutes"),
                                        tempObject.getLong("total_seconds")));
                                totalTimeToday += tempObject.getLong("total_seconds");
                            }

                            TodaySummary todaySummary = new TodaySummary();
                            todaySummary.setProjectList(projects);
                            todaySummary.setTotalTime(totalTimeToday);

                            System.out.println("NYT OLIS VALMIS SUMMARY");
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
