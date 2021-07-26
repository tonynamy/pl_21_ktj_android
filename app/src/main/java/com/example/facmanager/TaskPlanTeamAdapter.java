package com.example.facmanager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.facmanager.models.Team;

import java.util.List;

public class TaskPlanTeamAdapter extends HintSpinnerAdapter<Team> {

    List<Team> objects;


    public TaskPlanTeamAdapter(Context context, int resource, List<Team> objects) {
        super(context, resource, objects);

        this.objects = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView textView = (TextView) view;

        Team team = objects.get(position);

        textView.setText(team.name);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        TextView textView = (TextView) view;

        Team team = objects.get(position);

        textView.setText(team.name);

        return view;
    }

}
