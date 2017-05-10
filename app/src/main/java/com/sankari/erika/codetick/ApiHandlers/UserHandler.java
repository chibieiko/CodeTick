package com.sankari.erika.codetick.ApiHandlers;

import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Listeners.OnUserDataLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fetches user's data from Wakatime's server.
 * <p>
 * Passes user data onwards through OnUserDataLoadedListener.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class UserHandler {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Used to pass user data onwards.
     */
    private OnUserDataLoadedListener userListener;

    /**
     * Api handler instance.
     */
    private ApiHandler apiHandler;

    /**
     * Receives the api handler.
     *
     * @param apiHandler api handler
     */
    public UserHandler(ApiHandler apiHandler) {
        this.apiHandler = apiHandler;
    }

    /**
     * Sets user listener.
     *
     * @param listener user listener
     */
    public void addUserListener(OnUserDataLoadedListener listener) {
        userListener = listener;
    }

    /**
     * Gets user listener.
     *
     * @return user listener
     */
    public OnUserDataLoadedListener getUserListener() {
        return userListener;
    }

    /**
     * Tries to fetch user's details.
     * <p>
     * On success creates user object and calls user listener's
     * onUserDataSuccessfullyLoaded method. On error calls onUserDataLoadError.
     */
    public void getUserDetails(String url) {
        if (apiHandler.checkTokenExpiry()) {
            Request request = apiHandler.getRequest(url);
            apiHandler.getClient().newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    if (userListener != null) {
                        userListener.onUserDataLoadError(apiHandler.getContext().getResources().getString(R.string.error_connecting));
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
                            userListener.onUserDataLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
                        }
                    }
                }
            });

        } else {
            if (userListener != null) {
                userListener.onUserDataLoadError(apiHandler.getContext().getResources().getString(R.string.error_getting_data));
            }

            apiHandler.refreshToken(apiHandler.getPrefs().getString("token", null), false);
        }
    }
}
