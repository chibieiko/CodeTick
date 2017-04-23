package com.sankari.erika.codetick.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erika on 4/17/2017.
 */

public class TodaySummary {
    private List<TodayProject> todayProjectList;
    private long totalTime;

    public TodaySummary() {
        setTodayProjectList(new ArrayList<TodayProject>());
        setTotalTime(0);
    }

    public List<TodayProject> getTodayProjectList() {
        return todayProjectList;
    }

    public void setTodayProjectList(List<TodayProject> todayProjectList) {
        this.todayProjectList = todayProjectList;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        String summary = "Total time: " + totalTime;
        for (TodayProject todayProject : todayProjectList) {
            summary += " ProjectName: " + todayProject.getName();
        }

        return summary;
    }
}
