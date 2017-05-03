package com.sankari.erika.codetick.Classes;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erika on 4/30/2017.
 */

public class DaySummary implements Serializable {
    private List<ProjectListItem> projectList;
    private String date;
    private long total;

    public DaySummary() {}

    public DaySummary(List<ProjectListItem> projectList, String date, long total) {
        this.projectList = projectList;
        this.date = date;
        this.total = total;
    }

    public List<ProjectListItem> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectListItem> projectList) {
        this.projectList = projectList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
