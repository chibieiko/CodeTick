package com.sankari.erika.codetick.Classes;

import java.util.List;

/**
 * Created by erika on 4/17/2017.
 */

public class TodaySummary {
    private List<Project> projectList;
    private long totalTime;

    public TodaySummary() {
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
