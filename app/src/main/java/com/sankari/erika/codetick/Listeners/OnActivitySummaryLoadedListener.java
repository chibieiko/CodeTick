package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.ActivitySummary;

/**
 * Created by erika on 4/30/2017.
 */

public interface OnActivitySummaryLoadedListener {
    void onActivitySummaryLoadedSuccessfully(ActivitySummary activitySummary);
    void onActivitySummaryLoadError(String error);
}
