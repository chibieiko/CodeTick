package com.sankari.erika.codetick.Listeners;

/**
 * Created by erika on 4/16/2017.
 */

public interface OnDataLoadedListener {
    void onDataSuccessfullyLoaded(Object obj);
    void onDataLoadError(String error);
}