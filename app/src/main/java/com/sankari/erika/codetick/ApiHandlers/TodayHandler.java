package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.TodayProject;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.Listeners.OnTodaySummaryLoadedListener;
import com.sankari.erika.codetick.R;
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
 * Fetches today's coding data from Wakatime's server.
 * <p>
 * Passes data onwards through OnTodaySummaryLoadedListener.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class TodayHandler {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Used to pass today's data onwards.
     */
    private OnTodaySummaryLoadedListener todayListener;

    /**
     * Api handler instance.
     */
    private ApiHandler apiHandler;

    /**
     * Receives the api handler.
     *
     * @param handler api handler
     */
    public TodayHandler(ApiHandler handler) {
        this.apiHandler = handler;
    }

    /**
     * Sets the today listener.
     *
     * @param todayListener today listener
     */
    public void setTodayListener(OnTodaySummaryLoadedListener todayListener) {
        this.todayListener = todayListener;
    }

    /**
     * Tries to fetch user's today's coding data.
     * <p>
     * On success creates today summary object and calls today summary listener's
     * onTodaySummarySuccessfullyLoaded method. On error calls onTodaySummaryLoadError.
     */
    public void getTodayDetails() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL + "/users/current/summaries").newBuilder();
        urlBuilder.addQueryParameter("start", Util.convertDateToProperFormat(new Date()));
        urlBuilder.addQueryParameter("end", Util.convertDateToProperFormat(new Date()));
        String url = urlBuilder.build().toString();

        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    todayListener.onTodaySummaryLoadError(apiHandler.getContext().getResources().getString(R.string.error_connecting));
                    e.printStackTrace();
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

                            Debug.print(TAG, "onResponse", "TODAY SUMMARY READY", 6);
                            todayListener.onTodaySummarySuccessfullyLoaded(todaySummary);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        todayListener.onTodaySummaryLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
                    }
                }
            });
        } else {
            todayListener.onTodaySummaryLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
