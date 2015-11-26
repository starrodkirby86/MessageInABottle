package com.yarmatey.messageinabottle.inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.bottles.AvailableBottle;
import com.yarmatey.messageinabottle.bottles.PickedUpBottle;

import java.util.List;

/**
 * Created by Jason on 10/27/2015.
 */

public class DriftingBottlesAdapter extends RecyclerView.Adapter<DriftingBottlesAdapter.ViewHolder> {

    private ParseQueryAdapter<PickedUpBottle> parseAdapter;

    private ViewGroup parseParent;

    private DriftingBottlesAdapter driftingBottlesAdapter = this;
    public boolean isEmpty;

    public DriftingBottlesAdapter(Context context, ViewGroup parentIn) {
        parseParent = parentIn;
        ParseQueryAdapter.QueryFactory<PickedUpBottle> factory = new ParseQueryAdapter.QueryFactory<PickedUpBottle>() {
            @Override
            public ParseQuery<PickedUpBottle> create() {
                ParseQuery<PickedUpBottle> query = PickedUpBottle.getQuery();
                query.fromLocalDatastore();
                return query;
            }
        };
        parseAdapter = new ParseQueryAdapter<PickedUpBottle>(context, factory) {
            @Override
            public View getItemView(PickedUpBottle object, View v, ViewGroup parent) {
                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
                }
                super.getItemView(object, v, parent);
                TextView titleText = (TextView) v.findViewById(R.id.card_title);
                TextView description = (TextView) v.findViewById(R.id.card_message);
                String title;
                try {
                    //TODO make this cleaner
                    title = "A message from " + object.getLastUser().fetchIfNeeded().getUsername();
                } catch (ParseException e) {
                    title = "A message from a pirate";
                    e.printStackTrace();
                }
                titleText.setText(title);
                description.setText(object.getMessage());
                return v;
            }
        };
        parseAdapter.addOnQueryLoadListener(new OnQueryLoadListener());
        parseAdapter.loadObjects();
        setHasStableIds(true);

    }

    @Override
    public DriftingBottlesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        ImageView delete = (ImageView) v.findViewById(R.id.delete);
        ImageView edit = (ImageView) v.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Drop ye bottle?")
                        .setMessage("Add your feelings to how this bottle be")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PickedUpBottle pickedUpBottle = parseAdapter.getItem(vh.getAdapterPosition());
                                ParseGeoPoint point = pickedUpBottle.getPoint(); //currentLocation.getLatitude(), currentLocation.getLongitude());
                                AvailableBottle newBottle = new AvailableBottle();
                                newBottle.setAll(pickedUpBottle);
                                newBottle.setLastUser(ParseUser.getCurrentUser());
                                newBottle.setPoint(point);
                                newBottle.saveInBackground();
                                try {
                                    pickedUpBottle.delete();
                                    driftingBottlesAdapter.notifyItemRemoved(vh.getAdapterPosition());
                                    parseAdapter.loadObjects();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    parseAdapter.getItem(vh.getAdapterPosition()).unpin();
                    parseAdapter.getItem(vh.getAdapterPosition()).delete();
                    driftingBottlesAdapter.notifyItemRemoved(vh.getAdapterPosition());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected View card;

        public ViewHolder(View v) {
            super(v);
            card = v;
        }
    }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<PickedUpBottle> {

        public void onLoading() {

        }

        public void onLoaded(List<PickedUpBottle> objects, Exception e) {
            driftingBottlesAdapter.notifyDataSetChanged();
            isEmpty = objects.isEmpty();

        }
    }

    public void itemInserted() {
        //notifyItemInserted(this.getItemCount());
        parseAdapter.loadObjects();
        //driftingBottlesAdapter.notifyItemInserted(this.getItemCount());

    }




}
