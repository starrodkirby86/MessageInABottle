package com.yarmatey.messageinabottle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DriftingBottlesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DriftingBottlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriftingBottlesFragment extends Fragment{

    // TODO: Rename and change types of parameters
    private RecyclerView mRecyclerView;
    public PirateBooty mAdapter;
    private OnFragmentInteractionListener mListener;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data Parameter 1.
     * @return A new instance of fragment DriftingBottlesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriftingBottlesFragment newInstance(ArrayList<ParseObject> data) {
        DriftingBottlesFragment fragment = new DriftingBottlesFragment();
        return fragment;
    }

    public DriftingBottlesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_drifting_bottles, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.pirate_booty);
        //TODO add empty view functionality
        mRecyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new PirateBooty("ghost", false);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void addBottle (ParseObject message) {
        mAdapter.notifyItemInserted(mAdapter.getItemCount());
        mAdapter.loadObjects();


        //Create an explicit intent to go to Inventory
        Intent resultIntent = new Intent(getContext(), Inventory.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
                0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm  = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Notification mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setContentIntent(contentIntent)
                        .setSmallIcon(R.drawable.pirate_hat)
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle("Ye found me booty!")
                        .setContentText("Check ye booty to see")
                        .build();
        nm.notify(0, mBuilder);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
