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

public class PlaceSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private String[] asr;

    public PlaceSpinnerAdapter(Context context, String[] asr) {
        this.asr=asr;
        activity = context;
    }

    public int getCount() {
        return asr.length;
    }

    public Object getItem(int i) {
        return asr[i];
    }

    public long getItemId(int i) {
        return (long)i;
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
        txt.setText(asr[position]);
        txt.setTextColor(Color.parseColor("#141414"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), activity.getString(R.string.naum_square_regular));
        TextView txt = new TextView(activity);
        //txt.setGravity(Gravity.CENTER);
        txt.setTypeface(typeface);
        txt.setPadding(30, 16, 16, 16);
        txt.setTextSize(16);
        txt.setText(asr[i]);
        txt.setTextColor(Color.parseColor("#141414"));
        return  txt;
    }

}
