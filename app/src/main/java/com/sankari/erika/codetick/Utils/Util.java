package com.sankari.erika.codetick.Utils;

import com.sankari.erika.codetick.Classes.Token;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by erika on 4/16/2017.
 */

public class Util {

    public static Token parseTokenUrl(String url) {
        Token token = new Token();

        System.out.println("URL: " + url);
        String[] reply = url.split("&");

        for (String replyContent : reply) {
            String[] replyContentParts = replyContent.split("=");

            if (replyContentParts[0].equals("access_token")) {
                token.setAccessToken(replyContentParts[1]);
            } else if (replyContentParts[0].equals("refresh_token")) {
                token.setRefreshToken(replyContentParts[1]);
            } else if (replyContentParts[0].equals("expires_in")) {
                String expiryString = replyContentParts[1];
                Date today = new Date();
                long realtime = TimeUnit.MINUTES.toMillis(Long.parseLong(expiryString));
                token.setExpires(realtime + today.getTime());
            }
        }

        return token;
    }
}
