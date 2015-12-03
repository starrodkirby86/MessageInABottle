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
import com.yarmatey.messageinabottle.inventory.PirateMapFragment;

import java.util.List;

/**
 * Created by Jason on 11/27/2015.
 */
public class PirateMapAdapter extends RecyclerView.Adapter<PirateMapAdapter.ViewHolder> {


        private static final String BEST_RATING = "Yar Har! + ";
        private static final String GOOD_RATING = "Aye + ";
        private static final String BAD_RATING = "Nay + ";
        private static final String WORST_RATING = "Scurvy! + ";

        private ParseQueryAdapter<PirateMast> parseAdapter;

        private static final int MAX_POSTS = 250;
        private static final int RANGE = 10000;
        private ViewGroup parseParent;
        private PirateMapFragment fragment;
        private PirateMapAdapter pirateMastAdapter = this;

        public PirateMapAdapter(Context context, ViewGroup parentIn, final ParseGeoPoint location, PirateMapFragment fragment) {
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

                    List<Integer> ratings = object.getRatings();
                    ((TextView) v.findViewById(R.id.yar_har_rating)).setText(BEST_RATING + ratings.get(0));
                    ((TextView) v.findViewById(R.id.aye_rating)).setText(GOOD_RATING + ratings.get(1));
                    ((TextView) v.findViewById(R.id.nay_rating)).setText(BAD_RATING + ratings.get(2));
                    ((TextView) v.findViewById(R.id.scurvy_rating)).setText(WORST_RATING + ratings.get(3));

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
        public PirateMapAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_pirate_mast, parent, false);

            final ViewHolder vh = new ViewHolder(v);
            TextView [] rateHolders = new TextView[4];
            rateHolders[0] = (TextView) v.findViewById(R.id.yar_har_rating);
            rateHolders[1] = (TextView) v.findViewById(R.id.aye_rating);
            rateHolders[2] = (TextView) v.findViewById(R.id.nay_rating);
            rateHolders[3] = (TextView) v.findViewById(R.id.scurvy_rating);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.goToMarker(parseAdapter.getItem(vh.getAdapterPosition()));
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

        public class ViewHolder extends RecyclerView.ViewHolder {
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
                PirateMast bottle = parseAdapter.getItem(vh.getAdapterPosition());
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


        public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<PirateMast> {

            public void onLoading() {

            }

            public void onLoaded(List<PirateMast> objects, Exception e) {
                pirateMastAdapter.notifyDataSetChanged();

                for(PirateMast mast : objects)
                    fragment.addMarkerIfUnique(mast);
                //parseAdapter.notifyDataSetChanged();
                //isEmpty = objects.isEmpty();
            }
        }

        public void reload() {
            parseAdapter.loadObjects();
            //parseAdapter.notifyDataSetChanged();
        }
}
