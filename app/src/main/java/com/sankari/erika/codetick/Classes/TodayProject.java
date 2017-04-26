package com.sankari.erika.codetick.Classes;

/**
 * Created by erika on 4/10/2017.
 */

public class TodayProject {
    private String name;
    private int percent;
    private int hours;
    private int minutes;
    private long totalInSeconds;

    public TodayProject(String name, int percent, int hours, int minutes, long totalInSeconds) {
        this.name = name;
        this.percent = percent;
        this.hours = hours;
        this.minutes = minutes;
        this.totalInSeconds = totalInSeconds;
    }

    public TodayProject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public long getTotalInSeconds() {
        return totalInSeconds;
    }

    public void setTotalInSeconds(long totalInSeconds) {
        this.totalInSeconds = totalInSeconds;
    }
}
