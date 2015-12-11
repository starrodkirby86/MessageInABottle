package com.yarmatey.messageinabottle.bottles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import java.util.List;

/**
 * Created by Jason on 10/27/2015.
 */

public class DriftingBottlesAdapter extends RecyclerView.Adapter<DriftingBottlesAdapter.ViewHolder> {

    private static final String BEST_RATING = "Yar Har! + ";
    private static final String GOOD_RATING = "Aye + ";
    private static final String BAD_RATING = "Nay + ";
    private static final String WORST_RATING = "Scurvy! + ";


    private ParseQueryAdapter<PickedUpBottle> parseAdapter;

    private ViewGroup parseParent;

    private DriftingBottlesAdapter driftingBottlesAdapter = this;
    public boolean isEmpty;

    //Declaring preferences, warning on discard to be accessed later
    public SharedPreferences preferences;
    public boolean discardWarn = true;

    //Declaring SharedPreferencesListener here to be constantly available.
    public SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("discardWarning_switch")) {
                //If the switch has changed, we make it the new value. Note that if we cannot retrieve, set the warning to true anyways.
                discardWarn = preferences.getBoolean("discardWarning_switch",true);
            }
        }
    };

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
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bottle, parent, false);
                }
                super.getItemView(object, v, parent);
                TextView titleText = (TextView) v.findViewById(R.id.pirate_mast_title);
                TextView description = (TextView) v.findViewById(R.id.pirate_mast_message);
                String title="A message from ";
                try {
                    if(object.getLastUser().fetchIfNeeded().getUsername().length()<=15)
                        title = title + object.getLastUser().fetchIfNeeded().getUsername();
                    else
                        title=title+"a pirate";
                } catch (ParseException e) {
                    title = title+"a pirate";
                    e.printStackTrace();
                }
                titleText.setText(title);
                description.setText(object.getMessage());
                List<Integer> ratings = object.getRatings();
                ((TextView) v.findViewById(R.id.yar_har_rating)).setText(BEST_RATING + ratings.get(0));
                ((TextView) v.findViewById(R.id.aye_rating)).setText(GOOD_RATING + ratings.get(1));
                ((TextView) v.findViewById(R.id.nay_rating)).setText(BAD_RATING + ratings.get(2));
                ((TextView) v.findViewById(R.id.scurvy_rating)).setText(WORST_RATING + ratings.get(3));
                return v;
            }
        };
        parseAdapter.addOnQueryLoadListener(new OnQueryLoadListener());
        parseAdapter.loadObjects();
        setHasStableIds(true);

    }

    @Override
    public DriftingBottlesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        preferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        //To save shared preferences in an ongoing manner.
        preferences.registerOnSharedPreferenceChangeListener(listener);
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_bottle, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        ImageView delete = (ImageView) v.findViewById(R.id.delete);
        TextView [] rateHolders = new TextView[4];
        rateHolders[0] = (TextView) v.findViewById(R.id.yar_har_rating);
        rateHolders[1] = (TextView) v.findViewById(R.id.aye_rating);
        rateHolders[2] = (TextView) v.findViewById(R.id.nay_rating);
        rateHolders[3] = (TextView) v.findViewById(R.id.scurvy_rating);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (discardWarn)
                    new AlertDialog.Builder(parent.getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Warning: Discard?")
                            .setMessage("Are you sure you want to discard this bottle?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Proceed
                                    try {
                                        PickedUpBottle oldBottle = parseAdapter.getItem(vh.getAdapterPosition());
                                        AvailableBottle newBottle = new AvailableBottle();
                                        newBottle.setAll(oldBottle);
                                        newBottle.saveEventually();
                                        oldBottle.unpin();
                                        oldBottle.delete();
                                        driftingBottlesAdapter.notifyItemRemoved(vh.getAdapterPosition());
                                        parseAdapter.loadObjects();

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                else {
                    //Proceed
                    try {
                        parseAdapter.getItem(vh.getAdapterPosition()).unpin();
                        parseAdapter.getItem(vh.getAdapterPosition()).delete();
                        driftingBottlesAdapter.notifyItemRemoved(vh.getAdapterPosition());
                        parseAdapter.loadObjects();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDropBottleDialog(v.getContext(), vh.getAdapterPosition());
                return true;
            }
        });
        rateHolders[0].setOnClickListener(new RatingClick(vh, rateHolders));
        rateHolders[1].setOnClickListener(new RatingClick(vh, rateHolders));
        rateHolders[2].setOnClickListener(new RatingClick(vh, rateHolders));
        rateHolders[3].setOnClickListener(new RatingClick(vh, rateHolders));
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


    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View card;

        public ViewHolder(View v) {
            super(v);
            card = v;

        }
    }
    private class RatingClick implements View.OnClickListener{

        ViewHolder vh;
        List<Integer> ratings;
        int isRated;
        TextView [] rateHolders;
        public RatingClick(ViewHolder vh, TextView [] rateHolders) {
            this.vh = vh;
            this.rateHolders = rateHolders;
        }
        @Override
        public void onClick(View v) {
            PickedUpBottle bottle = parseAdapter.getItem(vh.getAdapterPosition());
            if (ratings == null)
                ratings = bottle.getRatings();
            isRated = bottle.getRated();
            if (isRated > 0) {
                int newVal;
                switch (isRated) {
                    case 1:
                        newVal = ratings.get(0) - 1;
                        setText(rateHolders[0], newVal, BEST_RATING);
                        ratings.set(0,newVal);
                        if (v.getId() == R.id.yar_har_rating){
                            bottle.setRated(0);
                            return;
                        }
                        break;
                    case 2:

                        newVal = ratings.get(1) - 1;
                        setText(rateHolders[1], newVal, GOOD_RATING);
                        ratings.set(1, newVal);
                        if (v.getId() == R.id.aye_rating){
                            bottle.setRated(0);
                            return;
                        }
                        break;
                    case 3:
                        newVal = ratings.get(2) - 1;
                        setText(rateHolders[2], newVal, BAD_RATING);
                        ratings.set(2, newVal);
                        if (v.getId() == R.id.nay_rating) {
                            bottle.setRated(0);
                            return;
                        }
                        break;
                    case 4:
                        newVal = ratings.get(3) - 1;
                        setText(rateHolders[3], newVal, WORST_RATING);
                        ratings.set(3, newVal);
                        if (v.getId() == R.id.scurvy_rating){
                            bottle.setRated(0);
                            return;
                        }
                        break;
                }
            }
            int rate;
            String base;
            switch (v.getId()) {
                case R.id.yar_har_rating:
                    rate = ratings.get(0) + 1;
                    ratings.set(0, rate);
                    base = BEST_RATING;
                    bottle.setRated(1);
                    break;
                case R.id.aye_rating:
                    rate = ratings.get(1) + 1;
                    ratings.set(1, rate);
                    base = GOOD_RATING;
                    bottle.setRated(2);
                    break;
                case R.id.nay_rating:
                    rate = ratings.get(2) + 1;
                    ratings.set(2, rate);
                    base = BAD_RATING;
                    bottle.setRated(3);
                    break;
                case R.id.scurvy_rating:
                    rate = ratings.get(3) + 1;
                    ratings.set(3, rate);
                    base = WORST_RATING;
                    bottle.setRated(4);
                    break;
                default:
                    return;
            }
            setText(v, rate, base);
            bottle.setRatings(ratings);
        }

        private void setText(View v, int rating, String base) {
            ((TextView) v).setText(base + rating);
        }
    }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<PickedUpBottle> {

        public void onLoading() {
            //TODO add loading animation
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
    public void showDropBottleDialog(Context context, final int position) {
        new AlertDialog.Builder(context)
                .setTitle("Cast yer bottle?")
                .setMessage("Are ye sure ye wish to cast yer bottle?")
                .setPositiveButton("Aye!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PickedUpBottle pickedUpBottle = parseAdapter.getItem(position);
                        ParseGeoPoint point = pickedUpBottle.getPoint(); //currentLocation.getLatitude(), currentLocation.getLongitude());
                        AvailableBottle newBottle = new AvailableBottle();
                        newBottle.setAll(pickedUpBottle);
                        newBottle.setLastUser(ParseUser.getCurrentUser());
                        newBottle.setPoint(point);
                        newBottle.saveInBackground();
                        try {
                            pickedUpBottle.delete();
                            driftingBottlesAdapter.notifyItemRemoved(position);
                            parseAdapter.loadObjects();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Nay!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

}
