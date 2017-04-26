package com.sankari.erika.codetick.Classes;

import java.util.List;

/**
 * Created by erika on 4/25/2017.
 */

public class ProjectDetails {
    private long totalTime;
    private long dailyAverage;
    private List<Language> languages;
    private String lastModified;
    private long bestDayTime;
    private String bestDayDate;
    private String name;

    public ProjectDetails() {}

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getDailyAverage() {
        return dailyAverage;
    }

    public void setDailyAverage(long dailyAverage) {
        this.dailyAverage = dailyAverage;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public long getBestDayTime() {
        return bestDayTime;
    }

    public void setBestDayTime(long bestDayTime) {
        this.bestDayTime = bestDayTime;
    }

    public String getBestDayDate() {
        return bestDayDate;
    }

    public void setBestDayDate(String bestDayDate) {
        this.bestDayDate = bestDayDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
