package com.sankari.erika.codetick.Classes;

import java.io.Serializable;

/**
 * Created by erika on 4/25/2017.
 */

public class Language implements Serializable {
    private String name;
    private int percent;
    private int hours;
    private int minutes;

    public Language(String name, int percent, int hours, int minutes) {
        this.name = name;
        this.percent = percent;
        this.hours = hours;
        this.minutes = minutes;
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
}
