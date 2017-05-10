package com.sankari.erika.codetick.Classes;

import java.util.List;

/**
 * Contains project's details.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ProjectDetails {

    /**
     * Total time used to code the project in the past 7 days.
     */
    private long totalTime;

    /**
     * Daily average time used to code the project.
     */
    private long dailyAverage;

    /**
     * List of languages used for the project.
     */
    private List<Language> languages;

    /**
     * Time coded on the best day.
     */
    private long bestDayTime;

    /**
     * The date when coding time for the project was the highest.
     */
    private String bestDayDate;

    /**
     * Name of the project.
     */
    private String name;

    /**
     * Gets total time used to code the project in the past 7 days.
     *
     * @return total time used to code the project
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * Sets total time used to code the project in the past 7 days
     *
     * @param totalTime total time used to code the project
     */
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * Gets daily average time used to code the project.
     *
     * @return daily average time used to code the project
     */
    public long getDailyAverage() {
        return dailyAverage;
    }

    /**
     * Sets daily average time used to code the project.
     *
     * @param dailyAverage daily average time used to code the project
     */
    public void setDailyAverage(long dailyAverage) {
        this.dailyAverage = dailyAverage;
    }

    /**
     * Gets list of languages used for the project.
     *
     * @return list of languages used for the project
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * Sets list of languages used for the project.
     *
     * @param languages list of languages used for the project
     */
    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    /**
     * Gets time coded on the best day.
     *
     * @return time coded on the best day
     */
    public long getBestDayTime() {
        return bestDayTime;
    }

    /**
     * Sets time coded on the best day.
     *
     * @param bestDayTime time coded on the best day
     */
    public void setBestDayTime(long bestDayTime) {
        this.bestDayTime = bestDayTime;
    }

    /**
     * Gets the date when coding time for the project was the highest.
     *
     * @return the date when coding time for the project was the highest
     */
    public String getBestDayDate() {
        return bestDayDate;
    }

    /**
     * Sets the date when coding time for the project was the highest.
     *
     * @param bestDayDate the date when coding time for the project was the highest
     */
    public void setBestDayDate(String bestDayDate) {
        this.bestDayDate = bestDayDate;
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
}
