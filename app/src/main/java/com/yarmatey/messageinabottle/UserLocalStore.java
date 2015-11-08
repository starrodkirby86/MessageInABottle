package com.yarmatey.messageinabottle;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

public class UserLocalStore {

    public static final String SP_NAME = "userDetails";
    static SharedPreferences userLocalDatabase;  // Shared preference allows to store data on phone

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public static void storeUserData(User user) {
        //     SharedPreferences userLocalDataBase;
        SharedPreferences.Editor spEditor = userLocalDatabase.edit(); //allows to edit what is in the shared preference
        spEditor.putString("name", user.name);
        spEditor.putInt("age", user.age);
        spEditor.putString("username", user.username);
        spEditor.putString("password", user.password);
        spEditor.commit(); //saves changes from spEdit
    }

    public User getLoggedInUser() { //returns what's contained in logged in user
        String name = userLocalDatabase.getString("name", ""); //default values if no name will be nothing "(nothing)"
        int age = userLocalDatabase.getInt("age", -1); //default value to be -1
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password", "");

        User storedUser = new User(name, age, username, password);

        return storedUser;
    }

    public static void setUserLoggedIn(boolean loggedIn) { //sets user login with true or false
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();  //save changes
    }

    public static boolean getUserLoggedIn() {
        if(userLocalDatabase.getBoolean("LoggedIn" , false) == true) { //if logged in == true
            return true;
        }
        else { // else false
            return  false;
        }
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();  //clear data after log out
        spEditor.commit();  //save changes after log out
    }

}
