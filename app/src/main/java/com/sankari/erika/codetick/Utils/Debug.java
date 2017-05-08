package com.sankari.erika.codetick.Utils;

import android.content.Context;
import android.util.Log;

import com.sankari.erika.codetick.R;

/**
 * Created by erika on 3/6/2017.
 */

public class Debug {

    private static int DEBUG_LEVEL = 5;

    public static void print(String className, String method, String message, int level) {
        if (level <= DEBUG_LEVEL) {
            Log.d(className, method + " :: " + message);
        }
    }

    public static void loadDebug(Context context) {
        DEBUG_LEVEL = Integer.parseInt(context.getResources().getString(R.string.debugLevel));
    }
}
