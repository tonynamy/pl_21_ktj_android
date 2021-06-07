package com.example.facmanager;

import java.util.HashMap;
import java.util.List;

public class AttendanceRecord {

    public static final int NOT_ATTEND = -1;
    public static final int ATTENDED = 0;
    public static final int LEAVED_WORK = 1;

    private HashMap<String, Integer> record = new HashMap<>();
    private HashMap<String, String> userNameIdMap = new HashMap<>();


    public void addRecord(String userName, String userId, int type) {

        if(record.containsKey(userName)) {

            int original_type = record.get(userName);

            if(original_type>type) return;

        }

        record.put(userName, type);

        userNameIdMap.put(userName, userId);
    }

    public String getUserId(String userName) {
        return userNameIdMap.get(userName);
    }

    public HashMap<String, Integer> getRecord() {
        return record;
    }


}
