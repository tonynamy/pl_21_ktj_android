package com.example.facmanager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class HintSpinnerAdapter<T> extends ArrayAdapter<T> {

    public Boolean isBlack = false;

    public HintSpinnerAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;

        if(position > 0 || isBlack){
            textView.setTextColor(Color.BLACK);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        TextView textView = (TextView) view;

        if(position > 0 || isBlack){
            textView.setTextColor(Color.BLACK);
        }

        return view;
    }

}