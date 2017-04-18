package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.User;

/**
 * Created by erika on 4/16/2017.
 */

public interface OnUserDataLoadedListener {
    void onUserDataSuccessfullyLoaded(User obj);
    void onUserDataLoadError(String error);
}