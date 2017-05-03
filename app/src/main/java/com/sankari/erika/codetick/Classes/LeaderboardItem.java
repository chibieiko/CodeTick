package com.sankari.erika.codetick.Classes;

import java.util.List;

/**
 * Created by erika on 5/3/2017.
 */

public class LeaderboardItem {
    private String name;
    private int rank;
    private long total;
    private long average;
    private List<String> languages;

    public LeaderboardItem() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getAverage() {
        return average;
    }

    public void setAverage(long average) {
        this.average = average;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
