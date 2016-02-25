package com.yarmatey.messageinabottle.login;

/**
 * Created by jason on 2/23/16.
 */
public class User {

    private String username;
    private String email;

    public User() {
        username = "pirate";
        email = "none@none.com";
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
