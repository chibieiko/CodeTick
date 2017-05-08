package com.sankari.erika.codetick.Classes;

import java.util.List;

/**
 * Created by erika on 4/30/2017.
 */

public class ActivitySummary {
    private List<DaySummary> daySummaryList;
    private long average;
    private long total = -1;

    public ActivitySummary() {}

    public List<DaySummary> getDaySummaryList() {
        return daySummaryList;
    }

    public void setDaySummaryList(List<DaySummary> daySummaryList) {
        this.daySummaryList = daySummaryList;
    }

    public long getAverage() {
        return average;
    }

    public void setAverage(long average) {
        this.average = average;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
