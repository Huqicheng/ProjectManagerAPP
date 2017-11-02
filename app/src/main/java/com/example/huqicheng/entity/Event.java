package com.example.huqicheng.entity;

import java.util.Date;



/**
 * Created by jiaxinf on 2017-10-17.
 */

public class Event implements java.io.Serializable{
	/** users can edit title,description,deadline **/
    long eventID;
	int groupId;
	int assignedTo; //assign to user_ID
	int assignedBy; //event assigned by user_ID
	String groupName;
    String title;
    String description;
    String eventStatus;
	Date deadLine;
    Date createdAt;
    Date updatedAt;

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(Date deadLine) {
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
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

	public long getEventID() {
		return eventID;
	}

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
