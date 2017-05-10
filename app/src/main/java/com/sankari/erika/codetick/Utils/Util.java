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
 * Provides helpful methods for other classes.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class Util {

    /**
     * Creates a token object from Wakatime's login reply.
     *
     * @param url Wakatime's login reply url containing all token information
     * @return token object
     */
    public static Token parseTokenUrl(String url) {
        Token token = new Token();

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

    /**
     * Logs the user out of the app.
     *
     * @param context context
     */
    public static void logout(Context context) {
        // Not async on purpose.
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().commit();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * Adds today's time to token's expiry date.
     * <p>
     * To be able to compare expiry date to today's date.getTime in the future this is necessary.
     *
     * @param expires token's time alive in seconds
     * @return expiry date in milliseconds
     */
    public static long getProperExpiryDate(String expires) {
        Date today = new Date();
        long expiresInMillis = TimeUnit.SECONDS.toMillis(Long.parseLong(expires));
        return expiresInMillis + today.getTime();
    }

    /**
     * Converts date to a string according to format.
     * <p>
     * For Wakatime's api query parameters.
     *
     * @param date date to be converted
     * @return string representation of the date
     */
    public static String convertDateToProperFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * Converts date string to a readable format (MMM dd).
     *
     * @param dateString string to convert
     * @param format     format the dateString is in
     * @return formatted string
     */
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

    /**
     * Converts time in seconds to a string of hours and minutes.
     *
     * @param time time in seconds
     * @return string of hours and minutes
     */
    public static String convertSecondsToHoursAndMinutes(Long time) {
        return String.format("%dh %dmin",
                TimeUnit.SECONDS.toHours(time),
                TimeUnit.SECONDS.toMinutes(time) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(time)));
    }

    /**
     * Checks if date string has today's date.
     *
     * @param dateString date string to check
     * @param format     format the date string is
     * @return true if date string has today's date, otherwise false
     */
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

    /**
     * Checks if date string has yesterday's date.
     *
     * @param dateString date string to check
     * @param format     format the date string is
     * @return true if date string has yesterday's date, otherwise false
     */
    public static boolean checkIfYesterday(String dateString, String format) {
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
