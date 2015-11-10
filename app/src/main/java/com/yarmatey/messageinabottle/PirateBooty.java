package com.yarmatey.messageinabottle;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

/**
 * Created by Jason on 10/27/2015.
 */
public class PirateBooty extends ParseRecyclerQueryAdapter<ParseObject, PirateBooty.ViewHolder> {

    public PirateBooty(String className, boolean hasStableIds) {
        super(className, hasStableIds);
    }

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


    @Override
    public PirateBooty.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleText.setText("A message be waitin' for ye");
        holder.description.setText(getItem(position).get("message").toString());
        holder.card.setCardBackgroundColor(R.color.colorAccent);
    }
}
