package com.sankari.erika.codetick.Classes;

import java.io.Serializable;

/**
 * Created by erika on 4/23/2017.
 */

public class ProjectListItem implements Serializable {
    private String name;
    private String id;
    private long time;

    public ProjectListItem() {}

    public ProjectListItem(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
