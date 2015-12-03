package com.yarmatey.messageinabottle.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.settings.SettingsActivity;


public class MessageActivity extends AppCompatActivity{

    private Spinner options;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        options = (Spinner) findViewById(R.id.bottle_types);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_of_bottles, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        options.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //Launch the Settings Activity
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.drop_bottle) {
            //grab EditText that contains user message

            EditText textView = (EditText) findViewById(R.id.message_edit);

            if (textView.getText().toString().trim().length() > 0) { //if contains characters, and not just whitespace
                FragmentManager fm = getSupportFragmentManager();
                DialogMap dialogMap = new DialogMap();
                Bundle args = new Bundle();
                args.putString("message", textView.getText().toString());
                args.putBoolean("type", options.getSelectedItem().toString().equals("Create a Bottle"));
                dialogMap.setArguments(args);
                dialogMap.show(fm, "Casting yer bottle!");
            } else //No message inserted
                Snackbar.make(textView, "Enter a Message!", Snackbar.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void exitWithValue(boolean mast) {
        Intent intent = new Intent();
        intent.putExtra("type", mast);
        setResult(RESULT_OK, intent);
        finish();
    }
}
