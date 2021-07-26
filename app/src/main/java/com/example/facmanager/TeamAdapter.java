package com.example.facmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    ArrayList<TeamItem> teamItemArrayList = new ArrayList<>();
    Boolean changeTeamMode = false;
    String user_id = "";

    public void addItem(TeamItem teamItem) {
        teamItemArrayList.add(teamItem);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public TeamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ViewHolder holder, int position) {
        holder.onBind(teamItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return teamItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTeamName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTeamName = itemView.findViewById(R.id.txtTeamName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int position = getAdapterPosition();

                    //팀변경시
                    if(changeTeamMode) {

                        Intent intent = new Intent(context, AttendActivity.class);
                        intent.putExtra("team_id", teamItemArrayList.get(position).teamId);
                        intent.putExtra("user_id", user_id);

                        ((Activity)context).setResult(Activity.RESULT_OK, intent);
                        ((Activity)context).finish();

                    //일반적인 상황
                    } else {
                        Intent intent = new Intent(context, AttendActivity.class);
                        String team_id = teamItemArrayList.get(position).teamId;
                        intent.putExtra("team_id", team_id);
                        context.startActivity(intent);
                    }

                }
            });
        }

        public void onBind(TeamItem teamItem) {
            txtTeamName.setText(teamItem.teamName);
        }
    }
}
