package com.example.app;

import java.sql.Time;

public class ownerTimer {
    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(String sqlTime) {
        this.startTime = Time.valueOf(sqlTime);
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    private Time startTime;
}
