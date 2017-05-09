package com.sankari.erika.codetick.Classes;

import java.io.Serializable;
import java.util.List;

/**
 * Contains day summary data.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class DaySummary implements Serializable {

    /**
     * Contains project list items.
     */
    private List<ProjectListItem> projectList;

    /**
     * Date of the day.
     */
    private String date;

    /**
     * Total time coded during the day.
     */
    private long total;

    /**
     * Gets project list.
     *
     * @return project list
     */
    public List<ProjectListItem> getProjectList() {
        return projectList;
    }

    /**
     * Sets project list.
     *
     * @param projectList project list
     */
    public void setProjectList(List<ProjectListItem> projectList) {
        this.projectList = projectList;
    }

    /**
     * Gets date of the day.
     *
     * @return date of the day
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date of the day.
     *
     * @param date date of the day
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets total time coded during the day.
     *
     * @return total time coded
     */
    public long getTotal() {
        return total;
    }

    /**
     * Sets total time coded during the day.
     *
     * @param total total time coded
     */
    public void setTotal(long total) {
        this.total = total;
    }
}
