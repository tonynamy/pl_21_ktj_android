package com.example.facmanager;

import android.content.ContentValues;
import android.os.Build;
import android.util.Log;

import com.example.facmanager.models.Auth;
import com.example.facmanager.models.Facility;
import com.example.facmanager.models.FacilityInfo;
import com.example.facmanager.models.Place;
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
import java.util.Map;

public class API {

    public static String ROOT_URL = "https://pl-21-ktj.run.goorm.io/";

    private static String LOGIN = ROOT_URL + "";
    private static String TEAMS = ROOT_URL + "teams";
    private static String TEAM_EDIT = ROOT_URL + "team_edit";
    private static String PLACES = ROOT_URL + "places";
    private static String ATTENDANCES = ROOT_URL + "attendance";
    private static String ATTENDANCE_ADD = ROOT_URL + "attendance_add";
    private static String ATTENDANCE_EDIT = ROOT_URL + "attendance_edit";
    private static String ADD_USER = ROOT_URL + "add_user";
    private static String FACILITY_INFO = ROOT_URL + "facility_info";
    private static String FACILITY_SEARCH = ROOT_URL + "facility_search";

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

    private Date getDateFromString(String str) {
        Date date = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public void add_user(String place_id, String username, Date birthday) {

        String url = ADD_USER;
        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        values.put("username", username);
        values.put("birthday", new SimpleDateFormat("yyMMdd").format(birthday));

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

    public void login(String place_id, String username, String birthday) {

        String url = LOGIN;
        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        values.put("username", username);
        values.put("birthday", birthday);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                Auth auth = new Auth();

                int level;
                String place_id;
                String place_name;
                String user_name = "";
                String team_id = "";

                Place place = new Place();

                try {

                    JSONObject jsonArray = new JSONObject(result);

                    place_id = jsonArray.getString("place_id");
                    place_name = jsonArray.getString("place_name");
                    user_name = jsonArray.getString("user_name");
                    team_id = jsonArray.getString("team_id");

                    place.id = place_id;
                    place.name = place_name;

                    level = jsonArray.getInt("level");

                } catch (JSONException e) {
                    e.printStackTrace();

                    level = 0;

                }

                auth.level = level;
                auth.user_name = user_name;
                auth.place = place;
                auth.team_id = team_id;

                apiCallback.onSuccess(auth);

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
        values.put("teammate_id", userId);
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

    public void editAttendance(String userId, String type, Date _date) {
        String url = ATTENDANCE_EDIT;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(_date);

        ContentValues values = new ContentValues();
        values.put("teammate_id", userId);
        values.put("type", type);
        values.put("date", date);

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

    public void editTeam(String userId, String teamId) {
        String url = TEAM_EDIT;

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("team_id", teamId);

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

                    for(int i=0; i<jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String place_id = jsonObject.getString("place_id");
                        //String created_at = jsonObject.getString("created_at");
                        //String updated_at = jsonObject.getString("updated_at");
                        //String deleted_at = jsonObject.getString("deleted_at");

                        Team team = new Team();
                        team.id = id;
                        team.name = name;
                        team.place_id = place_id;
                        /*
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
                        */

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

    public void getPlaces() {

        String url = PLACES;

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<Place> places = new ArrayList<>();

                try {

                    JSONArray jsonArray = new JSONArray(result);

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");

                        Place place = new Place();
                        place.id = id;
                        place.name = name;

                        places.add(place);

                    }

                    apiCallback.onSuccess(places);


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

    public void getAttendances(String team_id) {

        String url = ATTENDANCES;

        ContentValues values = new ContentValues();
        values.put("team_id", team_id);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<UserAttendance> userAttendances = new ArrayList<>();

                try {

                    JSONArray jsonArray = new JSONArray(result);

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String birthday = jsonObject.getString("birthday");
                        String date = jsonObject.getString("date");
                        int type = AttendanceRecord.NOT_ATTEND;

                        try {
                            type = jsonObject.getInt("type");
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                        UserAttendance userAttendance = new UserAttendance();
                        userAttendance.id = id;
                        userAttendance.name = name;
                        userAttendance.type = type;
                        userAttendance.birthday = birthday;
                        /*
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
                        try {
                            userAttendance.birthday = simpleDateFormat.parse(birthday);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        */

                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            userAttendance.date = simpleDateFormat1.parse(date);
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

        new NetworkTask(url, values, true, networkCallback).execute();

    }

    public void getFacilityInfo() {

        String url = FACILITY_INFO;

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                FacilityInfo facilityInfo = new FacilityInfo();

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    String[] str_types = jsonObject.getString("type").split(",");
                    int[] int_types = new int[str_types.length];

                    int i = 0;

                    for(String str_type : str_types) {

                        int_types[i++] = Integer.parseInt(str_type);
                    }

                    facilityInfo.types = int_types;

                    facilityInfo.subcontractors = jsonObject.getString("subcontractor").split(",");
                    facilityInfo.buildings = jsonObject.getString("building").split(",");
                    facilityInfo.floors = jsonObject.getString("floor").split(",");
                    facilityInfo.spots = jsonObject.getString("spot").split(",");

                    apiCallback.onSuccess(facilityInfo);


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

    public void searchFacility(String place_id, String serial, int type, String subcontractor, String building, String floor, String spot) {

        String url = FACILITY_SEARCH;

        ContentValues values = new ContentValues();

        if(!place_id.equals("")) {
            values.put("place_id", place_id);
        }

        if(!serial.equals("")) {
            values.put("serial", serial);
        }

        if(type > 0) {
            values.put("type", String.valueOf(type));
        }

        if(!subcontractor.equals("")) {
            values.put("subcontractor", subcontractor);
        }

        if(!building.equals("")) {
            values.put("building", building);
        }

        if(!floor.equals("")) {
            values.put("floor", floor);
        }

        if(!spot.equals("")) {
            values.put("spot", spot);
        }

        for(Map.Entry<String, Object> stringObjectEntry : values.valueSet()) {

            if(stringObjectEntry.getValue() == null) {
                values.remove(stringObjectEntry.getKey());
            }


        }

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<Facility> facilities = new ArrayList<>();

                try {

                    JSONArray jsonArray = new JSONArray(result);

                    for(int i=0;i<jsonArray.length();i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String id = jsonObject.getString("id");
                        String place_id = jsonObject.getString("place_id");
                        String serial = jsonObject.getString("serial");
                        String type = jsonObject.getString("type");
                        String subcontractor = jsonObject.getString("subcontractor");
                        String building = jsonObject.getString("building");
                        String floor = jsonObject.getString("floor");
                        String spot = jsonObject.getString("spot");
                        String started_at = jsonObject.getString("started_at");
                        String finished_at = jsonObject.getString("finished_at");
                        String edit_started_at = jsonObject.getString("edit_started_at");
                        String edit_finished_at = jsonObject.getString("edit_finished_at");
                        String dis_started_at = jsonObject.getString("dis_started_at");
                        String dis_finished_at = jsonObject.getString("dis_finished_at");
                        String expired_at = jsonObject.getString("expired_at");
                        String created_at = jsonObject.getString("created_at");


                        Facility facility = new Facility();

                        facility.id = id;
                        facility.place_id = place_id;
                        facility.serial = serial;
                        facility.type = Integer.parseInt(type);
                        facility.subcontractor = subcontractor;
                        facility.building = building;
                        facility.floor = floor;
                        facility.spot = spot;

                        facility.started_at = getDateFromString(started_at);
                        facility.finished_at = getDateFromString(finished_at);
                        facility.edit_started_at = getDateFromString(edit_started_at);
                        facility.edit_finished_at = getDateFromString(edit_finished_at);
                        facility.dis_started_at = getDateFromString(dis_started_at);
                        facility.dis_finished_at = getDateFromString(dis_finished_at);
                        facility.expired_at = getDateFromString(expired_at);
                        facility.created_at = getDateFromString(created_at);

                        facilities.add(facility);

                    }

                    apiCallback.onSuccess(facilities);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String error) {
                apiCallback.onFailed(error);
            }
        };

        new NetworkTask(url, values, true, networkCallback).execute();

    }


}
