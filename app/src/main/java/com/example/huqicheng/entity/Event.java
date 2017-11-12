package com.example.huqicheng.entity;

import java.util.Date;



/**
 * Created by jiaxinf on 2017-10-17.
 */

public class Event implements java.io.Serializable{
	/** users can edit title,description,deadline **/
    long eventId;
	int groupId;
	int assignedTo; //assign to user_ID
	int assignedBy; //event assigned by user_ID
	String groupName;
    String eventTitle;
    String eventDescription;
	String eventLocation;
    String eventStatus;
	long deadLine;   //timestamp
    long createdAt;
    long updatedAt;


	public long getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(long deadLine) {
		this.deadLine = deadLine;
	}

	public int getAssignedTo() {return assignedTo;}

	public void setAssignedTo(int assignedTo) {this.assignedTo = assignedTo;}

	public int getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(int assignedBy) {
		this.assignedBy = assignedBy;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	public long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public long getEventId() {
        return eventId;
    }

	public String getEventLocation() {return eventLocation;}

	public void setEventLocation(String eventLocation) {this.eventLocation = eventLocation;}

	public void setEventID(long eventId) {
        this.eventId = eventId;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
