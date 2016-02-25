package com.yarmatey.messageinabottle.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.displays.Inventory;


/**
 * A placeholder fragment containing a simple view.
 */
public class LogInActivityFragment extends Fragment {
    /*
        This fragment is used to implement the log in screen.

        TODO Check out built in Log in screen. Possible way better than this fragment
     */
    private String LOG_TAG = LogInActivityFragment.class.getSimpleName(); //Log tag for ADB

    public LogInActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflator to show XML screen
        View view =  inflater.inflate(R.layout.fragment_log_in, container, false);

        //Load in objects needed to log in
        final Button button = (Button) view.findViewById(R.id.login_button);
        final TextView register = (TextView) view.findViewById(R.id.register_here);
        final TextView anon = (TextView) view.findViewById(R.id.anon_log_in);
        final EditText username = (EditText) view.findViewById(R.id.username_prompt);
        final EditText password = (EditText) view.findViewById(R.id.password_prompt);


        //anonymous log in click
        anon.setOnClickListener(new logInClick());

        //log in with user/pass
        button.setOnClickListener(new logInClick(username, password));


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        return view;
    }

    public class logInClick implements View.OnClickListener { //when user clicks
        /*
            This class parses the username and password from the login activity
            sets on click listener to set for when log in button is pressed
         */
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
        private void displayUserDetails() {
            //User user = userLocalStore.getLoggedInUser();
            //password.setText();
        }

        //OnClickListener
        @Override
        public void onClick(final View v) {
            final Intent intent = new Intent(getActivity(), Inventory.class);//Intent to launch to MessageActivity
//            Intent intent2 = new Intent(getActivity(), RegisterActivity.class);

            //Log In button pressed
            if (v.getId() == R.id.login_button) {
                //TODO LOG IN
            }
            else if (v.getId() == R.id.anon_log_in) {
                //TODO ANON LOG IN
            }

        }
    }
}
