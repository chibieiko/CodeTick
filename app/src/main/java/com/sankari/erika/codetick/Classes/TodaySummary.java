package com.sankari.erika.codetick.Classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erika on 4/17/2017.
 */

public class TodaySummary {
    private List<Project> projectList;
    private long totalTime;

    public TodaySummary() {
        setProjectList(new ArrayList<Project>());
        setTotalTime(0);
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

    @Override
    public String toString() {
        String summary = "Total time: " + totalTime;
        for (Project project : projectList) {
            summary += " ProjectName: " + project.getName();
        }

        return summary;
    }
}
