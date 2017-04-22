package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Listeners.OnUserDataLoadedListener;

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

    OnUserDataLoadedListener userListener;
    ApiHandler apiHandler;

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
                    userListener.onUserDataLoadError(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    System.out.println("SUCCESS: " + result);
                    System.out.println("CODE: " + response.code());

                    if (response.code() == 200) {
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONObject userObject = new JSONObject(resultObject.getString("data"));

                            User user = new User(userObject.getString("display_name"),
                                    userObject.getString("email"),
                                    userObject.getString("photo"));

                            System.out.println(user);
                            System.out.println("USER LISTENER: " + userListener);
                            if (userListener != null) {
                                userListener.onUserDataSuccessfullyLoaded(user);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("USER LISTENER in error: " + userListener);

                        if (userListener != null) {
                            userListener.onUserDataLoadError("Error fetching user data from Wakatime's server...");
                        }
                    }
                }
            });
        } else {
            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
