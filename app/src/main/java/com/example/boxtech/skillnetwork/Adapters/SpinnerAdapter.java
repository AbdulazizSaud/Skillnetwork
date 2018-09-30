package com.example.boxtech.skillnetwork.Adapters;


import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.boxtech.skillnetwork.App.App;
import com.example.boxtech.skillnetwork.R;


import java.util.List;



public class SpinnerAdapter extends ArrayAdapter<String> {


    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setGravity(Gravity.CENTER);
        v.setTextSize(16);
        v.setTextColor(ContextCompat.getColor(getContext(), R.color.hint_color));
        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getDropDownView(position, convertView, parent);
        v.setGravity(Gravity.CENTER);
        v.setTextSize(16);
        v.setTextColor(ContextCompat.getColor(getContext(),R.color.hint_color));
        return v;
    }
}