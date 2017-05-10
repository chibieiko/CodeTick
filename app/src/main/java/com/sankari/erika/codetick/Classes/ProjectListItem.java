package com.sankari.erika.codetick.Classes;

import java.io.Serializable;

/**
 * Contains project list item data.
 *
 * @author Erika Sankari
 * @version 2017.0509
 * @since 1.7
 */
public class ProjectListItem implements Serializable {

    /**
     * Project's name.
     */
    private String name;

    /**
     * Project's id.
     */
    private String id;

    /**
     * Time used to code the project.
     */
    private long time;

    /**
     * Sets project's name and id.
     *
     * @param name project's name
     * @param id project's id
     */
    public ProjectListItem(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Creates empty project list item.
     */
    public ProjectListItem() {}

    /**
     * Gets project's name.
     *
     * @return project's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets project's name
     *
     * @param name project's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets project's id.
     *
     * @return project's id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets project's id.
     * @param id project's id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets time used to code the project.
     *
     * @return time used to code the project
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets time used to code the project.
     *
     * @param time time used to code the project
     */
    public void setTime(long time) {
        this.time = time;
    }
}
