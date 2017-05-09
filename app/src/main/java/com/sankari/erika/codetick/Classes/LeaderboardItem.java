package com.sankari.erika.codetick.Classes;

import java.util.List;

/**
 * Contains leaderboard item data.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class LeaderboardItem {

    /**
     * User's name.
     */
    private String name;

    /**
     * User's rank.
     */
    private int rank;

    /**
     * User's total time coded in the past 7 days.
     */
    private long total;

    /**
     * User's daily average time from the past 7 days.
     */
    private long average;

    /**
     * List of languages the user has coded with.
     */
    private List<String> languages;

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
     * Gets user's rank.
     *
     * @return user's rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets user's rank.
     *
     * @param rank user's rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Gets user's total time coded in the past 7 days.
     *
     * @return user's total time coded in the past 7 days
     */
    public long getTotal() {
        return total;
    }

    /**
     * Sets user's total time coded in the past 7 days.
     *
     * @param total user's total time coded in the past 7 days
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * Gets user's daily average time from the past 7 days.
     *
     * @return user's daily average time from the past 7 days
     */
    public long getAverage() {
        return average;
    }

    /**
     * Sets user's daily average time from the past 7 days.
     *
     * @param average user's daily average time from the past 7 days
     */
    public void setAverage(long average) {
        this.average = average;
    }

    /**
     * Gets list of languages the user has coded with.
     *
     * @return list of languages the user has coded with
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * Sets list of languages the user has coded with.
     *
     * @param languages list of languages the user has coded with
     */
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
