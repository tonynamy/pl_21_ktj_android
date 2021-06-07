package com.example.facmanager;

import android.content.ContentValues;
import android.os.Build;
import android.util.Log;

import com.example.facmanager.models.Team;
import com.example.facmanager.models.UserAttendance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class API {

    public static String ROOT_URL = "http://pl-21-ktj.run.goorm.io/";

    private static String LOGIN = ROOT_URL + "";
    private static String TEAMS = ROOT_URL + "teams";
    private static String ATTENDANCES = ROOT_URL + "attendance";
    private static String ATTENDANCE_ADD = ROOT_URL + "attendance_add";

    public interface APICallback {
        public void onSuccess(Object data);
        public void onFailed(String errorMsg);
    }

    public static class Builder {

        private final APICallback apiCallback;

        public Builder(APICallback apiCallback) {
            this.apiCallback = apiCallback;
        }

        public API build() {
            return new API(this);
        }
    }

    private APICallback apiCallback;

    public API(Builder builder) {
        this.apiCallback = builder.apiCallback;
    }

    public void login(String username) {

        String url = LOGIN;
        ContentValues values = new ContentValues();
        values.put("username", username);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {
                apiCallback.onSuccess(result);
            }

            @Override
            public void onFailed(String error) {
                apiCallback.onFailed(error);
            }
        };

        new NetworkTask(url, values, true, networkCallback).execute();

    }

    public void addAttendance(String userId, String type) {

        String url = ATTENDANCE_ADD;
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("type", type);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {
                apiCallback.onSuccess(result);
            }

            @Override
            public void onFailed(String error) {
                apiCallback.onFailed(error);
            }
        };

        new NetworkTask(url, values, true, networkCallback).execute();

    }

    public void getTeams() {

        String url = TEAMS;

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<Team> teams = new ArrayList<>();

                try {

                    JSONArray jsonArray = new JSONArray(result);

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String leader_id = jsonObject.getString("leader_id");
                        String created_at = jsonObject.getString("created_at");
                        String updated_at = jsonObject.getString("updated_at");
                        String deleted_at = jsonObject.getString("deleted_at");

                        Team team = new Team();
                        team.id = id;
                        team.name = name;
                        team.leader_id = leader_id;
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            team.created_at = simpleDateFormat.parse(created_at);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            team.updated_at = simpleDateFormat.parse(updated_at);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            team.deleted_at = simpleDateFormat.parse(deleted_at);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        teams.add(team);


                    }

                    apiCallback.onSuccess(teams);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String error) {
                apiCallback.onFailed(error);
            }
        };

        new NetworkTask(url, null, true, networkCallback).execute();

    }

    public void getAttendances() {

        String url = ATTENDANCES;

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<UserAttendance> userAttendances = new ArrayList<>();

                try {

                    JSONArray jsonArray = new JSONArray(result);

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String user_id = jsonObject.getString("user_id");
                        String user_name = jsonObject.getString("user_name");
                        String user_birthday = jsonObject.getString("user_birthday");
                        String date = jsonObject.getString("date");
                        int type = AttendanceRecord.NOT_ATTEND;

                        try {
                            type = jsonObject.getInt("type");
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        UserAttendance userAttendance = new UserAttendance();
                        userAttendance.id = id;
                        userAttendance.user_id = user_id;
                        userAttendance.user_name = user_name;
                        userAttendance.type = type;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            userAttendance.user_birthday = simpleDateFormat.parse(user_birthday);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            userAttendance.date = simpleDateFormat.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        userAttendances.add(userAttendance);


                    }

                    apiCallback.onSuccess(userAttendances);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String error) {
                apiCallback.onFailed(error);
            }
        };

        new NetworkTask(url, null, true, networkCallback).execute();

    }
}
