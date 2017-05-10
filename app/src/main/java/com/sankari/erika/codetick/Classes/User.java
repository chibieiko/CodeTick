package com.sankari.erika.codetick.Classes;

/**
 * Contains user data.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class User {

    /**
     * User's name.
     */
    private String name;

    /**
     * User's email.
     */
    private String email;

    /**
     * User's icon.
     */
    private String icon;

    /**
     * Sets user's name, email and icon.
     *
     * @param name  user's name
     * @param email user's email
     * @param icon  user's icon
     */
    public User(String name, String email, String icon) {
        setEmail(email);
        setName(name);
        setIcon(icon);
    }

    /**
     * Gets user's name.
     *
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets user's name.
     *
     * @param name user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets user's email.
     *
     * @return user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets user's email.
     *
     * @param email user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets user's icon.
     *
     * @return user's icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets user's icon.
     *
     * @param icon user's icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Prints user's name and email.
     *
     * @return string containing user's name and email
     */
    @Override
    public String toString() {
        return "Name: " + name + ", Email: " + email;
    }
}
