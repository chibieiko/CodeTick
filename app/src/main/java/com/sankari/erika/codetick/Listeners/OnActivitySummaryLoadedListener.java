package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.ActivitySummary;

/**
 * Listens for when activity summary has loaded.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public interface OnActivitySummaryLoadedListener {

    /**
     * Provides activity summary object.
     *
     * @param activitySummary holds activity summary data
     */
    void onActivitySummaryLoadedSuccessfully(ActivitySummary activitySummary);

    /**
     * Provides error message.
     *
     * @param error error message
     */
    void onActivitySummaryLoadError(String error);
}
