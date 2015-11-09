package com.yarmatey.messageinabottle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Jason on 10/27/2015.
 */
public class PirateBooty extends RecyclerView.Adapter<PirateBooty.ViewHolder> {

    private List<ParseObject> mDataSet;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView titleText;
        protected TextView description;
        protected CardView card;
        public ViewHolder(View v) {
            super(v);
            titleText = (TextView) v.findViewById(R.id.card_title);
            description = (TextView) v.findViewById(R.id.card_message);
            card = (CardView) v;
        }
    }

    public PirateBooty(List<ParseObject> dataSet) {
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
        if (!mDataSet.isEmpty()) {
            holder.titleText.setText("New Message");
            holder.description.setText(mDataSet.get(position).toString());
        }
        else {
            holder.titleText.setText("There be no message here!");
            holder.description.setText("Only be a pirate's tale.");
        }
        holder.card.setCardBackgroundColor(R.color.colorAccent);
    }

    public int getItemCount() {
        return mDataSet.size();
    }

}
