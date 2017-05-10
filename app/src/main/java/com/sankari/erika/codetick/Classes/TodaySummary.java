package com.sankari.erika.codetick.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains today summary data.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class TodaySummary {

    /**
     * Contains today project items.
     */
    private List<TodayProject> todayProjectList;

    /**
     * Total time coded today.
     */
    private long totalTime;

    /**
     * Sets initial values for today project list and total time.
     */
    public TodaySummary() {
        setTodayProjectList(new ArrayList<TodayProject>());
        setTotalTime(-1);
    }

    /**
     * Gets today project list.
     *
     * @return today project list
     */
    public List<TodayProject> getTodayProjectList() {
        return todayProjectList;
    }

    /**
     * Sets today project list.
     *
     * @param todayProjectList today project list
     */
    public void setTodayProjectList(List<TodayProject> todayProjectList) {
        this.todayProjectList = todayProjectList;
    }

    /**
     * Gets total time coded today.
     *
     * @return total time coded today
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * Sets total time coded today.
     *
     * @param totalTime total time coded today
     */
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * Prints today summary object's variables and their values.
     *
     * @return string of today summary object's variables and their values
     */
    @Override
    public String toString() {
        String summary = "Total time: " + totalTime;
        for (TodayProject todayProject : todayProjectList) {
            summary += " ProjectName: " + todayProject.getName();
        }

        return summary;
    }
}
