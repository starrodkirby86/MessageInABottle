package com.yarmatey.messageinabottle.displays;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.settings.SettingsActivity;
import com.yarmatey.messageinabottle.bottles.PirateMast;
import com.yarmatey.messageinabottle.message.MessageActivity;

public class Inventory extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //Constant for range of pickup
    public static final double RANGE = .01;

    //Constant for checking minimum accuracy to check for bottles
    public static final double MIN_ACCURACY = 20;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private GoogleApiClient mGoogleApiClient;
    private DriftingBottlesFragment driftingBottlesFragment;
    private PirateMapFragment staticBottleFragment;
    private UserPirateMastFragment userPirateMastFragment;
    public Location currentLocation;


    private String TAG = this.getClass().getSimpleName();


    //Declaring preferences, interval here to be accessed elsewhere in the class (as in in the listener)
    public SharedPreferences preferences;
    public Integer interval;

    //Declaring SharedPreferencesListener here to be constantly available.
    public SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("pickupFreq_list")) {
                //If the the pickupFrequency Interval has changed, we set it to the new state.
                interval=Integer.parseInt(preferences.getString("pickupFreq_list", "600"))*100;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.inventory_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MessageActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //To save shared preferences in an ongoing manner.
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Handle specific cases of id clicks.
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //Launch the Settings Activity
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }

//TODO: Replicate the following onResume as necessary to guarantee knowledge of location services.
    @Override
    public void onResume() {
        super.onResume();
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        if(!gps_enabled){

            //TODO: We have two options gents, we can either close the app off (replace the below with a load of noLocation.java) or continue allowing access
            final Context context = this;
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                }
            });
            dialog.show();
            //TODO: SEE ABOVE. End of the stuff we would replace with a load of noLocation.java.
        }
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected Status: Connected");

        //We'll take the sharedPreferences from preferences (key = pickupFreq_list)
        //We cannot store Integer arrays for whatever reason. So instead, we need to parse a String into an Integer
        //We have 600 = 60 seconds, 60 = 6 seconds, 6 = .6 s.
        interval=Integer.parseInt(preferences.getString("pickupFreq_list", "600"))*100;

        //Change the static interval to the now public dynamic interval.
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(interval); //In milliseconds
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Status: Disconnected");
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connected Status: Failed");
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    /**
     * Given the location is accurate enough, it will check with Parse to see if there is any
     * nearby bottles. If so, it will pick up the bottle and move into local storage and into
     * the picked-up bottle database, to preventing others from picking it up.
     * @param location: current location of the device
     */
    @Override
    public void onLocationChanged(Location location) {
        if (location != null && location.getAccuracy() < MIN_ACCURACY && location.getAccuracy() != 0) {
            Log.i("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude()); //print location in log
            currentLocation = location;
            //Create a point that Parse knows what the location is.
            //ParseQuery<AvailableBottle> query = AvailableBottle.getQuery(point, RANGE);

            //Now to run the query:
//            query.findInBackground(new FindCallback<AvailableBottle>() {
//                @Override
//                public void done(List<AvailableBottle> nearestBottle, ParseException e) {
//                    if (e == null && !nearestBottle.isEmpty()) {
//                        Log.d("location", "Retrieved Lat: " + nearestBottle.get(0).getParseGeoPoint(BottleAttribute.Location.state).getLatitude()
//                                + ", Lon: " + nearestBottle.get(0).getParseGeoPoint(BottleAttribute.Location.state).getLongitude());
//                        PickedUpBottle pickedUpBottle = new PickedUpBottle();
//                        pickedUpBottle.setAll(nearestBottle.get(0));
//                        try {
//                            pickedUpBottle.pin();
//                            pickedUpBottle.save();
//                            AvailableBottle.deleteAll(nearestBottle);
//                        } catch (ParseException e1) {
//                            e1.printStackTrace();
//                        }
//                        if (driftingBottlesFragment != null) {
//                            driftingBottlesFragment.addBottle();
//                        }
//                    } else {
//                        if (e == null) {
//                            Log.d("location", "No Bottles!");
//                        } else {
//                            Log.d("location", "Error: " + e.getMessage());
//                        }
//                    }
//                }
//            });
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                     driftingBottlesFragment= DriftingBottlesFragment.newInstance();
                    return driftingBottlesFragment;
                case 1:
                    staticBottleFragment = PirateMapFragment.newInstance();
                    return staticBottleFragment;
                case 2:
                    userPirateMastFragment = UserPirateMastFragment.newInstance();
                    return UserPirateMastFragment.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ye Own Booty";
                case 1:
                    return "Pirate Map";
                case 2:
                    return "Current User's Locker"; //TODO ADD CURRENT USER
                default:
                    return "Davy Jones' Locker";
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 101) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("type", false)) {
                    //TODO add better functionality to updating mast map
                    staticBottleFragment.newMast();
                }
            }
        }
    }

    public void notifyStaticBottleDataChange(PirateMast mast) {
        staticBottleFragment.notifyChange(mast);
    }
    public void notifyUserMastsDataChange() {
        userPirateMastFragment.notifyChange();
    }
}
