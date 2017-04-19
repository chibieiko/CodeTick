package com.sankari.erika.codetick.Classes;

/**
 * Created by erika on 4/16/2017.
 */

public class Token {
    private String accessToken;
    private String refreshToken;
    private long expires;
    private static boolean invalidRefreshToken = false;

    public Token() {
    }

    public Token(String accessToken, String refreshToken, long expires) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires = expires;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public static boolean isInvalidRefreshToken() {
        return invalidRefreshToken;
    }

    public static void setInvalidRefreshToken(boolean invalidRefreshToken) {
        invalidRefreshToken = invalidRefreshToken;
    }
}
