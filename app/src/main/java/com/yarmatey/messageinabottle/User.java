package com.yarmatey.messageinabottle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class User extends AppCompatActivity {

    String name;
    String username;
    String password;
    int age;

    public User(String name, int age, String username, String password) {
        this.name = name.toString();
        this.age = age;
        this.username = username.toString();
        this.password = password.toString();
    }

    public User(EditText username, EditText password) {
        this.username = username.toString();
        this.password = password.toString();
        this.age = -1;
        this.name = "";
    }
}
