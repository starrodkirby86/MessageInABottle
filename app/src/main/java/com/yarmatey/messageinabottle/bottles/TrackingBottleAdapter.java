package com.yarmatey.messageinabottle.bottles;

import android.content.Context;
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

import java.util.List;

/**
 * Created by Jason on 11/27/2015.
 */
public class TrackingBottleAdapter  extends RecyclerView.Adapter<TrackingBottleAdapter.ViewHolder> {

        private ParseQueryAdapter<AvailableBottle> parseAdapter;

        private ViewGroup parseParent;

        private TrackingBottleAdapter driftingBottlesAdapter = this;
        public boolean isEmpty;

        public TrackingBottleAdapter(Context context, ViewGroup parentIn) {
            parseParent = parentIn;
            ParseQueryAdapter.QueryFactory<AvailableBottle> factory = new ParseQueryAdapter.QueryFactory<AvailableBottle>() {
                @Override
                public ParseQuery<AvailableBottle> create() {
                    ParseQuery<AvailableBottle> query = AvailableBottle.getAuthorQuery(ParseUser.getCurrentUser());
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
        public TrackingBottleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_tracking_item, parent, false);

            final ViewHolder vh = new ViewHolder(v);


            ImageView delete = (ImageView) v.findViewById(R.id.delete);
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

        public class OnQueryLoadListener implements ParseQueryAdapter.OnQueryLoadListener<AvailableBottle> {

            public void onLoading() {

            }

            public void onLoaded(List<AvailableBottle> objects, Exception e) {
                driftingBottlesAdapter.notifyDataSetChanged();
                isEmpty = objects.isEmpty();

            }
        }

        public void itemInserted() {
            //notifyItemInserted(this.getItemCount());
            parseAdapter.loadObjects();
        }
}
