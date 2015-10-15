package com.yarmatey.messageinabottle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
        final Button button = (Button) view.findViewById(R.id.log_in);
        final TextView anon = (TextView) view.findViewById(R.id.anon_log_in);
        final EditText username = (EditText) view.findViewById(R.id.username);
        final EditText password = (EditText) view.findViewById(R.id.password);


        //anonymous log in click
        anon.setOnClickListener(new logInClick());

        //log in with user/pass
        button.setOnClickListener(new logInClick(username, password));

        return view;
    }

    public class logInClick implements View.OnClickListener { //when user clicks
        /*
            This class parses the username and password from the login activity
            sets on click listener to set for when log in button is pressed
         */
        //TODO Login with Facebook?
        //stores usernames
        //TODO add persistance for auto-login
        private EditText username;
        //stores password
        private EditText password;

        //CONSTRUCTORS
        public logInClick(EditText u, EditText p) { // retrieves username and password
            this.username = u;
            this.password = p;
        }
        public logInClick() { // for anonymous entry
            this.username = null;
            this.password = null;
        }
        //OnClickListener
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MessageActivity.class);//Intent to launch to MessageActivity

            //Log In button pressed
            if (v.getId() == R.id.log_in) {
                //parse log in info
                String user = username.getText().toString();
                String pass = password.getText().toString();
                //TODO add check with server for valid user/pass
                if (pass.length() > 0 &&
                        user.length() > 0) {
                    intent.putExtra("password", pass)
                            .putExtra("username", user); //add user/pass to next screen [DO WE NEED PASSWORD?]
                } else {
                    Toast.makeText(getContext(), "Invalid Username / Password: Try Again", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            startActivity(intent); //launch to next activity
        }
    }
}
