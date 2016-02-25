package com.yarmatey.messageinabottle.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.bottles.Bottle;
import com.yarmatey.messageinabottle.sql.BottleContract;

import java.util.List;

/**
 * Created by Jason on 10/27/2015.
 */

public class DriftingBottlesAdapter extends RecyclerView.Adapter<DriftingBottlesAdapter.ViewHolder> {

    private static final String BEST_RATING = "Yar Har! + ";
    private static final String GOOD_RATING = "Aye + ";
    private static final String BAD_RATING = "Nay + ";
    private static final String WORST_RATING = "Scurvy! + ";


    //private ParseQueryAdapter<PickedUpBottle> parseAdapter;
    public CursorAdapter mCursorAdapter;
    private ViewGroup parseParent;    private Context mContext;
    public int removed = 0;

    //Declaring preferences, warning on discard to be accessed later
    public SharedPreferences preferences;
    public boolean discardWarn = true;

    //Declaring SharedPreferencesListener here to be constantly available.
    public SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("discardWarning_switch")) {
                //If the switch has changed, we make it the new state. Note that if we cannot retrieve, set the warning to true anyways.
                discardWarn = preferences.getBoolean("discardWarning_switch",true);
            }
        }
    };

    public DriftingBottlesAdapter(Context context, ViewGroup parentIn) {
        parseParent = parentIn;
        this.mContext = context;
        Cursor cursor = context.getContentResolver().query(
                BottleContract.BottleEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
        }
//        ParseQueryAdapter.QueryFactory<PickedUpBottle> factory = new ParseQueryAdapter.QueryFactory<PickedUpBottle>() {
//            @Override
//            public ParseQuery<PickedUpBottle> create() {
//                ParseQuery<PickedUpBottle> query = PickedUpBottle.getQuery();
//                query.fromLocalDatastore();
//                return query;
//            }
//        };
//        parseAdapter = new ParseQueryAdapter<PickedUpBottle>(context, factory) {
//            @Override
//            public View getItemView(PickedUpBottle object, View v, ViewGroup parent) {
//                if (v == null) {
//                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bottle, parent, false);
//                }
//                super.getItemView(object, v, parent);
//                TextView titleText = (TextView) v.findViewById(R.id.pirate_mast_title);
//                TextView description = (TextView) v.findViewById(R.id.pirate_mast_message);
//                String title="A message from ";
//                try {
//                    if(object.getLastUser().fetchIfNeeded().getUsername().length()<=15)
//                        title = title + object.getLastUser().fetchIfNeeded().getUsername();
//                    else
//                        title=title+"a pirate";
//                } catch (ParseException e) {
//                    title = title+"a pirate";
//                    e.printStackTrace();
//                }
//                titleText.setText(title);
//                description.setText(object.getMessage());
//                List<Integer> ratings = object.getRatings();
//                ((TextView) v.findViewById(R.id.yar_har_rating)).setText(BEST_RATING + ratings.get(0));
//                ((TextView) v.findViewById(R.id.aye_rating)).setText(GOOD_RATING + ratings.get(1));
//                ((TextView) v.findViewById(R.id.nay_rating)).setText(BAD_RATING + ratings.get(2));
//                ((TextView) v.findViewById(R.id.scurvy_rating)).setText(WORST_RATING + ratings.get(3));
//                return v;
//            }
//        };
        mCursorAdapter = new CursorAdapter(context, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater
                        .from(parent.getContext()).inflate(R.layout.recyclerview_bottle, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView titleText = (TextView) view.findViewById(R.id.pirate_mast_title);
                TextView description = (TextView) view.findViewById(R.id.pirate_mast_message);
                Bottle bottle = new Bottle(cursor);
                String title="A message from " + bottle.getAuthor();
                titleText.setText(title);
                description.setText(bottle.getMessage());
                List<Integer> ratings = bottle.getRatings();
                ((TextView) view.findViewById(R.id.yar_har_rating)).setText(String.format("%s%d", BEST_RATING, ratings.get(0)));
                ((TextView) view.findViewById(R.id.aye_rating)).setText(String.format("%s%d", GOOD_RATING, ratings.get(1)));
                ((TextView) view.findViewById(R.id.nay_rating)).setText(String.format("%s%d", BAD_RATING, ratings.get(2)));
                ((TextView) view.findViewById(R.id.scurvy_rating)).setText(String.format("%s%d", WORST_RATING, ratings.get(3)));
            }
        };
    }

    @Override
    public DriftingBottlesAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        preferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        //To save shared preferences in an ongoing manner.
        preferences.registerOnSharedPreferenceChangeListener(listener);
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_bottle, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        View v = mCursorAdapter.getView(position, holder.card, parseParent);
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
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Warning: Discard?")
                            .setMessage("Are you sure you want to discard this bottle?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Proceed
                                    removeItem(holder.getAdapterPosition());
                                    //TODO SEND REQUEST TO HTTPS TO UPDATE BOTTLE
                                    //TODO CHANGE STATUS TO AVAILABLE
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                else {
                    //Proceed
                    removeItem(holder.getAdapterPosition());
                    //TODO SEND REQUEST TO HTTPS TO UPDATE BOTTLE
                    //TODO CHANGE STATUS TO AVAILABLE
                }
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDropBottleDialog(v.getContext(), holder.getAdapterPosition());
                return true;
            }
        });
        rateHolders[0].setOnClickListener(new RatingClick(holder, rateHolders));
        rateHolders[1].setOnClickListener(new RatingClick(holder, rateHolders));
        rateHolders[2].setOnClickListener(new RatingClick(holder, rateHolders));
        rateHolders[3].setOnClickListener(new RatingClick(holder, rateHolders));
    }


    public void removeItem(int idx) {
        mCursorAdapter.getCursor().moveToPosition(idx);
        removed = idx;
        long id = mCursorAdapter.getCursor()
                .getLong(mCursorAdapter.getCursor().getColumnIndex("_id"));
        mContext.getContentResolver().delete(
                BottleContract.BottleEntry.CONTENT_URI,
                "_id=" + String.valueOf(id),
                null
        );
        notifyItemRemoved(removed);

    }


    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
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
            Cursor cursor = mCursorAdapter.getCursor();
            cursor.moveToPosition(vh.getAdapterPosition());
            Bottle bottle = new Bottle(cursor);
            if (ratings == null)
                ratings = bottle.getRatings();
            isRated = bottle.getPreviousRating();
            if (isRated > 0) {
                int newVal;
                switch (isRated) {
                    case 1:
                        newVal = ratings.get(0) - 1;
                        setText(rateHolders[0], newVal, BEST_RATING);
                        ratings.set(0,newVal);
                        if (v.getId() == R.id.yar_har_rating){
                            bottle.setPreviousRating(0);
                            return;
                        }
                        break;
                    case 2:

                        newVal = ratings.get(1) - 1;
                        setText(rateHolders[1], newVal, GOOD_RATING);
                        ratings.set(1, newVal);
                        if (v.getId() == R.id.aye_rating){
                            bottle.setPreviousRating(0);
                            return;
                        }
                        break;
                    case 3:
                        newVal = ratings.get(2) - 1;
                        setText(rateHolders[2], newVal, BAD_RATING);
                        ratings.set(2, newVal);
                        if (v.getId() == R.id.nay_rating) {
                            bottle.setPreviousRating(0);
                            return;
                        }
                        break;
                    case 4:
                        newVal = ratings.get(3) - 1;
                        setText(rateHolders[3], newVal, WORST_RATING);
                        ratings.set(3, newVal);
                        if (v.getId() == R.id.scurvy_rating){
                            bottle.setPreviousRating(0);
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
                    bottle.setPreviousRating(1);
                    break;
                case R.id.aye_rating:
                    rate = ratings.get(1) + 1;
                    ratings.set(1, rate);
                    base = GOOD_RATING;
                    bottle.setPreviousRating(2);
                    break;
                case R.id.nay_rating:
                    rate = ratings.get(2) + 1;
                    ratings.set(2, rate);
                    base = BAD_RATING;
                    bottle.setPreviousRating(3);
                    break;
                case R.id.scurvy_rating:
                    rate = ratings.get(3) + 1;
                    ratings.set(3, rate);
                    base = WORST_RATING;
                    bottle.setPreviousRating(4);
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

    public void showDropBottleDialog(Context context, final int position) {
        new AlertDialog.Builder(context)
                .setTitle("Cast yer bottle?")
                .setMessage("Are ye sure ye wish to cast yer bottle?")
                .setPositiveButton("Aye!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Cursor cursor = mCursorAdapter.getCursor();
                        cursor.moveToPosition(position);
                        Bottle bottle = new Bottle(cursor);
                        bottle.setLastUser("Hello"); //TODO ADD USER
                        bottle.setStatus(0); //TODO ADD BOTTLE ENUM
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
