package com.yarmatey.messageinabottle;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

/**
 * Created by Jason on 10/27/2015.
 */
public class PirateBooty extends RecyclerView.Adapter<PirateBooty.ViewHolder> {

    private ParseQueryAdapter<ParseObject> parseAdapter;

    private ViewGroup parseParent;

    private PirateBooty pirateBooty = this;

    public PirateBooty(Context context, ViewGroup parentIn) {
        parseParent = parentIn;
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ghost");
                query.fromLocalDatastore();
                return query;
            }
        };
        parseAdapter = new ParseQueryAdapter<ParseObject>(context, factory) {
            @Override
            public View getItemView(ParseObject object, View v, ViewGroup parent) {
                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list_item, parent, false);
                }
                super.getItemView(object, v, parent);
                TextView titleText = (TextView) v.findViewById(R.id.card_title);
                TextView description = (TextView) v.findViewById(R.id.card_message);
                titleText.setText("A message be waitin' for ye");
                description.setText(object.get("message").toString());
                return v;
            }
        };
        parseAdapter.addOnQueryLoadListener(new OnQueryLoadListener());
        parseAdapter.loadObjects();
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
                    parseAdapter.getItem(vh.getAdapterPosition()).unpin();
                    parseAdapter.getItem(vh.getAdapterPosition()).delete();
                    //notifyItemRemoved(vh.getAdapterPosition());
                    parseAdapter.loadObjects();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        parseAdapter.getView(position, holder.card, parseParent);
    }

    @Override
    public int getItemCount() {
        return parseAdapter.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected View card;

        public ViewHolder(View v) {
            super(v);
            card = v;
        }
    }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<ParseObject> {

        public void onLoading() {

        }

        public void onLoaded(List<ParseObject> objects, Exception e) {
            pirateBooty.notifyDataSetChanged();
        }
    }

    public void itemInserted() {
        //notifyItemInserted(this.getItemCount());
        parseAdapter.loadObjects();
    }

}
