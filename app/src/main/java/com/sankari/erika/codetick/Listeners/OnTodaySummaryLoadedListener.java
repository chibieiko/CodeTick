package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.TodaySummary;

/**
 * Created by erika on 4/18/2017.
 */

public interface OnTodaySummaryLoadedListener {
    void onTodaySummarySuccessfullyLoaded(TodaySummary obj);
    void onTodaySummaryLoadError(String error);
}
