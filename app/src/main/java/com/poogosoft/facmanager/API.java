package com.poogosoft.facmanager;

import android.content.ContentValues;

import com.poogosoft.facmanager.models.Auth;
import com.poogosoft.facmanager.models.TaskplanEtc;
import com.poogosoft.facmanager.models.Facility;
import com.poogosoft.facmanager.models.FacilityInfo;
import com.poogosoft.facmanager.models.Place;
import com.poogosoft.facmanager.models.Team;
import com.poogosoft.facmanager.models.UserAttendance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class API {

    public static String ROOT_URL = "http://pl-21-ktj.run.goorm.io/api/";

    private static String LOGIN = ROOT_URL + "login";
    private static String AUTH_CHECK = ROOT_URL + "auth_check";
    private static String PLACE = ROOT_URL + "place";
    private static String PLACE_ADD = ROOT_URL + "place_add";
    private static String PLACE_EDIT = ROOT_URL + "place_edit";
    private static String USER = ROOT_URL + "user";
    private static String USER_ADD = ROOT_URL + "user_add";
    private static String USER_EDIT_LEVEL = ROOT_URL + "user_edit_level";
    private static String USER_EDIT_PASSWORD = ROOT_URL + "user_edit_password";
    private static String USER_DELETE = ROOT_URL + "user_delete";
    private static String TEAM = ROOT_URL + "team";
    private static String ATTENDANCE = ROOT_URL + "attendance";
    private static String ATTENDANCE_ADD = ROOT_URL + "attendance_add";
    private static String ATTENDANCE_EDIT = ROOT_URL + "attendance_edit";
    private static String ATTENDANCE_EDIT_TEAM = ROOT_URL + "attendance_edit_team";
    private static String FACILITY = ROOT_URL + "facility";
    private static String FACILITY_SEARCH_INFO = ROOT_URL + "facility_search_info";
    private static String FACILITY_SEARCH = ROOT_URL + "facility_search";
    private static String FACILITY_EDIT_STATE = ROOT_URL + "facility_edit_state";
    private static String FACILITY_EDIT_SUPER_MANAGER = ROOT_URL + "facility_edit_super_manager";
    private static String FACILITY_EDIT_SIZE = ROOT_URL + "facility_edit_size";
    private static String FACILITY_EDIT_PURPOSE = ROOT_URL + "facility_edit_purpose";
    private static String FACILITY_EDIT_EXPIRED_AT = ROOT_URL + "facility_edit_expired_at";
    private static String TASK_ADD = ROOT_URL + "task_add";
    private static String TASKPLAN = ROOT_URL + "taskplan";
    private static String TASKPLAN_TEAM = ROOT_URL + "taskplan_team";
    private static String TASKPLAN_EDIT = ROOT_URL + "taskplan_edit";
    private static String TASKPLAN_DELETE = ROOT_URL + "taskplan_delete";
    private static String TASKPLAN_ETC = ROOT_URL + "taskplan_etc";
    private static String SUPER_MANAGER = ROOT_URL + "super_manager";
    private static String PRODUCTIVITY = ROOT_URL + "productivity";
    private static String DASHBOARD = ROOT_URL + "dashboard";


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
            //e.printStackTrace();
        }

        return date;
    }

    /*-----------------------------------------로그인관련-----------------------------------------*/

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


    public void authCheck() {

        String url = AUTH_CHECK;

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

        new NetworkTask(url, null, true, networkCallback).execute();
    }

    /*------------------------------------------현장관련------------------------------------------*/

    public void getPlaces() {

        getPlaces(false);

    }
    public void getPlaces(Boolean auth_check) {

        String url = PLACE;

        ContentValues values = new ContentValues();
        values.put("auth_check", auth_check);

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

        new NetworkTask(url, values, true, networkCallback).execute();
    }

    public void addPlace(String place_name) {

        String url = PLACE_ADD;
        ContentValues values = new ContentValues();
        values.put("place_name", place_name);

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

    public void editPlace(String place_id, String place_name) {

        String url = PLACE_EDIT;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        values.put("place_name", place_name);

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


    /*----------------------------------------유저정보관련----------------------------------------*/

    public void getUser(String place_id) {

        String url = USER;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<UserInfoItem> userInfoList = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(result);

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        UserInfoItem userInfoItem = new UserInfoItem();

                        userInfoItem.user_id = jsonObject.getString("id");
                        userInfoItem.user_name = jsonObject.getString("username");
                        userInfoItem.user_birthday = jsonObject.getString("birthday");
                        if(jsonObject.getString("level") != "null") {
                            userInfoItem.user_level = jsonObject.getInt("level");
                        }

                        userInfoList.add(userInfoItem);
                    }

                    apiCallback.onSuccess(userInfoList);


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

    public void addUser(String place_id, String username, Date birthday) {

        String url = USER_ADD;
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

    public void editUserLevel(String place_id, String user_id, int level) {

        String url = USER_EDIT_LEVEL;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        values.put("id", user_id);
        values.put("level", level);

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

    public void editUserPassword(String password) {

        String url = USER_EDIT_PASSWORD;

        ContentValues values = new ContentValues();
        values.put("password", password);

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

    public void deleteUser(String place_id, String user_id) {

        String url = USER_DELETE;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        values.put("id", user_id);

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

    /*-------------------------------------------팀관련-------------------------------------------*/

    public void getTeams(String place_id) {

        getTeams(place_id, "");

    }
    public void getTeams(String place_id, String team_id) {

        String url = TEAM;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        if(!team_id.isEmpty()){
            values.put("team_id", team_id);
        }

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

                        Team team = new Team();
                        team.id = id;
                        team.name = name;
                        team.place_id = place_id;

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

        new NetworkTask(url, values, true, networkCallback).execute();
    }

    /*-----------------------------------------출퇴근관련-----------------------------------------*/

    public void getAttendances(String team_id) {

        String url = ATTENDANCE;

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

    public void editAttendanceTeam(String userId, String teamId) {
        String url = ATTENDANCE_EDIT_TEAM;

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

    /*-----------------------------------------시설물관련-----------------------------------------*/

    public void getFacility(String facility_id, String team_id) {

        String url = FACILITY;

        ContentValues values = new ContentValues();
        values.put("facility_id", facility_id);
        if(!team_id.isEmpty()) {
            values.put("team_id", team_id);
        }

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                Facility facility = new Facility();

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    facility.id = jsonObject.getString("id");
                    facility.place_id = jsonObject.getString("place_id");
                    facility.serial = jsonObject.getString("serial");
                    facility.o_serial = jsonObject.getString("o_serial");
                    facility.type = Integer.parseInt(jsonObject.getString("type"));
                    facility.super_manager = jsonObject.getString("super_manager");
                    facility.subcontractor = jsonObject.getString("subcontractor");
                    facility.building = jsonObject.getString("building");
                    facility.floor = jsonObject.getString("floor");
                    facility.spot = jsonObject.getString("spot");
                    facility.cube_result = jsonObject.getString("cube_result").equals("0") ? "" : jsonObject.getString("cube_result");
                    facility.danger_result = jsonObject.getString("danger_result").equals("0") ? "" : jsonObject.getString("danger_result");
                    facility.purpose = jsonObject.getString("purpose");
                    facility.started_at = getDateFromString(jsonObject.getString("started_at"));
                    facility.finished_at = getDateFromString(jsonObject.getString("finished_at"));
                    facility.edit_started_at = getDateFromString(jsonObject.getString("edit_started_at"));
                    facility.edit_finished_at = getDateFromString(jsonObject.getString("edit_finished_at"));
                    facility.dis_started_at = getDateFromString(jsonObject.getString("dis_started_at"));
                    facility.dis_finished_at = getDateFromString(jsonObject.getString("dis_finished_at"));
                    facility.expired_at = getDateFromString(jsonObject.getString("expired_at"));
                    facility.created_at = getDateFromString(jsonObject.getString("created_at"));

                    if(jsonObject.has("attendance")){ facility.attendance = jsonObject.getString("attendance"); }
                    if(jsonObject.has("taskplan_type")){ facility.taskplan_type = jsonObject.getString("taskplan_type"); }
                    if(jsonObject.has("taskplan_team_id")){ facility.taskplan_team_id = jsonObject.getString("taskplan_team_id"); }

                    apiCallback.onSuccess(facility);

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

    public void getFacilitySearchInfo(String place_id, String super_manager, int type, String subcontractor, String building, String floor, String spot) {

        String url = FACILITY_SEARCH_INFO;

        ContentValues values = new ContentValues();

        values.put("place_id", place_id);
        if(!super_manager.isEmpty()) {
            values.put("super_manager", super_manager);
        }
        if(type > 0) {
            values.put("type", type);
        }
        if(!subcontractor.isEmpty()) {
            values.put("subcontractor", subcontractor);
        }
        if(!building.isEmpty()) {
            values.put("building", building);
        }
        if(!floor.isEmpty()) {
            values.put("floor", floor);
        }
        if(!spot.isEmpty()) {
            values.put("spot", spot);
        }

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                FacilityInfo facilityInfo = new FacilityInfo();

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    String typeString = jsonObject.getString("type");
                    String[] str_types = typeString.split(",");

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

        new NetworkTask(url, values, true, networkCallback).execute();
    }

    public void searchFacility(String place_id, String serial, int type, String super_manager, String subcontractor, String building, String floor, String spot, int button_right, String filter) {

        String url = FACILITY_SEARCH;

        ContentValues values = new ContentValues();

        if(!place_id.isEmpty()) {
            values.put("place_id", place_id);
        }

        if(!serial.isEmpty()) {
            values.put("serial", serial);
        }

        if(type > 0) {
            values.put("type", String.valueOf(type));
        }

        if(!super_manager.isEmpty()) {
            values.put("super_manager", super_manager);
        }

        if(!subcontractor.isEmpty()) {
            values.put("subcontractor", subcontractor);
        }

        if(!building.isEmpty()) {
            values.put("building", building);
        }

        if(!floor.isEmpty()) {
            values.put("floor", floor);
        }

        if(!spot.isEmpty()) {
            values.put("spot", spot);
        }

        if(button_right > 0) {
            values.put("button_right", button_right);
        }

        if(filter != null && !filter.isEmpty()) {
            values.put("filter", filter);
        }
        /*
        for(Map.Entry<String, Object> stringObjectEntry : values.valueSet()) {

            if(stringObjectEntry.getValue() == null) {
                values.remove(stringObjectEntry.getKey());
            }
        }
        */
        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<Facility> facilities = new ArrayList<>();

                try {

                    JSONArray jsonArray = new JSONArray(result);

                    for(int i = 0; i < jsonArray.length(); i++) {

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

                        if(jsonObject.has("taskplan_type") && jsonObject.getString("taskplan_type") != "null") {
                            facility.taskplan_type = jsonObject.getString("taskplan_type");
                        }

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

    public void editFacilityState(String facility_id, int state_type) {

        String url = FACILITY_EDIT_STATE;

        ContentValues values = new ContentValues();
        values.put("id", facility_id);
        values.put("state_type", state_type);

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

    public void editFacilitySuperManager(String facility_id, String super_manager) {

        String url = FACILITY_EDIT_SUPER_MANAGER;

        ContentValues values = new ContentValues();
        values.put("id", facility_id);
        values.put("super_manager", super_manager);

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

    public void editFacilityPurpose(String facility_id, String purpose) {

        String url = FACILITY_EDIT_PURPOSE;

        ContentValues values = new ContentValues();
        values.put("id", facility_id);
        values.put("purpose", purpose);

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
    public void editFacilitySize(String facility_id, String size, Boolean is_danger) {

        String url = FACILITY_EDIT_SIZE;

        ContentValues values = new ContentValues();
        values.put("id", facility_id);
        values.put("size", size);
        values.put("is_danger", is_danger);

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

    public void editFacilityExpiredAt(String facility_id, Date expired_at) {

        String url = FACILITY_EDIT_EXPIRED_AT;

        ContentValues values = new ContentValues();
        values.put("id", facility_id);
        values.put("expired_at", new SimpleDateFormat("yyyy-MM-dd").format(expired_at));

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

    /*
    private Facility getFacilityFromJsonObject(JSONObject jsonObject) {
        return getFacilityFromJsonObject(jsonObject, false);
    }

    private Facility getFacilityFromJsonObject(JSONObject jsonObject, Boolean taskplan) {

        Facility facility = new Facility();

        try {

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

            if(taskplan) {

                String plan = jsonObject.getString("taskplan");
                String teamName = jsonObject.getString("team_name");

                TaskPlan taskPlan = new TaskPlan();
                Team team = new Team();

                team.name = teamName;

                taskPlan.team = team;
                taskPlan.plan = plan;

                facility.taskPlan = taskPlan;
            }


        } catch (JSONException e) {
            //e.printStackTrace();
        }

        return facility;
    }

    private ArrayList<Facility> getFacilityListFromJsonArray(JSONArray jsonArray, Boolean taskplan) {

        ArrayList<Facility> facilities = new ArrayList<>();

        for(int i=0; i<jsonArray.length(); i++) {

            Facility facility = new Facility();

            try {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                facility = getFacilityFromJsonObject(jsonObject, taskplan);

            } catch (Exception e) {
                //e.printStackTrace();
            }

            facilities.add(facility);
        }

        return facilities;
    }

    public void getFacilityInfo(String place_id) {

        String url = FACILITY_INFO;

        ContentValues values = new ContentValues();

        if(!place_id.equals("")) {
            values.put("place_id", place_id);
        }

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<Facility> expire_facilities = new ArrayList<>();
                ArrayList<Facility> construct_planned_facilities = new ArrayList<>();
                ArrayList<Facility> desturct_planned_facilities = new ArrayList<>();
                ArrayList<Facility> edit_planned_facilities = new ArrayList<>();

                List<ArrayList<Facility>> response = new ArrayList<>(4);

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray expire = jsonObject.getJSONArray("expire");
                    JSONArray construct = jsonObject.getJSONArray("construct");
                    JSONArray destruct = jsonObject.getJSONArray("destruct");
                    JSONArray edit = jsonObject.getJSONArray("edit");

                    response.add(getFacilityListFromJsonArray(expire, true));
                    response.add(getFacilityListFromJsonArray(construct, true));
                    response.add(getFacilityListFromJsonArray(destruct, true));
                    response.add(getFacilityListFromJsonArray(edit, true));

                    apiCallback.onSuccess(response);


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
    */

    /*-------------------------------------------작업관련-------------------------------------------*/

    public void addTask(String team_id, String facility_serial, int manday, int type) {

        String url = TASK_ADD;

        ContentValues values = new ContentValues();
        values.put("team_id", team_id);
        values.put("facility_serial", facility_serial);
        values.put("manday", manday);
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

    /*-----------------------------------------작업계획관련-----------------------------------------*/

    public void getTaskPlan(String place_id) {

        String url = TASKPLAN;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                ArrayList<TaskListItem> expire_facilities = new ArrayList<>();
                ArrayList<TaskListItem> construct_planned_facilities = new ArrayList<>();
                ArrayList<TaskListItem> edit_planned_facilities = new ArrayList<>();
                ArrayList<TaskListItem> desturct_planned_facilities = new ArrayList<>();
                ArrayList<TaskListItem> etc_taskplans = new ArrayList<>();

                List<ArrayList<TaskListItem>> response = new ArrayList<>(4);

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray expire = jsonObject.getJSONArray("expire");
                    for(int i = 0; i < expire.length(); i++) {
                        JSONObject expire_jsonObject = expire.getJSONObject(i);

                        TaskListItem taskListItem = new TaskListItem();
                        taskListItem.id = expire_jsonObject.getString("id");
                        taskListItem.serial = expire_jsonObject.getString("serial");
                        String expired_at = expire_jsonObject.getString("expired_at");
                        taskListItem.expired_date = getDateFromString(expired_at);

                        expire_facilities.add(taskListItem);
                    }

                    JSONArray construct = jsonObject.getJSONArray("construct");
                    for(int i = 0; i < construct.length(); i++) {
                        JSONObject construct_jsonObject = construct.getJSONObject(i);

                        TaskListItem taskListItem = new TaskListItem();
                        taskListItem.id = construct_jsonObject.getString("id");
                        taskListItem.serial = construct_jsonObject.getString("serial");
                        taskListItem.location = construct_jsonObject.getString("building") + " " + construct_jsonObject.getString("floor") + " " + construct_jsonObject.getString("spot");
                        taskListItem.team_name = construct_jsonObject.getString("team_name");
                        taskListItem.taskplan = construct_jsonObject.getInt("taskplan");

                        construct_planned_facilities.add(taskListItem);
                    }

                    JSONArray edit = jsonObject.getJSONArray("edit");
                    for(int i = 0; i < edit.length(); i++) {
                        JSONObject edit_jsonObject = edit.getJSONObject(i);

                        TaskListItem taskListItem = new TaskListItem();
                        taskListItem.id = edit_jsonObject.getString("id");
                        taskListItem.serial = edit_jsonObject.getString("serial");
                        taskListItem.location = edit_jsonObject.getString("building") + " " + edit_jsonObject.getString("floor") + " " + edit_jsonObject.getString("spot");
                        taskListItem.team_name = edit_jsonObject.getString("team_name");
                        taskListItem.taskplan = edit_jsonObject.getInt("taskplan");

                        edit_planned_facilities.add(taskListItem);
                    }

                    JSONArray destruct = jsonObject.getJSONArray("destruct");
                    for(int i = 0; i < destruct.length(); i++) {
                        JSONObject destruct_jsonObject = destruct.getJSONObject(i);

                        TaskListItem taskListItem = new TaskListItem();
                        taskListItem.id = destruct_jsonObject.getString("id");
                        taskListItem.serial = destruct_jsonObject.getString("serial");
                        taskListItem.location = destruct_jsonObject.getString("building") + " " + destruct_jsonObject.getString("floor") + " " + destruct_jsonObject.getString("spot");
                        taskListItem.team_name = destruct_jsonObject.getString("team_name");
                        taskListItem.taskplan = destruct_jsonObject.getInt("taskplan");

                        desturct_planned_facilities.add(taskListItem);
                    }

                    JSONArray etc = jsonObject.getJSONArray("etc");
                    for(int i = 0; i < etc.length(); i++) {
                        JSONObject etc_jsonObject = etc.getJSONObject(i);

                        TaskListItem taskListItem = new TaskListItem();
                        taskListItem.id = etc_jsonObject.getString("id");
                        taskListItem.location = etc_jsonObject.getString("facility_serial");
                        taskListItem.team_name = etc_jsonObject.getString("team_name");
                        taskListItem.taskplan = etc_jsonObject.getInt("type");

                        etc_taskplans.add(taskListItem);
                    }

                    response.add(expire_facilities);
                    response.add(construct_planned_facilities);
                    response.add(edit_planned_facilities);
                    response.add(desturct_planned_facilities);
                    response.add(etc_taskplans);

                    apiCallback.onSuccess(response);


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

    public void getTeamTaskPlan(String team_id) {

        String url = TASKPLAN_TEAM;

        ContentValues values = new ContentValues();
        values.put("team_id", team_id);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {
            @Override
            public void onSuccess(String result) {

                ArrayList<TaskListItem> plan_task = new ArrayList<>();
                ArrayList<TaskListItem> recent_task = new ArrayList<>();

                List<ArrayList<TaskListItem>> response = new ArrayList<>(2);

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray plan = jsonObject.getJSONArray("plan");
                    for(int i = 0; i < plan.length(); i++) {
                        JSONObject plan_jsonObject = plan.getJSONObject(i);

                        TaskListItem taskListItem = new TaskListItem();
                        taskListItem.id = plan_jsonObject.getString("id");
                        taskListItem.serial = plan_jsonObject.getString("serial");
                        taskListItem.location = plan_jsonObject.getString("building") + " " + plan_jsonObject.getString("floor") + " " + plan_jsonObject.getString("spot");
                        taskListItem.taskplan = plan_jsonObject.getInt("taskplan");

                        plan_task.add(taskListItem);
                    }

                    JSONArray etc_plan = jsonObject.getJSONArray("etc_plan");
                    for(int i = 0; i < etc_plan.length(); i++) {
                        JSONObject etc_plan_jsonObject = etc_plan.getJSONObject(i);

                        TaskListItem taskListItem = new TaskListItem();
                        taskListItem.id = etc_plan_jsonObject.getString("id");
                        taskListItem.location = etc_plan_jsonObject.getString("facility_serial");
                        taskListItem.taskplan = etc_plan_jsonObject.getInt("type");

                        plan_task.add(taskListItem);
                    }

                    JSONArray recent = jsonObject.getJSONArray("recent");
                    for(int i = 0; i < recent.length(); i++) {
                        JSONObject recent_jsonObject = recent.getJSONObject(i);

                        TaskListItem taskListItem = new TaskListItem();
                        taskListItem.id = recent_jsonObject.getString("id");
                        taskListItem.serial = recent_jsonObject.getString("serial");
                        taskListItem.location = recent_jsonObject.getString("building") + " " + recent_jsonObject.getString("floor") + " " + recent_jsonObject.getString("spot");
                        if(jsonObject.has("taskplan")) { taskListItem.taskplan = recent_jsonObject.getInt("taskplan"); }

                        String[] state_column = {"설치전", "설치중", "승인완료", "수정중", "수정완료", "해체중", "해체완료"};
                        String[] recent_state_arr = {
                                recent_jsonObject.getString("created_at"),
                                recent_jsonObject.getString("started_at"),
                                recent_jsonObject.getString("finished_at"),
                                recent_jsonObject.getString("edit_started_at"),
                                recent_jsonObject.getString("edit_finished_at"),
                                recent_jsonObject.getString("dis_started_at"),
                                recent_jsonObject.getString("dis_finished_at"),
                        };
                        taskListItem.progress = "설치전";
                        for(int s = recent_state_arr.length-1; s >= 0; s--) {
                            if(!recent_state_arr[s].equals("null")) {
                                taskListItem.progress = state_column[s];
                                break;
                            }
                        }

                        recent_task.add(taskListItem);
                    }

                    response.add(plan_task);
                    response.add(recent_task);

                    apiCallback.onSuccess(response);

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

    public void editTaskPlan(String place_id, String facility_serial, String team_id, int type) {

        String url = TASKPLAN_EDIT;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        values.put("facility_serial", facility_serial);
        values.put("team_id", team_id);
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

    public void deleteTaskplan(String place_id, String facility_serial) {

        String url = TASKPLAN_DELETE;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        values.put("facility_serial", facility_serial);

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

    public void getEtcTaskplan(String taskplan_id) {

        String url = TASKPLAN_ETC;

        ContentValues values = new ContentValues();
        values.put("taskplan_id", taskplan_id);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                TaskplanEtc taskplanEtc = new TaskplanEtc();

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    taskplanEtc.id = jsonObject.getString("id");
                    taskplanEtc.name = jsonObject.getString("facility_serial");
                    taskplanEtc.team_id = jsonObject.getString("team_id");
                    taskplanEtc.in_task = jsonObject.getBoolean("in_task");

                    apiCallback.onSuccess(taskplanEtc);

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

    /*-----------------------------------------담당자관련-----------------------------------------*/

    public void getSuperManager(String place_id) {

        String url = SUPER_MANAGER;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    String super_manager_names = jsonObject.getString("super_manager");

                    apiCallback.onSuccess(super_manager_names);


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

    /*-----------------------------------------생산성관련-----------------------------------------*/

    public void getProductivity(String place_id) {

        getProductivity(place_id, Calendar.getInstance().getTime());

    }

    public void getProductivity(String place_id, Date target_time) {

        String url = PRODUCTIVITY;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);

        if(target_time != null)
            values.put("target_time", target_time.getTime() / 1000L);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    JSONArray teams = jsonObject.getJSONArray("teams");
                    JSONObject totals_cube = jsonObject.getJSONObject("totals_cube");
                    JSONObject totals_square = jsonObject.getJSONObject("totals_square");
                    JSONObject totals_manday = jsonObject.getJSONObject("totals_manday");

                    ArrayList<ProductivityItem> productivityItems = new ArrayList<>();

                    for(int i = 0; i < teams.length(); i++) {
                        JSONObject team_jsonObject = teams.getJSONObject(i);

                        ProductivityItem productivityItem = new ProductivityItem();
                        String team_id = team_jsonObject.getString("id");
                        productivityItem.team_id = team_id;
                        productivityItem.team_name = team_jsonObject.getString("name");
                        productivityItem.productivity_cube = totals_cube.getString(team_id);
                        productivityItem.productivity_danger = totals_square.getString(team_id);
                        productivityItem.manday = totals_manday.getString(team_id);

                        productivityItems.add(productivityItem);
                    }

                    apiCallback.onSuccess(productivityItems);


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

    public void getTeamProductivity(String place_id, String team_id) {

        String url = DASHBOARD;

        ContentValues values = new ContentValues();
        values.put("place_id", place_id);
        values.put("team_id", team_id);

        NetworkTask.NetworkCallback networkCallback = new NetworkTask.NetworkCallback() {

            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    double total_cube = jsonObject.getDouble("total_cube");
                    double total_square = jsonObject.getDouble("total_square");
                    int total_manday = jsonObject.getInt("total_manday");
                    int safe_point = jsonObject.getInt("safe_points");

                    MenuActivity.Dashboard dashboard = new MenuActivity.Dashboard();
                    dashboard.cube = total_cube;
                    dashboard.square = total_square;
                    dashboard.manday = total_manday;
                    dashboard.safe_point = safe_point;

                    apiCallback.onSuccess(dashboard);


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
