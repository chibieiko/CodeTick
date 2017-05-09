package com.sankari.erika.codetick.Classes;

import java.util.List;

/**
 * Contains activity summary data.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ActivitySummary {

    /**
     * List of day summary objects.
     */
    private List<DaySummary> daySummaryList;

    /**
     * Daily average coding time of the past two weeks.
     */
    private long average;

    /**
     * Total time coded in the past two weeks.
     */
    private long total = -1;

    /**
     * Gets day summary list.
     *
     * @return day summary list
     */
    public List<DaySummary> getDaySummaryList() {
        return daySummaryList;
    }

    /**
     * Sets day summary list.
     *
     * @param daySummaryList day summary list
     */
    public void setDaySummaryList(List<DaySummary> daySummaryList) {
        this.daySummaryList = daySummaryList;
    }

    /**
     * Gets daily average coding time of the past two weeks.
     *
     * @return daily average time
     */
    public long getAverage() {
        return average;
    }

    /**
     * Sets daily average coding time of the past two weeks.
     *
     * @param average daily average time
     */
    public void setAverage(long average) {
        this.average = average;
    }

    /**
     * Gets total time coded in the past two weeks.
     *
     * @return total time coded
     */
    public long getTotal() {
        return total;
    }

    /**
     * Sets total time coded in the pas two weeks.
     *
     * @param total total time coded
     */
    public void setTotal(long total) {
        this.total = total;
    }
}
