package com.sankari.erika.codetick.Utils;

import android.content.Context;
import android.util.Log;

import com.sankari.erika.codetick.R;

/**
 * Provides methods for debugging.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class Debug {

    /**
     * Indicates debug level.
     */
    private static int DEBUG_LEVEL = 5;

    /**
     * Prints debug messages whose debug level matches or is lower than DEBUG_LEVEL.
     *
     * @param className class' name
     * @param method    method name
     * @param message   debug message
     * @param level     debug level indicating when to print
     */
    public static void print(String className, String method, String message, int level) {
        if (level <= DEBUG_LEVEL) {
            Log.d(className, method + " :: " + message);
        }
    }

    /**
     * Loads debug level from strings XML.
     *
     * @param context context
     */
    public static void loadDebug(Context context) {
        DEBUG_LEVEL = Integer.parseInt(context.getResources().getString(R.string.debugLevel));
    }
}
