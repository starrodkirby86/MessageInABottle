package com.yarmatey.messageinabottle.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.yarmatey.messageinabottle.R;

/**
 * Created by Boris on 10/25/2015.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    EditText etName, etAge, etUsername, etPassword, etPasswordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordCheck = (EditText) findViewById(R.id.etPassword2);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bRegister:

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String passwordCheck = etPasswordCheck.getText().toString();
                if (!password.equals(passwordCheck)) {
                    Snackbar.make(v, "Passwords do not match!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {

                    public void done(ParseException e) {
                        Toast.makeText(getApplicationContext(), "In the thread", Toast.LENGTH_SHORT).show();
                        if (e == null) { //Woo!! Let them use app now.
                            Intent intent_login = new Intent(RegisterActivity.this,LogInActivity.class);
                            startActivity(intent_login);
                 //           }
                        }
                        else {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }


    }
}
