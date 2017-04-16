package com.sankari.erika.codetick;

import com.sankari.erika.codetick.Classes.User;

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

    public ApiHandler() {
        client = new OkHttpClient();
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
                System.out.println("ERROR: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("SUCCESS: " + result);

                try {
                    JSONObject resultObject = new JSONObject(result);
                  //  System.out.println(resultObject.getString("data"));
                    if (true) {
                        JSONObject userObject = new JSONObject(resultObject.getString("data"));

                        User user = new User(userObject.getString("display_name"),
                                userObject.getString("email"),
                                userObject.getString("photo"));

                        System.out.println(user);
                    } else {
                        System.out.println("ERROR: " + resultObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
