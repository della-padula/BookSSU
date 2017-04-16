package com.terry.librarysearch;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PlaceSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private List<String> labels = new ArrayList<>();
    private List<Long> ids = new ArrayList<>();

    public PlaceSpinnerAdapter(Context context, String[] sources) {
        for (String source : sources) {
            String[] separated = source.split("\\|");
            labels.add(separated[0]);
            ids.add(Long.parseLong(separated[1]));
        }

        activity = context;
    }

    public int getCount() {
        return labels.size();
    }

    public Object getItem(int i) {
        return labels.get(i);
    }

    public long getItemId(int i) {
        return ids.get(i);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.naum_square_regular));
        TextView txt = new TextView(activity);
        txt.setPadding(40, 40, 40, 40); // left, top, right, bottom
        txt.setHeight(200);
        txt.setTypeface(typeface);
        txt.setTextSize(16);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(labels.get(position));
        txt.setTextColor(Color.parseColor("#141414"));
        return  txt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.naum_square_regular));
        TextView txt = new TextView(activity);
        txt.setTypeface(typeface);
        txt.setPadding(30, 16, 16, 16);
        txt.setTextSize(16);
        txt.setText(labels.get(position));
        txt.setTextColor(Color.parseColor("#141414"));
        return  txt;
    }
}
