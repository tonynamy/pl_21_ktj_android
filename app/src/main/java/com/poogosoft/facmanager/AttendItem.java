package com.poogosoft.facmanager;

import java.util.Date;

public class AttendItem {

    String id;
    String name;
    int type;

    Date attend_date;
    Date leave_date;

    public AttendItem(String id, String name) {
        this(id, name, -2);
    }

    public AttendItem(String id, String name, int type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    /*
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }
    */
}
