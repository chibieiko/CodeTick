package com.sankari.erika.codetick.ApiHandlers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sankari.erika.codetick.Activities.MainActivity;
import com.sankari.erika.codetick.Classes.Token;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.Urls;
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
 * Provides methods for other handlers and login activity.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ApiHandler {

    /**
     * Holds class name for debugging.
     */
    private final String TAG = this.getClass().getName();

    /**
     * Used to make request to Wakatime's server.
     */
    private OkHttpClient client;

    /**
     * For accessing shared preferences.
     */
    private Context context;

    /**
     * Holds user's token information.
     */
    private SharedPreferences prefs;

    /**
     * Sets context and initializes shared preferences and OkHttpClient.
     *
     * @param context context
     */
    public ApiHandler(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        client = new OkHttpClient();
    }

    /**
     * Gets context.
     *
     * @return context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Gets OkHttpClient.
     *
     * @return okHttpClient client
     */
    public OkHttpClient getClient() {
        return client;
    }

    /**
     * Gets shared preferences.
     *
     * @return shared preferences prefs
     */
    public SharedPreferences getPrefs() {
        return prefs;
    }

    /**
     * Checks if user's token has expired or not.
     *
     * @return false if token has expired and true otherwise
     */
    public boolean checkTokenExpiry() {
        long expires = prefs.getLong("expires", 0);

        Debug.print(TAG, "checkTokenExpiry", new Date(expires).toString(), 1);

        Date today = new Date();
        return today.getTime() < expires;
    }

    /**
     * Creates and returns a okHttpClient request.
     *
     * @param url request url
     * @return okHttpClient url
     */
    public Request getRequest(String url) {
        String token = prefs.getString("token", null);

        return new Request.Builder()
                .header("Authorization", "Bearer " + token)
                .url(url)
                .build();
    }

    /**
     * Attempts to acquire a new token for the user with refresh token.
     *
     * @param refreshToken user's refresh token
     * @param startMain indicates whether to start main activity on success or not
     */
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

        final HttpUrl.Builder uriBuilder = HttpUrl.parse(Urls.REFRESH_URL).newBuilder();
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
                e.printStackTrace();
                // Util.logout(context);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();

                Debug.print(TAG, "refreshToken::onResponse", result, 5);

                try {
                    JSONObject object = new JSONObject(result);
                    if (response.code() != 200) {
                        Debug.print(TAG, "refreshToken::onResponse", "ERROR TRYING TO REFRESH TOKEN", 5);

                        Token.setInvalidRefreshToken(true);
                        Util.logout(context);
                    } else {
                        prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        prefs.edit().putString("token", object.getString("access_token")).apply();
                        prefs.edit().putString("refreshToken", object.getString("refresh_token")).apply();
                        prefs.edit().putLong("expires", Util.getProperExpiryDate(object.getString("expires_in"))).apply();

                        Debug.print(TAG, "refreshToken::onResponse", "SUCCESS refreshing token", 5);

                        if (startMain) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.logout(context);
                }
            }
        });
    }
}
