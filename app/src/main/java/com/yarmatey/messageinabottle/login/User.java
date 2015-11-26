package com.yarmatey.messageinabottle.login;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class User extends AppCompatActivity {

    String name;
    String username;
    String password;
    String age;

    public User(String name, String age, String username, String password) {
        this.name = name;
        this.age = age;
        this.username = username;
        this.password = password;
    }

    public User(EditText username, EditText password) {
        this.username = username.toString();
        this.password = password.toString();
        this.age = "-1";
        this.name = "";
    }
}
