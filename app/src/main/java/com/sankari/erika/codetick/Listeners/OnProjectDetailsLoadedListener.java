package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.ProjectDetails;

/**
 * Created by erika on 4/25/2017.
 */

public interface OnProjectDetailsLoadedListener {
    void onProjectDetailsSuccessfullyLoaded(ProjectDetails projectDetails);
    void onProjectDetailsLoadError(String error);
}
