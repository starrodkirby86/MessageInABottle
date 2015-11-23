package com.yarmatey.messageinabottle;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Boris on 10/25/2015.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button bRegister;
    EditText etName, etAge, etUsername, etPassword, etPassword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        final Intent intent_loginf = new Intent(this,LogInActivityFragment.class);

        switch(v.getId()) {
            case R.id.bRegister:

                final String name = etName.getText().toString();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String age = etAge.getText().toString(); //changes what user puts into a int

                //              age = new Integer(Integer.parseInt(age));

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.signUpInBackground(new SignUpCallback() {

                    public void done(ParseException e) {
                        Toast.makeText(getApplicationContext(), "In the thread", Toast.LENGTH_SHORT).show();
                        if (e == null) { //Woo!! Let them use app now.
                    //        @Override
                 //           public void onClick(View v) {
                            Intent intent_login = new Intent(RegisterActivity.this,LogInActivity.class);
                            startActivity(intent_login);
                 //           }
                        }
                        else { //sign up didn't succeed. Look at parse exception to figure what went wrong

                        }
                    }
                });


                    User registeredData = new User(name, age, username, password);

//                    if(name.length() > 0 && username.length() > 0 && password.length() > 0 && age > 0) {
//                        Intent intent = new Intent(this, Inventory.class);
//                    }

                break;
                }


    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }
*/

   /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }
    */
}
