package com.example.huqicheng.entity;

/**
 * Created by jiaxinf on 2017-10-17.
 */

public class Event implements java.io.Serializable{
    long eventID;
    String title;
    String description;

    public String getEventTitle() {
        return title;
    }

    public String geteventDescription() {
        return description;
    }

    public long getEventId() {
        return eventID;
    }

    public void setEventID(long groupId) {
        this.eventID = groupId;
    }

    public void setEventTitle(String eventTitle) {
        this.title = eventTitle;
    }

    public void setEventDescription(String eventDescription) {
        this.description = eventDescription;
    }
}
