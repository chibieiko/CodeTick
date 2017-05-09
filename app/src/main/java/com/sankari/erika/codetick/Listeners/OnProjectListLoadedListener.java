package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.ProjectListItem;

import java.util.List;

/**
 * Listens for when project list has loaded.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public interface OnProjectListLoadedListener {

    /**
     * Provides project list containing project list items.
     *
     * @param projects project list
     */
    void onProjectListSuccessfullyLoaded(List<ProjectListItem> projects);

    /**
     * Provides error message.
     *
     * @param error error message
     */
    void onProjectListLoadError(String error);
}
