package com.yarmatey.messageinabottle.login;


import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public final String SP_NAME = "userDetails";
    static SharedPreferences userLocalDatabase;  // Shared preference allows to store data on phone

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user) {
        //     SharedPreferences userLocalDataBase;
        SharedPreferences.Editor spEditor = userLocalDatabase.edit(); //allows to edit what is in the shared preference
        spEditor.putString("name", user.name);
        spEditor.putString("age", user.age);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.commit(); //saves changes from spEdit
    }

    public User getLoggedInUser() { //returns what's contained in logged in user
        String name = userLocalDatabase.getString("name", ""); //default values if no name will be nothing "(nothing)"
        String age = userLocalDatabase.getString("age", "-1"); //default value to be -1
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");

        User storedUser = new User(name, age, username, password);

        return storedUser;
    }

    public void setUserLoggedIn(boolean loggedIn) { //sets user login with true or false
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();  //save changes
    }

    public boolean getUserLoggedIn() {
       if(userLocalDatabase.getBoolean("LoggedIn", false)) { //if logged in == true
            return true;
        }
        else { // else false
            return  false;
        }

 /*       SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("LoggedIn",spEditor);
        */
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();  //clear data after log out
        spEditor.commit();  //save changes after log out
    }

}
