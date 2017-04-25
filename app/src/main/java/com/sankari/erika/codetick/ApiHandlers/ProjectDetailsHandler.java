package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.Language;
import com.sankari.erika.codetick.Classes.ProjectDetails;
import com.sankari.erika.codetick.Listeners.OnProjectDetailsLoadedListener;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by erika on 4/25/2017.
 */

public class ProjectDetailsHandler {
    private final String TAG = this.getClass().getName();
    private OnProjectDetailsLoadedListener projectDetailsLoadedListener;
    private ApiHandler apiHandler;
    private String projectName;

    public ProjectDetailsHandler(ApiHandler apiHandler, String projectName) {
        this.apiHandler = apiHandler;
        this.projectName = projectName;
    }

    public void setProjectDetailsLoadedListener(OnProjectDetailsLoadedListener projectDetailsLoadedListener) {
        this.projectDetailsLoadedListener = projectDetailsLoadedListener;
    }

    public void getProjectDetails() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL + "/users/current/stats/last_7_days?project=" + projectName).newBuilder();
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

                    Debug.print(TAG, "getProjectDetails:onResponse", result, 4);
                    Debug.print(TAG, "getProjectDetails:onResponse", "code: " + response.code(), 4);

                    if (response.code() == 200) {
                        System.out.println("Moro tääl on 200");
                        ProjectDetails projectDetails = new ProjectDetails();
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONObject data = new JSONObject(resultObject.getString("data"));

                            // Best day data.
                            JSONObject bestDay = new JSONObject(data.getString("best_day"));
                            projectDetails.setBestDayDate(bestDay.getString("date"));
                            projectDetails.setBestDayTime(bestDay.getLong("total_seconds"));

                            // Daily average.
                            projectDetails.setDailyAverage(data.getLong("daily_average"));

                            // Total time.
                            projectDetails.setTotalTime(data.getLong("total_seconds"));

                            // Last modified date.
                            projectDetails.setLastModified(data.getString("modified_at"));

                            // List of languages.
                            ArrayList<Language> languages = new ArrayList<>();
                            JSONArray languagesArray = data.getJSONArray("languages");
                            for (int i = 0; i < languagesArray.length(); i++) {
                                JSONObject languageObj = languagesArray.getJSONObject(i);
                                languages.add(new Language(languageObj.getString("name"),
                                        languageObj.getLong("percent"),
                                        languageObj.getInt("hours"),
                                        languageObj.getInt("minutes")));
                            }

                            projectDetails.setLanguages(languages);

                            Debug.print(TAG, "onResponse", "PROJECT DETAILS READY", 4);
                            projectDetailsLoadedListener.onProjectDetailsSuccessfullyLoaded(projectDetails);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if (response.code() == 202) {
                        try {
                            Debug.print(TAG, "getProjectDetails:onResponse", "code: " + response.code(), 4);
                            // calls Thread to sleep for one second and then try again to get project details.
                            TimeUnit.SECONDS.sleep(1);
                            getProjectDetails();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        projectDetailsLoadedListener.onProjectDetailsLoadError("Error fetching data from Wakatime's server...");
                    }
                }
            });
        } else {
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
