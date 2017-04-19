package com.sankari.erika.codetick.Classes;

/**
 * Created by erika on 4/10/2017.
 */

public class User {

    private String name;
    private String email;
    private String photo;

    public User(String name, String email, String photo) {
        setEmail(email);
        setName(name);
        setPhoto(photo);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Email: " + email;
    }
}
