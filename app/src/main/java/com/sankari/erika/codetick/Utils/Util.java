package com.sankari.erika.codetick.Utils;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.sankari.erika.codetick.Activities.LoginActivity;
import com.sankari.erika.codetick.Classes.Token;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
                token.setExpires(getProperExpiryDate(expiryString));
                System.out.println("EXPIRY DATE FOR NOW: " + new Date(token.getExpires()));
            }
        }

        return token;
    }

    public static void logout(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static long getProperExpiryDate(String expires) {
        Date today = new Date();
        long expiresInMillis = TimeUnit.SECONDS.toMillis(Long.parseLong(expires));
        return expiresInMillis + today.getTime();
    }

    // For Wakatime's api query parameters.
    public static String convertDateToProperFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String convertStringToReadableDateString(String dateString, String format) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMM dd");
        DateFormat dateFormat2 = new SimpleDateFormat(format);
        try {
            date = dateFormat2.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

         return dateFormat.format(date);
    }

    public static String convertSecondsToHoursAndMinutes(Long time) {
        return String.format("%dh %dmin",
                TimeUnit.SECONDS.toHours(time),
                TimeUnit.SECONDS.toMinutes(time) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time)));
    }

    public static boolean checkIfToday(String dateString, String format) {
        Date today = new Date();
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calToday = Calendar.getInstance();
        calToday.setTime(today);
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);

        return calToday.get(Calendar.YEAR) == calDate.get(Calendar.YEAR)
                && calToday.get(Calendar.DAY_OF_YEAR) == calDate.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean checkIfTomorrow(String dateString, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date todayDate = new Date();
        Calendar calYesterday = Calendar.getInstance();
        calYesterday.setTime(todayDate);
        calYesterday.add(Calendar.DATE, -1);

        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);

        return calYesterday.get(Calendar.YEAR) == calDate.get(Calendar.YEAR)
                && calYesterday.get(Calendar.DAY_OF_YEAR) == calDate.get(Calendar.DAY_OF_YEAR);
    }
}
