package com.sankari.erika.codetick.Classes;

/**
 * Contains user's token data.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class Token {

    /**
     * User's access token.
     */
    private String accessToken;

    /**
     * Refresh token used to get a new access token when access token has expired.
     */
    private String refreshToken;

    /**
     * Expiry date in milliseconds.
     */
    private long expires;

    /**
     * Indicates whether refresh token is invalid or valid.
     */
    private static boolean invalidRefreshToken = false;

    /**
     * Sets access token, refresh token and expiry date.
     *
     * @param accessToken  user's access token
     * @param refreshToken refresh token used to get a new access token when access token has expired
     * @param expires      expiry date in milliseconds
     */
    public Token(String accessToken, String refreshToken, long expires) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires = expires;
    }

    /**
     * Gets user's access token.
     *
     * @return user's access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets user's access token.
     *
     * @param accessToken user's access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Gets refresh token used to get a new access token when access token has expired.
     *
     * @return refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets refresh token used to get a new access token when access token has expired.
     *
     * @param refreshToken refresh token
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Gets expiry date in milliseconds.
     *
     * @return expiry date in milliseconds
     */
    public long getExpires() {
        return expires;
    }

    /**
     * Sets expiry date in milliseconds.
     *
     * @param expires expiry date in milliseconds
     */
    public void setExpires(long expires) {
        this.expires = expires;
    }

    /**
     * Checks if refresh token is invalid.
     *
     * @return true if invalid and false if valid
     */
    public static boolean isInvalidRefreshToken() {
        return invalidRefreshToken;
    }

    /**
     * Sets refresh token to invalid or valid.
     *
     * @param invalidRefreshToken true if invalid and false if valid
     */
    public static void setInvalidRefreshToken(boolean invalidRefreshToken) {
        invalidRefreshToken = invalidRefreshToken;
    }
}
