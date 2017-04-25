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

    public ProjectDetails() {}

    public ProjectDetails(long totalTime, long dailyAverage, List<Language> languages, String lastModified, long bestDayTime, String bestDayDate) {
        this.totalTime = totalTime;
        this.dailyAverage = dailyAverage;
        this.languages = languages;
        this.lastModified = lastModified;
        this.bestDayTime = bestDayTime;
        this.bestDayDate = bestDayDate;
    }

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
}
