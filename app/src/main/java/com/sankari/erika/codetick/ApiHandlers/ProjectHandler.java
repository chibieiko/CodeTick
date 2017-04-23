package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.ProjectListItem;
import com.sankari.erika.codetick.Listeners.OnProjectListLoadedListener;
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
 * Created by erika on 4/23/2017.
 */

public class ProjectHandler {
    private final String TAG = this.getClass().getName();
    private OnProjectListLoadedListener projectListLoadedListener;
    private ApiHandler apiHandler;

    public ProjectHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    public void setProjectListLoadedListener(OnProjectListLoadedListener projectListLoadedListener) {
        this.projectListLoadedListener = projectListLoadedListener;
    }

    public void getProjectListing() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Urls.BASE_URL + "/users/current/projects").newBuilder();
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

                    Debug.print(TAG, "getProjectListing:onResponse", result, 4);
                    Debug.print(TAG, "getProjectListing:onResponse", "code: " + response.code(), 4);

                    if (response.code() == 200) {
                        System.out.println("IM HERE");
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONArray projectsArray = new JSONArray(resultObject.getString("data"));

                            ArrayList<ProjectListItem> projectList = new ArrayList<>();

                            for (int i = 0; i < projectsArray.length(); i++) {
                                JSONObject tempObject = projectsArray.getJSONObject(i);
                                projectList.add(new ProjectListItem (
                                        tempObject.getString("name"),
                                        tempObject.getString("id")));
                            }

                            Debug.print(TAG, "onResponse", "NYT OLIS VALMIS PROJECT LIST", 4);
                            projectListLoadedListener.onProjectListSuccessfullyLoaded(projectList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        projectListLoadedListener.onProjectListLoadError("Error fetching data from Wakatime's server...");
                    }
                }
            });
        } else {
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}