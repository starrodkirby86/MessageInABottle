package com.yarmatey.messageinabottle.bottles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        private ParseQueryAdapter<PirateMast> parseAdapter;

        private static final int MAX_POSTS = 250;
        private static final int RANGE = 10000;
        private int count = 0;
        private ViewGroup parseParent;
        private StaticBottlesFragment fragment;
        private MapBottleAdapter pirateMastAdapter = this;
        public boolean isEmpty;

        public MapBottleAdapter(Context context, ViewGroup parentIn, final ParseGeoPoint location, StaticBottlesFragment fragment) {
            this.fragment = fragment;
            parseParent = parentIn;
            ParseQueryAdapter.QueryFactory<PirateMast> factory = new ParseQueryAdapter.QueryFactory<PirateMast>() {
                @Override
                public ParseQuery<PirateMast> create() {
                    ParseQuery<PirateMast> query = PirateMast.getQuery(location, RANGE, MAX_POSTS);
                    return query;
                }
            };
            parseAdapter = new ParseQueryAdapter<PirateMast>(context, factory) {
                @Override
                public View getItemView(PirateMast object, View v, ViewGroup parent) {
                    if (v == null) {
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_pirate_mast, parent, false);
                    }
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

                    /*
                    title = "A message from ";
                    if (!ParseAnonymousUtils.isLinked(object.getLastUser()))
                        title = title + object.getLastUser().getUsername();
                    else
                        title = title + "a pirate";
                    */

                    titleText.setText(title);
                    description.setText(object.getMessage());
                    super.getItemView(object, v, parent);
                    return v;
                }
            };
            parseAdapter.addOnQueryLoadListener(new OnQueryLoadListener());
            parseAdapter.loadObjects();

        }

        @Override
        public MapBottleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_pirate_mast, parent, false);

            final ViewHolder vh = new ViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.goToMarker(parseAdapter.getItem(vh.getAdapterPosition()));
                }
            });
            return vh;
        }
    @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            parseAdapter.getView(position, holder.card, parseParent);

            fragment.addMarkerIfUnique(
                    parseAdapter.getItem(holder.getAdapterPosition()));
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

        public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<PirateMast> {

            public void onLoading() {

            }

            public void onLoaded(List<PirateMast> objects, Exception e) {
                pirateMastAdapter.notifyDataSetChanged();
                //parseAdapter.notifyDataSetChanged();
                //isEmpty = objects.isEmpty();
            }
        }

        public void itemInserted() {
            parseAdapter.loadObjects();
            //parseAdapter.notifyDataSetChanged();
        }
}
