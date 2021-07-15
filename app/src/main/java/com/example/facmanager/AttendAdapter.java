package com.example.facmanager;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AttendAdapter extends RecyclerView.Adapter<AttendAdapter.ViewHolder> {

    interface OnAttendBtnClickListener {
        public void onClick(View v, AttendItem attendItem, int new_type);
    }

    interface OnAttendanceUpdateListner {
        public void onUpdate();
    }

    OnAttendBtnClickListener onAttendBtnClickListener;
    OnAttendanceUpdateListner onAttendanceUpdateListner;
    ArrayList<AttendItem> attendItemArrayList = new ArrayList<>();
    String team_id;

    public AttendAdapter(OnAttendBtnClickListener onAttendBtnClickListener, OnAttendanceUpdateListner onAttendanceUpdateListner) {
        this.onAttendBtnClickListener = onAttendBtnClickListener;
        this.onAttendanceUpdateListner = onAttendanceUpdateListner;
    }

    public void addItem(AttendItem attendItem) {
        attendItemArrayList.add(attendItem);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void clear() {
        attendItemArrayList.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attend, parent, false);
        return new ViewHolder(view, onAttendBtnClickListener, onAttendanceUpdateListner);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setItem(attendItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return attendItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OnAttendBtnClickListener onAttendBtnClickListener;
        OnAttendanceUpdateListner onAttendanceUpdateListner;
        AttendItem attendItem;
        TextView txtName;
        TextView txtAttend;
        Button btnAttend;


        public ViewHolder(@NonNull View itemView) {
             this(itemView, null, null);
        }

        public ViewHolder(@NonNull View itemView, OnAttendBtnClickListener onAttendBtnClickListener, OnAttendanceUpdateListner onAttendanceUpdateListener) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAttend = itemView.findViewById(R.id.txtAttend);
            btnAttend = itemView.findViewById(R.id.btnAttend);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    //int position = getAdapterPosition();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_attendance, null);
                    builder.setView(view);

                    TextView txtDialogTitle = view.findViewById(R.id.txtDialogTitle);
                    LinearLayout layoutAttend = view.findViewById(R.id.layoutAttend);
                    TimePicker timeAttend = view.findViewById(R.id.pickerAttend);
                    LinearLayout layoutLeave = view.findViewById(R.id.layoutLeave);
                    TimePicker timeLeave = view.findViewById(R.id.pickerLeave);
                    Button btnChangeTeam = view.findViewById(R.id.btnChangeTeam);
                    TextView txtDialogCancel = view.findViewById(R.id.txtDialogCancel);
                    TextView txtDialogSubmit = view.findViewById(R.id.txtDialogSubmit);

                    AlertDialog dialog = builder.create();

                    txtDialogTitle.setText(attendItem.getName()+ " 출근 상황 수정");

                    SimpleDateFormat sdf_hour = new SimpleDateFormat("HH");
                    SimpleDateFormat sdf_minute = new SimpleDateFormat("mm");

                    switch (attendItem.type) {
                        case AttendanceRecord.NOT_ATTEND :
                            layoutAttend.setVisibility(View.GONE);
                            layoutLeave.setVisibility(View.GONE);
                            break;
                        case AttendanceRecord.ATTENDED :
                            layoutAttend.setVisibility(View.VISIBLE);
                            layoutLeave.setVisibility(View.GONE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                timeAttend.setHour(Integer.parseInt(sdf_hour.format(attendItem.attend_date)));
                                timeAttend.setMinute(Integer.parseInt(sdf_minute.format(attendItem.attend_date)));
                            } else {
                                timeAttend.setCurrentHour(Integer.parseInt(sdf_hour.format(attendItem.attend_date)));
                                timeAttend.setCurrentMinute(Integer.parseInt(sdf_minute.format(attendItem.attend_date)));
                            }

                            break;
                        case AttendanceRecord.LEAVED_WORK :
                            layoutAttend.setVisibility(View.VISIBLE);
                            layoutLeave.setVisibility(View.VISIBLE);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                timeAttend.setHour(Integer.parseInt(sdf_hour.format(attendItem.attend_date)));
                                timeAttend.setMinute(Integer.parseInt(sdf_minute.format(attendItem.attend_date)));
                            } else {
                                timeAttend.setCurrentHour(Integer.parseInt(sdf_hour.format(attendItem.attend_date)));
                                timeAttend.setCurrentMinute(Integer.parseInt(sdf_minute.format(attendItem.attend_date)));
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                timeLeave.setHour(Integer.parseInt(sdf_hour.format(attendItem.leave_date)));
                                timeLeave.setMinute(Integer.parseInt(sdf_minute.format(attendItem.leave_date)));
                            } else {
                                timeLeave.setCurrentHour(Integer.parseInt(sdf_hour.format(attendItem.leave_date)));
                                timeLeave.setCurrentMinute(Integer.parseInt(sdf_minute.format(attendItem.leave_date)));
                            }
                            break;
                    }

                    txtDialogSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            API.APICallback apiCallback = new API.APICallback() {

                                @Override
                                public void onSuccess(Object data) {

                                    Toast.makeText(context, "출근기록 업데이트에 성공했습니다", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String errorMsg) {

                                    Toast.makeText(context, "출근기록 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show();
                                }
                            };

                            API api = new API.Builder(apiCallback).build();

                            if (attendItem.type == 0) {

                                Calendar calendar = Calendar.getInstance();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if(timeAttend.getHour() < 5) {
                                        calendar.add(Calendar.DATE, 1);
                                    }
                                } else {
                                    if(timeAttend.getCurrentHour() < 5) {
                                        calendar.add(Calendar.DATE, 1);
                                    }
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    calendar.set(Calendar.HOUR_OF_DAY, timeAttend.getHour());
                                    calendar.set(Calendar.MINUTE, timeAttend.getMinute());
                                } else {
                                    calendar.set(Calendar.HOUR_OF_DAY, timeAttend.getCurrentHour());
                                    calendar.set(Calendar.MINUTE, timeAttend.getCurrentMinute());
                                }

                                api.editAttendance(attendItem.getId(), "0", calendar.getTime());


                            } else if (attendItem.type == 1) {

                                Calendar calendar = Calendar.getInstance();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if(timeAttend.getHour() < 5) {
                                        calendar.add(Calendar.DATE, 1);
                                    }
                                } else {
                                    if(timeAttend.getCurrentHour() < 5) {
                                        calendar.add(Calendar.DATE, 1);
                                    }
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    calendar.set(Calendar.HOUR_OF_DAY, timeAttend.getHour());
                                    calendar.set(Calendar.MINUTE, timeAttend.getMinute());
                                } else {
                                    calendar.set(Calendar.HOUR_OF_DAY, timeAttend.getCurrentHour());
                                    calendar.set(Calendar.MINUTE, timeAttend.getCurrentMinute());
                                }

                                api.editAttendance(attendItem.getId(), "0", calendar.getTime());

                                Calendar calendar1 = Calendar.getInstance();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if(timeLeave.getHour() < 5) {
                                        calendar1.add(Calendar.DATE, 1);
                                    }
                                } else {
                                    if(timeLeave.getCurrentHour() < 5) {
                                        calendar1.add(Calendar.DATE, 1);
                                    }
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    calendar1.set(Calendar.HOUR_OF_DAY, timeLeave.getHour());
                                    calendar1.set(Calendar.MINUTE, timeLeave.getMinute());
                                } else {
                                    calendar1.set(Calendar.HOUR_OF_DAY, timeLeave.getCurrentHour());
                                    calendar1.set(Calendar.MINUTE, timeLeave.getCurrentMinute());
                                }

                                api.editAttendance(attendItem.getId(), "1", calendar1.getTime());
                            }

                            onAttendanceUpdateListener.onUpdate();
                            dialog.dismiss();

                        }
                    });

                    txtDialogCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnChangeTeam.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AttendFilterActivity.class);
                            Boolean changeTeamMode = true;
                            intent.putExtra("changeTeamMode", changeTeamMode);
                            intent.putExtra("user_id", attendItem.id);
                            ((Activity)context).startActivityForResult(intent, 200);
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            btnAttend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int new_type;
                    int type = attendItem.getType();

                    switch (type) {
                        case AttendanceRecord.NOT_ATTEND :
                            new_type = AttendanceRecord.ATTENDED;
                            break;
                        case AttendanceRecord.ATTENDED :
                            new_type = AttendanceRecord.LEAVED_WORK;
                            break;
                        default :
                            new_type = AttendanceRecord.NOT_ATTEND;
                            break;
                    }

                    onAttendBtnClickListener.onClick(v, attendItem, new_type);
                }
            });

            this.onAttendBtnClickListener = onAttendBtnClickListener;
            this.onAttendanceUpdateListner = onAttendanceUpdateListener;
        }

        public void setItem(AttendItem attendItem) {
            this.attendItem = attendItem;

            txtName.setText(attendItem.getName());
            int type = attendItem.getType();
            String typeText = "";
            String buttonText = "";
            int typeColor;
            switch (type) {
                case  AttendanceRecord.NOT_ATTEND :
                    typeText = "출근전";
                    buttonText = "출근";
                    typeColor = Color.GRAY;
                    break;
                case  AttendanceRecord.ATTENDED :
                    typeText = "출근";
                    buttonText = "퇴근";
                    typeColor = Color.BLUE;
                    break;
                case  AttendanceRecord.LEAVED_WORK :
                    typeText = "퇴근";
                    typeColor = Color.RED;
                    btnAttend.setVisibility(View.INVISIBLE);
                    break;
                default:
                    typeText = "알수없음";
                    typeColor = Color.RED;
                    break;
            }
            txtAttend.setText(typeText);
            txtAttend.setTextColor(typeColor);
            btnAttend.setText(buttonText);
        }
    }

}
