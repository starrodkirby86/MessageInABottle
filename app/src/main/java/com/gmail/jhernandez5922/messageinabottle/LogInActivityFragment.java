package com.gmail.jhernandez5922.messageinabottle;

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


    private String LOG_TAG = LogInActivityFragment.class.getSimpleName();




    public LogInActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_log_in, container, false);

        final Button button = (Button) view.findViewById(R.id.log_in);
        final TextView anon = (TextView) view.findViewById(R.id.anon_log_in);
        final EditText username = (EditText) view.findViewById(R.id.username);
        final EditText password = (EditText) view.findViewById(R.id.password);
        anon.setOnClickListener(new logInClick());
        button.setOnClickListener(new logInClick(username, password));
        return view;
    }

    public class logInClick implements View.OnClickListener { //when user clicks
        private EditText username;
        private EditText password;
        public logInClick(EditText u, EditText p) { // retrieves username and password
            this.username = u;
            this.password = p;
        }
        public logInClick() { // for anonymous entry
            this.username = null;
            this.password = null;
        }
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MessageActivity.class);
            if (v.getId() == R.id.log_in) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (pass.length() > 0 &&
                        user.length() > 0) {
                    intent.putExtra("password", pass)
                            .putExtra("username", user);
                } else {
                    Toast.makeText(getContext(), "Invalid Username / Password: Try Again", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            startActivity(intent);
        }
    }
}
