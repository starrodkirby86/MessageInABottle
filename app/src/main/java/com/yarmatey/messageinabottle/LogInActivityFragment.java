package com.yarmatey.messageinabottle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


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
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
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
            User user = userLocalStore.getLoggedInUser();

            username.setText(user.username);
            password.setText(user.password);
        }

        //OnClickListener
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), Inventory.class);//Intent to launch to MessageActivity
//            Intent intent2 = new Intent(getActivity(), RegisterActivity.class);
            CheckBox checkBox = (CheckBox) getActivity().findViewById(R.id.checkBox);

            if(authenticate() == true) {
                displayUserDetails();
            }

            //Log In button pressed
            if (v.getId() == R.id.bLogin) {
                //parse log in info


                if(checkBox.isChecked() == true) {
                    User user = new User(username, password);
                    userLocalStore.storeUserData(user);
                    userLocalStore.setUserLoggedIn(true);
                    displayUserDetails();
                }
//                if(v.getId() == R.id.checkBox) {
//                    User user = new User(username, password);
//                    UserLocalStore.storeUserData(user);
//                    UserLocalStore.setUserLoggedIn(true);
//                }

                startActivity(intent);

            }

//            if(v.getId() == R.id.register) {
//                startActivity(intent2);
//            }

            startActivity(intent); //launch to next activity
        }
    }
}
