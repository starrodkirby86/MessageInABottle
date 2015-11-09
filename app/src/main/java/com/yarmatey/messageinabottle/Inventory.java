package com.yarmatey.messageinabottle;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ArrayList<ParseObject> bottleList;
    private DriftingBottlesFragment driftingBottlesFragment;


    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        bottleList = new ArrayList<>();
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
                startActivity(intent);
            }
        });

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connected Status: Connected");

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
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

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && location.getAccuracy() < LocationUpdater.MIN_ACCURACY && location.getAccuracy() != 0) {
            Log.i("LOCATION UPDATED TO ", location.getLatitude() + ", " + location.getLongitude()); //print location in log
            //Create a point that Parse knows what the location is.
            ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            //Replicating the below code:
            //ParseGeoPoint userLocation = (ParseGeoPoint) foundBottle.get("location");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("bottle");
            query.whereNear("location", point);
            //Retrieve 1 Bottle. Do not proceed unto 2.
            query.setLimit(1);
            query.whereWithinKilometers("location", point, LocationUpdater.RANGE);
            //Now to run the query:
            final ParseObject foundBottle = new ParseObject("ghost");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> localBottle, ParseException e) {
                    if (e == null && !localBottle.isEmpty()) {
                        Log.d("location", "Retrieved Lat: " + localBottle.get(0).getParseGeoPoint("location").getLatitude() + ", Lon: " + localBottle.get(0).getParseGeoPoint("location").getLongitude());
                        ParseGeoPoint bottleLoc = localBottle.get(0).getParseGeoPoint("location");
                        foundBottle.put("location", bottleLoc);
                        String message = localBottle.get(0).getString("message");
                        foundBottle.put("message", message);
                        int type = localBottle.get(0).getInt("type");
                        foundBottle.put("type", type);
                        foundBottle.saveInBackground();
                        try {
                            ParseObject.pinAll(localBottle);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            Toast.makeText(getApplicationContext(), "DX", Toast.LENGTH_SHORT).show();
                        }
                        localBottle.get(0).deleteInBackground();
                        //message.setText(foundBottle.getString("message"));
                        Toast.makeText(getApplicationContext(), foundBottle.getString("message"), Toast.LENGTH_SHORT).show();
                        if (!bottleList.contains(foundBottle.get("message").toString())) {
                            bottleList.add(foundBottle);
                            if (driftingBottlesFragment != null) {
                                driftingBottlesFragment.addBottle(foundBottle);
                            }
                        }
                    } else {
                        if (e == null) {
                            Log.d("location", "No Bottles!");
                        } else {
                            Log.d("location", "Error: " + e.getMessage());
                        }
                    }
                }
            });
//            if (foundBottle.get("message") != null && !bottleList.contains(foundBottle.get("message").toString())) {
//                bottleList.add(foundBottle.get("message").toString());
//                if (driftingBottlesFragment != null) {
//                    driftingBottlesFragment.addBottle(foundBottle.get("message").toString());
//                }
//            }
        }
    }

    public ArrayList<ParseObject> getBottleList() {
        return bottleList;
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
                     driftingBottlesFragment= DriftingBottlesFragment.newInstance(bottleList);
                    return driftingBottlesFragment;
                case 1:
                    return StaticBottlesFragment.newInstance("Foo", "bar");
                case 2:
                    return PlaceholderFragment.newInstance(2);
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
                    return "Pirate Masts";
                case 2:
                    return "Davy Jones' Locker";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_inventory, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));


            //Notification [DEBUG ONLY]

//            //Create an explicit intent to go to Inventory
//            Intent resultIntent = new Intent(getContext(), Inventory.class);
//            PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
//                    0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//            NotificationManager nm  = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//            Notification mBuilder =
//                    new NotificationCompat.Builder(getContext())
//                    .setContentIntent(contentIntent)
//                    .setSmallIcon(R.drawable.pirate_hat)
//                    .setWhen(System.currentTimeMillis())
//                    .setAutoCancel(true)
//                    .setContentTitle("Ye found me booty!")
//                    .setContentText("Check ye booty to see")
//                    .build();
//            nm.notify(0, mBuilder);




            return rootView;
        }
    }
}
