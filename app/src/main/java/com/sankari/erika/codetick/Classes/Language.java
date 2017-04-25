package com.sankari.erika.codetick.Classes;

/**
 * Created by erika on 4/25/2017.
 */

public class Language {
    private String name;
    private float percent;
    private int hours;
    private int minutes;

    public Language(String name, float percent, int hours, int minutes) {
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

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
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
