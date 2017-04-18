package com.sankari.erika.codetick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sankari.erika.codetick.Activities.MainActivity;
import com.sankari.erika.codetick.Classes.Project;
import com.sankari.erika.codetick.Classes.TodaySummary;
import com.sankari.erika.codetick.Classes.Token;
import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Listeners.OnUserDataLoadedListener;
import com.sankari.erika.codetick.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by erika on 4/10/2017.
 */

public class ApiHandler {

    private OkHttpClient client;
    private OnUserDataLoadedListener userListener;
    private Context context;
    private SharedPreferences prefs;

    public ApiHandler(Context context) {
        this.context = context;
        client = new OkHttpClient();
    }

    public void addUserListener(OnUserDataLoadedListener listener) {
        userListener = listener;
    }

    public OnUserDataLoadedListener getUserListener() {
        return userListener;
    }

    private boolean checkTokenExpiry() {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long expires = prefs.getLong("expires", 0);
        System.out.println("EXPIRY DATE IS: " + new Date(expires));

        Date today = new Date();
        if (today.getTime() < expires) {
            return true;
        } else {
            return false;
        }
    }

    private Request getRequest(String url) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String token = prefs.getString("token", null);

        return new Request.Builder()
                .header("Authorization", "Bearer " + token)
                .url(url)
                .build();
    }

    public void getUserDetails(String url) {
        if (checkTokenExpiry()) {
            Request request = getRequest(url);
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    if (userListener != null) {
                        userListener.onUserDataLoadError(e.toString());
                    } else {
                        System.out.println("NO USER LISTENER IN API HANDLER");
                    }
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
                            userListener.onUserDataSuccessfullyLoaded(user);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        userListener.onUserDataLoadError("Error fetching user data from Wakatime's server...");
                    }
                }
            });
        } else {
            refreshToken(prefs.getString("token", null), false);
        }
    }

    public void getTodayDetails() {
        // todo Get today total time and time per project and project name

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://wakatime.com/api/v1/users/current/summaries").newBuilder();
        urlBuilder.addQueryParameter("start", Util.convertDateToProperFormat(new Date()));
        urlBuilder.addQueryParameter("end", Util.convertDateToProperFormat(new Date()));
        String url = urlBuilder.build().toString();

        if (checkTokenExpiry()) {
            Request request = getRequest(url);
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    System.out.println("TODAY SUMMARY SUCCESS: " + result);
                    System.out.println("SUMMARY CODE: " + response.code());

                    if (response.code() == 200) {
                        try {
                            JSONObject resultObject = new JSONObject(result);
                            JSONObject summaryObject = new JSONObject(resultObject.getString("data"));
                            JSONArray jsonProjects = new JSONArray(summaryObject.getJSONArray("projects"));

                            ArrayList<Project> projects = new ArrayList<Project>();
                            long totalTimeToday = 0;

                            for (int i = 0; i < jsonProjects.length(); i++) {
                                JSONObject tempObject = jsonProjects.getJSONObject(i);
                                projects.add(new Project(
                                        tempObject.getString("name"),
                                        tempObject.getInt("percent"),
                                        tempObject.getInt("hours"),
                                        tempObject.getInt("minutes"),
                                        tempObject.getLong("total_seconds")));
                                totalTimeToday += tempObject.getLong("total_seconds");
                            }

                            TodaySummary todaySummary = new TodaySummary();
                            todaySummary.setProjectList(projects);
                            todaySummary.setTotalTime(totalTimeToday);

                            System.out.println(todaySummary);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        userListener.onUserDataLoadError("Error fetching user data from Wakatime's server...");
                    }
                }
            });
        } else {
            refreshToken(prefs.getString("token", null), false);
        }
    }

    public void refreshToken(String refreshToken, final boolean startMain) {
        String appSecret = "";
        String appId = "";
        String redirectUri = "";
        Properties config = new Properties();

        // Loads app information from file.
        try {
            config.load(context.getAssets().open("config.properties"));
            appSecret = config.getProperty("secret");
            appId = config.getProperty("id");
            redirectUri = config.getProperty("redirectUri");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final HttpUrl.Builder uriBuilder = HttpUrl.parse("https://wakatime.com/oauth/token").newBuilder();
        String url = uriBuilder.build().toString();

        final String postForm = "client_id=" + appId +
                "&client_secret=" + appSecret +
                "&grant_type=refresh_token" +
                "&redirect_uri=" + redirectUri +
                "&refresh_token=" + refreshToken;

        Request request = new Request.Builder()
                .url(url)
                .method("POST", new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse("application/x-www-form-urlencoded");
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {
                        sink.writeUtf8(postForm);
                    }
                })
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //todo show snackbar
                Util.logout(context);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("Refresh token Response: " + result);

                try {
                    JSONObject object = new JSONObject(result);
                    if (response.code() != 200) {
                        // todo show snackbar to prompt user to login
                        System.out.println("ERROR TRYING TO REFRESH TOKEN");
                        Token.setInvalidRefreshToken(true);
                        Util.logout(context);
                    } else {
                        prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        prefs.edit().putString("token", object.getString("access_token")).apply();
                        prefs.edit().putString("refreshToken", object.getString("refresh_token")).apply();
                        prefs.edit().putLong("expires", Util.getProperExpiryDate(object.getString("expires_in"))).apply();

                        System.out.println("SUCCESS refreshing token");
                        if (startMain) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
