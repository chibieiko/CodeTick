package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.ProjectDetails;

/**
 * Listens for when project details have loaded.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public interface OnProjectDetailsLoadedListener {

    /**
     * Provides project details object.
     *
     * @param projectDetails holds project details
     */
    void onProjectDetailsSuccessfullyLoaded(ProjectDetails projectDetails);

    /**
     * Provides error message.
     *
     * @param error error message
     */
    void onProjectDetailsLoadError(String error);
}
