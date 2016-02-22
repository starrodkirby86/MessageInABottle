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
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.inventory.UserPirateMastFragment;

import java.util.List;

/**
 * Created by Jason on 12/2/2015.
 */
public class UserPirateMastAdapter extends RecyclerView.Adapter<UserPirateMastAdapter.ViewHolder> {

    private static final String BEST_RATING = "Yar Har! + ";
    private static final String GOOD_RATING = "Aye + ";
    private static final String BAD_RATING = "Nay + ";
    private static final String WORST_RATING = "Scurvy! + ";


    private ParseQueryAdapter<PirateMast> parseAdapter;

    private ViewGroup parseParent;
    private UserPirateMastFragment fragment;
    private UserPirateMastAdapter userPostingsFragment = this;
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

    public UserPirateMastAdapter(Context context, ViewGroup parentIn, UserPirateMastFragment fragment) {
        parseParent = parentIn;
        this.fragment = fragment;
        ParseQueryAdapter.QueryFactory<PirateMast> factory = new ParseQueryAdapter.QueryFactory<PirateMast>() {
            @Override
            public ParseQuery<PirateMast> create() {
                return PirateMast.getUserQuery(ParseUser.getCurrentUser());
            }
        };
        parseAdapter = new ParseQueryAdapter<PirateMast>(context, factory) {
            @Override
            public View getItemView(PirateMast object, View v, ViewGroup parent) {
                if (v == null) {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_bottle, parent, false);
                }
                super.getItemView(object, v, parent);
                TextView titleText = (TextView) v.findViewById(R.id.pirate_mast_title);
                TextView description = (TextView) v.findViewById(R.id.pirate_mast_message);
                String title="A message from ";
                //try {
//                    if(object.getParseUser().fetchIfNeeded().getUsername().length()<=15)
//                        title = title + object.getLastUser().fetchIfNeeded().getUsername();
//                    else
                        title=title+"a pirate";
                //} catch (ParseException e) {
                    //title = title+"a pirate";
                    //e.printStackTrace();
                //}
                titleText.setText(title);
                //description.setText(object.getMessage());
                //List<Integer> ratings = object.getRatings();
//                ((TextView) v.findViewById(R.id.yar_har_rating)).setText(BEST_RATING + ratings.get(0));
//                ((TextView) v.findViewById(R.id.aye_rating)).setText(GOOD_RATING + ratings.get(1));
//                ((TextView) v.findViewById(R.id.nay_rating)).setText(BAD_RATING + ratings.get(2));
//                ((TextView) v.findViewById(R.id.scurvy_rating)).setText(WORST_RATING + ratings.get(3));
                return v;
            }
        };
        parseAdapter.addOnQueryLoadListener(new OnQueryLoadListener());
        parseAdapter.loadObjects();
        setHasStableIds(true);

    }

    @Override
    public UserPirateMastAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        preferences = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        //To save shared preferences in an ongoing manner.
        preferences.registerOnSharedPreferenceChangeListener(listener);
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_bottle, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        ImageView delete = (ImageView) v.findViewById(R.id.delete);
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
                                        PirateMast oldBottle = parseAdapter.getItem(vh.getAdapterPosition());
                                        oldBottle.unpin();
                                        oldBottle.delete();
                                        userPostingsFragment.notifyItemRemoved(vh.getAdapterPosition());
                                        parseAdapter.loadObjects();
                                        fragment.sendDataChange(oldBottle);

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
                        userPostingsFragment.notifyItemRemoved(vh.getAdapterPosition());
                        parseAdapter.loadObjects();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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


    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View card;

        public ViewHolder(View v) {
            super(v);
            card = v;

        }
    }

    public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<PirateMast> {

        public void onLoading() {

        }

        public void onLoaded(List<PirateMast> objects, Exception e) {
            userPostingsFragment.notifyDataSetChanged();
            isEmpty = objects.isEmpty();

        }
    }

    public void reload() {
        parseAdapter.loadObjects();
    }
}
