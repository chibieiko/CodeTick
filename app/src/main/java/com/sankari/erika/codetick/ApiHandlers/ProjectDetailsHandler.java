package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.Language;
import com.sankari.erika.codetick.Classes.ProjectDetails;
import com.sankari.erika.codetick.Listeners.OnProjectDetailsLoadedListener;
import com.sankari.erika.codetick.R;
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
 * Fetches project details form the past 7 days from Wakatime's server.
 * <p>
 * Passes activity data onwards through OnProjectDetailsLoadedListener.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ProjectDetailsHandler {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Used to pass project details onwards.
     */
    private OnProjectDetailsLoadedListener projectDetailsLoadedListener;

    /**
     * Api handler instance.
     */
    private ApiHandler apiHandler;

    /**
     * Used to signal Wakatime's api which project's data the app wants.
     */
    private String projectName;

    /**
     * Receives the api handler and project name.
     *
     * @param apiHandler  api handler
     * @param projectName project's name that the app wants data of
     */
    public ProjectDetailsHandler(ApiHandler apiHandler, String projectName) {
        this.apiHandler = apiHandler;
        this.projectName = projectName;
    }

    /**
     * Sets the project details listener.
     *
     * @param projectDetailsLoadedListener project details
     */
    public void setProjectDetailsLoadedListener(OnProjectDetailsLoadedListener projectDetailsLoadedListener) {
        this.projectDetailsLoadedListener = projectDetailsLoadedListener;
    }

    /**
     * Tries to fetch project details from the past 7 days.
     * <p>
     * On success creates project details object and calls project details listener's
     * onProjectDetailsSuccessfullyLoaded method. On error calls onProjectDetailsLoadError.
     */
    public void getProjectDetails() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL + "/users/current/stats/last_7_days?project=" + projectName).newBuilder();
        String url = urlBuilder.build().toString();

        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    projectDetailsLoadedListener.onProjectDetailsLoadError(apiHandler.getContext().getResources().getString(R.string.error_connecting));
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();

                    Debug.print(TAG, "getProjectDetails:onResponse", result, 6);
                    Debug.print(TAG, "getProjectDetails:onResponse", "code: " + response.code(), 6);

                    if (response.code() == 200) {
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

                            // Project name
                            projectDetails.setName(data.getString("project"));

                            // List of languages.
                            ArrayList<Language> languages = new ArrayList<>();
                            JSONArray languagesArray = data.getJSONArray("languages");
                            for (int i = 0; i < languagesArray.length(); i++) {
                                JSONObject languageObj = languagesArray.getJSONObject(i);
                                languages.add(new Language(languageObj.getString("name"),
                                        languageObj.getInt("percent"),
                                        languageObj.getInt("hours"),
                                        languageObj.getInt("minutes")));
                            }

                            projectDetails.setLanguages(languages);

                            Debug.print(TAG, "onResponse", "PROJECT DETAILS READY", 6);
                            projectDetailsLoadedListener.onProjectDetailsSuccessfullyLoaded(projectDetails);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Happens when Wakatime's server informs that it is preparing the data,
                        // check back later...
                    } else if (response.code() == 202) {
                        try {
                            Debug.print(TAG, "getProjectDetails:onResponse", "code: " + response.code(), 6);
                            // calls Thread to sleep for one second and then try again to get project details.
                            TimeUnit.SECONDS.sleep(1);
                            getProjectDetails();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        projectDetailsLoadedListener.onProjectDetailsLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
                    }
                }
            });
        } else {
            projectDetailsLoadedListener.onProjectDetailsLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
