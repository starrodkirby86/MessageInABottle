package com.yarmatey.messageinabottle.login;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        etUsername = (EditText) findViewById(R.id.username_prompt);
        etPassword = (EditText) findViewById(R.id.password_prompt);
        etPasswordCheck = (EditText) findViewById(R.id.etPassword2);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);


    }

    @Override
    public void onClick(final View v) {
        switch(v.getId()) {
            case R.id.bRegister:

                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String passwordCheck = etPasswordCheck.getText().toString();
                if (!password.equals(passwordCheck)) {
                    Snackbar.make(v, "Passwords do not match!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                break;
        }


    }
}
