package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.ActivitySummary;
import com.sankari.erika.codetick.Classes.DaySummary;
import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.Listeners.OnActivitySummaryLoadedListener;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by erika on 4/23/2017.
 */

public class ActivityHandler {
    // get daily avg and total time coded in 7 days and project info for graph
    // https://wakatime.com/api/v1/users/current/stats?range=last_7_days
    // https://wakatime.com/api/v1/users/current/stats/last_30_days

    // Kaks viikkoa taaksepäin horizontal ja yks viikko taaksepäin vertical,
    // Hae kaks viikkoa ja parsi siitä sit kaikki

    // https://wakatime.com/api/v1/users/current/summaries?start=2017-04-16&end=2017-04-30

    private final String TAG = this.getClass().getName();
    private OnActivitySummaryLoadedListener activitySummaryListener;
    private ApiHandler apiHandler;

    public ActivityHandler(ApiHandler handler) {
        this.apiHandler = handler;
    }

    public void setActivitySummaryListener(OnActivitySummaryLoadedListener activitySummaryListener) {
        this.activitySummaryListener = activitySummaryListener;
    }

    public void getActivitySummary() {
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(todayDate);
        c.add(Calendar.DATE, -14);
        String twoWeeksBefore = sdf.format(c.getTime());
        String today = sdf.format(todayDate);

        System.out.println("TODAY: " + today);
        System.out.println("TWO WEEKS BEFORE: " + twoWeeksBefore);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL + "/users/current/summaries").newBuilder();
        urlBuilder.addQueryParameter("start", twoWeeksBefore);
        urlBuilder.addQueryParameter("end", today);
        String url = urlBuilder.build().toString();

        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();

                    Debug.print(TAG, "onResponse", "code: " + response.code(), 3);
                    Debug.print(TAG, "onResponse", result, 3);

                    if (response.code() == 200) {
                        try {
                            ActivitySummary activitySummary = new ActivitySummary();
                            ArrayList<DaySummary> dayList = new ArrayList<>();
                            long totalTime = 0;

                            JSONObject resultObject = new JSONObject(result);
                            JSONArray summaryArray = new JSONArray(resultObject.getString("data"));

                            // Add days to dayList.
                            for (int i = 0; i < summaryArray.length(); i++) {
                                DaySummary daySummary = new DaySummary();
                                JSONObject dayJsonObj = summaryArray.getJSONObject(i);

                                // Set day grand total time.
                                JSONObject grandTotal = dayJsonObj.getJSONObject("grand_total");
                                daySummary.setTotal(grandTotal.getLong("total_seconds"));
                                totalTime += grandTotal.getLong("total_seconds");

                                // Set day date.
                                JSONObject range = dayJsonObj.getJSONObject("range");
                                daySummary.setDate(range.getString("date"));

                                // Set day project list.
                                JSONArray projectsArray = dayJsonObj.getJSONArray("projects");
                                ArrayList<ProjectListItem> projects = new ArrayList<>();
                                for (int j = 0; j < projectsArray.length(); j++) {
                                    ProjectListItem project = new ProjectListItem();
                                    JSONObject tempObj = projectsArray.getJSONObject(j);
                                    project.setName(tempObj.getString("name"));
                                    project.setTime(tempObj.getLong("total_seconds"));
                                    projects.add(project);
                                }

                                daySummary.setProjectList(projects);

                                // Add ready day to day list.
                                dayList.add(daySummary);
                            }

                            // Sorts the dayList according to date.
                            Collections.sort(dayList, new Comparator<DaySummary>() {
                                @Override
                                public int compare(DaySummary o1, DaySummary o2) {
                                    return o2.getDate().compareTo(o1.getDate());
                                }
                            });

                            // Add sorted dayList to activity summary.
                            activitySummary.setDaySummaryList(dayList);
                            activitySummary.setTotal(totalTime);
                            long avg = totalTime / 14;
                            activitySummary.setAverage(avg);

                            Debug.print(TAG, "onResponse", "ACTIVITY SUMMARY READY", 5);
                            activitySummaryListener.onActivitySummaryLoadedSuccessfully(activitySummary);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        activitySummaryListener.onActivitySummaryLoadError("Error fetching today's stats from Wakatime's server...");
                    }
                }
            });
        } else {
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
