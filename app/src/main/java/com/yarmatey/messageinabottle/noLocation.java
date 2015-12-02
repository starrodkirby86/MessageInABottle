package com.yarmatey.messageinabottle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;
import com.yarmatey.messageinabottle.inventory.Inventory;
import com.yarmatey.messageinabottle.login.LogInActivity;

public class noLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_location);

        final Context context = this;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
        dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
                //get gps
                finish();
            }
        });
        dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        dialog.show();
    }

/*@Override
    public void onResume() {
        super.onResume();
        goToNextIntent();
    }*/

    public void goToNextIntent(){
        //Stack Overflow: Handling GPS not being on
        //http://stackoverflow.com/questions/10311834/how-to-check-if-location-services-are-enabled
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        if(!gps_enabled){
            startActivity(new Intent(noLocation.this, noLocation.class));

        }

        else {
            if (ParseUser.getCurrentUser() != null)
                 startActivity(new Intent(noLocation.this, Inventory.class));
            else
                 startActivity(new Intent(noLocation.this, LogInActivity.class));
        }

        finish();
    }
}


