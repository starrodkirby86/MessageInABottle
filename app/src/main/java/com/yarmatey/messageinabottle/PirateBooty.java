package com.yarmatey.messageinabottle;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
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
        final ViewHolder vh = new ViewHolder(v);
        ImageView delete = (ImageView) v.findViewById(R.id.delete);
        ImageView edit = (ImageView) v.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), MessageActivity.class));
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getItem(vh.getAdapterPosition()).unpin();
                    getItem(vh.getAdapterPosition()).delete();
                    notifyItemRemoved(vh.getAdapterPosition());
                    loadObjects();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleText.setText("A message be waitin' for ye");
        holder.description.setText(getItem(holder.getAdapterPosition()).get("message").toString());

    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
