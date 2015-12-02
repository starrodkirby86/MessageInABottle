package com.yarmatey.messageinabottle;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;
import com.yarmatey.messageinabottle.inventory.Inventory;
import com.yarmatey.messageinabottle.login.LogInActivity;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_LENGTH = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //Stack Overflow: Handling GPS not being on
        //http://stackoverflow.com/questions/10311834/how-to-check-if-location-services-are-enabled
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        if(!gps_enabled){
            startActivity(new Intent(SplashScreen.this, noLocation.class));
            finish();
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ParseUser.getCurrentUser() != null)
                        startActivity(new Intent(SplashScreen.this, Inventory.class));
                    else {
                        startActivity(new Intent(SplashScreen.this, LogInActivity.class));
                    }
                    SplashScreen.this.finish();
                }
            }, SPLASH_LENGTH);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
        return true;
    }

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
}
