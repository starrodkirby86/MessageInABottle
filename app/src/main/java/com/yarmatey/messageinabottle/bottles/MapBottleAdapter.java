package com.yarmatey.messageinabottle.bottles;

import android.content.Context;
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
import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.inventory.StaticBottlesFragment;

import java.util.List;

/**
 * Created by Jason on 11/27/2015.
 */
public class MapBottleAdapter extends RecyclerView.Adapter<MapBottleAdapter.ViewHolder> {

        private ParseQueryAdapter<AvailableBottle> parseAdapter;

        private static final int MAX_POSTS = 25;
        private static final int RANGE = 1000;

        private ViewGroup parseParent;
        private StaticBottlesFragment fragment;
        private MapBottleAdapter driftingBottlesAdapter = this;
        public boolean isEmpty;

        public MapBottleAdapter(Context context, ViewGroup parentIn, final ParseGeoPoint location, StaticBottlesFragment fragment) {
            this.fragment = fragment;
            parseParent = parentIn;
            ParseQueryAdapter.QueryFactory<AvailableBottle> factory = new ParseQueryAdapter.QueryFactory<AvailableBottle>() {
                @Override
                public ParseQuery<AvailableBottle> create() {
                    ParseQuery<AvailableBottle> query = AvailableBottle.getNearbyQuery(location, RANGE, MAX_POSTS);
                    return query;
                }
            };
            parseAdapter = new ParseQueryAdapter<AvailableBottle>(context, factory) {
                @Override
                public View getItemView(AvailableBottle object, View v, ViewGroup parent) {
                    if (v == null) {
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_tracking_item, parent, false);
                    }
                    super.getItemView(object, v, parent);
                    TextView titleText = (TextView) v.findViewById(R.id.card_title);
                    TextView description = (TextView) v.findViewById(R.id.card_message);
                    String title="A message from ";
                    try {
                        //TODO make this cleaner
                        if(object.getLastUser().fetchIfNeeded().getUsername().length()<=15)
                        title = title + object.getLastUser().fetchIfNeeded().getUsername();
                        else
                            title=title+"a pirate";
                    } catch (ParseException e) {
                        title = title+"a pirate";
                        e.printStackTrace();
                    }

                    /*
                    title = "A message from ";
                    if (!ParseAnonymousUtils.isLinked(object.getLastUser()))
                        title = title + object.getLastUser().getUsername();
                    else
                        title = title + "a pirate";
                    */

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
        public MapBottleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_tracking_item, parent, false);

            final ViewHolder vh = new ViewHolder(v);
            ImageView delete = (ImageView) v.findViewById(R.id.delete);
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

        public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<AvailableBottle> {

            public void onLoading() {

            }

            public void onLoaded(List<AvailableBottle> objects, Exception e) {
                driftingBottlesAdapter.notifyDataSetChanged();
                isEmpty = objects.isEmpty();

                //IMPLEMENTATION OF mapsVal for opening maps.
                //Retrieve the preferences from this fragment's context.
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parseParent.getContext());
                //Pull the map_switch and return false if this value does not exist (the default value)
                boolean mapsVal = preferences.getBoolean("map_switch", false);

                if(mapsVal) {
                    fragment.clearMarkers();
                    for (AvailableBottle bottle : objects) {
                        fragment.addMarker(bottle);
                    }
                }
            }
        }

        public void itemInserted() {
            //notifyItemInserted(this.getItemCount());
            parseAdapter.loadObjects();
        }
}
