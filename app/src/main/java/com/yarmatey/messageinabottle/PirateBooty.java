package com.yarmatey.messageinabottle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jason on 10/27/2015.
 */
public class PirateBooty extends RecyclerView.Adapter<PirateBooty.ViewHolder> {

    private String[] mDataSet;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v;
        }
    }

    public PirateBooty(String [] dataSet) {
        this.mDataSet = dataSet;
    }

    @Override
    public PirateBooty.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataSet[position]);
    }

    public int getItemCount() {
        return mDataSet.length;
    }

}
