package com.sankari.erika.codetick.Classes;

/**
 * Contains project's data displayed in today fragment.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class TodayProject {

    /**
     * Project's name
     */
    private String name;

    /**
     * Percentage conveying time used for the project today.
     */
    private int percent;

    /**
     * Hours used to code the project today.
     */
    private int hours;

    /**
     * Minutes used to code the project today combined with hours.
     */
    private int minutes;

    /**
     * Total time used to code the project today.
     */
    private long totalInSeconds;

    /**
     * Sets all the variables for the project object.
     *
     * @param name           project's name
     * @param percent        percentage conveying time used for the project today
     * @param hours          hours used to code the project today
     * @param minutes        minutes used to code the project today combined with hours
     * @param totalInSeconds total time used to code the project today
     */
    public TodayProject(String name, int percent, int hours, int minutes, long totalInSeconds) {
        this.name = name;
        this.percent = percent;
        this.hours = hours;
        this.minutes = minutes;
        this.totalInSeconds = totalInSeconds;
    }

    /**
     * Gets project's name.
     *
     * @return project's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets project's name.
     *
     * @param name project's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets percentage conveying time used for the project today.
     *
     * @return percentage conveying time used for the project today
     */
    public int getPercent() {
        return percent;
    }

    /**
     * Sets percentage conveying time used for the project today.
     *
     * @param percent percentage conveying time used for the project today
     */
    public void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     * Gets hours used to code the project today.
     *
     * @return hours used to code the project today
     */
    public int getHours() {
        return hours;
    }

    /**
     * Sets hours used to code the project today.
     *
     * @param hours hours used to code the project today
     */
    public void setHours(int hours) {
        this.hours = hours;
    }

    /**
     * Gets minutes used to code the project today combined with hours.
     *
     * @return minutes used to code the project today combined with hours
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Sets minutes used to code the project today combined with hours.
     *
     * @param minutes minutes used to code the project today combined with hours
     */
    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    /**
     * Gets total time used to code the project today.
     *
     * @return total time used to code the project today
     */
    public long getTotalInSeconds() {
        return totalInSeconds;
    }

    /**
     * Sets total time used to code the project today.
     *
     * @param totalInSeconds total time used to code the project today
     */
    public void setTotalInSeconds(long totalInSeconds) {
        this.totalInSeconds = totalInSeconds;
    }
}
