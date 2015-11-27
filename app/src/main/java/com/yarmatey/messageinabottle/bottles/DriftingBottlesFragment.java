package com.yarmatey.messageinabottle.bottles;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.inventory.Inventory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link DriftingBottlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriftingBottlesFragment extends Fragment{

    // TODO: Rename and change types of parameters
    private RecyclerView mRecyclerView;
    public DriftingBottlesAdapter mAdapter;
    private TextView emptyMessage;
    private int newBottleCount;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DriftingBottlesFragment.
     */
    public static DriftingBottlesFragment newInstance() {
        DriftingBottlesFragment fragment = new DriftingBottlesFragment();
        return fragment;
    }

    public DriftingBottlesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newBottleCount = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_drifting_bottles, container, false);

        TextView label = (TextView) v.findViewById(R.id.drifting_label);
        label.setText("Here be ye loot, " + ParseUser.getCurrentUser().getUsername());

        //TODO Implement empty screen
        emptyMessage = (TextView) v.findViewById(R.id.empty_message);
        emptyMessage.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.pirate_booty);
        mRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DriftingBottlesAdapter(getContext(), container);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getItemAnimator().setRemoveDuration(250);
        mRecyclerView.getItemAnimator().setAddDuration(500);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public void addBottle () {
        mAdapter.itemInserted();
        //Create an explicit intent to go to Inventory
        Intent resultIntent = new Intent(getContext(), Inventory.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
                0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm  = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        newBottleCount++;
        String contentTitle;
        if(newBottleCount == 1) {
            contentTitle = "Ye found a new bottle!";
        }
        else {
            contentTitle = "Ye found " + newBottleCount + " new bottles!";
        }
        Notification mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.pirate_hat)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle(contentTitle)
                        .setContentText("Touch this here banner to see!")
                        .build();
        nm.notify(0, mBuilder);
    }
}
