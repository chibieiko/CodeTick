package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.TodaySummary;

/**
 * Listens for when today summary has loaded.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public interface OnTodaySummaryLoadedListener {

    /**
     * Provides today summary object.
     *
     * @param obj holds today summary data
     */
    void onTodaySummarySuccessfullyLoaded(TodaySummary obj);

    /**
     * Provides error message.
     *
     * @param error error message
     */
    void onTodaySummaryLoadError(String error);
}
