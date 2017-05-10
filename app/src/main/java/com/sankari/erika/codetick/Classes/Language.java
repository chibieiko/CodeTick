package com.sankari.erika.codetick.Classes;

import java.io.Serializable;

/**
 * Contains language data.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class Language implements Serializable {

    /**
     * Name of the language.
     */
    private String name;

    /**
     * Percentage of time coded with this language.
     */
    private int percent;

    /**
     * Hours coded with this language.
     */
    private int hours;

    /**
     * Minutes coded with this language combined with hours.
     */
    private int minutes;

    /**
     * Creates language object.
     *
     * @param name name of the language
     * @param percent percentage of time coded with this language
     * @param hours hours coded with this language
     * @param minutes minutes coded with this language combined with hours
     */
    public Language(String name, int percent, int hours, int minutes) {
        this.name = name;
        this.percent = percent;
        this.hours = hours;
        this.minutes = minutes;
    }

    /**
     * Gets name of the language.
     *
     * @return name of the language
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the language
     *
     * @param name name of the language
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets percentage of time coded with this language.
     *
     * @return percentage of time coded with this language
     */
    public int getPercent() {
        return percent;
    }

    /**
     * Sets percentage of time coded with this language.
     *
     * @param percent percentage of time coded with this language
     */
    public void setPercent(int percent) {
        this.percent = percent;
    }


    /**
     * Gets hours coded with this language.
     *
     * @return hours coded with this language
     */
    public int getHours() {
        return hours;
    }

    /**
     * Sets hours coded with this language.
     *
     * @param hours hours coded with this language
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * Gets minutes coded with this language combined with hours.
     *
     * @return minutes coded with this language combined with hours
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Sets minutes coded with this language combined with hours.
     *
     * @param minutes minutes coded with this language combined with hours
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
