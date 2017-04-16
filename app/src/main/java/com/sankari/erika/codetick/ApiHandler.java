package com.sankari.erika.codetick;

import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Listeners.OnDataLoadedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by erika on 4/10/2017.
 */

public class ApiHandler {

    OkHttpClient client;
    private OnDataLoadedListener userListener;

    public ApiHandler() {
        client = new OkHttpClient();
    }

    public void addListener(OnDataLoadedListener listener) {
        userListener = listener;
    }

    private Request getRequest(String url, String token) {
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + token)
                .url(url)
                .build();

        return request;
    }

    public void getUserDetails(String url, String token) {
        Request request = getRequest(url, token);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // todo show snackbar
                System.out.println("ERROR: " + e);
                if (userListener != null) {
                    userListener.onDataLoadError(e.toString());
                } else {
                    System.out.println("NO USER LISTENER IN API HANDLER");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("SUCCESS: " + result);

                try {
                    JSONObject resultObject = new JSONObject(result);
                    JSONObject userObject = new JSONObject(resultObject.getString("data"));

                    User user = new User(userObject.getString("display_name"),
                            userObject.getString("email"),
                            userObject.getString("photo"));

                    System.out.println(user);
                    if (userListener != null) {
                        userListener.onDataSuccessfullyLoaded(user);
                    } else {
                        System.out.println("NO USER LISTENER IN API HANDLER");
                    }

                } catch (JSONException e) {
                    System.out.println("ERROR, could not fetch data properly");
                    e.printStackTrace();
                }
            }
        });
    }
}
