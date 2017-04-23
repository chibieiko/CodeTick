package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.ProjectListItem;

import java.util.List;

/**
 * Created by erika on 4/23/2017.
 */

public interface OnProjectListLoadedListener {
    void onProjectListSuccessfullyLoaded(List<ProjectListItem> projects);
    void onProjectListLoadError(String error);
}
