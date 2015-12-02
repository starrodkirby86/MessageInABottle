package com.yarmatey.messageinabottle.inventory;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.bottles.UserPirateMastAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserPirateMastFragment extends Fragment {

    private RecyclerView mRecyclerView;
    public UserPirateMastAdapter mAdapter;

    public UserPirateMastFragment() {
    }


    public static UserPirateMastFragment newInstance() {
        UserPirateMastFragment fragment = new UserPirateMastFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_inventory, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.pirate_booty);
        mRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserPirateMastAdapter(getContext(), container);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getItemAnimator().setRemoveDuration(250);
        mRecyclerView.getItemAnimator().setAddDuration(500);
        return v;
    }
}
