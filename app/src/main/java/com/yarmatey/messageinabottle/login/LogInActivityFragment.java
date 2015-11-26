package com.yarmatey.messageinabottle.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.inventory.Inventory;


/**
 * A placeholder fragment containing a simple view.
 */
public class LogInActivityFragment extends Fragment {
    /*
        This fragment is used to implement the log in screen.

        TODO Check out built in Log in screen. Possible way better than this fragment
     */
    private String LOG_TAG = LogInActivityFragment.class.getSimpleName(); //Log tag for ADB

    private UserLocalStore userLocalStore;

    public LogInActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflator to show XML screen
        View view =  inflater.inflate(R.layout.fragment_log_in, container, false);

        //Load in objects needed to log in
        final Button button = (Button) view.findViewById(R.id.bLogin);
        final Button button2 = (Button) view.findViewById(R.id.register);
        final TextView anon = (TextView) view.findViewById(R.id.anon_log_in);
        final EditText username = (EditText) view.findViewById(R.id.etUsername);
        final EditText password = (EditText) view.findViewById(R.id.etPassword);
        final TextView logout = (TextView) view.findViewById(R.id.log_out);
        userLocalStore = new UserLocalStore(getContext()); //use this instead of class


        //anonymous log in click
        anon.setOnClickListener(new logInClick());

        //log in with user/pass
        button.setOnClickListener(new logInClick(username, password));


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseUser.getCurrentUser() != null) {
                    Snackbar.make(v, "Logging out of: " + ParseUser.getCurrentUser().getUsername(), Snackbar.LENGTH_SHORT).show();
                    ParseUser.logOut();
                }
            }
        });

        return view;
    }



//    private void displayUserDetails() {
//        User user = UserLocalStore.getUserLoggedIn();
//        username.setText();
//    }

    public class logInClick implements View.OnClickListener { //when user clicks
        /*
            This class parses the username and password from the login activity
            sets on click listener to set for when log in button is pressed
         */
        //TODO Login with Facebook?
        //stores usernames
        //TODO add persistance for auto-login
  //      private EditText username;
        //stores password
   //     private EditText password;

        EditText username; //changed
        EditText password;




        //CONSTRUCTORS
        public logInClick(EditText u, EditText p) { // retrieves username and password
            this.username = u;
            this.password = p;
        }
        public logInClick() { // for anonymous entry
            this.username = null;
            this.password = null;
        }

        private boolean authenticate() {

            return  userLocalStore.getUserLoggedIn();
        }
        private void displayUserDetails() {
            //User user = userLocalStore.getLoggedInUser();
            ParseUser user = ParseUser.getCurrentUser();
            if (user != null)
                username.setText(user.getUsername());
            //password.setText();
        }

        //OnClickListener
        @Override
        public void onClick(final View v) {
            final Intent intent = new Intent(getActivity(), Inventory.class);//Intent to launch to MessageActivity
//            Intent intent2 = new Intent(getActivity(), RegisterActivity.class);
            CheckBox checkBox = (CheckBox) getActivity().findViewById(R.id.checkBox);

            if(authenticate()) {
                displayUserDetails();
            }

            //Log In button pressed
            if (v.getId() == R.id.bLogin) {
                //parse log in info
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser != null) {
                            startActivity(intent);
                        }
                        else { //signup failed. look at parseExceptioin to see what happened
                            Snackbar.make(v,"Invalid username or password, try again!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });


                if(checkBox.isChecked()) {
                    User user = new User(username, password);
                    userLocalStore.storeUserData(user);
                    userLocalStore.setUserLoggedIn(true);
                    displayUserDetails();
                }

            }
            else if (v.getId() == R.id.anon_log_in) {
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e == null) {
                            Log.d("Message In A Bottle", "LOG IN STATUS: Anonymous login succeed!");
                            startActivity(intent);
                        }
                        else {
                            Log.d("Message In A Bottle", "LOG IN STATUS: Anonymous Login Failed!");
                        }
                    }
                });
            }

        }
    }
}
