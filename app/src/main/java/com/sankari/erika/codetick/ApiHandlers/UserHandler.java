package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Listeners.OnUserDataLoadedListener;
import com.sankari.erika.codetick.Utils.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by erika on 4/19/2017.
 */

public class UserHandler {

    private final String TAG = this.getClass().getName();
    private OnUserDataLoadedListener userListener;
    private ApiHandler apiHandler;

    public UserHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    public void addUserListener(OnUserDataLoadedListener listener) {
        userListener = listener;
    }

    public OnUserDataLoadedListener getUserListener() {
        return userListener;
    }

    public void getUserDetails(String url) {
        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    if (userListener != null) {
                        userListener.onUserDataLoadError("Error connecting to Wakatime's server. Try again later");
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    Debug.print(TAG, "onResponse", result, 6);
                    Debug.print(TAG, "onResponse", "code: " + response.code(), 6);

                    if (response.code() == 200) {
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONObject userObject = new JSONObject(resultObject.getString("data"));

                            User user = new User(userObject.getString("display_name"),
                                    userObject.getString("email"),
                                    userObject.getString("photo"));

                            if (userListener != null) {
                                userListener.onUserDataSuccessfullyLoaded(user);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (userListener != null) {
                            userListener.onUserDataLoadError("Error fetching data from Wakatime's server. Try again later");
                        }
                    }
                }
            });
        } else {
            if (userListener != null) {
                userListener.onUserDataLoadError("Error fetching data from Wakatime's server. Try again later");
            }

            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
