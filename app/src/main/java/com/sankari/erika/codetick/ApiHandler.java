package com.sankari.erika.codetick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sankari.erika.codetick.Activities.MainActivity;
import com.sankari.erika.codetick.Classes.Token;
import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Listeners.OnDataLoadedListener;
import com.sankari.erika.codetick.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    OkHttpClient client;
    private OnDataLoadedListener userListener;
    private Context context;
    private SharedPreferences prefs;

    public ApiHandler(Context context) {
        this.context = context;
        client = new OkHttpClient();
    }

    public void addListener(OnDataLoadedListener listener) {
        userListener = listener;
    }

    private boolean checkTokenExpiry() {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long expires = prefs.getLong("expires", 0);
        System.out.println("EXPIRY DATE IS: " + new Date(expires));

        Date today = new Date();
        if (today.getTime() < expires) {
            return true;
        } else {
            refreshToken(prefs.getString("refreshToken", null), false);
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

    public void refreshToken(String refreshToken, final boolean startMain) {
        String appSecret = "";
        String appId = "";
        String redirectUri = "";
        Properties config = new Properties();

        try {
            config.load(context.getAssets().open("config.properties"));
            appSecret = config.getProperty("secret");
            appId = config.getProperty("id");
            redirectUri = config.getProperty("redirectUri");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("APP ID: " + appId);
        System.out.println("APP secret: " + appSecret);
        System.out.println("RedirectUri: " + redirectUri);

        final HttpUrl.Builder uriBuilder = HttpUrl.parse("https://wakatime.com/oauth/token").newBuilder();
        uriBuilder.addQueryParameter("client_id", appId);
        uriBuilder.addQueryParameter("client_secret", appSecret);
        uriBuilder.addQueryParameter("grant_type", "refresh_token");
        uriBuilder.addQueryParameter("redirect_uri", redirectUri);
        uriBuilder.addQueryParameter("refresh_token", refreshToken);
        String url = uriBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .method("POST", new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public void writeTo(BufferedSink sink) throws IOException {

                    }
                })
                .build();

        System.out.println("CLIENT: " + client);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("ERROR refreshing token");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println("Response: " + result);

                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getString("error") != null) {
                        // todo throw out and show snackbar
                        System.out.println("ERROR TRYING TO REFRESH TOKEN");
                    } else {
                        Token token = Util.parseTokenUrl(result);
                        prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        prefs.edit().putString("token", token.getAccessToken()).apply();
                        prefs.edit().putString("refreshToken", token.getRefreshToken()).apply();
                        prefs.edit().putLong("expires", token.getExpires()).apply();

                        if (startMain) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("SUCCESS refreshing token");
            }
        });
    }

    public void getUserDetails(String url) {
        if (checkTokenExpiry()) {
            Request request = getRequest(url);

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
}
