package com.sankari.erika.codetick.ApiHandlers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sankari.erika.codetick.Activities.MainActivity;
import com.sankari.erika.codetick.Classes.Token;
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

    private OkHttpClient client;
    private Context context;
    private SharedPreferences prefs;

    public ApiHandler(Context context) {
        this.context = context;
        client = new OkHttpClient();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public OkHttpClient getClient() {
        return client;
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    public boolean checkTokenExpiry() {
        long expires = prefs.getLong("expires", 0);
        System.out.println("EXPIRY DATE IS: " + new Date(expires));

        Date today = new Date();
        if (today.getTime() < expires) {
            return true;
        } else {
            return false;
        }
    }

    public Request getRequest(String url) {
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
