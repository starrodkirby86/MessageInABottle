package com.yarmatey.messageinabottle.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yarmatey.messageinabottle.R;
import com.yarmatey.messageinabottle.bottles.Bottle;
import com.yarmatey.messageinabottle.bottles.enums.MessageRatings;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Jason on 2/24/2016.
 */
public class Ratings extends LinearLayout implements View.OnClickListener{

    private Bottle bottle;
    private TextView best;
    private TextView good;
    private TextView bad;
    private TextView worst;

    public Ratings(Context context) {
        super(context);
        init(context);
    }

    public Ratings(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Ratings(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.ratings_layout, this, true);

        best = (TextView) rootView.findViewById(R.id.best_rating);
        good = (TextView) rootView.findViewById(R.id.good_rating);
        bad = (TextView) rootView.findViewById(R.id.bad_rating);
        worst = (TextView) rootView.findViewById(R.id.worst_rating);

        best.setOnClickListener(this);
        good.setOnClickListener(this);
        bad.setOnClickListener(this);
        worst.setOnClickListener(this);

        bottle = new Bottle();
    }

    public void setBottle(Bottle bottle) {
        this.bottle = bottle;
        update();
    }


    private void update() {
        ArrayList<Integer> ratings = (ArrayList<Integer>) bottle.getRatings();
        best.setText(String.format(Locale.US, "%s + %d", MessageRatings.Best.value, ratings.get(MessageRatings.Best.index)));
        good.setText(String.format(Locale.US, "%s + %d", MessageRatings.Good.value, ratings.get(MessageRatings.Good.index)));
        bad.setText(String.format(Locale.US, "%s + %d", MessageRatings.Bad.value, ratings.get(MessageRatings.Bad.index)));
        worst.setText(String.format(Locale.US, "%s + %d", MessageRatings.Worst.value, ratings.get(MessageRatings.Worst.index)));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.best_rating:
                bottle.overwriteRating(MessageRatings.Best.index);
                break;
            case R.id.good_rating:
                bottle.overwriteRating(MessageRatings.Good.index);
                break;
            case R.id.bad_rating:
                bottle.overwriteRating(MessageRatings.Bad.index);
                break;
            case R.id.worst_rating:
                bottle.overwriteRating(MessageRatings.Worst.index);
                break;
        }
        update();
    }
}
