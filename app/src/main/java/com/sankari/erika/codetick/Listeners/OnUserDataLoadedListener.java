package com.sankari.erika.codetick.Listeners;

import com.sankari.erika.codetick.Classes.User;

/**
 * Listens for when user data has loaded.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public interface OnUserDataLoadedListener {

    /**
     * Provides a user object.
     *
     * @param obj holds information about the user
     */
    void onUserDataSuccessfullyLoaded(User obj);

    /**
     * Provides error message.
     *
     * @param error error message
     */
    void onUserDataLoadError(String error);
}