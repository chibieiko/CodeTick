package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.Listeners.OnProjectListLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fetches user's project list from Wakatime's server.
 * <p>
 * Passes list onwards through OnProjectListLoadedListener.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ProjectHandler {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Used to pass activity data onwards.
     */
    private OnProjectListLoadedListener projectListLoadedListener;

    /**
     * Api handler instance.
     */
    private ApiHandler apiHandler;

    /**
     * Receives the api handler.
     *
     * @param apiHandler api handler
     */
    public ProjectHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    /**
     * Sets the project list loaded listener.
     *
     * @param projectListLoadedListener project list loaded listener
     */
    public void setProjectListLoadedListener(OnProjectListLoadedListener projectListLoadedListener) {
        this.projectListLoadedListener = projectListLoadedListener;
    }

    /**
     * Tries to fetch user's project list.
     * <p>
     * On success creates project list containing project list items and calls project list
     * listener's onProjectListSuccessfullyLoaded method. On error calls onProjectListLoadError.
     */
    public void getProjectListing() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL + "/users/current/projects").newBuilder();
        String url = urlBuilder.build().toString();

        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    projectListLoadedListener.onProjectListLoadError(apiHandler.getContext().getResources().getString(R.string.error_connecting));
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();

                    Debug.print(TAG, "getProjectListing:onResponse", result, 6);
                    Debug.print(TAG, "getProjectListing:onResponse", "code: " + response.code(), 6);

                    if (response.code() == 200) {
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONArray projectsArray = new JSONArray(resultObject.getString("data"));

                            ArrayList<ProjectListItem> projectList = new ArrayList<>();

                            for (int i = 0; i < projectsArray.length(); i++) {
                                JSONObject tempObject = projectsArray.getJSONObject(i);
                                projectList.add(new ProjectListItem(
                                        tempObject.getString("name"),
                                        tempObject.getString("id")));
                            }

                            Debug.print(TAG, "onResponse", "PROJECT LIST READY", 6);
                            projectListLoadedListener.onProjectListSuccessfullyLoaded(projectList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        projectListLoadedListener.onProjectListLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
                    }
                }
            });
        } else {
            projectListLoadedListener.onProjectListLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}