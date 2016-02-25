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

/**
 * Created by Jason on 10/27/2015.
 */

public class DriftingBottlesAdapter extends RecyclerView.Adapter<DriftingBottlesAdapter.ViewHolder> {

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
        Ratings ratings = (Ratings) v.findViewById(R.id.ratings);
        Cursor cursor = mCursorAdapter.getCursor();
        cursor.moveToPosition(holder.getAdapterPosition());
        ratings.setBottle(new Bottle(cursor));
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


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected View card;

        public ViewHolder(View v) {
            super(v);
            card = v;

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
